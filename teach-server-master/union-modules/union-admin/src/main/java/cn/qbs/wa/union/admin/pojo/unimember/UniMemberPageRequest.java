package cn.qbs.wa.union.admin.pojo.unimember;

import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 统一会员用户表(UniMember)创建统一会员用户表参数
 *
 * @author makejava
 * @version 1.0
 * @date 2022-07-21 09:11:24
 */
@Data
public class UniMemberPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "单位ID")
    private Long unitId;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "启用状态")
    private Integer enabled;

    @ApiModelProperty(value = "单位ID")
    private List<Long> unitIdList;

    @ApiModelProperty("搜索内容")
    String searchContent;

}

