package cn.qbs.wa.teach.common.sms.service;

import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.sms.config.AliSmsConfig;
import cn.qbs.wa.teach.common.sms.config.SmsConfig;
import cn.qbs.wa.teach.common.sms.config.WaProxyConf;
import cn.qbs.wa.teach.common.sms.utils.VerificationCodeUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.HttpClientConfig;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 9:00
 */
@Component
@Slf4j
public class AliSmsService implements SmsService {

    @Autowired
    AliSmsConfig aliSmsConfig;

    @Autowired
    WaProxyConf waProxyConf;


    /**
     * 　　* @description: length 验证码长度
     * 　　* @author vieux
     * 　　* @date 2021/11/2 9:17
     */


    @Override
    public String sendVerCodeMsg(String phone, Integer length) {

        DefaultProfile profile = DefaultProfile.getProfile(aliSmsConfig.getRegionId(), aliSmsConfig.getAccessKeyId(), aliSmsConfig.getAccessSecret());
        HttpClientConfig httpClientConfig = profile.getHttpClientConfig();
        if (waProxyConf.getEnable()) {
            String template = "http://{}:{}";
            httpClientConfig.setHttpProxy(StrUtil.format(template, waProxyConf.getIp().trim(), waProxyConf.getPort()));
        }
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(aliSmsConfig.getDoMain());//短信服务的服务接入地址
        request.setSysVersion(aliSmsConfig.getVersion());//API的版本号
        request.setSysAction(aliSmsConfig.getAction());//API的名称
        request.putQueryParameter("PhoneNumbers", phone);//接收短信的手机号码
        request.putQueryParameter("SignName", aliSmsConfig.getSignName());//短信签名名称
        request.putQueryParameter("TemplateCode", aliSmsConfig.getTemplateCode());//短信模板ID
        String code = VerificationCodeUtil.create(length);//生成随机码
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", code);
            request.putQueryParameter("TemplateParam", jsonObject.toString());//短信模板变量对应的实际值
            CommonResponse response = client.getCommonResponse(request);
            log.info("阿里短信:{}", response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return code;
    }

    @Override
    public void sendCustomMsg(String phone, String value, SmsConfig smsConfig) {
        if(smsConfig!=null){
            AliSmsConfig customConfig= (AliSmsConfig) smsConfig;
            DefaultProfile profile = DefaultProfile.getProfile(customConfig.getRegionId(), customConfig.getAccessKeyId(), customConfig.getAccessSecret());
            IAcsClient client = new DefaultAcsClient(profile);
            HttpClientConfig httpClientConfig = profile.getHttpClientConfig();
            if (waProxyConf.getEnable()) {
                String template = "http://{}:{}";
                httpClientConfig.setHttpProxy(StrUtil.format(template, waProxyConf.getIp().trim(), waProxyConf.getPort()));
            }
            CommonRequest request = new CommonRequest();
            request.setSysMethod(MethodType.POST);
            request.setSysDomain(customConfig.getDoMain());//短信服务的服务接入地址
            request.setSysVersion(customConfig.getVersion());//API的版本号
            request.setSysAction(customConfig.getAction());//API的名称
            request.putQueryParameter("PhoneNumbers", phone);//接收短信的手机号码
            request.putQueryParameter("SignName", customConfig.getSignName());//短信签名名称
            request.putQueryParameter("TemplateCode", customConfig.getTemplateCode());//短信模板ID
            request.putQueryParameter("TemplateParam", value);//短信模板变量对应的实际值
            //短信模板变量对应的实际值。支持传入多个参数，示例：{"name":"张三","number":"15038****76"}。
            try {
                CommonResponse response = client.getCommonResponse(request);
                log.info("阿里短信:{}", response.getData());
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }

    }


}
