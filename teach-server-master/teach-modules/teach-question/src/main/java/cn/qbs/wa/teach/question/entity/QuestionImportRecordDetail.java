package cn.qbs.wa.teach.question.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zcm
 * @since 2022-06-20 11:02:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionImportRecordDetail extends Model {

    private static final long serialVersionUID = -96494575591837581L;


    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 机构id
     */
    @ApiModelProperty(value = "机构id")
    private Long orgId;

    /**
     * 试题导入记录id
     */
    @ApiModelProperty(value = "试题导入记录id")
    private Long questionImportRecordId;

    /**
     * 导入数据
     */
    @ApiModelProperty(value = "导入数据")
    private String importData;

    /**
     * 是否成功
     */
    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    /**
     * 失败原因
     */
    @ApiModelProperty(value = "失败原因")
    private String failureReason;

}
