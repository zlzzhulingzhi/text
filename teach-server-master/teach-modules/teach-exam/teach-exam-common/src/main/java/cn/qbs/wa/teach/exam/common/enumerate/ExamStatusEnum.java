package cn.qbs.wa.teach.exam.common.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 考试状态枚举
 * @Author zcm
 * @Date 2021-12-21 16:33
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum ExamStatusEnum {

    // 1 未考试 2 考试中 3考试结束

    NOT_START(1, "未开始"),
    DURING_EXAM(2, "考试中"),
    EXAM_ENDED(3, "考试结束"),
    NOT_ON_SHELF(4, "未上架或已下架");

    private final int status;

    private final String name;

    /**
     * 根据文本值获取对应的枚举值
     */
    public static ExamStatusEnum fromName(String text) {
        ExamStatusEnum[] values = ExamStatusEnum.values();
        for (ExamStatusEnum value : values) {
            if(value.getName().equals(text)){
                return value;
            }
        }

        return null;
    }

    public static ExamStatusEnum fromStatus(int status) {
        ExamStatusEnum[] values = values();
        for (ExamStatusEnum value : values) {
            if (value.getStatus() == status) {
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

    @Override
    public String toString() {
        return String.valueOf(status);
    }

}
