package cn.qbs.wa.teach.question.util;

//import cn.hutool.core.io.FileTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件类型检查工具类
 *
 * @Author zcm
 * @Date 2021/11/11 15:10
 * @Version 1.0
 */
@Slf4j
public class FileTypeCheckUtil {

    private static final String FILE_ZIP = "zip";
    private static final String FILE_XLS = ".xls";
    private static final String FILE_XLSX = ".xlsx";

    public static boolean isExcel(MultipartFile file) {
        if (file != null) {
            String filename = file.getOriginalFilename();
            String fileType = filename.substring(filename.lastIndexOf("."));
            //先判断后缀名
            if (FILE_XLS.equalsIgnoreCase(fileType) || FILE_XLSX.equalsIgnoreCase(fileType)) {
                /*try {
                    String type = FileTypeUtil.getType(file.getInputStream());
                    //根据首部字节判断文件类型
                    if (FILE_ZIP.contains(type) || FILE_XLS.contains(type)) {
                        return true;
                    }
                } catch (Exception e) {
                    log.error("Excel文件检测失败：", e);
                }*/

                return true;
            }
        }

        return false;
    }

}
