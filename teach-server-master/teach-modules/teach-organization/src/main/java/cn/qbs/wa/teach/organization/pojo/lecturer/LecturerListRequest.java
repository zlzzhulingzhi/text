package cn.qbs.wa.teach.organization.pojo.lecturer;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 讲师表(Lecturer)列表
 *
 * @author makejava
 * @since 2021-11-17 11:25:32
 */
@Data
public class LecturerListRequest  {

    @ApiModelProperty(value = "机构id")
    private List<Long> idList;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "用户id")
    private Long userId;


}

