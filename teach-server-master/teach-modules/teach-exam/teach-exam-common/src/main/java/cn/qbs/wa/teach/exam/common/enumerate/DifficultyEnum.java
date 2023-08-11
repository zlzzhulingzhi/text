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
public enum DifficultyEnum {

    SIMPLE(1, "简单"),
    ORDINARY(2, "普通"),
    DIFFICULT(3, "困难");

    private final int id;

    private final String name;

    /**
     * 根据文本值获取对应的枚举值
     */
    public static DifficultyEnum fromName(String text) {
        DifficultyEnum[] values = DifficultyEnum.values();
        for (DifficultyEnum value : values) {
            if(value.getName().equals(text)){
                return value;
            }
        }

        return null;
    }

    public static DifficultyEnum fromId(int id) {
        DifficultyEnum[] values = values();
        for (DifficultyEnum value : values) {
            if (value.getId() == id) {
                return value;
            }
        }
        return null;
    }

    public static List<Map<String, Object>> list() {
        return Arrays.stream(values()).map(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", item.getId());
            map.put("name", item.getName());
            return map;
        }).collect(Collectors.toList());
    }
}
