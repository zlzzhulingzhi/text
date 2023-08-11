package cn.qbs.wa.teach.exam.common.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 考试记录状态枚举
 * @Author zcm
 * @Date 2021-12-16 14:33
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum ExamineeRecordStatusEnum {

    // 0 未完成 1 待批改 2已批改 3中断

    UNFINISH(0, "未完成","0"),
    TO_BE_CORRECTED(1, "待批改","1"),
    CORRECTED(2, "已批改","2"),
    INTERRUPT(3, "中断","3");

    private final int status;

    private final String name;
    private final String value;



    /**
     * 根据文本值获取对应的枚举值
     */
    public static ExamineeRecordStatusEnum fromName(String text) {
        ExamineeRecordStatusEnum[] values = ExamineeRecordStatusEnum.values();
        for (ExamineeRecordStatusEnum value : values) {
            if(value.getName().equals(text)){
                return value;
            }
        }

        return null;
    }

    public static ExamineeRecordStatusEnum fromStatus(int status) {
        ExamineeRecordStatusEnum[] values = values();
        for (ExamineeRecordStatusEnum value : values) {
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
