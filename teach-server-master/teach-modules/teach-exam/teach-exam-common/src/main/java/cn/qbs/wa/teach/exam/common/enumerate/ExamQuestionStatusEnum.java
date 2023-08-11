package cn.qbs.wa.teach.exam.common.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 考题状态枚举
 * @Author zcm
 * @Date 2022-01-13 10:13
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum ExamQuestionStatusEnum {

    ADDING(0, "添加中"),
    ADDED(1, "已添加");

    private final int status;

    private final String name;

    /**
     * 根据文本值获取对应的枚举值
     */
    public static ExamQuestionStatusEnum fromName(String text) {
        ExamQuestionStatusEnum[] values = ExamQuestionStatusEnum.values();
        for (ExamQuestionStatusEnum value : values) {
            if(value.getName().equals(text)){
                return value;
            }
        }

        return null;
    }

    public static ExamQuestionStatusEnum fromStatus(int id) {
        ExamQuestionStatusEnum[] values = values();
        for (ExamQuestionStatusEnum value : values) {
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
