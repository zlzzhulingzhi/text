package cn.qbs.wa.teach.question.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * @author zcm
 * @since 2022-06-17 17:41:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionImportRecord extends Model {

    private static final long serialVersionUID = -87561297706824818L;


    /**
     * 主键
     */
    private Long id;

    /**
     * 机构id
     */
    private Long orgId;

    /**
     * 导入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime importTime;

    /**
     * 总数
     */
    private Integer total;

    /**
     * 成功数量
     */
    private Integer success;

    /**
     * 失败数量
     */
    private Integer failure;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
