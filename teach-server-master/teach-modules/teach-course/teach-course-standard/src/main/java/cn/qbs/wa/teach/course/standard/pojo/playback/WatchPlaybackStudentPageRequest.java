package cn.qbs.wa.teach.course.standard.pojo.playback;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * 分页查询观看回放学员参数
 *
 * @Author zcm
 * @Date 2022-06-24 9:48
 * @Version 1.0
 */
@Data
public class WatchPlaybackStudentPageRequest extends BasePageRequest {

    @NotNull(message = "章节id不能为空！")
    @ApiModelProperty(value = "章节id")
    private Long componentId;

    @NotNull(message = "基础直播id不能为空！")
    @ApiModelProperty(value = "基础直播id不能为空")
    private Long basicLiveId;

    @ApiModelProperty(value = "学员姓名")
    private String studentName;

    @ApiModelProperty(value = "用户id集合")
    private Set<Long> userIdList;

}

