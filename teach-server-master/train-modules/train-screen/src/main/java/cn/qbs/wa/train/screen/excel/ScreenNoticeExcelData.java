package cn.qbs.wa.train.screen.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/12 14:38
 */
@Data
public class ScreenNoticeExcelData {

    // 标题 内容 发布时间 基础数据类.这里的排序和excel里面的排序一致
    @ApiModelProperty(value = "行号")
    private Integer rowNum;

    @ApiModelProperty(value = "标题")
    @ExcelProperty(value = "*标题")
    private String title;

    @ApiModelProperty(value = "内容")
    @ExcelProperty(value = "*内容")
    private String content;

    @ApiModelProperty(value = "发布时间")
    @ExcelProperty(value = "*发布时间")
    private String publishDate;

}
