package cn.qbs.wa.teach.common.live.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.live.config.TencentTRTCLiveConf;
import cn.qbs.wa.teach.common.live.config.WaProxyConf;
import cn.qbs.wa.teach.common.live.pojo.board.WhiteBoardPushResponse;
import com.alibaba.fastjson.JSON;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tiw.v20190919.TiwClient;
import com.tencentcloudapi.tiw.v20190919.models.*;
import com.tencentyun.TLSSigAPIv2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/6/23 17:31
 */

@Component
@Slf4j
public class TencentWhiteBoardService {

    @Autowired
    TencentTRTCLiveConf trtcLiveConf;

    @Autowired
    WaProxyConf waProxyConf;


    //urlencode(SdkAppID_RoomID_PushUserID_main)
    private String streamTemplate = "{}_{}_{}_main";

    public String getStreamName(String roomId, String pushUserId) {
        return StrUtil.format(streamTemplate, trtcLiveConf.getAppId(), roomId, pushUserId);
    }

    public WhiteBoardPushResponse pushStream(String userId, Long roomId) {

        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
        Credential cred = new Credential(trtcLiveConf.getSecretId(), trtcLiveConf.getSecretKey());
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("tiw.tencentcloudapi.com");
        if (waProxyConf.getEnable()) {
            httpProfile.setProxyHost(waProxyConf.getIp());
            httpProfile.setProxyPort(waProxyConf.getPort());
        }
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        TiwClient client = new TiwClient(cred, "ap-guangzhou", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        StartWhiteboardPushRequest req = new StartWhiteboardPushRequest();
        req.setSdkAppId(Long.valueOf(trtcLiveConf.getAppId()));
        req.setRoomId(roomId);
        req.setPushUserId(userId);
        req.setPushUserSig(getUserSig(userId));
        req.setVideoBitrate(2000L);

        Whiteboard whiteboard1 = new Whiteboard();
        whiteboard1.setWidth(1920L);
        whiteboard1.setHeight(1080L);
        req.setWhiteboard(whiteboard1);

        // 返回的resp是一个StartWhiteboardPushResponse的实例，与请求对象对应
        StartWhiteboardPushResponse resp = null;
        try {
            resp = client.StartWhiteboardPush(req);
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        // 输出json格式的字符串回包
        log.info("白板推流结果{}", JSON.toJSONString(resp));
        return BeanUtil.copyProperties(resp, WhiteBoardPushResponse.class);
    }

    public StopWhiteboardPushResponse stopStream(String taskId) {

        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
        Credential cred = new Credential(trtcLiveConf.getSecretId(), trtcLiveConf.getSecretKey());
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("tiw.tencentcloudapi.com");
        if (waProxyConf.getEnable()) {
            httpProfile.setProxyHost(waProxyConf.getIp());
            httpProfile.setProxyPort(waProxyConf.getPort());
        }
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        TiwClient client = new TiwClient(cred, "ap-guangzhou", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        StopWhiteboardPushRequest req = new StopWhiteboardPushRequest();
        req.setSdkAppId(Long.valueOf(trtcLiveConf.getAppId()));
        req.setTaskId(taskId);
        // 返回的resp是一个StopWhiteboardPushResponse的实例，与请求对象对应
        StopWhiteboardPushResponse resp = null;
        try {
            resp = client.StopWhiteboardPush(req);
        } catch (TencentCloudSDKException e) {
            log.error(e.toString());
            return null;
        }

        log.info("白板停止推流结果{}", JSON.toJSONString(resp));
        return resp;

    }

    public CreateTranscodeResponse createTranscode(String url) {
        Credential cred = new Credential(trtcLiveConf.getSecretId(), trtcLiveConf.getSecretKey());
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("tiw.tencentcloudapi.com");
        if (waProxyConf.getEnable()) {
            httpProfile.setProxyHost(waProxyConf.getIp());
            httpProfile.setProxyPort(waProxyConf.getPort());
        }
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        TiwClient client = new TiwClient(cred, "ap-guangzhou", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        CreateTranscodeRequest req = new CreateTranscodeRequest();
        req.setIsStaticPPT(true);
        req.setSdkAppId(Long.valueOf(trtcLiveConf.getAppId()));
        req.setUrl(url);
        // 返回的resp是一个CreateTranscodeResponse的实例，与请求对象对应
        CreateTranscodeResponse resp = null;
        try {
            resp = client.CreateTranscode(req);
        } catch (TencentCloudSDKException e) {
            log.error("fail:{}", e);
        }
        log.info("白板文件转码结果{}", JSON.toJSONString(resp));
        return resp;
    }


    public String getUserSig(String userId) {
        TLSSigAPIv2 api = new TLSSigAPIv2(Long.valueOf(trtcLiveConf.getAppId()), trtcLiveConf.getPriKey());
        return api.genUserSig(userId, 180 * 86400);
    }


    public DescribeTranscodeResponse getTranscodeResult(String taskId) {

        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
        Credential cred = new Credential(trtcLiveConf.getSecretId(), trtcLiveConf.getSecretKey());
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("tiw.tencentcloudapi.com");
        if (waProxyConf.getEnable()) {
            httpProfile.setProxyHost(waProxyConf.getIp());
            httpProfile.setProxyPort(waProxyConf.getPort());
        }
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        TiwClient client = new TiwClient(cred, "ap-guangzhou", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DescribeTranscodeRequest req = new DescribeTranscodeRequest();
        req.setSdkAppId(Long.valueOf(trtcLiveConf.getAppId()));
        req.setTaskId(taskId);
        // 返回的resp是一个DescribeTranscodeResponse的实例，与请求对象对应
        DescribeTranscodeResponse resp = null;
        try {
            resp = client.DescribeTranscode(req);
        } catch (TencentCloudSDKException e) {
            log.error("fail:{}", e);
        }
        // 输出json格式的字符串回包
        log.info("白板文件转码结果{}", JSON.toJSONString(resp));
        return resp;
    }
}
