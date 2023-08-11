package cn.qbs.wa.teach.common.log.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步调用日志服务
 *
 * @author ruoyi
 */
@Service
public class AsyncLogService {
    /**
     * 保存系统日志记录
     */
    @Async
    public void saveSysLog(OperLogDTO operLogDTO) {
    }
}
