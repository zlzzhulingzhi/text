package cn.qbs.wa.train.logistics.pojo.wlecturer;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 插件-讲师表(WLecturer)创建插件-讲师表参数
 *
 * @author makejava
 * @since 2022-03-01 13:44:49
 */
@Data
public class WLecturerAddRequest {

    @ApiModelProperty(value = "主键id组")
    private List<Long> idList;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}

