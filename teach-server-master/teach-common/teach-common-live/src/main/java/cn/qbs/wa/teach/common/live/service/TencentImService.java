package cn.qbs.wa.teach.common.live.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.qbs.wa.teach.common.live.config.TencentTRTCLiveConf;
import cn.qbs.wa.teach.common.live.config.WaProxyConf;
import cn.qbs.wa.teach.common.live.pojo.im.GetGroupInfoRequestBody;
import cn.qbs.wa.teach.common.live.pojo.im.GetGroupInfoResponse;
import cn.qbs.wa.teach.common.live.pojo.im.ImGroupResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.tencentyun.TLSSigAPIv2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/6/1 10:10
 */
@Slf4j
@Component
public class TencentImService {


    @Autowired
    TencentTRTCLiveConf trtcLiveConf;

    @Autowired
    WaProxyConf waProxyConf;


    private final String getGroupUrl = "https://console.tim.qq.com/v4/group_open_http_svc/get_appid_group_list?usersig={}&identifier={}&sdkappid={}&contenttype=json";

    /**
     * 获取直播群在线人数url
     */
    public static final String GET_ONLINE_MEMBER_NUM_URL = "https://console.tim.qq.com/v4/group_open_http_svc/get_online_member_num?sdkappid=%s&identifier=%s&usersig=%s&random=%s&contenttype=json";

    /**
     * 获取群详细资料
     */
    public static final String GET_GROUP_INFO_URL = "https://console.tim.qq.com/v4/group_open_http_svc/get_group_info?sdkappid=%s&identifier=%s&usersig=%s&random=%s&contenttype=json";

    private final String delGroupUrl = "https://console.tim.qq.com/v4/group_open_http_svc/destroy_group?usersig={}&identifier={}&sdkappid={}&contenttype=json";

    public String getUserSig(String userId) {
        TLSSigAPIv2 api = new TLSSigAPIv2(Long.valueOf(trtcLiveConf.getAppId()), trtcLiveConf.getPriKey());
        return api.genUserSig(userId, 180 * 86400);
    }

    public List<ImGroupResponse> getGroupList(String userName, String userSig) {
        String groupUrl = StrUtil.format(getGroupUrl, userSig, userName, trtcLiveConf.getAppId());
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("Limit", 1000);
        paramMap.put("Next", 0);
        HttpRequest httpRequest = HttpRequest.post(groupUrl)
                .timeout(20000);//超时，毫秒
        if (waProxyConf.getEnable()) {
            httpRequest.setHttpProxy(waProxyConf.getIp(), waProxyConf.getPort());
        }
        String result = httpRequest.body(JSON.toJSONString(paramMap)).execute().body();
        if (StrUtil.isNotBlank(result)) {
            JSONObject jsonObject = JSON.parseObject(result);
            JSONArray groupIdList = jsonObject.getJSONArray("GroupIdList");
            return groupIdList.toJavaList(ImGroupResponse.class);
        }
        return null;
    }

    public String delGroup(String userName, String userSig, String groupId) {
        String groupUrl = StrUtil.format(delGroupUrl , userSig, userName, trtcLiveConf.getAppId());
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("GroupId", groupId);
        HttpRequest httpRequest = HttpRequest.post(groupUrl)
                .timeout(20000);//超时，毫秒
        if (waProxyConf.getEnable()) {
            httpRequest.setHttpProxy(waProxyConf.getIp(), waProxyConf.getPort());
        }
        return httpRequest.body(JSON.toJSONString(paramMap)).execute().body();
    }


    public Integer getOnlineMemberNum(String userName, String userSig, String groupId) {
        String getOnlineMemberNumUrl = String.format(GET_ONLINE_MEMBER_NUM_URL, trtcLiveConf.getAppId(), userName, userSig, RandomStringUtils.randomNumeric(32));
//        log.info("getOnlineMemberNumUrl: {}", getOnlineMemberNumUrl);
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("GroupId", groupId);
        HttpRequest httpRequest = HttpRequest.post(getOnlineMemberNumUrl).timeout(20000);
        if (waProxyConf.getEnable()) {
            httpRequest.setHttpProxy(waProxyConf.getIp(), waProxyConf.getPort());
        }
        String body = httpRequest.body(JSON.toJSONString(paramMap)).execute().body();
        JSONObject jsonObj = JSON.parseObject(body);
        if ("OK".equals(jsonObj.getString("ActionStatus"))) {
            return jsonObj.getInteger("OnlineMemberNum");
        } else {
            log.error("获取直播群在线人数失败: {}", body);
        }

        return null;
    }

    /**
     * 获取群详细资料
     * @param userName
     * @param userSig
     * @param requestBody
     * @return
     */
    public GetGroupInfoResponse getGroupInfo(String userName, String userSig, GetGroupInfoRequestBody requestBody) {
        String getGroupInfoUrl = String.format(GET_GROUP_INFO_URL, trtcLiveConf.getAppId(), userName, userSig, RandomStringUtils.randomNumeric(32));
        String requestBodyJson = JSON.toJSONString(requestBody);
        log.info("获取群详细资料, requestBodyJson: {}", requestBodyJson);
        HttpRequest httpRequest = HttpRequest.post(getGroupInfoUrl).body(requestBodyJson).timeout(10000);
        if (waProxyConf.getEnable()) {
            httpRequest.setHttpProxy(waProxyConf.getIp(), waProxyConf.getPort());
        }

        try {
            HttpResponse response = httpRequest.execute();
            String responseBody = response.body();
            GetGroupInfoResponse getGroupInfoResponse = JSON.parseObject(responseBody, new TypeReference<GetGroupInfoResponse>(){});
            if (!"OK".equals(getGroupInfoResponse.getActionStatus())) {
                log.error("获取直播群在线人数失败: {}", responseBody);
            }

            return getGroupInfoResponse;
        } catch (HttpException e) {
            log.error("获取直播群在线人数失败: ", e);
            e.printStackTrace();
        }

        return null;
    }

}
