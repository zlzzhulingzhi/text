package cn.qbs.wa.teach.organization.pojo.studentgroup;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 学员-分组(StudentGroup)更新学员-分组参数
 *
 * @author makejava
 * @since 2022-03-28 16:07:15
 */
@Data
public class StudentGroupUpdateRequest {
    
    @ApiModelProperty(value = "")
    private Long id;
    
    @ApiModelProperty(value = "机构id")
    private Long orgId;
    
    @ApiModelProperty(value = "学员id")
    private Long studentId;
    
    @ApiModelProperty(value = "分组id")
    private Long groupId;

}

