package cn.qbs.wa.teach.exam.admin.service;

import cn.qbs.wa.teach.question.api.pojo.DTO.paper.BasicPaper;
import cn.qbs.wa.teach.question.api.pojo.DTO.paper.PaperDetailDTO;

import java.util.List;

/**
 *
 * @Author zcm
 * @Date 2021-12-24 10:32
 * @Version 1.0
 */
public interface PaperService {

    PaperDetailDTO getPaperDetail(Long paperId);

    boolean editable(Long paperId, boolean editable);

    List<BasicPaper> getBasicList(List<Long> paperIdList);

}
