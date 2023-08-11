package cn.qbs.wa.teach.course.standard.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageSourceType {

    FILE("本地文件"),
    URL("网络资源"),
    INPUT("文件流");

    @EnumValue
    private String name;

}

