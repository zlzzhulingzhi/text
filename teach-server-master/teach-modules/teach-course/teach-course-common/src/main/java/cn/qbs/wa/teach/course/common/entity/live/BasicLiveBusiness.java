package cn.qbs.wa.teach.course.common.entity.live;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author makejava
 * @since 2021-12-29 11:07:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BasicLiveBusiness extends Model {

    private static final long serialVersionUID = 605601363847887976L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "基础直播id")
    private Long basicLiveId;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "业务类型 1 直播课程 2直播子课程")
    private Integer businessType;

    @ApiModelProperty(value = "业务id")
    private Long businessId;

    @ApiModelProperty(value = "业务名称")
    private String businessName;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
