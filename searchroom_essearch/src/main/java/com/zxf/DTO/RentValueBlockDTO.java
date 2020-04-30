package com.zxf.DTO;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class RentValueBlockDTO {

    /**
     * 价格区间定义
     */
    public static final Map<String, RentValueBlockDTO> PRICE_BLOCK;

    /**
     * 面积区间定义
     */
    public static final Map<String, RentValueBlockDTO> AREA_BLOCK;

    /**
     * 无限制区间
     */
    public static final RentValueBlockDTO ALL = new RentValueBlockDTO("*", -1, -1);

    static {
        PRICE_BLOCK = ImmutableMap.<String, RentValueBlockDTO>builder()
                .put("*-1000", new RentValueBlockDTO("*-1000", -1, 1000))
                .put("1000-3000", new RentValueBlockDTO("1000-3000", 1000, 3000))
                .put("3000-*", new RentValueBlockDTO("3000-*", 3000, -1))
                .build();

        AREA_BLOCK = ImmutableMap.<String, RentValueBlockDTO>builder()
                .put("*-30", new RentValueBlockDTO("*-30", -1, 30))
                .put("30-50", new RentValueBlockDTO("30-50", 30, 50))
                .put("50-*", new RentValueBlockDTO("50-*", 50, -1))
                .build();
    }

    private String key;
    private int min;
    private int max;

    public RentValueBlockDTO(String key, int min, int max) {
        this.key = key;
        this.min = min;
        this.max = max;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public static RentValueBlockDTO matchPrice(String key) {
        RentValueBlockDTO block = PRICE_BLOCK.get(key);
        if (block == null) {
            return ALL;
        }
        return block;
    }

    public static RentValueBlockDTO matchArea(String key) {
        RentValueBlockDTO block = AREA_BLOCK.get(key);
        if (block == null) {
            return ALL;
        }
        return block;
    }

}
