package cn.qbs.wa.teach.course.api.pojo.DTO.wcourse;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 插件-讲师表(WLecturer)创建插件-讲师表参数
 *
 * @author makejava
 * @since 2022-03-01 13:44:49
 */
@Data
public class WCourseAddDTO {

    @ApiModelProperty(value = "主键id组")
    @NotEmpty
    private List<Long> idList;

    @ApiModelProperty(value = "机构id")
    private Long orgId;



}

