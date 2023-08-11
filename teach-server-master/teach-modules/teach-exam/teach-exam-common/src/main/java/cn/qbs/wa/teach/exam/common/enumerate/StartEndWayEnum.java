package cn.qbs.wa.teach.exam.common.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 启动结束方式枚举
 *
 * @Author zcm
 * @Date 2022-05-24 8:52
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum StartEndWayEnum {

    AUTOMATIC(1, "自动"),
    MANUAL(2, "手动");

    private final int id;

    private final String name;

    /**
     * 根据文本值获取对应的枚举值
     */
    public static StartEndWayEnum fromName(String text) {
        StartEndWayEnum[] values = StartEndWayEnum.values();
        for (StartEndWayEnum value : values) {
            if(value.getName().equals(text)){
                return value;
            }
        }

        return null;
    }

    public static StartEndWayEnum fromId(int id) {
        StartEndWayEnum[] values = values();
        for (StartEndWayEnum value : values) {
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
