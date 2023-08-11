package cn.qbs.wa.teach.common.weixin.miniapp.service;

import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

/**
 * 小程序码服务
 *
 * @author yjx
 */
@Service
@RefreshScope
public class MiniAppCodeService {

    @Value("${tempDir}")
    private String tempDir;

    @Value("${wx.miniapp.envVersion:release}")
    private String envVersion;

    @Resource
    private WxMaService wxMaService;

    public File createMiniAppCode(String page, String scene) throws WxErrorException {
        // 获取小程序二维码生成实例
        WxMaQrcodeService wxMaQrcodeService = wxMaService.getQrcodeService();
        return wxMaQrcodeService.createWxaCodeUnlimit(scene, page, tempDir, false, envVersion, 430, true, null, false);
    }
}