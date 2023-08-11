package cn.qbs.wa.teach.exam.answer.service;

import cn.qbs.wa.teach.question.api.pojo.DTO.paper.PaperDetailDTO;

/**
 *
 * @Author zcm
 * @Date 2021-12-24 10:32
 * @Version 1.0
 */
public interface PaperService {

    PaperDetailDTO getPaperDetail(Long paperId);

}
