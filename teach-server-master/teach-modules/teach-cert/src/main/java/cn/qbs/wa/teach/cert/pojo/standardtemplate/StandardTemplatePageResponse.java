package cn.qbs.wa.teach.cert.pojo.standardtemplate;

import cn.qbs.wa.teach.cert.pojo.common.CommonConfigDTO;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 证书模板(StandardTemplate)分页查询证书模板响应
 *
 * @author makejava
 * @since 2022-01-19 11:38:22
 */
@Data
public class StandardTemplatePageResponse {


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "证书模板名称")
    private String name;

    @ApiModelProperty(value = "0 禁用 1正常")
    private Integer enabled;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "证书搜索配置")
    private CommonConfigDTO certSearchConfig;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}

