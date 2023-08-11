package cn.qbs.wa.teach.course.standard.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QrCodeType {
    COURSE(1,"课程"),
    MICRO(2,"微页"),
    BARGAIN(3,"砍价"),
    GROUPON(4,"拼团"),
    CAMPUS(5,"校区");

    @EnumValue
    private int type;

    private String name;

    public static QrCodeType getType(Integer type){
        QrCodeType[] values = QrCodeType.values();
        for (QrCodeType value : values) {
            if (value.type==type){
                return value;
            }
        }
        return null;
    }
}
