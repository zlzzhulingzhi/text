package cn.qbs.wa.teach.course.live.api.pojo.DTO.liveshow;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 基础直播场次表(BasicLiveShow)创建基础直播场次表参数
 *
 * @author makejava
 * @since 2021-12-20 16:46:42
 */
@Data
public class BasicLiveShowInfoDTO {


    /**
     * 　　两个参数选一即可查询出来
     *
     *
     */

    @ApiModelProperty(value = "基础直播id")
    private Long basicLiveId;

    @ApiModelProperty(value = "流名称")
    private String streamName;






}

