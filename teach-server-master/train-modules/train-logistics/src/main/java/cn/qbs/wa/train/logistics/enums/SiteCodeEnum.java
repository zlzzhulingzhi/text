package cn.qbs.wa.train.logistics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author vieux
 * @description: TODO
 * @date 2021/11/10 15:07
 */
@Getter
@AllArgsConstructor
public enum SiteCodeEnum {


    SUSHE("ss", "宿舍"),

    JIAOXUELOU("jxl", "教学楼");


    private String code;

    private String name;


}
