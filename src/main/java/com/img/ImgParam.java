package com.img;

import com.clzy.geo.xf.entity.Layer;
import lombok.Data;

import java.util.List;

/**
 * @Author: tangs
 * @Date: 2023/1/10 15:55
 */
@Data
public class ImgParam {
    private Integer level = 15;
    private String bobx;
    private List<Layer> layerData;
    private String titile="专题图层";
    private Double range = 20000d;
    //矢量
    private boolean vector = false;
    private String tk = "bc40bbc9bcea6453d7dd8cf791a57a98,16b5e683f9648dc9c3212ad40ab0c1f0,ffc7ae446eb4e37ac8b1512fc4ec234a";
    //开启地名
    private Boolean location = true;
    private Integer width = 4096;
    private Integer height = 3112;
}
