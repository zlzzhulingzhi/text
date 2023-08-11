package cn.qbs.wa.teach.common.core.utils;

import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/2/5 15:23
 */
public class TreeUtil {

  /**
   * 创建树结构
   *
   * @param tress
   * @return
   */
  public static <T extends TreeNode> List<T> tree(List<T> tress) {
    // 返回的节点树
    List<T> rootNodes = new ArrayList<>();
    Iterator<T> it = tress.iterator();
    while (it.hasNext()) {
      T next = it.next();
      // parent（上级Id）为0的是根节点
      if ("0".equals(next.getParentId().toString())) {
        rootNodes.add(next);
        it.remove();
      }
    }
    // 遍历，找到二级节点
    for (T t : rootNodes) {
      List<T> child = getChild(tress, t.getId().toString());
      t.setChildren(child);
    }

    Collections.sort(rootNodes);
    return rootNodes;
  }

  /**
   * 查子节点
   *
   * @param trees
   * @param parentId
   * @return
   */
  public static <T extends TreeNode> List<T> getChild(List<T> trees, String parentId) {

    // 子节点列表
    List<T> childList = new ArrayList<>();
    Iterator<T> it = trees.iterator();
    while (it.hasNext()) {
      T t = it.next();
      if (parentId.equals(t.getParentId().toString())) {
        childList.add(t);
        it.remove();
      }
    }

    // 遍历 递归获取子节点的子节点
    for (T t : childList) {
      List<T> child = getChild(trees, t.getId().toString());
      t.setChildren(child);
    }
    // 递归出口  childList长度为0
    if (childList.size() == 0) {
      return new ArrayList<>();
    }

    Collections.sort(childList);
    return childList;
  }

  public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    Set<Object> seen = ConcurrentHashMap.newKeySet();
    return t -> seen.add(keyExtractor.apply(t));
  }

  public static <T> List<T> copyBeanList(List<?> resourceList, Class<T> target) {

    List<T> targetList = new ArrayList<>();
    if (null == resourceList || resourceList.isEmpty()) {
      return targetList;
    }
    resourceList.forEach(
        e -> {
          T o = null;
          try {
            o = target.newInstance();
          } catch (InstantiationException | IllegalAccessException ex) {
            // ex.printStackTrace();
          }
          BeanUtils.copyProperties(e, o);
          targetList.add(o);
        });
    return targetList;
  }
}
