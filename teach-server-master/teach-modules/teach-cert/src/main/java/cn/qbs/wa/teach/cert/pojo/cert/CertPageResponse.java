package cn.qbs.wa.teach.cert.pojo.cert;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 证书(Cert)分页查询证书响应
 *
 * @author makejava
 * @since 2022-01-19 19:19:23
 */
@Data
public class CertPageResponse  {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "证书名称")
    private String name;

    @ApiModelProperty(value = "模板名称")
    private String templateName;

    @ApiModelProperty(value = "0 禁用 1正常")
    private Integer enabled;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "证书图片路径")
    private String certImageUrl;

    @ApiModelProperty(value = "证书pdf路径")
    private String certImagePdfUrl;

    @ApiModelProperty(value = "0 禁用 1正常")
    private Integer awardEnabled;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "发放证书人数")
    private Integer awardCount;

    @ApiModelProperty(value = "发放证书无效人数")
    private Integer awardInvalidCount;

    @ApiModelProperty(value = "发放证书有效人数")
    private Integer awardEffectiveCount;

    @ApiModelProperty(value = "模板id")
    private Long templateId;

    public String getCertImagePdfUrl() {
        if (StrUtil.isNotBlank(certImageUrl)) {
            if (certImageUrl.contains("png")) {
                return certImageUrl.replace("png", "pdf");
            }
            if (certImageUrl.contains("jpg")) {
                return certImageUrl.replace("jpg", "pdf");
            }

        }
        return certImagePdfUrl;
    }

}

