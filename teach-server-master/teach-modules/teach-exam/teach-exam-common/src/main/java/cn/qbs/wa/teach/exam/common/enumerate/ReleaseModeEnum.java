package cn.qbs.wa.teach.exam.common.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 发布方式枚举
 * @Author zcm
 * @Date 2022-01-05 11:42
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum ReleaseModeEnum {

    // 1: 创建时发布, 2 手动发布, 3: 定时发布
    CREATED_RELEASE(1, "创建时发布"),
    MANUAL_RELEASE(2, "手动发布"),
    TIMED_RELEASE(3, "定时发布");

    private final int id;

    private final String name;

    /**
     * 根据文本值获取对应的枚举值
     */
    public static ReleaseModeEnum fromName(String text) {
        ReleaseModeEnum[] values = ReleaseModeEnum.values();
        for (ReleaseModeEnum value : values) {
            if(value.getName().equals(text)){
                return value;
            }
        }

        return null;
    }

    public static ReleaseModeEnum fromId(int id) {
        ReleaseModeEnum[] values = values();
        for (ReleaseModeEnum value : values) {
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
