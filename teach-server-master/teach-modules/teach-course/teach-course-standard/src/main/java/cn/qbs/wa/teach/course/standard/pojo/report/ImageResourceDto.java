package cn.qbs.wa.teach.course.standard.pojo.report;


import cn.qbs.wa.teach.course.standard.enums.ImageSourceType;
import lombok.Data;

@Data
public class ImageResourceDto {

    private ImageSourceType type;
    private String url;

    public ImageResourceDto() {
    }

    public ImageResourceDto(ImageSourceType type, String url) {
        this.type = type;
        this.url = url;
    }
}
