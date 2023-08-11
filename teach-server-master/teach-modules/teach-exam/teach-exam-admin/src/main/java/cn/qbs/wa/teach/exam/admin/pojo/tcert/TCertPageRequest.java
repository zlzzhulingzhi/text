package cn.qbs.wa.teach.exam.admin.pojo.tcert;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 任务证书表(TCert)分页查询参数
 *
 * @author makejava
 * @since 2022-05-16 13:47:59
 */
@Data
public class TCertPageRequest extends BasePageRequest {
    
    @ApiModelProperty(value = "机构id")
    private Long orgId;
    
    @ApiModelProperty(value = "考试id")
    private Long examId;
    
    @ApiModelProperty(value = "删除")
    private Integer deleted;

    @ApiModelProperty(value = "证书名称")
    private String name;

}

