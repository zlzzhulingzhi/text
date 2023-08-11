package cn.qbs.wa.teach.exam.common.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 规则类型枚举
 *
 * @Author zcm
 * @Date 2021-12-15 09:33
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum RuleTypeEnum {

    // type 规则类型 1 防作弊规则 2 试题规则
    ANTI_CHEATING_RULE(1,  "打乱试题顺序"),
    QUESTION_RULE(2, "禁止复制、剪切、粘贴");

    private final int type;

    private final String name;



    /**
     * 根据文本值获取对应的枚举值
     */
    public static RuleTypeEnum fromName(String text) {
        RuleTypeEnum[] values = RuleTypeEnum.values();
        for (RuleTypeEnum value : values) {
            if (value.getName().equals(text)) {
                return value;
            }
        }

        return null;
    }

    public static RuleTypeEnum fromType(int type) {
        RuleTypeEnum[] values = RuleTypeEnum.values();
        for (RuleTypeEnum value : values) {
            if (value.getType() == type) {
                return value;
            }
        }

        return null;
    }

    public static List<Map<String, Object>> list(int type) {
        return Arrays.stream(values())
                .filter(item -> item.getType() == type)
                .map(item -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", item.getType());
                    map.put("name", item.getName());
                    return map;
                }).collect(Collectors.toList());
    }

}
