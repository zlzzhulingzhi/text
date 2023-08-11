package cn.qbs.wa.train.logistics.pojo.stat;

import lombok.Data;

import java.io.Serializable;

@Data
public class GroupClassroomDTO implements Serializable {

    /**
     * 教室类型
     */
    private String roomType;

    /**
     * 教室类型总数
     */
    private Integer roomNum;

    /**
     * 教室类型已使用数
     */
    private Integer usedNum;

    /**
     * 排序
     */
    private Integer sort;
}
