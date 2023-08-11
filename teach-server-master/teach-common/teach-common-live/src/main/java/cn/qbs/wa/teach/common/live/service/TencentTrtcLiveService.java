package cn.qbs.wa.teach.common.live.service;

import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.live.config.TencentLiveConf;
import cn.qbs.wa.teach.common.live.config.TencentTRTCLiveConf;
import cn.qbs.wa.teach.common.live.config.WaProxyConf;
import cn.qbs.wa.teach.common.live.pojo.LiveAddressResponse;
import cn.qbs.wa.teach.common.live.pojo.LiveRequest;
import com.alibaba.fastjson.JSON;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.live.v20180801.LiveClient;
import com.tencentcloudapi.live.v20180801.models.DescribeLiveStreamOnlineListRequest;
import com.tencentcloudapi.live.v20180801.models.DescribeLiveStreamOnlineListResponse;
import com.tencentcloudapi.live.v20180801.models.DropLiveStreamRequest;
import com.tencentcloudapi.live.v20180801.models.DropLiveStreamResponse;
import com.tencentyun.TLSSigAPIv2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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
public class TencentTrtcLiveService implements BaseLiveService {

    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static final String appNameTemplate = "trtc_";


    @Autowired
    TencentLiveConf conf;

    @Autowired
    TencentTRTCLiveConf trtcLiveConf;

    @Autowired
    WaProxyConf waProxyConf;


    @PostConstruct
    public void init() {
        log.info("腾讯云直播服务初始化");
    }

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

    public String getAppName() {
        return StrUtil.format(appNameTemplate, trtcLiveConf.getAppId());
    }


    /**
     * 返回流名称
     * streamId 为 ${sdkAppId}_${roomId}_${userId}_${streamType}
     * streamType： 摄像头画面为 main，屏幕分享为 aux （WebRTC 由于同时只支持一路上行，因此 WebRTC 上屏幕分享的流类型是 main）
     *
     * @param request 请求参数
     * @return stream name
     */
    public String getStreamName(LiveRequest request) {
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
        return api.genUserSig(userId, 180 * 86400);
    }


    public DropLiveStreamResponse stopStream(String streamName) {

        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
        Credential cred = new Credential(trtcLiveConf.getSecretId(), trtcLiveConf.getSecretKey());
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("live.tencentcloudapi.com");
        if (waProxyConf.getEnable()) {
            httpProfile.setProxyHost(waProxyConf.getIp());
            httpProfile.setProxyPort(waProxyConf.getPort());
        }
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        LiveClient client = new LiveClient(cred, "", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DropLiveStreamRequest req = new DropLiveStreamRequest();
        req.setStreamName(streamName);
        req.setDomainName(conf.getPushDomain());
        req.setAppName(getAppName());
        // 返回的resp是一个DropLiveStreamResponse的实例，与请求对象对应
        DropLiveStreamResponse resp = null;
        try {
            resp = client.DropLiveStream(req);
        } catch (TencentCloudSDKException e) {
            log.error("fail:", e);
            return null;
        }
        log.info("断流结果{}", JSON.toJSONString(resp));
        return resp;
    }


    public DescribeLiveStreamOnlineListResponse getLiveList() {
        Credential cred = new Credential(trtcLiveConf.getSecretId(), trtcLiveConf.getSecretKey());
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("live.tencentcloudapi.com");
        if (waProxyConf.getEnable()) {
            httpProfile.setProxyHost(waProxyConf.getIp());
            httpProfile.setProxyPort(waProxyConf.getPort());
        }
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        LiveClient client = new LiveClient(cred, "", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DescribeLiveStreamOnlineListRequest req = new DescribeLiveStreamOnlineListRequest();
        req.setPageSize(20L);
        // 返回的resp是一个DescribeLiveStreamOnlineListResponse的实例，与请求对象对应
        DescribeLiveStreamOnlineListResponse resp = null;
        try {
            resp = client.DescribeLiveStreamOnlineList(req);
        } catch (TencentCloudSDKException e) {
            log.error("fail:", e);
        }
        return resp;
    }

    public void stopStream(String streamName, String appName, String domainName) {

        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
        Credential cred = new Credential(trtcLiveConf.getSecretId(), trtcLiveConf.getSecretKey());
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("live.tencentcloudapi.com");
        if (waProxyConf.getEnable()) {
            httpProfile.setProxyHost(waProxyConf.getIp());
            httpProfile.setProxyPort(waProxyConf.getPort());
        }
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        LiveClient client = new LiveClient(cred, "", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DropLiveStreamRequest req = new DropLiveStreamRequest();
        req.setStreamName(streamName);
        req.setDomainName(domainName);
        req.setAppName(appName);
        // 返回的resp是一个DropLiveStreamResponse的实例，与请求对象对应
        DropLiveStreamResponse resp = null;
        try {
            resp = client.DropLiveStream(req);
        } catch (TencentCloudSDKException e) {
            log.error("fail:", e);
        }
        log.info("断流结果{}", JSON.toJSONString(resp));
    }
}
