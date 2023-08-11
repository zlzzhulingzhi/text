package cn.qbs.wa.teach.organization.pojo.studentgroup;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 学员-分组(StudentGroup)分页查询参数
 *
 * @author makejava
 * @since 2022-03-28 16:07:14
 */
@Data
public class StudentGroupPageRequest extends BasePageRequest {
    
    @ApiModelProperty(value = "机构id")
    private Long orgId;
    
    @ApiModelProperty(value = "学员id")
    private Long studentId;
    
    @ApiModelProperty(value = "分组id")
    private Long groupId;

}

