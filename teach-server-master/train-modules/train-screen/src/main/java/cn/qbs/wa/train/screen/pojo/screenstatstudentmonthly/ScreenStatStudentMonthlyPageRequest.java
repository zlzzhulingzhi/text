package cn.qbs.wa.train.screen.pojo.screenstatstudentmonthly;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 大屏-每月学员数量统计(ScreenStatStudentMonthly)分页查询参数
 *
 * @author makejava
 * @since 2022-10-14 15:06:30
 */
@Data
public class ScreenStatStudentMonthlyPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "年份")
    private String year;

    @ApiModelProperty(value = "月份")
    private Integer month;

    @ApiModelProperty(value = "数量")
    private Integer num;

}

