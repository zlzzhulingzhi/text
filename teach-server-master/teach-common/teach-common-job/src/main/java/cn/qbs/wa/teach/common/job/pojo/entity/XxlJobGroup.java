package cn.qbs.wa.teach.common.job.pojo.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author makejava
 * @since 2021-11-30 17:28:12
 */
@Data
public class XxlJobGroup  {

    private static final long serialVersionUID = 393010391711447476L;


    private Integer id;


    private String appName;


    private String title;


    private Integer addressType;


    private String addressList;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
