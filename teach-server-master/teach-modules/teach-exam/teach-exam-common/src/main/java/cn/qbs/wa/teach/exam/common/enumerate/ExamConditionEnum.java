package cn.qbs.wa.teach.exam.common.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 获取证书的条件
 */
@Getter
@AllArgsConstructor
public enum ExamConditionEnum {
    //通过考试为到达合格的分数
    PASS_EXAM(0,"pass_exam","通过考试"),
    //提交试卷就是完成考试
    COMPLETE_TEST(1,"complete_test","完成考试");

    private final int status;

    private final String code;

    private final String name;


    /**
     * 根据文本值获取对应的枚举值
     */
    public static ExamConditionEnum fromName(String text) {
        ExamConditionEnum[] values = ExamConditionEnum.values();
        for (ExamConditionEnum value : values) {
            if(value.getCode().equals(text)){
                return value;
            }
        }

        return null;
    }

    public static ExamConditionEnum fromId(int id) {
        ExamConditionEnum[] values = values();
        for (ExamConditionEnum value : values) {
            if (value.getStatus() == id) {
                return value;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(status);
    }

}
