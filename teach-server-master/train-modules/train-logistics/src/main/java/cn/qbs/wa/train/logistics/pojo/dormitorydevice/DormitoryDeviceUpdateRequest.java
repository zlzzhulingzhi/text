package cn.qbs.wa.train.logistics.pojo.dormitorydevice;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 宿舍设施(DormitoryDevice)更新宿舍设施参数
 *
 * @author makejava
 * @since 2022-10-13 09:48:04
 */
@Data
public class DormitoryDeviceUpdateRequest {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "宿舍ID")
    private Long dormitoryId;

    @ApiModelProperty(value = "设备ID列表")
    private List<Long> sceneDeviceId;

}

