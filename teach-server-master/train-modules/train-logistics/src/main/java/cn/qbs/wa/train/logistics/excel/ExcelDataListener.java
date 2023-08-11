package cn.qbs.wa.train.logistics.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 模板的读取类
 *
 * @author Jiaju Zhuang
 */
@Getter
@Slf4j
public class ExcelDataListener<T> implements ReadListener<T> {

    /**
     * 缓存的数据
     */
    private final List<T> dataList;

    private static final int DEFAULT_SIZE = 100;

    public ExcelDataListener () {
        this.dataList = new ArrayList<>(DEFAULT_SIZE);
    }

    public ExcelDataListener (int size) {
        if (size <= 0) {
            size = DEFAULT_SIZE;
        }
        this.dataList = new ArrayList<>(size);
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        dataList.add(data);
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("所有数据解析完成！");
    }
}
