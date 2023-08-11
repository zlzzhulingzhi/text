package cn.qbs.wa.train.logistics.pojo.scenedevice;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 场景设备(SceneDevice)更新场景设备参数
 *
 * @author makejava
 * @since 2022-10-12 19:03:50
 */
@Data
public class SceneDeviceUpdateRequest {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "类别(Classroom-教室申请，Dormitory-宿舍申请)")
    private String category;

    @ApiModelProperty(value = "设备名字")
    private String deviceName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "排序序号")
    private Integer sort;

    @ApiModelProperty(value = "启用状态(0-不可用  1-可用)")
    private Integer enabled;

}

