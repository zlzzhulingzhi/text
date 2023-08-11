const {$LIBRARY} = require('@/api/config')

export const LibraryType = {
  // 新增文库分类
  create: params => $LIBRARY.post(`category/add`, params),
  // 新增模板分类
  addTemplate: params => $LIBRARY.post(`category/addTemplate`, params),
  // 查询子类
  childrenList: params => $LIBRARY.post(`category/children/list`, params),
  // 删除文库分类
  delete: params => $LIBRARY.post(`category/delete`, params),
  // 通过id查询文库分类详情
  detail: params => $LIBRARY.post(`category/detail`, params),
  // 分类的禁用启用操作
  enabled: params => $LIBRARY.post(`category/enabled`, params),
  // 分页查询文库分类
  page: params => $LIBRARY.post(`category/page`, params),
  // 查询所有分类树
  tree: params => $LIBRARY.post(`category/tree`, params),
  // 查询字典表树列表
  list: params => $LIBRARY.post(`category/tree/list`, params),
  // 通过id修改文库分类详情
  update: params => $LIBRARY.post(`category/update`, params),
  // 计算分类绑定文件的总数量
  calculate: params => $LIBRARY.post(`category/calculate`, params),
  count: params => $LIBRARY.post(`category/count`, params)

}

export const LibraryList = {
  // 文库资料发布接口
  add: params => $LIBRARY.post('library/add', params),
  // 删除文库
  delete: params => $LIBRARY.post('library/delete', params),
  // 查询文库表详情
  detail: params => $LIBRARY.post('library/detail', params),
  // 文件禁用和启用,支持批量操作
  enabled: params => $LIBRARY.post('library/enabled', params),
  // 查询文件的分类菜单
  findMenuById: params => $LIBRARY.post('library/findMenuById', params),
  // 文库调用发布接口
  libraryPage: params => $LIBRARY.post('library/library/page', params),
  // 分页查询文库表
  page: params => $LIBRARY.post('library/page', params),
  // 修改文库表
  update: params => $LIBRARY.post('library/update', params)
}

export const LibraryResource = {
  // 获取的资源分类
  remoteTree: params => $LIBRARY.post('category/remoteTree', params),
  // 文库嗲用发布的接口
  libraryPage: params => $LIBRARY.post('library/library/page', params)
}
