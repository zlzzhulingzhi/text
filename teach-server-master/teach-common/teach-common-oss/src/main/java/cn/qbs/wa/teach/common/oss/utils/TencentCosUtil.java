package cn.qbs.wa.teach.common.oss.utils;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.oss.config.TencentCosProperties;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.utils.IOUtils;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sts.v20180813.StsClient;
import com.tencentcloudapi.sts.v20180813.models.GetFederationTokenRequest;
import com.tencentcloudapi.sts.v20180813.models.GetFederationTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * @author yjx
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({TencentCosProperties.class})
public class TencentCosUtil {

    @Resource
    private TencentCosProperties txCosProperties;

    /**
     * 临时密钥有效时长
     */
    private static final long CREDENTIAL_DURATION_SECONDS = 86400L;

    /**
     * allowPrefix
     */
    public static final String DEFAULT_ALLOW_PREFIX = "*";

    /**
     * 终端地址
     */
    public static final String ENDPOINT = "sts.tencentcloudapi.com";

    /**
     * 名称
     */
    public static final String COS_NAME = "cos-sts-java";

    /**
     * 所有上传
     */
    public static final String UPLOAD_TYPE_ALL = "all";

    /**
     * 简单上传
     */
    private static final String UPLOAD_TYPE_SIMPLE = "simple";

    /**
     * 分块上传
     */
    private static final String UPLOAD_TYPE_MULTIPART = "multipart";

    /**
     * 获取腾讯云 COS 临时密钥
     *
     * @param type simple(简单上传) / multipart(分块上传)
     * @return 临时密钥信息
     */
    public String getCosCredential(String type) {
        //腾讯云已会自己识别分块上传
        return this.getAllCredential();
//        if (UPLOAD_TYPE_SIMPLE.equalsIgnoreCase(type)) {
//            credential = this.getSimpleCredential();
//        } else if (UPLOAD_TYPE_MULTIPART.equalsIgnoreCase(type)) {
//            credential = this.getMultipartCredential();
//        } else {
//            credential = this.getAllCredential();
//        }
    }

    /**
     * 简单上传
     * notes: 最大支持上传5GB文件。如需上传大于5GB的文件，请使用 分块上传，详见：https://cloud.tencent.com/document/product/436/7749
     *
     * @return 临时密钥
     */
    private String getSimpleCredential() {
        // 密钥的权限列表。简单上传、表单上传和分片上传需要以下的权限
        // 其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
        String[] allowActions = new String[]{
                // 简单上传
                "name/cos:PutObject",
                //获取详情
                "name/cos:GetObject"
        };
        try {
            JSONObject credential = this.getCredential(allowActions);
            // 成功返回临时密钥信息
            return credential.toString();
        } catch (Exception e) {
            // 失败抛出异常
            log.error("腾讯云获取临时密钥失败.", e);
            throw new IllegalArgumentException("腾讯云获取临时密钥失败!");
        }
    }

    /**
     * 分块上传
     *
     * @return 临时密钥
     */
    private String getMultipartCredential() {
        // 密钥的权限列表。简单上传、表单上传和分片上传需要以下的权限
        // 其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
        String[] allowActions = new String[]{
                //分块上传：初始化分块操作
                "name/cos:InitiateMultipartUpload",
                //分块上传：List 进行中的分块上传
                "name/cos:ListMultipartUploads",
                //分块上传：List 已上传分块操作
                "name/cos:ListParts",
                //分块上传：上传分块块操作
                "name/cos:UploadPart",
                //分块上传：完成所有分块上传操作
                "name/cos:CompleteMultipartUpload",
                //取消分块上传操作
                "name/cos:AbortMultipartUpload",
                //获取详情,
                "name/cos:GetObject"
        };
        try {
            JSONObject credential = this.getCredential(allowActions);
            // 成功返回临时密钥信息
            return credential.toString();
        } catch (Exception e) {
            // 失败抛出异常
            log.error("腾讯云获取临时密钥失败.", e);
            throw new IllegalArgumentException("腾讯云获取临时密钥失败 !");
        }
    }


    /**
     * 所有模式上传
     *
     * @return 临时密钥
     */
    private String getAllCredential() {
        // 密钥的权限列表。简单上传、表单上传和分片上传需要以下的权限
        // 其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
        String[] allowActions = new String[]{
                // 简单上传
                "name/cos:PutObject",
                //表单上传对象
                "name/cos:PostObject",
                //分块上传：初始化分块操作
                "name/cos:InitiateMultipartUpload",
                //分块上传：List 进行中的分块上传
                "name/cos:ListMultipartUploads",
                //分块上传：List 已上传分块操作
                "name/cos:ListParts",
                //分块上传：上传分块块操作
                "name/cos:UploadPart",
                //分块上传：完成所有分块上传操作
                "name/cos:CompleteMultipartUpload",
                //取消分块上传操作
                "name/cos:AbortMultipartUpload",
                //获取详情,
                "name/cos:GetObject",
        };
        try {
            JSONObject credential = this.getCredential(allowActions);
            // 成功返回临时密钥信息
            return credential.toString();
        } catch (Exception e) {
            // 失败抛出异常
            log.error("腾讯云获取临时密钥失败.", e);
            throw new IllegalArgumentException("腾讯云获取临时密钥失败 !");
        }
    }

    private TreeMap<String, Object> getCommonConfigMap() {
        TreeMap<String, Object> config = new TreeMap<>();
        config.put("SecretId", this.txCosProperties.getSecretId());
        config.put("SecretKey", this.txCosProperties.getSecretKey());
        config.put("region", this.txCosProperties.getRegion());
        config.put("bucket", this.txCosProperties.getBucket());
        // 临时密钥有效时长，单位是秒，默认1800秒，最长可设定有效期为7200秒
        config.put("durationSeconds", CREDENTIAL_DURATION_SECONDS);
        // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的具体路径，例子：a.jpg 或者 a/* 或者 * 。
        // 如果填写了“*”，将允许用户访问所有资源；除非业务需要，否则请按照最小权限原则授予用户相应的访问权限范围。
        config.put("allowPrefix", "*");
        // 返回公共配置信息
        return config;
    }

    private JSONObject getCredential(String[] allowActions) throws TencentCloudSDKException {
        return this.getCredential(DEFAULT_ALLOW_PREFIX, allowActions);
    }

    private JSONObject getCredential(String allowPrefix, String[] allowActions) throws TencentCloudSDKException {
        if (log.isDebugEnabled()) {
            log.debug("腾讯cos配置：{}", txCosProperties);
        }
        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
        Credential cred = new Credential(txCosProperties.getSecretId(), txCosProperties.getSecretKey());
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(ENDPOINT);
        // 开启代理
        if (StrUtil.isAllNotBlank(this.txCosProperties.getProxyHost(), this.txCosProperties.getProxyPort())) {
            httpProfile.setProxyHost(this.txCosProperties.getProxyHost());
            httpProfile.setProxyPort(Integer.parseInt(this.txCosProperties.getProxyPort()));
        }
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        StsClient client = new StsClient(cred, txCosProperties.getRegion(), clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        GetFederationTokenRequest req = new GetFederationTokenRequest();
        req.setName(COS_NAME);
        req.setPolicy(getPolicy(allowPrefix, allowActions));
        req.setDurationSeconds(CREDENTIAL_DURATION_SECONDS);
        // 返回的resp是一个GetFederationTokenResponse的实例，与请求对象对应
        GetFederationTokenResponse resp = client.GetFederationToken(req);
        // 输出json格式的字符串回包
        if (log.isDebugEnabled()) {
            log.debug(GetFederationTokenResponse.toJsonString(resp));
        }
        JSONObject jsonObject = new JSONObject(GetFederationTokenResponse.toJsonString(resp));
        return downCompat(jsonObject);
    }

    /**
     * 构造 Policy
     */
    private String getPolicy(String allowPrefix, String[] allowActions) {
        String bucket = txCosProperties.getBucket();
        String region = txCosProperties.getRegion();
        int lastSplit = bucket.lastIndexOf("-");
        String appId = bucket.substring(lastSplit + 1);

        // 构造操作权限
        JSONArray statements = new JSONArray();
        statements.put(getStatement(bucket, region, appId, allowPrefix, allowActions));
        statements.put(getSpecialStatement(bucket, region, appId));

        JSONObject policy = new JSONObject();
        policy.put("version", "2.0");
        policy.put("statement", statements);
        if (log.isDebugEnabled()) {
            log.info("构造腾讯cos策略: {}", policy);
        }
        return policy.toString();
    }

    /**
     * 构造 操作权限
     * @param bucket 桶
     * @param region 区域
     * @param appId  应用ID
     * @param allowPrefix  授予权限的目录
     * @param allowActions 授予权限码
     */
    private JSONObject getStatement(String bucket, String region, String appId, String allowPrefix, String[] allowActions) {
        JSONObject statement = new JSONObject();
        statement.put("effect", "allow");
        if (!allowPrefix.startsWith("/")) {
            allowPrefix = "/" + allowPrefix;
        }
        statement.put("resource", String.format("qcs::cos:%s:uid/%s:%s%s", region, appId, bucket, allowPrefix));
        JSONArray actions = new JSONArray();
        for (String action : allowActions) {
            actions.put(action);
        }
        statement.put("action", actions);
        return statement;
    }

    /**
     * 仅提供 /train/platform/video/ 目录下, 获取 查询对象元数据 操作权限
     * @param bucket 桶
     * @param region 区域
     * @param appId  应用ID
     */
    private JSONObject getSpecialStatement(String bucket, String region, String appId) {
        JSONObject statement = new JSONObject();
        statement.put("effect", "allow");
        statement.put("resource", String.format("qcs::cos:%s:uid/%s:%s%s", region, appId, bucket, "/train/platform/video/*"));
        statement.put("action", new JSONArray("[\"name/cos:HeadObject\"]"));
        return statement;
    }

    /**
     * v2接口的key首字母小写，v3改成大写，此处做了向下兼容
     */
    private JSONObject downCompat(JSONObject resultJson) {
        JSONObject dcJson = new JSONObject();

        for (String key : resultJson.keySet()) {
            Object value = resultJson.get(key);
            if (value instanceof JSONObject) {
                dcJson.put(headerToLowerCase(key), downCompat((JSONObject) value));
            } else {
                String newKey = "Token".equals(key) ? "sessionToken" : headerToLowerCase(key);
                dcJson.put(newKey, resultJson.get(key));
            }
        }
        return dcJson;
    }

    private String headerToLowerCase(String source) {
        return Character.toLowerCase(source.charAt(0)) + source.substring(1);
    }

    public String putObject(File file,String filePath) {

        COSClient cosClient = createCosClient();
        String bucketName = txCosProperties.getBucket();
        String key = filePath + FileNameUtil.getName(file);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setHeader("expires", new Date(1660000000000L));

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
        putObjectRequest.withMetadata(objectMetadata);
        try {
            cosClient.putObject(putObjectRequest);
        } finally {
            // 确认本进程不再使用 cosClient 实例之后，关闭之
            cosClient.shutdown();
        }
        return txCosProperties.getViewPrefix() + key;
    }

    public byte[] getObject(String key) {
        // 移除预览前缀, 得到真实key值
        List<String> prefixList = Arrays.asList("https", "http", txCosProperties.getViewPrefix());
        for (String prefix : prefixList) {
            if (StrUtil.startWith(key, prefix)) {
                key = StrUtil.removePrefix(key, prefix);
            }
        }
        COSClient cosClient = createCosClient();
        try {
            String bucketName = txCosProperties.getBucket();
            COSObject cosObject = cosClient.getObject(bucketName, key);
            // 处理下载到的流
            // 这里是直接读取，按实际情况来处理
            try (COSObjectInputStream cosObjectInput = cosObject.getObjectContent()) {
                return IOUtils.toByteArray(cosObjectInput);
            } catch (IOException e) {
                log.error("COS处理下载到的文件流异常", e);
                throw new RuntimeException(e.getMessage());
            }
        } finally {
            // 确认本进程不再使用 cosClient 实例之后，关闭之
            cosClient.shutdown();
        }
    }

    /**
     * 创建 COSClient 实例，这个实例用来后续调用请求
     * @return cos客户端
     */
    private COSClient createCosClient() {
        COSCredentials cred = new BasicCOSCredentials(txCosProperties.getSecretId(), txCosProperties.getSecretKey());
        // 设置bucket的区域, COS地域的简称请参照 https://www.qcloud.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(txCosProperties.getRegion()));
        // 生成cos客户端
        return new COSClient(cred, clientConfig);
    }

}
