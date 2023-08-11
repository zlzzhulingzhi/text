package cn.qbs.wa.train.logistics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileTypeEnum {

    pic("pic","图片"),
    doc("doc","文件");

    private String code;

    private String name;


}
