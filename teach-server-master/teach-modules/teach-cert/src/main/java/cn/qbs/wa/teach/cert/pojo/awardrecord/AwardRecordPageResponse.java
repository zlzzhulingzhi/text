package cn.qbs.wa.teach.cert.pojo.awardrecord;

import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import cn.qbs.wa.teach.common.security.enums.SensitiveTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 颁发记录(AwardRecord)分页查询颁发记录响应
 *
 * @author makejava
 * @since 2022-01-19 11:38:18
 */
@Data
public class AwardRecordPageResponse {


    @ApiModelProperty(value = "主键id")
    Long id;

    @ApiModelProperty(value = "用户id")
    Long userId;

    @ApiModelProperty(value = "证书id")
    Long certId;

    @ApiModelProperty(value = "用户名称")
    String userName;

    @ApiModelProperty(value = "身份证号码")
    @EncodeContent(decode=false,desensitized = true,desensitizedType = SensitiveTypeEnum.ID_CARD)
    String idNum;

    @ApiModelProperty(value = "手机号码")
    String phone;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    LocalDateTime createTime;

    @ApiModelProperty(value = "禁用0 启用1")
    Integer enabled;

    @ApiModelProperty(value = "证书图片路径")
    private String certImageUrl;

    @ApiModelProperty(value = "证书pdf路径")
    private String certImagePdfUrl;

    @ApiModelProperty(value = "是否需要重新生成")
    private Boolean retryAward=false;

    @ApiModelProperty(value = "证书编号")
    private String certNum;

    @ApiModelProperty(value = "来源标记")
    private String sourceMark;

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

