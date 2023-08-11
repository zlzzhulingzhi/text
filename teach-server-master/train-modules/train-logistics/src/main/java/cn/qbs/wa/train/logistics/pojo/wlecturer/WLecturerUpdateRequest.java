package cn.qbs.wa.train.logistics.pojo.wlecturer;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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

