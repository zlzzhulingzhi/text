package cn.qbs.wa.teach.exam.consumer.service;

import cn.qbs.wa.teach.question.api.pojo.DTO.paper.PaperDetailDTO;

/**
 *
 * @Author zcm
 * @Date 2022-01-02 11:12
 * @Version 1.0
 */
public interface PaperService {

    PaperDetailDTO getPaperDetail(Long paperId, Long orgId);

}
