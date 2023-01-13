package com.img;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.clzy.geo.core.config.Global;
import com.clzy.geo.core.utils.DateUtils;
import com.clzy.geo.core.utils.StringUtils;
import com.clzy.geo.file.utils.SysFileUtil;
import com.clzy.geo.manage.util.GeoHash;
import com.clzy.geo.xf.entity.Layer;
import com.freewayso.image.combiner.ImageCombiner;
import com.freewayso.image.combiner.enums.OutputFormat;
import com.freewayso.image.combiner.enums.ZoomMode;
import com.google.common.util.concurrent.AtomicDouble;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import javax.imageio.ImageIO;

@Slf4j
@Component
public class TianDiTuDownload {

    //矢量（行政） - 等经纬度
    public static String vec_c = "http://{server}.tianditu.gov.cn/vec_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=c&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk={tk}";
    //矢量（行政） - 墨卡托
    public static String vec_w = "http://{server}.tianditu.gov.cn/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk={tk}";
    //矢量注记（行政） - 等经纬度
    public static String cva_c = "http://{server}.tianditu.gov.cn/cva_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=c&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk={tk}";
    //矢量注记（行政） - 墨卡托
    public static String cva_w = "http://{server}.tianditu.gov.cn/cva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk={tk}";

    //影像 - 等经纬度
    public static String img_c = "http://{server}.tianditu.gov.cn/img_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=c&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk={tk}";
    //影像 - 墨卡托
    public static String img_w = "http://{server}.tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk={tk}";
    //影像注记 - 等经纬度
    public static String cia_c = "http://{server}.tianditu.gov.cn/cia_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=c&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk={tk}";
    //影像注记 - 墨卡托
    public static String cia_w = "http://{server}.tianditu.gov.cn/cia_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk={tk}";

    //地形 - 等经纬度
    public static String ter_c = "http://{server}.tianditu.gov.cn/ter_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=ter&STYLE=default&TILEMATRIXSET=c&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk={tk}";
    //地形 - 墨卡托
    public static String ter_w = "http://{server}.tianditu.gov.cn/ter_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=ter&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk={tk}";
    //地形注记 - 等经纬度
    public static String cta_c = "http://{server}.tianditu.gov.cn/cta_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cta&STYLE=default&TILEMATRIXSET=c&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk={tk}";
    //地形注记 - 墨卡托
    public static String cta_w = "http://{server}.tianditu.gov.cn/cta_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cta&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk={tk}";

    public static String[] servers = {"t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7"};

//    private AtomicDouble process = new AtomicDouble(0);
    private Map<String, AtomicDouble> process = new HashMap<>();

    private AtomicInteger j = new AtomicInteger(1);

    public Double getProcess(String title) {
        if (process.get(title) == null) {
            return 100d;
        }
        double v = process.get(title).get();
        if (v >= 100) {
//            process.remove(title);
            return 100d;
        }
        return v;
    }

    public boolean check() {
        Collection<AtomicDouble> values = process.values();
        if (CollectionUtils.isEmpty(values)) {
            return false;
        }
        for (AtomicDouble d : values) {
            if (d.get() < 100) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        process.clear();
    }

    public static void main(String[] args) throws Exception {
//        System.out.println(4096/72*2.54);
//        System.out.println(Math.sqrt(4096*4096+3112*3112)/Math.sqrt(1024*1024+768*768));
//        download("D:\\tdt", "101.939067,29.990218,101.9746,30.00978");
        TianDiTuDownload diTuDownload = new TianDiTuDownload();
        ImgParam param = new ImgParam();
        List<Layer> layers = new ArrayList<>();

        Layer layer = new Layer();
        layer.setLayer("sc119:layer-45");
        layer.setLayerName("学校");
        layer.setIcon("gx");
        layers.add(layer);
        Layer layer1 = new Layer();
        layer1.setLayer("sc119:layer-58");
        layer1.setLayerName("公共文化场所");
        layer1.setIcon("blcs");
        layers.add(layer1);
        Layer layer2 = new Layer();
        layer2.setLayer("sc119:layer-59");
        layer2.setLayerName("宗教活动场所");
        layer2.setIcon("wzcb");
        layers.add(layer2);
        Layer layer3 = new Layer();
        layer3.setLayer("sc119:layer-60");
        layer3.setLayerName("大型超市、百货店和亿元以上商品交易所");
        layer3.setIcon("slxf");
        layers.add(layer3);
        Layer layer4 = new Layer();
        layer4.setLayer("sc119:layer-66");
        layer4.setLayerName("隐患点");
        layer4.setIcon("zfqy");
        layers.add(layer4);


        param.setBobx("101.939067,29.990218,101.9746,30.00978");
        param.setLevel(15);
        param.setTitile("测试");
        param.setLayerData(layers);

        String s = diTuDownload.create(param);
        System.out.println(JSON.toJSONString(param));

    }

    /**
     * 返回文件路径
     *
     * @param param
     * @return
     */
    public String create(ImgParam param) {
        AtomicDouble pro = new AtomicDouble(0);
        process.put(param.getBobx(), pro);
        download(param);
        return merge(param);
    }



    public void download(ImgParam param) {
        String basePath = SysFileUtil.getDirImg(Global.getConfig("apps.file.upload"));
        //这里放你的天地图开发者秘钥，注意天地图API访问次数限制
        String[] tk = param.getTk().split(",");
        String[] urlArr = {img_w, cia_c};//要下载的图层
        if (param.isVector()) {
            urlArr[0] = vec_w;
            urlArr[1] = cva_c;
        }
        int minZoom = param.getLevel();
        int maxZoom = param.getLevel();
        String[] split = param.getBobx().split(",");
        double startLat = Double.parseDouble(split[3]);//开始纬度（从北到南）
        double endLat = Double.parseDouble(split[1]);//结束纬度（从北到南）
        double startLon = Double.parseDouble(split[0]);//开始经度（从西到东）
        double endLon = Double.parseDouble(split[2]);//结束经度（从西到东）

        ExecutorService exe = Executors.newFixedThreadPool(10);
        CountDownLatch countDown = new CountDownLatch(2);
        //等经纬度第一层是1x2，纬度数量是2^0，经度数量是2^1
        //墨卡托投影第一层是2x2，纬度数量是2^1，经度数量是2^1
        for (int i = 0; i < urlArr.length; i++) {
            String url = urlArr[i].replace("{tk}", tk[(int) (Math.random() * (tk.length))]);
            System.out.println(url);
            String layerName = url.split("tianditu.gov.cn/")[1].split("/wmts?")[0];
            System.out.println(layerName);
            if (layerName.endsWith("c")) {
                //等经纬度
                if (!param.getLocation()) {
                    countDown.countDown();
                    continue;
                }
                exe.execute(() -> {
                    try {

                        List<String> yPath = new ArrayList<>();
                        for (int z = minZoom; z <= maxZoom; z++) {
                            double deg = 360.0 / Math.pow(2, z) / 256;//一个像素点代表多少度
                            int startX = (int) ((startLon + 180) / deg / 256);//减数取整
                            int endX = (int) ((endLon + 180) / deg / 256);//加数取整
                            int startY = (int) ((90 - startLat) / deg / 256);
                            int endY = (int) ((90 - endLat) / deg / 256);
                            double div = 40d / (endY - startY);
                            for (int y = startY; y <= endY; y++) {
                                if (y < endY) {
                                    System.out.println("下载图层注记，当前进度：" + process.get(param.getBobx()).addAndGet(div));
                                }
                                CopyOnWriteArrayList<String> xPath = new CopyOnWriteArrayList<>();
                                CountDownLatch countDownLatch = new CountDownLatch(endX - startX + 1);
                                for (int x = startX; x <= endX; x++) {
                                    int finalZ = z;
                                    int finalY = y;
                                    int finalX = x;
                                    exe.execute(() -> {
                                        try {
                                            final String newUrl = url.replace("{server}", servers[(int) (Math.random() * servers.length)]).replace("{z}", finalZ + "").replace("{y}", finalY + "").replace("{x}", finalX + "");
                                            //System.out.println(newUrl);
                                            final String filePath = basePath + layerName + File.separator + finalZ + File.separator + finalY + File.separator + finalX + ".png";
                                            File file = new File(filePath);
                                            if (!file.exists()) {
                                                if (!file.getParentFile().exists()) {
                                                    file.getParentFile().mkdirs();
                                                }
                                                boolean loop = true;
                                                int count = 0;
                                                while (loop && count < 5) {//下载出错进行重试，最多5次
                                                    count++;
                                                    try {
                                                        InputStream in = getFileInputStream(newUrl);
                                                        OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                                                        byte[] b = new byte[8192];
                                                        int len = 0;
                                                        while ((len = in.read(b)) > -1) {
                                                            out.write(b, 0, len);
                                                            out.flush();
                                                        }
                                                        out.close();
                                                        in.close();
                                                        loop = false;
                                                    } catch (Exception e) {
                                                        loop = true;
                                                    }
                                                }
                                                if (loop) {
                                                    System.out.println("下载失败：" + newUrl);
                                                }
                                            }
                                            xPath.add(filePath);
                                        } finally {
                                            countDownLatch.countDown();
                                        }
                                    });
                                }
                                try {
                                    countDownLatch.await(60, TimeUnit.MINUTES);
                                    String path = basePath + "tmp" + File.separator + j.getAndIncrement() + ".png";
//                                    List<String> collect = xPath.stream().distinct().collect(Collectors.toList());
                                    Collections.sort(xPath, (o1, o2) -> {
                                        if (StringUtils.isEmpty(o1) || StringUtils.isEmpty(o2)) {
                                            return 0;
                                        }
                                        return o1.compareTo(o2);
                                    });
                                    ImgDealUtils.merge(xPath, path, true);
                                    yPath.add(path);
                                } catch (Exception e) {
//                                    e.printStackTrace();
                                }
                            }
                        }
                        yPath = yPath.stream().distinct().collect(Collectors.toList());
                        ImgDealUtils.merge(yPath, basePath + "merge" + File.separator + startLon + ".png", false);
//                        System.out.println("当前进度：" + process.get(param.getBobx()).addAndGet(5));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        countDown.countDown();
                    }
                });
            } else {
                //墨卡托
                exe.execute(() -> {
                    try {
                        List<String> yPath = new ArrayList<>();
                        for (int z = minZoom; z <= maxZoom; z++) {
                            double deg = 360.0 / Math.pow(2, z) / 256;//一个像素点代表多少度
                            int startX = (int) ((startLon + 180) / deg / 256);
                            int endX = (int) ((endLon + 180) / deg / 256);
                            /**
                             * 这里是基于等经纬度坐标转墨卡托坐标的公式得到的算法。（）
                             * 先算出当前地图级别下纬度方向用该有多少个像素点（一个瓦片是256个点，一共有Math.pow(2, z)个瓦片）
                             * 再算出指定纬度坐标与赤道的距离（北半球为正南半球为负）（这里抵消了带入赤道周长的计算，因为它是一个固定值，把赤道周长定为1就抵消了）：Math.log(Math.tan((90 + startLat) * Math.PI / 360)) / (Math.PI / 180)
                             * 就可以得到纬度坐标距赤道有多少个点：(Math.log(Math.tan((90 + startLat) * Math.PI / 360)) / (Math.PI / 180)) / (360/Math.pow(2, z)/256) + 0.5)
                             * 然后就可以计算出其瓦片坐标。
                             */
                            int startY = (((int) Math.pow(2, z) * 256 / 2) - (int) ((Math.log(Math.tan((90 + startLat) * Math.PI / 360)) / (Math.PI / 180)) / (360 / Math.pow(2, z) / 256) + 0.5)) / 256;
                            int endY = (((int) Math.pow(2, z) * 256 / 2) - (int) ((Math.log(Math.tan((90 + endLat) * Math.PI / 360)) / (Math.PI / 180)) / (360 / Math.pow(2, z) / 256) + 0.5)) / 256;
                            double div = 40d / (endY - startY);
                            for (int y = startY; y <= endY; y++) {//加入判断是等经纬度还是墨卡托
                                if (y < endY) {
                                    System.out.println("下载图层影像，当前进度：" + process.get(param.getBobx()).addAndGet(div));
                                }
//                                Vector<String> xPath = new CopyOnWriteArrayList<String>();
                                CopyOnWriteArrayList<String> xPath = new CopyOnWriteArrayList<>();
                                CountDownLatch countDownLatch = new CountDownLatch(endX - startX + 1);
                                for (int x = startX; x <= endX; x++) {
                                    int finalZ = z;
                                    int finalY = y;
                                    int finalX = x;
                                    exe.execute(() -> {
                                        try {
                                            final String newUrl = url.replace("{server}", servers[(int) (Math.random() * servers.length)]).replace("{z}", finalZ + "").replace("{y}", finalY + "").replace("{x}", finalX + "");
                                            //System.out.println(newUrl);
                                            final String filePath = basePath + layerName + File.separator + finalZ + File.separator + finalY + File.separator + finalX + ".png";
                                            File file = new File(filePath);
                                            if (!file.exists()) {
                                                if (!file.getParentFile().exists()) {
                                                    file.getParentFile().mkdirs();
                                                }
                                                boolean loop = true;
                                                int count = 0;
                                                while (loop && count < 5) {//下载出错进行重试，最多5次
                                                    count++;
                                                    try {
                                                        InputStream in = getFileInputStream(newUrl);
                                                        OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                                                        byte[] b = new byte[8192];
                                                        int len = 0;
                                                        while ((len = in.read(b)) > -1) {
                                                            out.write(b, 0, len);
                                                            out.flush();
                                                        }
                                                        out.close();
                                                        in.close();
                                                        loop = false;
                                                    } catch (Exception e) {
                                                        loop = true;
                                                    }
                                                }
                                                if (loop) {
                                                    System.out.println("下载失败：" + newUrl);
                                                }
                                            }
                                            xPath.add(filePath);
                                        } finally {
                                            countDownLatch.countDown();
                                        }

                                    });
                                }
                                try {
                                    countDownLatch.await(60, TimeUnit.MINUTES);
                                    String path = basePath + "tmp" + File.separator + j.getAndIncrement() + ".png";
//                                    List<String> collect = xPath.stream().distinct().collect(Collectors.toList());
                                    Collections.sort(xPath, (o1, o2) -> {
                                        if (StringUtils.isEmpty(o1) || StringUtils.isEmpty(o2)) {
                                            return 0;
                                        }
                                        return o1.compareTo(o2);
                                    });
                                    ImgDealUtils.merge(xPath, path, true);
                                    yPath.add(path);
                                } catch (Exception e) {
//                                    e.printStackTrace();
                                }
                            }
                        }
                        yPath = yPath.stream().distinct().collect(Collectors.toList());
                        ImgDealUtils.merge(yPath, basePath + "merge" + File.separator + startLat + ".png", false);
//                        System.out.println("当前进度：" + process.get(param.getBobx()).addAndGet(5));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        countDown.countDown();
                    }
                });
            }
        }
        try {
            countDown.await(120, TimeUnit.MINUTES);
            ImgDealUtils.delAllFile(basePath + "tmp" + File.separator);
            process.get(param.getBobx()).set(80);
            System.out.println("开始融合图片");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        exe.shutdown();
    }

    //获取文件下载流
    public InputStream getFileInputStream(String url) throws Exception {
        InputStream is = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        request.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        HttpResponse response = httpclient.execute(request);
        response.setHeader("Content-Type", "application/octet-stream");
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == HttpStatus.SC_OK) {
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }
        return is;
    }

    public String merge(ImgParam param) {
        String filePath = null;
        try {
            String[] split = param.getBobx().split(",");
            double startLat = Double.parseDouble(split[3]);//开始纬度（从北到南）
            double startLon = Double.parseDouble(split[0]);//开始经度（从西到东）
            String imgPath = SysFileUtil.getDirImg(Global.getConfig("apps.file.upload")) + "merge" + File.separator + startLat + ".png";
            BufferedImage image = ImageIO.read(new FileInputStream(imgPath));
            //合成器（指定背景图和输出格式，整个图片的宽高和相关计算依赖于背景图，所以背景图的大小是个基准）
            ImageCombiner combiner = null;
            if (image.getWidth() > 4096) {
                combiner = new ImageCombiner(image, OutputFormat.PNG);
            }else {
                combiner = new ImageCombiner(image,param.getWidth(),param.getHeight(),ZoomMode.WidthHeight, OutputFormat.PNG);
            }
            //叠加地名
            if (param.getLocation()) {
                String imgPath1 = SysFileUtil.getDirImg(Global.getConfig("apps.file.upload")) + "merge" + File.separator + startLon + ".png";
                BufferedImage image1 = ImageIO.read(new FileInputStream(imgPath1));
                combiner.addImageElement(image1, 0, 0, combiner.getCanvasWidth(), combiner.getCanvasHeight(), ZoomMode.WidthHeight);
            }

            //加图片元素
            String url = "http://server.clzytech.com:8861/geoserver/sc119/wms?transparent=true&format=image/png&service=WMS&version=1.1.1&request=GetMap&styles=&layers=%s&bbox=" + param.getBobx() + "&width=" + combiner.getCanvasWidth() + "&height=" + combiner.getCanvasHeight() + "&srs=EPSG:4326";
            //add layer
            for (Layer data : param.getLayerData()) {
                combiner.addImageElement(String.format(url, data.getLayer()), 0, 0);
            }
            process.get(param.getBobx()).set(85);
            System.out.println("当前进度：" + 85);
        
            //图例
            String tlPath = tl(param);
            BufferedImage tl = ImageIO.read(new FileInputStream(tlPath));
            int hei = (int) (combiner.getCanvasHeight() / 3.112);
            combiner.addImageElement(tl, 10, combiner.getCanvasHeight() - hei-10, (int) (combiner.getCanvasWidth() / 5.12), hei, ZoomMode.WidthHeight);
            process.get(param.getBobx()).set(90);
            System.out.println("当前进度：" + 90);
            //标题
            String titlePath = title(param);
            BufferedImage title = ImageIO.read(new FileInputStream(titlePath));
            combiner.addImageElement(title, (int) (combiner.getCanvasWidth() / 2.502), 10, (int) (combiner.getCanvasWidth() / 4.096), (int) (combiner.getCanvasHeight() / 22.23), ZoomMode.WidthHeight);
            process.get(param.getBobx()).set(95);
            System.out.println("当前进度：" + 95);
            //方向
            String directPath = ClassUtils.getDefaultClassLoader().getResource("img").getPath() + File.separator + "direct.png";
            BufferedImage direct = ImageIO.read(new FileInputStream(directPath));
            combiner.addImageElement(direct, combiner.getCanvasWidth()-200, 280,140,140,ZoomMode.WidthHeight);

            //比例尺
            double c = combiner.getCanvasWidth() / (72 * 2.54);
            String blc = "比例尺： 1:" + (int) (param.getRange() / c) + "m";
            combiner.addTextElement(blc, "黑体", Font.BOLD, 40, combiner.getCanvasWidth() - 400, combiner.getCanvasHeight() - 100).setColor(Color.white);

            //执行图片合并
            combiner.combine();

            //可以获取流（并上传oss等）
//            InputStream is = combiner.getCombinedImageStream();

            filePath = SysFileUtil.getDirImg(Global.getConfig("apps.file.upload")) + "topic" + File.separator + param.getTitile() + "_" + param.getWidth() + "_" + param.getHeight() + "_" + System.currentTimeMillis() + ".png";
            ImgDealUtils.newFolder(filePath);
            //也可以保存到本地
            combiner.save(filePath);
            System.out.println(param.getTitile() + "专题图层生成成功 " + DateUtils.getDateTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        process.get(param.getBobx()).set(100);
        return filePath;
    }

    public Integer getBLC(String bobx, double distance) {
        String[] split = bobx.split(",");
        double getDistance = GeoHash.GetDistance(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
        return (int)((getDistance * 1000 / distance)*1000000);
    }

    public String tl(ImgParam param) {
        String filePath = null;
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(new File(ClassUtils.getDefaultClassLoader().getResource("img").getPath() + File.separator + "tl.png")));
            ImageCombiner combiner = new ImageCombiner(image, OutputFormat.PNG);
            //图例
            String icon = "http://server.clzytech.com:8861/geoserver/wms?REQUEST=GetLegendGraphic&VERSION=1.0.0&FORMAT=image/png&WIDTH=20&HEIGHT=20&STRICT=false&style=sc119:";
            for (int i = 0; i < param.getLayerData().size(); i++) {
                combiner.addImageElement(icon + param.getLayerData().get(i).getIcon(), 28, 50 + i * 30, 20, 20, ZoomMode.Origin).setRoundCorner(20);
                combiner.addTextElement(param.getLayerData().get(i).getLayerName(),"黑体",Font.BOLD, 16, 58, 50 + i * 30).setColor(Color.white);
            }
            //执行图片合并
            combiner.combine();

            filePath = SysFileUtil.getDirImg(Global.getConfig("apps.file.upload")) + "topic" + File.separator + "tl_" + System.currentTimeMillis() + ".png";
            ImgDealUtils.newFolder(filePath);
            //也可以保存到本地
            combiner.save(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    public String title(ImgParam param) {
        String filePath = null;
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(new File(ClassUtils.getDefaultClassLoader().getResource("img").getPath() + File.separator + "title.png")));
            ImageCombiner combiner = new ImageCombiner(image,OutputFormat.PNG);

            //图例
            ;
            combiner.addTextElement(param.getTitile(), "黑体", Font.BOLD, 34, image.getWidth() / 2 - 34 * param.getTitile().length() / 2, 6).setColor(Color.white);
            //执行图片合并
            combiner.combine();

            filePath = SysFileUtil.getDirImg(Global.getConfig("apps.file.upload")) + "topic" + File.separator + "title_" + System.currentTimeMillis() + ".png";
            ImgDealUtils.newFolder(filePath);
            //也可以保存到本地
            combiner.save(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }
}