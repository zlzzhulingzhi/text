package cn.qbs.wa.teach.exam.common.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 考生考试状态枚举
 * @Author WX
 * @Date 2022-8-29 17:08
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum StudentExamStatusEnum {

    // 1 未开始 2进行中 3已完成

    NOT_START(1, "未开始","1"),
    UNDERWAY(2, "进行中","2"),
    COMPLETED(3, "已完成","3");

    private final int status;

    private final String name;
    private final String value;



    /**
     * 根据文本值获取对应的枚举值
     */
    public static StudentExamStatusEnum fromName(String text) {
        StudentExamStatusEnum[] values = StudentExamStatusEnum.values();
        for (StudentExamStatusEnum value : values) {
            if(value.getName().equals(text)){
                return value;
            }
        }

        return null;
    }

    public static StudentExamStatusEnum fromStatus(int status) {
        StudentExamStatusEnum[] values = values();
        for (StudentExamStatusEnum value : values) {
            if (value.getStatus() == status) {
                return value;
            }
        }
        return null;
    }

    public static List<Map<String, Object>> list() {
        return Arrays.stream(values()).map(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", item.getStatus());
            map.put("name", item.getName());
            return map;
        }).collect(Collectors.toList());
    }

}
