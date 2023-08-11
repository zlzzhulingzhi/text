package cn.qbs.wa.train.logistics.pojo.dormitorydevice;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 宿舍设施(DormitoryDevice)分页查询参数
 *
 * @author makejava
 * @since 2022-10-13 09:48:04
 */
@Data
public class DormitoryDevicePageRequest extends BasePageRequest {

    @ApiModelProperty(value = "宿舍ID")
    private Long dormitoryId;

    @ApiModelProperty(value = "设备ID")
    private Long sceneDeviceId;

}

