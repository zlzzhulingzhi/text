package cn.qbs.wa.teach.course.standard.pojo.report;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TextRectDto {

    private int width;

    private int height;

    private List<String> rows;

    public TextRectDto(int width, int height, List<String> rows) {
        this.width = width;
        this.height = height;
        this.rows = rows;
    }

    public TextRectDto(int width, int height, String content) {
        this.width = width;
        this.height = height;
        this.rows = new ArrayList<>();
        rows.add(content);
    }
}
