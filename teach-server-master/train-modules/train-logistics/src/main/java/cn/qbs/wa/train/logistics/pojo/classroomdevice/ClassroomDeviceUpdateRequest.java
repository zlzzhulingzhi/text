package cn.qbs.wa.train.logistics.pojo.classroomdevice;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 教室设施(ClassroomDevice)更新教室设施参数
 *
 * @author makejava
 * @since 2022-10-12 18:57:19
 */
@Data
public class ClassroomDeviceUpdateRequest {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "教室ID")
    private Long classroomId;

    @ApiModelProperty(value = "设备ID")
    private List<Long> sceneDeviceId;

}

