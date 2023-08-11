package cn.qbs.wa.teach.course.live.api.pojo.DTO.livebusiness;

import cn.qbs.wa.teach.course.live.api.pojo.DTO.livelecture.LiveLectureSingleDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/12/29 15:34
 */
@Data
public class LiveBusinessUpdateDTO {

    /**
     　　* 不传basicLiveId的时候为删除

     　　*/

    @ApiModelProperty(value = "基础直播id")
    private Long basicLiveId;

    @ApiModelProperty(value = "业务类型 1 直播课程 2直播子课程")
    private Integer businessType;

    @ApiModelProperty(value = "业务id")
    private Long businessId;

    @ApiModelProperty(value = "业务名称")
    private String businessName;

    @ApiModelProperty(value = "讲师数组")
    private List<LiveLectureSingleDTO> lectureList;
}
