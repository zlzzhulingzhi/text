package cn.qbs.wa.teach.exam.common.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 批改状态枚举
 * @Author zcm
 * @Date 2022-01-10 13:49
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum CorrectStatusEnum {

    // 0未批改  1已批改
    UNCORRECTED(0, "未批改"),
    CORRECTED(1, "已批改");

    private final int status;

    private final String name;

    /**
     * 根据文本值获取对应的枚举值
     */
    public static CorrectStatusEnum fromName(String text) {
        CorrectStatusEnum[] values = CorrectStatusEnum.values();
        for (CorrectStatusEnum value : values) {
            if(value.getName().equals(text)){
                return value;
            }
        }

        return null;
    }

    public static CorrectStatusEnum fromStatus(int id) {
        CorrectStatusEnum[] values = values();
        for (CorrectStatusEnum value : values) {
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
