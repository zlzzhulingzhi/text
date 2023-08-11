package cn.qbs.wa.teach.course.api.pojo.DTO.wcourse;

import cn.qbs.wa.teach.common.core.domain.BasePageSearchComDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/3/1 14:01
 */
@Data
public class WCoursePageSearchDTO extends BasePageSearchComDTO {


    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "id数组")
    private List<Long> idList;

    @ApiModelProperty(value = "课程名称")
    private String courseName;


    @ApiModelProperty(value = "分类id数组")
    List<Long> categoryIdList;

    @ApiModelProperty(value = "【课程类型 live：直播、record：录播、mix：综合】")
    private String courseType;

    @ApiModelProperty(value = "课程费用类型 1 免费 2 精品")
    private Integer courseFeeType;

    @ApiModelProperty(hidden = true)
    private Integer gooded;

    @ApiModelProperty(hidden = true)
    private Integer free;

    @ApiModelProperty(value = "上架状态 0 下架 1上架")
    private Integer shelfStatus;






}
