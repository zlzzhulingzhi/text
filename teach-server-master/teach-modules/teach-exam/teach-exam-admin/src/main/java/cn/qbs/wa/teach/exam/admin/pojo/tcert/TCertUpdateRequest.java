package cn.qbs.wa.teach.exam.admin.pojo.tcert;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 任务证书表(TCert)更新任务证书表参数
 *
 * @author makejava
 * @since 2022-05-16 13:48:02
 */
@Data
public class TCertUpdateRequest {
    
    @ApiModelProperty(value = "")
    private String id;
    
    @ApiModelProperty(value = "机构id")
    private Long orgId;
    
    @ApiModelProperty(value = "考试id")
    private Long examId;
    
    @ApiModelProperty(value = "删除")
    private Integer deleted;

}

