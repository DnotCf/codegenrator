package com.img;

import lombok.Data;

/**
 * @Author: tangs
 * @Date: 2023/1/10 10:03
 */
@Data
public class MergeBean {
    //图片宽
    private int width;
    //图片高
    private int height;
    //图片的像素集合
    private int[] imageArray;
}
