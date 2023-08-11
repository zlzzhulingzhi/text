package cn.qbs.wa.teach.organization.pojo.lecturer;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

import java.util.List;

/**
 * 讲师表(Lecturer)分页查询参数
 *
 * @author makejava
 * @since 2021-11-17 11:25:32
 */
@Data
public class LecturerPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "职工id")
    private Long employeeId;

    @ApiModelProperty(value = "简介")
    private String intro;

    @ApiModelProperty(value = "头像地址")
    private String headImgUrl;

    @ApiModelProperty(value = "0 禁用 1启用")
    private Integer enabled;

    @ApiModelProperty(value = "讲师名称")
    private String name;

    @ApiModelProperty(value = "id数组")
    private List<Long> idList;


}

