package cn.qbs.wa.train.logistics.pojo.scenedevice;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

/**
 * 场景设备(SceneDevice)创建场景设备参数
 *
 * @author makejava
 * @since 2022-10-12 19:03:50
 */
@Data
public class SceneDeviceAddRequest {

    @ApiModelProperty(value = "类别(Classroom-教室申请，Dormitory-宿舍申请)")
    @NotEmpty(message = "类别不能为空")
    private String category;

    @ApiModelProperty(value = "设备名字")
    private String deviceName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "排序序号")
    private Integer sort;

}

