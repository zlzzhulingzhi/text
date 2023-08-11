package cn.qbs.wa.train.logistics.pojo.stat;

import lombok.Data;

import java.io.Serializable;

@Data
public class GroupDormitoryDTO implements Serializable {

    /**
     * 区域
     */
    private String building;

    /**
     * 房型类型编号
     */
    private String roomType;

    /**
     * 房型类型名称
     */
    private String roomTypeName;

    /**
     * 总房数
     */
    private Integer roomNum;

    /**
     * 已使用房数
     */
    private Integer usedNum;

    /**
     * 维修房数
     */
    private Integer maintNum;

    /**
     * 排序
     */
    private Integer sort;
}

