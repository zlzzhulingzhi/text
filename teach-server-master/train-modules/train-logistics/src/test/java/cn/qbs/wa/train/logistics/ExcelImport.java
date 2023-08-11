package cn.qbs.wa.train.logistics;

import cn.qbs.wa.train.logistics.excel.DormitoryDailyExcelListener;
import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ExcelImport {

    /**
     * 不创建对象的读
     */
    @Test
    public void noModelRead() {
        String fileName = "D:\\Desktop\\客房可用与占用(含超预留).xls";
        // 这里 只要，然后读取第一个sheet 同步读取会自动finish
        EasyExcel.read(fileName, new DormitoryDailyExcelListener()).sheet().doRead();
    }
}
