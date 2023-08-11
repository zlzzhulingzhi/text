package cn.qbs.wa.teach.common.live.service;

import cn.qbs.wa.teach.common.live.config.TencentLiveConf;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.trtc.v20190722.TrtcClient;
import com.tencentcloudapi.trtc.v20190722.models.DescribeCallDetailRequest;
import com.tencentcloudapi.trtc.v20190722.models.DescribeCallDetailResponse;
import com.tencentcloudapi.trtc.v20190722.models.DescribeRoomInformationRequest;
import com.tencentcloudapi.trtc.v20190722.models.DescribeRoomInformationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/1/3 14:23
 */
@Component
public class TencentTrtcStatisticService {

    @Autowired
    TencentLiveConf tencentLiveConf;


    /**
     * 　　* @description: 查询历史房间列表
     * 　　* @author vieux
     * 　　* @date 2022/1/3 15:40
     */
    public DescribeRoomInformationResponse roomInformationPage(String appId, String roomId, LocalDateTime startTime, LocalDateTime endTime) throws TencentCloudSDKException {

        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
        Credential cred = new Credential(tencentLiveConf.getSecretId(), tencentLiveConf.getSecretKey());
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("trtc.tencentcloudapi.com");
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        TrtcClient client = new TrtcClient(cred, "ap-guangzhou", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DescribeRoomInformationRequest req = new DescribeRoomInformationRequest();
        req.setSdkAppId(appId);
        req.setStartTime(startTime.toEpochSecond(ZoneOffset.of("+8")));
        req.setEndTime(endTime.toEpochSecond(ZoneOffset.of("+8")));
        req.setRoomId(roomId);
        req.setPageSize("10");
        // 返回的resp是一个DescribeRoomInformationResponse的实例，与请求对象对应
        return client.DescribeRoomInformation(req);
        // 输出json格式的字符串回包

    }

    /**
     * 　　* @description: 查询房间用户列表
     * 　　* @author vieux
     * 　　* @date 2022/1/3 15:41
     */
    public DescribeCallDetailResponse roomUserInformationPage(String appId, String commId, LocalDateTime endTime) throws TencentCloudSDKException {
        //  开始时间 结束时间 只允许1小时
        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
        Credential cred = new Credential(tencentLiveConf.getSecretId(), tencentLiveConf.getSecretKey());
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("trtc.tencentcloudapi.com");
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        TrtcClient client = new TrtcClient(cred, "ap-guangzhou", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DescribeCallDetailRequest req = new DescribeCallDetailRequest();
        req.setCommId(commId);
        req.setStartTime(endTime.minusMinutes(30L).toEpochSecond(ZoneOffset.of("+8")));
        req.setEndTime(endTime.toEpochSecond(ZoneOffset.of("+8")));
        req.setSdkAppId(appId);
        // 返回的resp是一个DescribeCallDetailResponse的实例，与请求对象对应
        return client.DescribeCallDetail(req);

    }


}
