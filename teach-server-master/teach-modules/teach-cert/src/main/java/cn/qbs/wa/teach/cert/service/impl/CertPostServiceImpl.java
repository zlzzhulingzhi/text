package cn.qbs.wa.teach.cert.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.cert.service.CertPostService;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.oss.utils.TencentCosUtil;
import cn.qbs.wa.teach.common.post.drawable.Poster;
import cn.qbs.wa.teach.common.post.utils.PdfUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/2/12 15:58
 */
@Service
@Slf4j
public class CertPostServiceImpl implements CertPostService {


    String filePathTemplate = "/cert/{}/";

    @Autowired
    TencentCosUtil tencentCosUtil;


    /**
     * 　　* @description: 证书生成
     * 　　* @author vieux
     * 　　* @date 2022/2/12 15:59
     */
    @Override
    public Map<Long, String> addCertPost(List<Poster> posterList) {
        if (CollUtil.isNotEmpty(posterList)) {
            Map<Long, String> certUrlMap = new HashMap<>();
            for (Poster poster : posterList) {
                File file = null;
                try {
                    log.info("证书信息{}", JSON.toJSONString(poster));
                    file = poster.draw();
                    String value = tencentCosUtil.putObject(file, StrUtil.format(filePathTemplate, SecurityContextHolder.getOrgId().toString()));
                    certUrlMap.put(poster.getId(), value);
                    File pdfFile = PdfUtils.pdf(ListUtil.toList(file.getAbsolutePath()),poster.getHeight(),poster.getWidth(), FileNameUtil.getPrefix(file)+".pdf");
                    tencentCosUtil.putObject(pdfFile, StrUtil.format(filePathTemplate, SecurityContextHolder.getOrgId().toString()));
                    file.delete();
                    pdfFile.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return certUrlMap;
        }
        return null;
    }


}
