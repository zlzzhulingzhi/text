package cn.qbs.wa.teach.exam.common.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 考试模式枚举
 *
 * @Author zcm
 * @Date 2022-05-18 16:02
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum ExamModeEnum {

    // 1-标准模式 2-高级模式
    STANDARD(1, "标准模式"),
    ADVANCED(2, "高级模式");

    private final int mode;

    private final String name;

    /**
     * 根据文本值获取对应的枚举值
     */
    public static ExamModeEnum fromName(String text) {
        ExamModeEnum[] values = ExamModeEnum.values();
        for (ExamModeEnum value : values) {
            if(value.getName().equals(text)){
                return value;
            }
        }

        return null;
    }

    public static ExamModeEnum fromStatus(int mode) {
        ExamModeEnum[] values = values();
        for (ExamModeEnum value : values) {
            if (value.getMode() == mode) {
                return value;
            }
        }
        return null;
    }

    public static List<Map<String, Object>> list() {
        return Arrays.stream(values()).map(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("mode", item.getMode());
            map.put("name", item.getName());
            return map;
        }).collect(Collectors.toList());
    }

}
