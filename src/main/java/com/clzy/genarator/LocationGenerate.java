package com.clzy.genarator;

import com.alibaba.fastjson.JSON;

import java.text.NumberFormat;
import java.util.*;

/**
 * @Author: tangs
 * @Date: 2022/1/23 11:41
 */
public class LocationGenerate {

    private static String generateLocation(double lat, double lon, double precision) {
        List<Map<String, Double>> areas = new ArrayList<>(4);
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(4);
        Random random = new Random();
        double r = getRandom(random, 3);
        Map<String, Double> map = new HashMap<>();
        map.put("lat", Double.valueOf(nf.format( lat+ r)));
        map.put("lng", Double.valueOf(nf.format(lon - r)));
        areas.add(map);

        if (Math.random() > precision) {
            Map<String, Double> rand = new HashMap<>();
            rand.put("lat", Double.valueOf(nf.format(lat + getRandom(random,4))));
            rand.put("lng", Double.valueOf(nf.format(lon)));
            areas.add(rand);
        }
        r = getRandom(random, 3);
        Map<String, Double> map1 = new HashMap<>();
        map1.put("lat", Double.valueOf(nf.format(lat + r)));
        map1.put("lng", Double.valueOf(nf.format(lon + r)));
        areas.add(map1);

        if (Math.random() > precision) {
            Map<String, Double> rand = new HashMap<>();
            rand.put("lat", Double.valueOf(nf.format(lat)));
            rand.put("lng", Double.valueOf(nf.format(lon + getRandom(random,4))));
            areas.add(rand);
        }
        r = getRandom(random, 3);
        Map<String, Double> map2 = new HashMap<>();
        map2.put("lat", Double.valueOf(nf.format(lat - r)));
        map2.put("lng", Double.valueOf(nf.format(lon + r)));
        areas.add(map2);

        if (Math.random() < precision) {
            Map<String, Double> rand = new HashMap<>();
            rand.put("lat", Double.valueOf(nf.format(lat - getRandom(random,4))));
            rand.put("lng", Double.valueOf(nf.format(lon)));
            areas.add(rand);
        }
        r = getRandom(random, 3);
        Map<String, Double> map3 = new HashMap<>();
        map3.put("lat", Double.valueOf(nf.format(lat - r)));
        map3.put("lng", Double.valueOf(nf.format(lon - r)));
        areas.add(map3);

        if (Math.random() < precision) {
            Map<String, Double> rand = new HashMap<>();
            rand.put("lat", Double.valueOf(nf.format(lat)));
            rand.put("lng", Double.valueOf(nf.format(lon - getRandom(random,4))));
            areas.add(rand);
        }
        return JSON.toJSONString(areas);
    }

    private String generateLocation(String la,String lo) {
        double lat = Double.parseDouble(la);
        double lon = Double.parseDouble(lo);
        return generateLocation(lat, lon, 0.5d);
    }

    private static double getRandom(Random random,int flag) {
        int r = random.nextInt(10);
        if (r < flag) {
            r=flag;
        }
        return r * 0.0002;
    }


    public static void main(String[] args) {


        String[] p = new String[]{"620112010001","36.0505","103.95259","620112010002","36.05038","103.95367","620112010003","36.05064","103.95528","620112010004","36.03116","104.03001","620112010005","36.03095","104.0304","620112010006","36.02899","104.03611","620112010007","36.00186","104.10215","620112010008","35.93285","104.19949","620112010009","35.84968","104.25013","620112010010","35.84655","104.25141","620112010011","35.84273","104.25322","620112010012","35.69268","104.28785","620112010013","35.39493","104.61267","620112010014","35.26889","104.69696","620112010015","35.18054","104.71687","620112010016","35.1931","104.71447","620112010017","35.11022","104.71597","620112010018","34.88716","104.7249","620112010019","34.79026","104.80253","620112010020","34.76531","105.05041","620112010021","34.76044","105.29803","620112010022","34.73904","105.42541","620112010023","34.73601","105.43983","620112010024","34.73213","105.44802","620112010025","34.73139","105.4551","620112010026","34.73033","105.45622","620112010027","34.72856","105.45794","620112010028","34.72609","105.46145","620112010029","34.73244","105.45321","620112010030","34.71015","105.50128","620112010031","34.70768","105.50334","620112010032","34.70394","105.5066","620112010033","34.70098","105.51012","620112010034","34.69879","105.52214","620112010035","34.71594","105.55286","620112010036","34.71622","105.56145","620112010037","34.7165","105.56711","620112010038","34.70571","105.59355","620112010039","34.70211","105.61681","620112010040","34.63264","105.73607","620112010041","34.62396","105.77418","620112010042","34.62025","105.80147","620112010043","34.6141","105.81645","620112010044","34.61573","105.80272","620112010045","34.61573","105.80272","620112010046","34.61371","105.8131","620112010047","34.60637","105.80933","620112010048","34.60737","105.84926","620112010049","34.59542","105.86237","620112010050","34.59245","105.8731","620112010051","34.58623","105.8767","620112010052","34.58213","105.87567","620112010053","34.57019","105.95455","620112010054","34.58015","105.95301","620112010055","34.59379","105.95181","620112010056","34.75159","106.09458"};
        int len = p.length / 3;
        String projectNo;
        double lon, lat;
        for (int i = 0; i < len; i++) {
            projectNo = p[i * 3];
            lon = Double.parseDouble(p[i * 3 + 2]);
            lat = Double.parseDouble(p[i * 3 + 1]);
//            String id= IdGen.uuid();
            String insert = "INSERT INTO `XF_DISASTER_REPORT` (`ID`, `WARNING_DESCR`, `WARNING_LEVEL`, `LAT`, `LON`, `PROJECT_ID`, `LOCATION`, `CREATE_DATE`, `AREA`, `SOURCE`, `PROCESS`, `QR`) VALUES ('%s', '兰州测试', '4', '%s', '%s', '%s', '成都市龙泉驿区山泉镇美满村9组', '2022-01-23 11:08:00', NULL, '兰州测试', '0', 'https://testmini.clzytech.com/xf/index?projectId=%s&disasterId=%s');";
            StringBuilder areaBuilder = new StringBuilder(generateLocation(lat, lon, 0.5));
            areaBuilder.insert(0, "UPDATE MONITORING_PROJECT SET LOCATION_AREA = '").append("' WHERE LOCATION_AREA IS NULL AND PROJECT_NO = '").append(projectNo).append("';");
            System.out.println(areaBuilder);
//            System.out.println(String.format(insert, id, lat, lon, projectNo, projectNo, id));
        }


    }
}
