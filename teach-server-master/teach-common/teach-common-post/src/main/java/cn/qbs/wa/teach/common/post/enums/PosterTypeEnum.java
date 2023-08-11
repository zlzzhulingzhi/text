package cn.qbs.wa.teach.common.post.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum PosterTypeEnum {

    TEXT("text", "文本"),

    IMAGE("image", "图片"),

    BLOCK("block", "矩形"),

    LINE("line", "线");

    private final String code;

    private final String name;


}

