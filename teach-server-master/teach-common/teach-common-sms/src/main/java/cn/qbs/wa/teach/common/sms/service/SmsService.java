package cn.qbs.wa.teach.common.sms.service;

import cn.qbs.wa.teach.common.sms.config.SmsConfig;

public interface SmsService {


    String sendVerCodeMsg(String phone, Integer length);


    void sendCustomMsg(String phone, String value, SmsConfig smsConfig);
}
