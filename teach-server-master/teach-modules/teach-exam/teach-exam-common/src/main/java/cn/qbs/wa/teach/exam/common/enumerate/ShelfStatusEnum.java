package cn.qbs.wa.teach.exam.common.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 难度枚举
 * @Author zcm
 * @Date 2021-12-15 09:33
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum ShelfStatusEnum {

    OFF(0, "下架"),
    ON(1, "上架");

    private final int status;

    private final String name;

    /**
     * 根据文本值获取对应的枚举值
     */
    public static ShelfStatusEnum fromName(String text) {
        ShelfStatusEnum[] values = ShelfStatusEnum.values();
        for (ShelfStatusEnum value : values) {
            if(value.getName().equals(text)){
                return value;
            }
        }

        return null;
    }

    public static ShelfStatusEnum fromStatus(int id) {
        ShelfStatusEnum[] values = values();
        for (ShelfStatusEnum value : values) {
            if (value.getStatus() == id) {
                return value;
            }
        }
        return null;
    }

    public static List<Map<String, Object>> list() {
        return Arrays.stream(values()).map(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("status", item.getStatus());
            map.put("name", item.getName());
            return map;
        }).collect(Collectors.toList());
    }

}
