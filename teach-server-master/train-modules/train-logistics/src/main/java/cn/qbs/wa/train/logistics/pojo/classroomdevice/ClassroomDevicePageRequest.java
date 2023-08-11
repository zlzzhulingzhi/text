package cn.qbs.wa.train.logistics.pojo.classroomdevice;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 教室设施(ClassroomDevice)分页查询参数
 *
 * @author makejava
 * @since 2022-10-12 18:57:19
 */
@Data
public class ClassroomDevicePageRequest extends BasePageRequest {

    @ApiModelProperty(value = "教室ID")
    private Long classroomId;

    @ApiModelProperty(value = "设备ID")
    private Long sceneDeviceId;

}

