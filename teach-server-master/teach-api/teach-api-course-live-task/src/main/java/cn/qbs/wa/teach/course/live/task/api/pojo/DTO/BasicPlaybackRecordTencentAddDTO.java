package cn.qbs.wa.teach.course.live.task.api.pojo.DTO;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 基础直播回放表(BasicPlaybackRecord)创建基础直播回放表参数
 *
 * @author makejava
 * @since 2021-12-20 16:46:46
 */
@Data
public class BasicPlaybackRecordTencentAddDTO {

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "基础直播场次id")
    private Long basicLiveShowId;


    @ApiModelProperty(value = "回放地址")
    private String playbackUrl;


    @ApiModelProperty(value = "事件类型")
    private Long eventType;


    @ApiModelProperty(value = "用户 APPID")
    private Long appId;

    @ApiModelProperty(value = "推流域名")
    private String app;


    @ApiModelProperty(value = "推流路径")
    private String appName;


    @ApiModelProperty(value = "直播流名称")
    private String streamName;


    @ApiModelProperty(value = "同直播流名称")
    private String channelId;

    /**
     * 在 云点播平台 可以唯一定位一个点播视频文件
     */

    @ApiModelProperty(value = "点播 file ID")
    private String fileId;

    /**
     * FLV，HLS，MP4，AAC
     */

    @ApiModelProperty(value = "文件格式")
    private String fileFormat;

    /**
     * 仅 API 创建的录制任务有意义，即 CreateRecordTask 返回的任务 ID
     */

    @ApiModelProperty(value = "录制任务 ID")
    private String taskId;

    /**
     * 不能以该值作为录制内容的开始时间，录制内容的开始时间 = end_time – duration
     */

    @ApiModelProperty(value = "录制任务开始写文件的时间")
    private Long startTime;


    @ApiModelProperty(value = "录制任务结束写文件的时间")
    private Long endTime;

    /**
     * 单位秒
     */
    @ApiModelProperty(value = "录制文件时长")
    private Long duration;

    /**
     * 单位字节
     */

    @ApiModelProperty(value = "录制文件大小")
    private Long fileSize;



    @ApiModelProperty(value = "用户推流 URL 所带参数")
    private String streamParam;

    /**
     * 事件通知安全签名 sign = MD5（key + t）。
     * 说明：腾讯云把加密 key 和 t 进行字符串拼接后通过 MD5 计算得出 sign 值，并将其放在通知消息里，您的后台服务器在收
     * 到通知消息后可以根据同样的算法确认 sign 是否正确，进而确认消息是否确实来自腾讯云后台。
     */

    @ApiModelProperty(value = "事件通知安全签名")
    private String sign;


    /**
     * 事件通知签名过期 UNIX 时间戳。
     * 来自腾讯云的消息通知默认过期时间是10分钟，如果一条消息通知中的 t 值所指定的时间已经过期，则可以判定这条通知无效，进而可以防止网络重放攻击。
     * t 的格式为十进制 UNIX 时间戳，即从1970年01月01日（UTC/GMT 的午夜）开始所经过的秒数。
     */

    @ApiModelProperty(value = "过期时间")
    private Long expireDate;
}

