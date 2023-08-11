package cn.qbs.wa.train.logistics.pojo.classroomdevice;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 教室设施(ClassroomDevice)创建教室设施参数
 *
 * @author makejava
 * @since 2022-10-12 18:57:19
 */
@Data
public class ClassroomDeviceAddRequest {

    @ApiModelProperty(value = "教室ID")
    private Long classroomId;

    @ApiModelProperty(value = "设备ID列表")
    private List<Long> sceneDeviceId;

}

