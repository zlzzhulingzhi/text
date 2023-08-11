package cn.qbs.wa.teach.question.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 分类分组
 * @Author zcm
 * @Date 2021/11/4 10:33
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum CategoryGroupEnum {

    QUESTION(1, "试题"),
    PAPER(2, "试卷");

    private final int id;

    private final String name;

    /**
     * 根据文本值获取对应的枚举值
     */
    public static CategoryGroupEnum fromName(String text) {
        CategoryGroupEnum[] values = CategoryGroupEnum.values();
        for (CategoryGroupEnum value : values) {
            if(value.getName().equals(text)){
                return value;
            }
        }

        return null;
    }

    public static CategoryGroupEnum fromId(int id) {
        CategoryGroupEnum[] values = values();
        for (CategoryGroupEnum value : values) {
            if (value.getId() == id) {
                return value;
            }
        }
        return null;
    }

}
