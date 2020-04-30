package com.zxf.VO;

import java.io.Serializable;

/**
 * @author zxf
 */
public class PhotoVo implements Serializable {

    //照片路径
    private String path;

    //宽度
    private int width;

    //高度
    private int height;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
