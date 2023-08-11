package cn.qbs.wa.train.logistics.pojo.dormitory;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 宿舍表(Dormitory)创建宿舍表参数
 *
 * @author makejava
 * @since 2022-10-08 17:40:00
 */
@Data
public class DormitoryAddRequest {

    @ApiModelProperty(value = "宿舍单元(字典值)")
    @NotEmpty(message = "宿舍单元不能为空")
    private String building;

    @ApiModelProperty(value = "楼层(字典值)")
    @NotEmpty(message = "楼层不能为空")
    private String floor;

    @ApiModelProperty(value = "房号")
    @NotEmpty(message = "房号不能为空")
    private String roomNo;

    @ApiModelProperty(value = "房型(字典值)")
    private String roomType;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "启用状态(0-不可用  1-可用)")
    private Integer enabled;

    @ApiModelProperty(value = "删除状态 0-正常 1-已删除")
    private Integer deleted;

    @ApiModelProperty(value = "文件类型(pic-图片 doc-文件)")
    private String fileType;

    @ApiModelProperty(value = "文件地址")
    private String[] fileUrl;

    @ApiModelProperty(value = "设备ID列表")
    private List<Long> sceneDeviceId;


}

