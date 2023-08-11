package cn.qbs.wa.train.screen.pojo.screenstatdatadynamic;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 大屏-动态数据统计(ScreenStatDataDynamic)分页查询参数
 *
 * @author makejava
 * @since 2022-10-14 15:15:30
 */
@Data
public class ScreenStatDataDynamicPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "模块(classroom-教室 dormitory-宿舍)")
    private String module;

    @ApiModelProperty(value = "数据名称")
    private String dataName;

    @ApiModelProperty(value = "已使用数量")
    private Integer usingNum;

    @ApiModelProperty(value = "空闲数量")
    private Integer freeNum;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

    @ApiModelProperty(value = "展示状态(0-不展示 1-展示)")
    private String display;

}

