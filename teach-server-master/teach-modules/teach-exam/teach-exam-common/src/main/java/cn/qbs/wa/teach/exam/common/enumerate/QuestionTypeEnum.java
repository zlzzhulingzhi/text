package cn.qbs.wa.teach.exam.common.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author zcm
 * @Date 2021/11/4 10:33
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum QuestionTypeEnum {

    DAN_XUAN_TI(1000, "单选题"),
    DUO_XUAN_TI(1010, "多选题"),
    PAN_DUAN_TI(1020, "判断题"),
    TIAN_KONG_TI(1030, "填空题"),
    JIE_DA_TI(1040, "解答题"),
    SHI_CAO_TI(1050, "实操题");

    private final long id;

    private final String name;

    /**
     * 根据文本值获取对应的枚举值
     */
    public static QuestionTypeEnum fromName(String text) {
        QuestionTypeEnum[] values = QuestionTypeEnum.values();
        for (QuestionTypeEnum value : values) {
            if(value.getName().equals(text)){
                return value;
            }
        }

        return null;
    }

    public static QuestionTypeEnum fromId(long id) {
        QuestionTypeEnum[] values = values();
        for (QuestionTypeEnum value : values) {
            if (value.getId() == id) {
                return value;
            }
        }
        return null;
    }

}
