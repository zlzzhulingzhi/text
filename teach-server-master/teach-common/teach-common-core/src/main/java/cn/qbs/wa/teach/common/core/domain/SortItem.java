package cn.qbs.wa.teach.common.core.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

/**
 * 排序项
 * @Author zcm
 * @Date 2021/12/10 16:08
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortItem implements Serializable {

    @ApiModelProperty(value = "排序属性")
    @NotBlank(message = "排序属性不能为空！")
    private String attr;

    @ApiModelProperty(value = "排序正序/倒序")
    private String order = "desc";

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SortItem sortItem = (SortItem) o;
        return attr.equals(sortItem.attr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attr);
    }

}
