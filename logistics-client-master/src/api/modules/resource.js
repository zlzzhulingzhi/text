const {$RESOURCE} = require('@/api/config')

export const ResourceType = {
  treeTotal: params => $RESOURCE.post(`category/tree`, params),
  // 添加分类
  create: params => $RESOURCE.post(`category/add`, params),
  // 分类修改
  update: params => $RESOURCE.post(`category/update`, params),
  // 删除分类
  delete: params => $RESOURCE.post(`category/delete`, params),
  // 分页查询分类接口
  page: params => $RESOURCE.post(`category/page`, params),
  // 分页查询子分类
  childrenList: params => $RESOURCE.post(`category/children/list`, params),
  // 禁用启用/
  enabled: params => $RESOURCE.post(`category/enabled`, params),
  // 公共分类
  defaultTypeList: params => $RESOURCE.post(`category/tree/list`, params),
  // 分类资源总数
  calculate: params => $RESOURCE.post(`category/calculate`, params),
  // 新增通用分类
  addTemplate: params => $RESOURCE.post(`category/addTemplate`, params),
  count: params => $RESOURCE.post(`category/count`, params)
}


export const ResourceFile = {
  // 查询资源列表
  pageList: params => $RESOURCE.post(`file/page`, params),
  // 修改文件名
  fileUpdataName: params => $RESOURCE.post(`file/update`, params),
  // 删除资源
  delete: params => $RESOURCE.post(`file/delete`, params),
  // 批量添加资源单个添加资源
  upload: params => $RESOURCE.post(`file/upload`, params),
  // 批量下载
  download: params => $RESOURCE.post(`file/download`, params),
  // 查询修改文件分类
  updateMenuById: params => $RESOURCE.post(`file/updateMenuById`, params),
  // 查询单个文件分类
  findMenuById: params => $RESOURCE.post(`file/findMenuById`, params),
  // 是否启用资源
  enabled: params => $RESOURCE.post(`file/enabled`, params),
  // 获取临时接口
  temporary: params => $RESOURCE.post(`obs/temporary`, params)

}