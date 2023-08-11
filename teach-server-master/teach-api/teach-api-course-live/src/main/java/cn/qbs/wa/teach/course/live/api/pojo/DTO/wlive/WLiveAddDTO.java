package cn.qbs.wa.teach.course.live.api.pojo.DTO.wlive;


import lombok.Data;

import java.util.List;

/**
 * 插件-讲师表(WLecturer)创建插件-讲师表参数
 *
 * @author makejava
 * @since 2022-03-01 13:44:49
 */
@Data
public class WLiveAddDTO {

    List<WLiveAddDetail> liveAddDetailList;



}

