package cn.qbs.wa.teach.common.live.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.live.config.TencentVodConf;
import cn.qbs.wa.teach.common.live.config.WaProxyConf;
import cn.qbs.wa.teach.common.live.pojo.vod.*;
import com.alibaba.fastjson.JSON;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/6/14 8:56
 */
@Component
@Slf4j
public class TencentVodService {

    @Autowired
    TencentVodConf tencentVodConf;

    @Autowired
    WaProxyConf waProxyConf;


    public VodClipResponse clip(VodClipRequest request) {
        Credential cred = new Credential(tencentVodConf.getSecretId(), tencentVodConf.getSecretKey());
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("vod.tencentcloudapi.com");
        if (waProxyConf.getEnable()) {
            httpProfile.setProxyHost(waProxyConf.getIp());
            httpProfile.setProxyPort(waProxyConf.getPort());
        }
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        VodClient client = new VodClient(cred, "", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        SimpleHlsClipRequest req = new SimpleHlsClipRequest();
        req.setUrl(request.getUrl());
        req.setStartTimeOffset(request.getStartTimeOffset());
        req.setEndTimeOffset(request.getEndTimeOffset());
        req.setIsPersistence(1L);
        req.setSubAppId(Long.valueOf(tencentVodConf.getSubAppId()));
        // 返回的resp是一个SimpleHlsClipResponse的实例，与请求对象对应
        SimpleHlsClipResponse resp = null;
        try {
            resp = client.SimpleHlsClip(req);
        } catch (TencentCloudSDKException e) {
            log.error("fail:",e);
        }
        // 输出json格式的字符串回包
        log.info("剪辑结果{}", JSON.toJSONString(resp));
        return BeanUtil.copyProperties(resp, VodClipResponse.class);
    }


    public VodClipFullResponse clipFull(VodClipFullRequest request) {
        if (CollUtil.isNotEmpty(request.getClipList())) {
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            Credential cred = new Credential(tencentVodConf.getSecretId(), tencentVodConf.getSecretKey());
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vod.tencentcloudapi.com");
            if (waProxyConf.getEnable()) {
                httpProfile.setProxyHost(waProxyConf.getIp());
                httpProfile.setProxyPort(waProxyConf.getPort());
            }
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            VodClient client = new VodClient(cred, "", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            EditMediaRequest req = new EditMediaRequest();
            req.setInputType("File");
            req.setSubAppId(Long.valueOf(tencentVodConf.getSubAppId()));

            List<VodClipRequest> clipList = request.getClipList();
            List<EditMediaFileInfo> editMediaFileInfoList = new ArrayList<>();
            for (VodClipRequest vodClipRequest : clipList) {
                EditMediaFileInfo editMediaFileInfo = new EditMediaFileInfo();
                editMediaFileInfo.setFileId(vodClipRequest.getFileId());
                editMediaFileInfo.setStartTimeOffset(vodClipRequest.getStartTimeOffset());
                editMediaFileInfo.setEndTimeOffset(vodClipRequest.getEndTimeOffset());
                editMediaFileInfoList.add(editMediaFileInfo);
            }

            req.setFileInfos(editMediaFileInfoList.toArray(new EditMediaFileInfo[editMediaFileInfoList.size()]));

            // 返回的resp是一个EditMediaResponse的实例，与请求对象对应
            EditMediaResponse resp = null;
            try {
                resp = client.EditMedia(req);
                log.info("剪辑结果{}", JSON.toJSONString(resp));
                return BeanUtil.copyProperties(resp, VodClipFullResponse.class);
            } catch (TencentCloudSDKException e) {
                log.error("fail:",e);
            }

        }

        return null;
    }

    public EditMediaTask getTaskDetail(String taskId) {
        Credential cred = new Credential(tencentVodConf.getSecretId(), tencentVodConf.getSecretKey());
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("vod.tencentcloudapi.com");
        if (waProxyConf.getEnable()) {
            httpProfile.setProxyHost(waProxyConf.getIp());
            httpProfile.setProxyPort(waProxyConf.getPort());
        }
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        VodClient client = new VodClient(cred, "", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DescribeTaskDetailRequest req = new DescribeTaskDetailRequest();
        req.setSubAppId(Long.valueOf(tencentVodConf.getSubAppId()));
        req.setTaskId(taskId);
        // 返回的resp是一个DescribeTaskDetailResponse的实例，与请求对象对应
        DescribeTaskDetailResponse resp = null;
        try {
            resp = client.DescribeTaskDetail(req);
            log.info("任务获取结果{}", JSON.toJSONString(resp));
            return resp.getEditMediaTask();
        } catch (TencentCloudSDKException e) {
            log.error("fail:",e);
        }
        return null;
    }


    public VodDownloadResponse download(VodDownloadRequest request) {

        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
        Credential cred = new Credential(tencentVodConf.getSecretId(), tencentVodConf.getSecretKey());
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("vod.tencentcloudapi.com");
        if (waProxyConf.getEnable()) {
            httpProfile.setProxyHost(waProxyConf.getIp());
            httpProfile.setProxyPort(waProxyConf.getPort());
        }
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        VodClient client = new VodClient(cred, "", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        ProcessMediaRequest req = new ProcessMediaRequest();
        req.setFileId(request.getFileId());
        MediaProcessTaskInput mediaProcessTaskInput1 = new MediaProcessTaskInput();

        TranscodeTaskInput[] transcodeTaskInputs1 = new TranscodeTaskInput[1];
        TranscodeTaskInput transcodeTaskInput1 = new TranscodeTaskInput();
        transcodeTaskInput1.setDefinition(request.getDEFINITION());
        transcodeTaskInputs1[0] = transcodeTaskInput1;

        mediaProcessTaskInput1.setTranscodeTaskSet(transcodeTaskInputs1);

        req.setMediaProcessTask(mediaProcessTaskInput1);

        // 返回的resp是一个ProcessMediaResponse的实例，与请求对象对应
        ProcessMediaResponse resp = null;
        try {
            resp = client.ProcessMedia(req);
        } catch (TencentCloudSDKException e) {
            log.error("fail:",e);
        }
        // 输出json格式的字符串回包
        log.info("任务获取结果{}", JSON.toJSONString(resp));
        return BeanUtil.copyProperties(resp, VodDownloadResponse.class);


    }
}
