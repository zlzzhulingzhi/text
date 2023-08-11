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
 * 直播频次统计图 (每一分钟)
 *
 * @author zcm
 * @since 2022-06-22 18:57:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BasicLiveShowFrequencyStatistic extends Model {

    private static final long serialVersionUID = -90989254391565406L;


    /**
     * 
     */
    @ApiModelProperty(value = "")
    private Long id;

    /**
     * 机构id
     */
    @ApiModelProperty(value = "机构id")
    private Long orgId;

    /**
     * 直播场次id
     */
    @ApiModelProperty(value = "直播场次id")
    private Long basicLiveShowId;

    /**
     * 在线人数
     */
    @ApiModelProperty(value = "在线人数")
    private Integer uniqueVisitor;

    /**
     * 
     */
    @ApiModelProperty(value = "")
    private Long createBy;

    /**
     * 
     */
    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
