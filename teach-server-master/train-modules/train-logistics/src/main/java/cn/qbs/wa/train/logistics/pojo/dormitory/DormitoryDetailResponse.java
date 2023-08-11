package cn.qbs.wa.train.logistics.pojo.dormitory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.train.logistics.entity.Dormitory;

/**
 * 宿舍表(Dormitory)宿舍表详情
 *
 * @author makejava
 * @since 2022-10-08 17:40:00
 */
@Data
public class DormitoryDetailResponse extends Dormitory {

    /*@ApiModelProperty(value = "文件类型(pic-图片 doc-文件)")
    private String fileType;

    @ApiModelProperty(value = "文件地址")
    private String[] fileUrl;

    @ApiModelProperty(value = "设备名字")
    private String deviceName;*/
}

