package cn.qbs.wa.teach.organization.api.pojo.DTO.wlecturer;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 插件-讲师表(WLecturer)创建插件-讲师表参数
 *
 * @author makejava
 * @since 2022-03-01 13:44:49
 */
@Data
public class WLecturerAddDTO {

    @ApiModelProperty(value = "主键id组")
    @NotEmpty
    private List<Long> idList;

    @ApiModelProperty(value = "机构id")
    @NotNull
    private Long orgId;



}

