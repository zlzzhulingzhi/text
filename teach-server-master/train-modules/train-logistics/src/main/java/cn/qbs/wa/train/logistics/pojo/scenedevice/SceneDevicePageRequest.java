package cn.qbs.wa.train.logistics.pojo.scenedevice;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 场景设备(SceneDevice)分页查询参数
 *
 * @author makejava
 * @since 2022-10-12 19:03:49
 */
@Data
public class SceneDevicePageRequest extends BasePageRequest {

    @ApiModelProperty(value = "类别(Classroom-教室申请，Dormitory-宿舍申请)")
    private String category;

    @ApiModelProperty(value = "设备名字")
    private String deviceName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "排序序号")
    private Integer sort;

}

