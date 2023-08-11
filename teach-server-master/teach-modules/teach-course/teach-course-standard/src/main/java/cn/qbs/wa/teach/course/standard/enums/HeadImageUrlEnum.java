package cn.qbs.wa.teach.course.standard.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 @description: TODO
 @author wx
 @date 2022/07/12 11:27
 *
 */
@Getter
@AllArgsConstructor
public enum HeadImageUrlEnum {


    /**
     * 头像
     * */
    HEAD_IMAGE_URL_ENUM( "头像","https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132");

    @JsonValue
    private final String name;

    private final String code;



}
