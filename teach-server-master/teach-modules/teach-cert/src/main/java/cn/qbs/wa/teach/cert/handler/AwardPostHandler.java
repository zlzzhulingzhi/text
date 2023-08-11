package cn.qbs.wa.teach.cert.handler;

import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.cert.entity.AwardRecordDetail;
import cn.qbs.wa.teach.cert.enums.CertTemplateConfigEnum;
import cn.qbs.wa.teach.common.post.drawable.Image;
import cn.qbs.wa.teach.common.post.drawable.Poster;
import cn.qbs.wa.teach.common.post.drawable.Text;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/2/14 11:34
 */
@Component
public class AwardPostHandler {

    private void addText(List<Text> texts, AwardRecordDetail awardRecordDetail) {
        if (StrUtil.isNotBlank(awardRecordDetail.getKeyValue())) {
            String customStyle = awardRecordDetail.getCustomStyle();
            Text text = JSON.parseObject(customStyle, Text.class);
            text.setText(awardRecordDetail.getKeyValue());
            text.setLineNum(10);
            texts.add(text);
        }

    }

    private void addImage(List<Image> images, AwardRecordDetail awardRecordDetail) {
        //有可能头像没有值
        if (StrUtil.isNotBlank(awardRecordDetail.getKeyValue())) {
            String customStyle = awardRecordDetail.getCustomStyle();
            Image image = JSON.parseObject(customStyle, Image.class);
            if (awardRecordDetail.getKeyValue().startsWith("//")) {
                image.setUrl("https:" + awardRecordDetail.getKeyValue());
            }
            if (awardRecordDetail.getKeyValue().startsWith("http")) {
                image.setUrl(awardRecordDetail.getKeyValue());
            }
            images.add(image);
        }
    }

    private void setPoster(Poster poster, AwardRecordDetail awardRecordDetail) {
        String customStyle = awardRecordDetail.getCustomStyle();
        Image image = JSON.parseObject(customStyle, Image.class);
        poster.setWidth(image.getWidth());
        poster.setHeight(image.getHeight());
    }

    public Poster load(List<AwardRecordDetail> awardRecordDetailList) {
        Poster poster = new Poster();
        ArrayList<Text> texts = new ArrayList<>();
        ArrayList<Image> images = new ArrayList<>();
        for (AwardRecordDetail awardRecordDetail : awardRecordDetailList) {
            CertTemplateConfigEnum certTemplateConfigEnum = CertTemplateConfigEnum.getEnumByCode(awardRecordDetail.getCode());
            switch (certTemplateConfigEnum) {
                case CERT_NAME:
                    addText(texts, awardRecordDetail);
                    break;
                case USER_NAME:
                    addText(texts, awardRecordDetail);
                    break;
                case HEAD_IMG:
                    addImage(images, awardRecordDetail);
                    break;
                case ID_NUM:
                    addText(texts, awardRecordDetail);
                    break;
                case EXAM_INFO:
                    addText(texts, awardRecordDetail);
                    break;
                case SEARCH_ADDRESS:
                    addText(texts, awardRecordDetail);
                    break;
                case CERT_CONTENT:
                    addText(texts, awardRecordDetail);
                    break;
                case CERT_NUM_RULE:
                    addText(texts, awardRecordDetail);
                    break;
                case AWARD_COMPANY:
                    addText(texts, awardRecordDetail);
                    break;
                case AWARD_DATE:
                    addText(texts, awardRecordDetail);
                    break;
                case BACKGROUND_URL:
                    addImage(images, awardRecordDetail);
                    setPoster(poster, awardRecordDetail);
                    break;
                default:
                    break;
            }
        }
        poster.setImages(images);
        poster.setTexts(texts);
        return poster;
    }
}
