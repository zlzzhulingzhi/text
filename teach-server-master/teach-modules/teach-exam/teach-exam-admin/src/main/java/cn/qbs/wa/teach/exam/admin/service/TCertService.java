package cn.qbs.wa.teach.exam.admin.service;

import cn.qbs.wa.teach.cert.api.pojo.DTO.cert.awardRecord.AwardRecordPageResponseDTO;
import cn.qbs.wa.teach.cert.api.pojo.DTO.cert.awardRecord.PersonCertDetailResponseDTO;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.exam.admin.pojo.exam.ExamRecord;
import cn.qbs.wa.teach.exam.common.entity.TCert;
import cn.qbs.wa.teach.exam.admin.pojo.tcert.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 任务证书表(TCert)表服务接口
 *
 * @author makejava
 * @since 2022-05-16 13:47:59
 */
public interface TCertService extends IService<TCert> {

    /**
     * 新增任务证书表
     * @param params
     * @return
     */
    boolean add(TCertAddRequest params);

    /**
     * 分页查询任务证书表
     * @param params
     * @return
     */
    IPage<TCertPageResponse> page(TCertPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    TCertDetailResponse detail(Serializable id);

    /**
     * 更新任务证书表
     * @param params
     * @return
     */
    boolean update(TCertUpdateRequest params);

    /**
     * 删除任务证书表
     * @param idList
     * @return
     */
    boolean deleteByIds(TCertUpdateRequest params);

    List<CertDetailResponse> certList(TCertDetailRequest request);

    PageResultComDTO<AwardRecordPageResponseDTO> awardRecordPage(AwardRecordPageRequest idRequest);

    List<PersonCertDetailResponseDTO> getCertInfo(ExamRecord examRecord);
}

