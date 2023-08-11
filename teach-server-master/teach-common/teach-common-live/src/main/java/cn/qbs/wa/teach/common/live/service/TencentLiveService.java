package cn.qbs.wa.teach.common.live.service;

import cn.hutool.core.util.IdUtil;
import cn.qbs.wa.teach.common.live.config.TencentLiveConf;
import cn.qbs.wa.teach.common.live.config.WaProxyConf;
import cn.qbs.wa.teach.common.live.pojo.LiveAddressResponse;
import cn.qbs.wa.teach.common.live.pojo.LiveRequest;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.live.v20180801.LiveClient;
import com.tencentcloudapi.live.v20180801.models.*;
import com.tencentcloudapi.tiw.v20190919.models.StartWhiteboardPushResponse;
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
 * @date 2021/12/15 8:56
 */
@Service
@Slf4j
public class TencentLiveService implements BaseLiveService {


    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    @Autowired
    TencentLiveConf conf;

    @Autowired
    WaProxyConf waProxyConf;



    @Override
    public LiveAddressResponse getLiveAddress(LiveRequest request) {
        String streamName = IdUtil.getSnowflake().nextIdStr();
        Long endTime = request.getEndTime();
        String pushSafeUrl = getPushSafeUrl(streamName, endTime);
        String pullSafeUrl = getPullSafeUrl(streamName, endTime);
        LiveAddressResponse liveAddress = new LiveAddressResponse();
        String pushDomain = conf.getPushDomain();
        String appName = conf.getAppName();
        String pullDomain = conf.getPullDomain();
        liveAddress.setPushRtmpUrl("rtmp://" + pushDomain + "/" + appName + "/" + streamName + "?" + pushSafeUrl);
        liveAddress.setPushWebrtcUrl("webrtc://" + pushDomain + "/" + appName + "/" + streamName + "?" + pushSafeUrl);
        liveAddress.setPullRtmpUrl("rtmp://" + pullDomain + "/" + appName + "/" + streamName + "?" + pullSafeUrl);
        // TODO 暂时先不配置https证书，先搞完demo再弄
        liveAddress.setPullFlvUrl("https://" + pullDomain + "/" + appName + "/" + streamName + ".flv?" + pullSafeUrl);
        liveAddress.setPullM3u8Url("https://" + pullDomain + "/" + appName + "/" + streamName + ".m3u8?" + pullSafeUrl);
        liveAddress.setPullUdpUrl("webrtc://" + pullDomain + "/" + appName + "/" + streamName + "?" + pullSafeUrl);
        liveAddress.setPushObsUrl("rtmp://" + pushDomain + "/" + appName + "/");
        liveAddress.setPushObsSecret(streamName + "?" + pushSafeUrl);
        liveAddress.setStreamName(streamName);
        return liveAddress;
    }

    /**
     * 取得推流MD5后缀
     * KEY+ streamName + txTime
     *
     * @param streamName 流名称
     * @param txTime     有效时间
     * @return 结果字符串
     */
    private String getPushSafeUrl(String streamName, long txTime) {
        String input = new StringBuilder().
                append(conf.getPushKey()).
                append(streamName).
                append(Long.toHexString(txTime).toUpperCase()).toString();
        return generateMessageDigestUrl(input, txTime);
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


    public DescribeLiveStreamStateResponse getStreamStatus(String streamName) throws TencentCloudSDKException {
        Credential cred = new Credential(conf.getSecretId(), conf.getSecretKey());
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        if(waProxyConf.getEnable()){
            httpProfile.setProxyHost(waProxyConf.getIp());
            httpProfile.setProxyPort(waProxyConf.getPort());
        }
        httpProfile.setEndpoint("live.tencentcloudapi.com");
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        LiveClient client = new LiveClient(cred, "", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DescribeLiveStreamStateRequest req = new DescribeLiveStreamStateRequest();
        req.setAppName(conf.getAppName());
        req.setDomainName(conf.getPushDomain());
        req.setStreamName(streamName);

        // 返回的resp是一个DescribeLiveStreamStateResponse的实例，与请求对象对应
        return client.DescribeLiveStreamState(req);
    }



    public StreamOnlineInfo[] getOnlineStream() throws TencentCloudSDKException {
        Credential cred = new Credential(conf.getSecretId(), conf.getSecretKey());
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        if(waProxyConf.getEnable()){
            httpProfile.setProxyHost(waProxyConf.getIp());
            httpProfile.setProxyPort(waProxyConf.getPort());
        }

        httpProfile.setEndpoint("live.tencentcloudapi.com");
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        LiveClient client = new LiveClient(cred, "", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DescribeLiveStreamOnlineListRequest req = new DescribeLiveStreamOnlineListRequest();
        req.setPageSize(100L);
        // 返回的resp是一个DescribeLiveStreamOnlineListResponse的实例，与请求对象对应
        DescribeLiveStreamOnlineListResponse resp = client.DescribeLiveStreamOnlineList(req);
        return resp.getOnlineInfo();

    }




}
