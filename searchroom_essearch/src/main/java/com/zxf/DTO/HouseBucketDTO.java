package com.zxf.DTO;

public class HouseBucketDTO {
    /**
     * 聚合bucket的key  什么区
     */
    private String key;

    /**
     * 聚合结果值  什么区几套出租
     */
    private long count;

    public HouseBucketDTO(String key, long count) {
        this.key = key;
        this.count = count;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
