package cn.qbs.wa.train.logistics.pojo.student;


import cn.qbs.wa.teach.common.core.domain.EncodeUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 学员(Student)更新学员参数
 *
 * @author makejava
 * @since 2022-02-28 10:40:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StudentUpdateRequest extends EncodeUser {

    @NotNull(message = "ID不能为空")
    @ApiModelProperty(value = "【主键】")
    private Long id;
    
    @ApiModelProperty(value = "【真实姓名】")
    private String realName;
    
    @ApiModelProperty(value = "【昵称】")
    private String nickName;
    
    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;

    @ApiModelProperty(value = "【0表示账号不可用  1表示账号可用】")
    private Integer enabled;
    
    @ApiModelProperty(value = "【头像地址】")
    private String headImgUrl;

    @ApiModelProperty(value = "【部门ID】")
    private Long deptId;
}

