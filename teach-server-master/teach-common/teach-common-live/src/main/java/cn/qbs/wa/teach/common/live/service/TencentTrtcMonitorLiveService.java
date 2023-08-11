package cn.qbs.wa.teach.common.live.service;

import cn.qbs.wa.teach.common.live.config.TencentLiveConf;
import cn.qbs.wa.teach.common.live.config.TencentTRTCMonitorLiveConf;
import cn.qbs.wa.teach.common.live.pojo.LiveAddressResponse;
import cn.qbs.wa.teach.common.live.pojo.LiveRequest;
import com.tencentyun.TLSSigAPIv2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/12/24 8:46
 */
@Service
@Slf4j
public class TencentTrtcMonitorLiveService implements BaseLiveService {

    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    @Autowired
    TencentLiveConf conf;

    @Autowired
    TencentTRTCMonitorLiveConf trtcLiveConf;

    @Override
    public LiveAddressResponse getLiveAddress(LiveRequest request) {
        String streamName = getStreamName(request);
        Long endTime = request.getEndTime();
        String pullSafeUrl = getPullSafeUrl(streamName, endTime);
        LiveAddressResponse liveAddress = new LiveAddressResponse();
        String appName = conf.getAppName();
        String pullDomain = conf.getPullDomain();

        // 沿用之前的拉流域名
        liveAddress.setPullRtmpUrl("rtmp://" + pullDomain + "/" + appName + "/" + streamName + "?" + pullSafeUrl);
        liveAddress.setPullFlvUrl("https://" + pullDomain + "/" + appName + "/" + streamName + ".flv?" + pullSafeUrl);
        liveAddress.setPullM3u8Url("https://" + pullDomain + "/" + appName + "/" + streamName + ".m3u8?" + pullSafeUrl);
        liveAddress.setPullUdpUrl("webrtc://" + pullDomain + "/" + appName + "/" + streamName + "?" + pullSafeUrl);
        liveAddress.setStreamName(streamName);
        return liveAddress;
    }


    /**
     * 返回流名称
     * streamId 为 ${sdkAppId}_${roomId}_${userId}_${streamType}
     * streamType： 摄像头画面为 main，屏幕分享为 aux （WebRTC 由于同时只支持一路上行，因此 WebRTC 上屏幕分享的流类型是 main）
     *
     * @param request 请求参数
     * @return stream name
     */
    private String getStreamName(LiveRequest request) {
        String connStr = "_";
        String streamType = "main";
        return trtcLiveConf.getAppId() +
                connStr +
                request.getRoomId() +
                connStr +
                request.getUserId() +
                connStr +
                streamType;
    }

    /**
     * 取得拉流MD5后缀
     * KEY+ streamName + txTime
     *
     * @param streamName 流名称
     * @param txTime     有效时间
     * @return 结果字符串
     */
    private String getPullSafeUrl(String streamName, long txTime) {
        String input = new StringBuilder().
                append(conf.getPullKey()).
                append(streamName).
                append(Long.toHexString(txTime).toUpperCase()).toString();
        return generateMessageDigestUrl(input, txTime);
    }


    /**
     * 生成对应字符串的MD5编码结果
     *
     * @param originalSuffix 原始的后缀
     * @param txTime         有效时间
     * @return MD5编码后的url
     */
    private String generateMessageDigestUrl(String originalSuffix, long txTime) {
        String txSecret = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            txSecret = byteArrayToHexString(
                    messageDigest.digest(originalSuffix.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return txSecret == null ? "" :
                new StringBuilder().
                        append("txSecret=").
                        append(txSecret).
                        append("&").
                        append("txTime=").
                        append(Long.toHexString(txTime).toUpperCase()).
                        toString();
    }

    private static String byteArrayToHexString(byte[] data) {
        char[] out = new char[data.length << 1];

        for (int i = 0, j = 0; i < data.length; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }

    public String getUserSig(String userId) {
        TLSSigAPIv2 api = new TLSSigAPIv2(Long.valueOf(trtcLiveConf.getAppId()), trtcLiveConf.getPriKey());
        return api.genUserSig(userId, 180*86400);
    }




}
