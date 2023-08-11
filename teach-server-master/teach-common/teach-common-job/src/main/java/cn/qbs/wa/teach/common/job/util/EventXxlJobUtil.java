package cn.qbs.wa.teach.common.job.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.qbs.wa.teach.common.job.pojo.DTO.XxlSearchDTO;
import cn.qbs.wa.teach.common.job.pojo.entity.XxlJobGroup;
import cn.qbs.wa.teach.common.job.pojo.entity.XxlJobInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.biz.model.RegistryParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.RegistryConfig;
import com.xxl.job.core.util.DateUtil;
import com.xxl.job.core.util.IpUtil;
import com.xxl.job.core.util.NetUtil;
import com.xxl.job.core.util.XxlJobRemotingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.net.HttpCookie;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/30 17:07
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class EventXxlJobUtil {

    @Value("${xxl.job.admin.login-username:admin}")
    private String loginUsername;

    @Value("${xxl.job.admin.login-pwd:123456}")
    private String loginPwd;

    @Value("${xxl.job.executor.ip:}")
    private String ip;

    @Value("${xxl.job.admin.addresses:''}")
    private String adminAddresses;

    @Value("${xxl.job.executor.port:-1}")
    private int port;

    @Value("${xxl.job.executor.appname}")
    private String appName;

    @Value("${xxl.job.accessToken:}")
    private String accessToken;

    private static final String XXL_JOB_COOKIE_KEY = "xxl-job-cookie";

    private final RedisTemplate redisTemplate;


    /**
     * 增加执行任务
     *
     * @param eventId      事件id
     * @param executorDate 执行日期
     */
    public void addExecutorTask(Long eventId, String jobHandler, Object params, Date executorDate) {
        String desc = DateUtil.format(executorDate, DatePattern.CHINESE_DATE_TIME_PATTERN + "执行资源分发任务(" + eventId + ")");
        String cron = getCron(executorDate);

        XxlJobInfo xxlJobInfo = new XxlJobInfo();
        xxlJobInfo.setJobGroup(getJobGroupId());
        xxlJobInfo.setJobDesc(desc);
        xxlJobInfo.setExecutorRouteStrategy("FIRST");
        xxlJobInfo.setScheduleType("CRON");
        xxlJobInfo.setScheduleConf(cron);
        xxlJobInfo.setGlueType("BEAN");
        xxlJobInfo.setExecutorHandler(jobHandler);
        xxlJobInfo.setExecutorBlockStrategy("SERIAL_EXECUTION");
        xxlJobInfo.setExecutorTimeout(5);
        xxlJobInfo.setExecutorFailRetryCount(2);
        xxlJobInfo.setAuthor("wa");
        xxlJobInfo.setTriggerStatus(1);
        xxlJobInfo.setGlueRemark(desc);
        xxlJobInfo.setMisfireStrategy("DO_NOTHING");
        xxlJobInfo.setExecutorParam(JSON.toJSONString(params));
        HashMap paramMap = JSON.parseObject(JSON.toJSONString(xxlJobInfo), HashMap.class);
        log.info("增加xxl执行任务,请求参数:{}", paramMap);
        HttpResponse response = HttpRequest.post(adminAddresses + "/jobinfo/add").form(paramMap).cookie(getCookie()).execute();
        if (response.isOk()) {
            JSONObject jsonObject = JSON.parseObject(response.body());
            log.info("增加xxl执行任务成功,返回信息:{}", jsonObject);
            return;
        }
        log.error("调用xxl增加执行任务失败:{}", JSON.parseObject(response.body()));


    }

    /**
     * 获取执行id
     *
     * @return int
     */
    private int getJobGroupId() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("appname", appName);
        paramMap.put("start", "0");
        paramMap.put("length", "100");
        log.info("获取xxl执行id,请求参数:{}", paramMap);
        //通过appname 查询appname对应的id
        HttpResponse response = HttpRequest.post(adminAddresses + "/jobgroup/pageList").form(paramMap).cookie(getCookie()).execute();
        if (response.isOk()) {
            XxlSearchDTO xxlSearchDto = JSON.parseObject(response.body(), XxlSearchDTO.class);
            log.info("获取xxl执行器成功,返回信息:{}", xxlSearchDto);
            List<XxlJobGroup> list = xxlSearchDto.getData();
            if (CollectionUtils.isEmpty(list)) {
                //新增成功没有返回id
                createJobGroup();
                //在调一次列表查询 获取组id
                return getJobGroupId();
            }
            return list.get(0).getId();
        }
        log.error("调用xxl获取执行器失败:{}", JSON.parseObject(response.body()));
        throw new RuntimeException();
    }

    /**
     * 创建执行器
     */
    private void createJobGroup() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("appname", appName);
        paramMap.put("title", appName);
        //注册方式  0自动  1 为手动注册
        paramMap.put("addressType", "0");
        log.info("调用xxl增加执行器,请求参数：{}", paramMap);
        HttpResponse response = HttpRequest.post(adminAddresses + "/jobgroup/save").form(paramMap).cookie(getCookie()).execute();
        if (response.isOk()) {
            log.info("调用xxl增加执行器,成功返回信息:{}", JSON.parseObject(response.body()));
            //注册执行器
            registry();
            return;
        }

    }

    /**
     * 注册执行器
     * 不能设置超时
     * AdminBiz adminBiz = new AdminBizClient(adminAddresses, accessToken)
     * ReturnT<String> returnT = adminBiz.registry(registryParam);
     */
    private void registry() {
        //copy xxl-job 源码
        port = port > 0 ? port : NetUtil.findAvailablePort(9999);
        ip = (ip != null && ip.trim().length() > 0) ? ip : IpUtil.getIp();
        String ipPort = IpUtil.getIpPort(ip, port);
        String address = "http://{ip_port}/".replace("{ip_port}", ipPort);
        RegistryParam registryParam = new RegistryParam(RegistryConfig.RegistType.EXECUTOR.name(), appName, address);
        ReturnT<String> returnT = XxlJobRemotingUtil.postBody(adminAddresses + "api/registry", accessToken, 6, registryParam, String.class);
        log.info("注册执行器返回结果：{}", returnT);

    }


    /***
     * 生成 日期对应的  cron表达式
     * convert Date to cron ,eg.  "0 06 10 15 1 ? 2014"
     * @param date  : 时间点
     * @return String
     */
    private String getCron(Date date) {
        String dateFormat = "ss mm HH dd MM ? yyyy";
        return DateUtil.format(date, dateFormat);
    }


    /**
     * 获取cookie
     *
     * @return String
     */
    private String getCookie() {
        String cookieStr = (String) redisTemplate.opsForValue().get(XXL_JOB_COOKIE_KEY);
        if (cookieStr != null && !cookieStr.isEmpty()) {
            return cookieStr;
        }
        log.info("登录 XXL-JOB 获取 Cookie ...");
        String path = adminAddresses + "/login";
        Map<String, Object> hashMap = new HashMap<>(3);
        hashMap.put("userName", loginUsername);
        hashMap.put("password", loginPwd);
        hashMap.put("ifRemember", "on");
        log.info("获取xxl cookie,请求参数：{}", hashMap);
        HttpResponse response = HttpRequest.post(path).form(hashMap).execute();
        boolean ok = response.isOk();
        if (ok) {
            List<HttpCookie> cookies = response.getCookies();
            log.info("获取xxl cookie成功,返回信息:{}", cookies);
            StringBuilder sb = new StringBuilder();
            for (HttpCookie cookie : cookies) {
                sb.append(cookie.toString());
            }
            cookieStr = sb.toString();
            // Redis缓存有效期比Cookie实际有效期短一些
            Long timeout = Long.valueOf(Integer.MAX_VALUE - 100);
            redisTemplate.opsForValue().set(XXL_JOB_COOKIE_KEY, cookieStr, timeout, TimeUnit.SECONDS);
            return cookieStr;
        }
        log.error("调用xxl获取cookie失败:{}", JSON.parseObject(response.body()));
        throw new RuntimeException();
    }


}
