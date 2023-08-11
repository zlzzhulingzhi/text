package cn.qbs.wa.train.logistics.pojo.classroom;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.train.logistics.entity.Classroom;

import java.util.List;

/**
 * 教室表(Classroom)教室表详情
 *
 * @author makejava
 * @since 2022-10-11 17:30:12
 */
@Data
public class ClassroomDetailResponse extends Classroom {

    @ApiModelProperty(value = "文件类型(pic-图片 doc-文件)")
    private String fileType;

    @ApiModelProperty(value = "文件地址")
    private String[] fileUrl;

    @ApiModelProperty(value = "设备ID列表")
    private List<Long> sceneDeviceId;
}

