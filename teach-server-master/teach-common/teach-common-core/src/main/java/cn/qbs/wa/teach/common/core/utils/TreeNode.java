package cn.qbs.wa.teach.common.core.utils;


import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/2/5 15:25
 */
public class TreeNode<T> implements Comparable<T> {
    protected Long id;
    protected Long parentId;

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }

    List<T> children = new ArrayList<T>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void add(T node) {
        children.add(node);
    }

    @Override
    public int compareTo(T o) {
        // 具体比较业务由子类自己实现
        return 0;
    }

}
