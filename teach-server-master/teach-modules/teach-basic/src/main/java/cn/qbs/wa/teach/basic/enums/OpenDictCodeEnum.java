package cn.qbs.wa.teach.basic.enums;

/**
 * 对外开发的字典编码
 * 
 * @author yjx
 */
public enum OpenDictCodeEnum {

    /**
     * 性别
     */
    SEX,

    /**
     * 学历
     */
    EDUCATION,

    /**
     * 工作
     */
    JOB,
    
    /**
     * 职位
     */
    POSITION,

    ;

    /**
     * 根据代号获取枚举
     * @param code 枚举代号
     * @return 枚举
     */
    public static OpenDictCodeEnum getEnumByCode(String code) {
        if (code == null || "".equals(code.trim())) {
            return null;
        }
        try {
            return valueOf(code.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}
