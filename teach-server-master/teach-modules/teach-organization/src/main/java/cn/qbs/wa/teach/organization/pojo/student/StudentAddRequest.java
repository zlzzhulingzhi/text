package cn.qbs.wa.teach.organization.pojo.student;


import cn.qbs.wa.teach.common.core.domain.EncodeUser;
import cn.qbs.wa.teach.organization.pojo.invitation.InvitationGroupResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * 学员(Student)创建学员参数
 *
 * @author makejava
 * @since 2022-02-28 10:40:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StudentAddRequest extends EncodeUser {

    @NotNull(message = "机构ID不能为空")
    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【系统用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【邮箱】")
    private String email;

    @ApiModelProperty(value = "【昵称】")
    private String nickName;

    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;

    @ApiModelProperty(value = "【头像地址】")
    private String headImgUrl;

    @ApiModelProperty(value = "【0表示账号不可用  1表示账号可用】")
    private Integer enabled;

    @ApiModelProperty(value = "标签")
    private String groupName;

    @ApiModelProperty(value = "标签集")
    private List<Long> groupIdList;

    @ApiModelProperty(value = "【部门ID】")
    private Long deptId;

    @ApiModelProperty(value = "部门")
    private String deptName;

    @ApiModelProperty(value = "数据校验结果")
    private Boolean passed;

    @ApiModelProperty(value = "错误原因")
    private Set<String> errorReasons;

}

