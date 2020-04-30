package com.zxf.DTO;

import java.io.Serializable;

/**
 * Created by 瓦力.
 */
public class HousePictureDTO implements Serializable {
    private Integer id;

    private Integer houseId;

    private String path;

    private String cdnPrefix;

    private int width;

    private int height;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHouseId() {
        return houseId;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCdnPrefix() {
        return cdnPrefix;
    }

    public void setCdnPrefix(String cdnPrefix) {
        this.cdnPrefix = cdnPrefix;
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

    @Override
    public String toString() {
        return "HousePictureDTO{" +
                "id=" + id +
                ", houseId=" + houseId +
                ", path='" + path + '\'' +
                ", cdnPrefix='" + cdnPrefix + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
