package cn.qbs.wa.train.logistics.service;

import cn.qbs.wa.train.logistics.entity.ClazzStudentSign;
import cn.qbs.wa.train.logistics.pojo.clazzsign.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 班级学员签到表(ClazzStudentSign)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-26 10:38:28
 */
public interface ClazzStudentSignService extends IService<ClazzStudentSign> {

    /**
     * 班级学员签到表-分页
     * @param request
     * @return
     */
    IPage<SignInStudentResponse> pageSignIn(SignInPageRequest request);

    /**
     * 班级签到记录-分页
     * @param request
     * @return
     */
    IPage<SignInRecordResponse> pageSignInRecord(SignInRecordPageRequest request);

    /**
     * 班级考勤表
     * @param request
     * @return
     */
    List<SignInStudentResponse> classSignTable(SignInClazzRequest request);

    /**
     * 单个签到
     * @param request
     * @return
     */
    Boolean singleSignIn(SignInSingleRequest request);

    /**
     * 批量签到
     * @param request
     * @return
     */
    Boolean batchSignIn(SignInBatchRequest request);

    List<ClazzSignExcelDTO> generateExcelData(SignInRecordPageRequest request);


}

