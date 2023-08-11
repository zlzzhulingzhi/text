package cn.qbs.wa.teach.organization.pojo.wlecturer;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 插件-讲师表(WLecturer)更新插件-讲师表参数
 *
 * @author makejava
 * @since 2022-03-01 13:44:49
 */
@Data
public class WLecturerUpdateRequest {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}

