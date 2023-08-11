package cn.qbs.wa.train.logistics.controller.manage;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.train.logistics.constants.RedisCacheKey;
import cn.qbs.wa.train.logistics.entity.Clazz;
import cn.qbs.wa.train.logistics.entity.ClazzStudent;
import cn.qbs.wa.train.logistics.enums.ClassStatusEnum;
import cn.qbs.wa.train.logistics.pojo.clazzsign.*;
import cn.qbs.wa.train.logistics.service.ClazzStudentSignService;
import cn.qbs.wa.train.logistics.service.manage.ClazzService;
import cn.qbs.wa.train.logistics.service.manage.ClazzStudentService;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yjx
 */
@Api(tags = "上课签到")
@Slf4j
@RestController
@RequestMapping("/sign-in")
public class ClazzSignInController {

    @Resource
    private ClazzService clazzService;

    @Resource
    private ClazzStudentService clazzStudentService;

    @Resource
    private ClazzStudentSignService clazzStudentSignService;

    @Resource
    private RedissonClient redissonClient;

    @ApiOperation("学员签到-分页")
    @PostMapping("/page")
    @RequiresPermissions("Class:SignIn")
    public R<IPage<SignInStudentResponse>> pageSignIn(@RequestBody @Validated SignInPageRequest request) {
        if (request.getStudentStatus() == null) {
            request.setStudentStatus(1);
        }
        if (request.getLessonStart() == null) {
            request.setLessonStart(1);
            request.setLessonEnd(10);
        } else if (request.getLessonEnd() == null) {
            request.setLessonEnd(request.getLessonStart() + 9);
        }
        return R.ok(clazzStudentSignService.pageSignIn(request));
    }

    @ApiOperation("班级签到记录-分页")
    @PostMapping("/page-record")
    @RequiresPermissions("Class:Attend")
    public R<IPage<SignInRecordResponse>> pageSignInRecord(@RequestBody @Validated SignInRecordPageRequest request) {
        return R.ok(clazzStudentSignService.pageSignInRecord(request));
    }

    @ApiOperation("班级考勤表")
    @PostMapping("/class-sign-table")
    @RequiresPermissions("Class:Attend")
    public R<List<SignInStudentResponse>> classSignTable(@RequestBody @Validated SignInClazzRequest request) {
        if (request.getLessonStart() == null) {
            request.setLessonStart(1);
            request.setLessonEnd(5);
        } else if (request.getLessonEnd() == null) {
            request.setLessonEnd(request.getLessonStart() + 4);
        }
        return R.ok(clazzStudentSignService.classSignTable(request));
    }

    @SneakyThrows
    @ApiOperation("学员签到-单个新增")
    @PostMapping("/single")
    @RequiresPermissions("Class:SignIn")
    public R<Boolean> singleSignIn(@RequestBody @Validated SignInSingleRequest request) {
        // 先判断操作人是否为该班级的负责人以及签到者为班级的学员
        validate(request.getClazzId(), request.getMemberId());
        Boolean result = Boolean.FALSE;
        // 班级批量签到与班级单个学员签到互斥。 班级锁用于判断是否存在批量签到的情况
        RLock clazzLock = redissonClient.getReadWriteLock(RedisCacheKey.getMemberSignInLockKey(request.getClazzId())).readLock();
        String msg = "签到出错";
        if (clazzLock.tryLock(3L, 5L, TimeUnit.SECONDS)) {
            try {
                // 根据学生ID加锁
                RLock lock = redissonClient.getLock(RedisCacheKey.getMemberSignInLockKey(request.getClazzId(), request.getMemberId()));
                if (lock.tryLock(3L, 3L, TimeUnit.SECONDS)) {
                    try {
                        result = clazzStudentSignService.singleSignIn(request);
                        if (!result) {
                            msg = "该课次已签到，请刷新后查看";
                        }
                    } catch (Exception e) {
                        log.error("签到异常", e);
                    } finally {
                        if (lock.isHeldByCurrentThread()) {
                            lock.unlock();
                        }
                    }
                } else {
                    return R.fail("数据处理中，请勿重复操作");
                }
            } catch (Exception e) {
                log.error("获取签到锁异常", e);
            } finally {
                if (clazzLock.isHeldByCurrentThread()) {
                    clazzLock.unlock();
                }
            }
        } else {
            return R.fail("正在进行批量签到，请稍后再试");
        }
        return result ? R.ok(true) : R.fail(msg);
    }

    @SneakyThrows
    @ApiOperation("学员签到-批量新增")
    @PostMapping("/batch")
    @RequiresPermissions("Class:SignIn")
    public R<Boolean> batchSignIn(@RequestBody @Validated SignInBatchRequest request) {
        // 先判断操作人是否为该班级的负责人
        validate(request.getClazzId(), null);
        Boolean result = Boolean.FALSE;
        // 班级批量签到与班级单个学员签到互斥。 班级锁用于判断是否存在单个签到的情况
        RLock clazzLock = redissonClient.getReadWriteLock(RedisCacheKey.getMemberSignInLockKey(request.getClazzId())).writeLock();
        if (clazzLock.tryLock(5L, 5L, TimeUnit.SECONDS)) {
            try {
                result = clazzStudentSignService.batchSignIn(request);
            } catch (Exception e) {
                log.error("签到异常", e);
            } finally {
                if (clazzLock.isHeldByCurrentThread()) {
                    clazzLock.unlock();
                }
            }
        } else {
            return R.fail("数据处理中，请勿重复操作");
        }
        return result ? R.ok(true) : R.fail("批量签到出错");
    }

    /**
     * 校验班级、学员合法性
     * @param clazzId  班级ID
     * @param memberId 学员ID
     */
    private void validate(Long clazzId, Long memberId) {
        Clazz clazz = clazzService.getById(clazzId);
        if (clazz == null || clazz.getDeleted().equals(Constants.DB_TRUE)) {
            throw new ServiceException("该班级不存在");
        }
        if (ClassStatusEnum.CLOSE.getCode().equals(clazz.getState())) {
            throw new ServiceException("该班级已结班，无法签到");
        }
        if (clazz.getEmployeeId() != null && !clazz.getEmployeeId().equals(SecurityContextHolder.getEmployeeId())) {
            throw new ServiceException("当前签到人员非本班级负责教师");
        }

        if (memberId != null) {
            Long count = clazzStudentService.lambdaQuery().eq(ClazzStudent::getClazzId, clazzId).eq(ClazzStudent::getMemberId, memberId).count();
            if (count != null && count == 0) {
                throw new ServiceException("签到学员非班级学员");
            }
        }
    }

    @PostMapping("getSignInRecord")
    public R<Boolean> getSignInRecord(@RequestBody SignInRecordPageRequest request) {
        List<ClazzSignExcelDTO> excelData = clazzStudentSignService.generateExcelData(request);
        if (!excelData.isEmpty()) {
            return R.ok(true);
        }else {
            return R.ok(false);
        }
    }

    @ApiOperation(value = "导出考勤记录到Excel")
    @PostMapping("exportExcel")
    public void exportExcel(HttpServletResponse response, @RequestBody SignInRecordPageRequest request) throws IOException {
        Clazz clazz=clazzService.getById(request.getClazzId());
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode(clazz.getName()+"考勤记录-"+ LocalDate.now(), "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        List<ClazzSignExcelDTO> excelData = clazzStudentSignService.generateExcelData(request);
        if (!excelData.isEmpty()) {
            EasyExcel.write(response.getOutputStream(), ClazzSignExcelDTO.class).sheet(clazz.getName()).doWrite(excelData);
        }
    }
}
