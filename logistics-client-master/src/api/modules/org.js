const { $LOGISTICS } = require('@/api/config')

export const Dept = {
  create: params => $LOGISTICS.post(`/dept/add`, params),
  childrenList: params => $LOGISTICS.post(`dept/children/list`, params),
  adminTreeList: params => $LOGISTICS.post(`/dept/admin/tree-list`, params),
  delete: params => $LOGISTICS.post(`/dept/delete`, params),
  detail: params => $LOGISTICS.post(`/dept/detail`, params),
  batchEnabled: params => $LOGISTICS.post(`dept/batch-enabled`, params),
  page: params => $LOGISTICS.post(`/dept/page`, params),
  treeList: params => $LOGISTICS.post(`/dept/tree-list`, params),
  update: params => $LOGISTICS.post(`/dept/update`, params),
  studentCount: params => $LOGISTICS.post(`/dept/student/count`, params),
  employeeCount: params => $LOGISTICS.post(`/dept/employee/count`, params)
}


// organization-role-controller Organization Role Controller
export const OrganizationRole = {
  create: params => $LOGISTICS.post(`/organizationRole/add`, params),
  adminList: params => $LOGISTICS.post(`/organizationRole/admin/list`, params),
  delete: params => $LOGISTICS.post(`/organizationRole/delete`, params),
  detail: params => $LOGISTICS.post(`/organizationRole/detail`, params),
  batchEnabled: params => $LOGISTICS.post(`organizationRole/batch-enabled`, params),
  page: params => $LOGISTICS.post(`/organizationRole/page`, params),
  list: params => $LOGISTICS.post(`/organizationRole/list`, params),
  update: params => $LOGISTICS.post(`/organizationRole/update`, params)
}


// 机构管理 Organization Controller
export const Organization = {
  create: params => $LOGISTICS.post(`/organization/add`, params),
  delete: params => $LOGISTICS.post(`/organization/delete`, params),
  detail: params => $LOGISTICS.post(`/organization/detail`, params),
  detailAdmin: params => $LOGISTICS.post(`/organization/admin/detail`, params),
  batchEnabled: params => $LOGISTICS.post(`organization/batch-enabled`, params),
  list: params => $LOGISTICS.post(`/organization/list`, params),
  page: params => $LOGISTICS.post(`/organization/page`, params),
  update: params => $LOGISTICS.post(`/organization/update`, params),
  adminUpdate: params => $LOGISTICS.post(`/organization/admin/update`, params),
  categoryList: params => $LOGISTICS.post(`/organization/category/list`, params),
  orgDeptTree: params => $LOGISTICS.post(`/organization/org-dept-tree`, params)
}


// 机构菜单管理 Organization Menu Controller
export const OrganizationMenu = {
  create: params => $LOGISTICS.post(`/organizationMenu/add`, params),
  delete: params => $LOGISTICS.post(`/organizationMenu/delete`, params),
  detail: params => $LOGISTICS.post(`/organizationMenu/detail`, params),
  page: params => $LOGISTICS.post(`/organizationMenu/page`, params),
  adminList: params => $LOGISTICS.post(`/organizationMenu/admin/list`, params),
  list: params => $LOGISTICS.post(`/organizationMenu/list`, params),
  update: params => $LOGISTICS.post(`/organizationMenu/update`, params)
}


// 机构角色管理 Employee Role Controller
export const EmployeeRole = {
  create: params => $LOGISTICS.post(`/employeeRole/add`, params),
  delete: params => $LOGISTICS.post(`/employeeRole/delete`, params),
  detail: params => $LOGISTICS.post(`/employeeRole/detail`, params),
  page: params => $LOGISTICS.post(`/employeeRole/page`, params),
  list: params => $LOGISTICS.post(`/employeeRole/list`, params),
  update: params => $LOGISTICS.post(`/employeeRole/update`, params)
}


// 职工用户管理 Employee Controller
export const Employee = {
  create: params => $LOGISTICS.post(`/employee/add`, params),
  delete: params => $LOGISTICS.post(`/employee/delete`, params),
  detail: params => $LOGISTICS.post(`/employee/detail`, params),
  page: params => $LOGISTICS.post(`/employee/page`, params),
  resetPassword: params => $LOGISTICS.post(`/employee/reset-password`, params),
  list: params => $LOGISTICS.post(`/employee/list`, params),
  batchAdminEnabled: params => $LOGISTICS.post(`/employee/admin/batch-enabled`, params),
  adminCreate: params => $LOGISTICS.post(`/employee/admin/add`, params),
  adminUpdate: params => $LOGISTICS.post(`/employee/admin/update`, params),
  batchCreate: params => $LOGISTICS.post(`/employee/batch/add`, params),
  batchAdminCreate: params => $LOGISTICS.post(`/employee/batch/admin/add`, params),
  adminList: params => $LOGISTICS.post(`/employee/admin/list`, params),
  pageAdmin: params => $LOGISTICS.post(`/employee/admin/page`, params),
  update: params => $LOGISTICS.post(`/employee/update`, params),
  updateRole: params => $LOGISTICS.post(`/employee/update-role`, params),
  loginPermission: params => $LOGISTICS.post(`employee/login-permission`, params),
  // 创建学员账号
  createStudent: params => $LOGISTICS.post(`/employee/create-student`, params),
  categoryCount: params => $LOGISTICS.post(`/employee/category/count`, params)
}

// 学员管理
export const Student = {
  // 分页
  page: params => $LOGISTICS.post(`student/page`, params),
  // 平台分页
  pageAdmin: params => $LOGISTICS.post(`/student/admin/page`, params),
  // 详情
  detail: params => $LOGISTICS.post(`student/detail`, params),
  // 新增
  add: params => $LOGISTICS.post(`student/add`, params),
  // 修改
  update: params => $LOGISTICS.post(`student/update`, params),
  // 删除
  delete: params => $LOGISTICS.post(`student/delete`, params),
  // 导入预览
  importPreview: params => $LOGISTICS.post(`student/import/preview`, params.fileFormData),
  // 导入保存
  importSave: params => $LOGISTICS.post(`student/import/save`, params),
  // 导出（暂无）
  export: params => $LOGISTICS.post(``, params),
  // 学员组织批量
  studentDeptAdd: params => $LOGISTICS.post(`studentDept/add`, params),
  // 机构管理员组织树形列表显示
  deptTreeList: params => $LOGISTICS.post(`dept/tree-list`, params),
  // 新学员详情
  details: params => $LOGISTICS.post(`student/details`, params),
  categoryCount: params => $LOGISTICS.post(`/student/category/count`, params)
}


// 组织管理 Employee Dept Controller
export const EmployeeDept = {
  create: params => $LOGISTICS.post(`/employeeDept/add`, params),
  delete: params => $LOGISTICS.post(`/employeeDept/delete`, params),
  detail: params => $LOGISTICS.post(`/employeeDept/detail`, params),
  page: params => $LOGISTICS.post(`/employeeDept/page`, params),
  update: params => $LOGISTICS.post(`/employeeDept/update`, params)
}


// 讲师管理 Lecturer Controller
export const Lecturer = {
  create: params => $LOGISTICS.post(`/lecturer/add`, params),
  delete: params => $LOGISTICS.post(`/lecturer/delete`, params),
  detail: params => $LOGISTICS.post(`/lecturer/detail`, params),
  page: params => $LOGISTICS.post(`/lecturer/page`, params),
  list: params => $LOGISTICS.post(`/lecturer/list`, params),
  batchCreate: params => $LOGISTICS.post(`/lecturer/batch/add`, params),
  batchAdminCreate: params => $LOGISTICS.post(`/lecturer/batch/admin/add`, params),
  update: params => $LOGISTICS.post(`/lecturer/update`, params),
  loginPermission: params => $LOGISTICS.post(`lecturer/login-permission`, params),
  resetPassword: params => $LOGISTICS.post(`lecturer/reset-password`, params)
}


// 职工导入管理 Import Employee Controller
export const ImportEmployee = {
  create: params => $LOGISTICS.post(`/importEmployee/add`, params),
  delete: params => $LOGISTICS.post(`/importEmployee/delete`, params),
  adminDownload: params => $LOGISTICS.post(`/importEmployee/admin/download`, params, { responseType: 'arraybuffer' }),
  detail: params => $LOGISTICS.post(`/importEmployee/detail`, params),
  download: params => $LOGISTICS.post(`/importEmployee/download`, params, { responseType: 'arraybuffer' }),
  page: params => $LOGISTICS.post(`/importEmployee/page`, params),
  adminPreview: params => $LOGISTICS.post(`/importEmployee/admin/preview?orgId=${params.orgId}&deptId=${params.deptId}`, params.fileFormData),
  preview: params => $LOGISTICS.post(`/importEmployee/preview?orgId=${params.orgId}&deptId=${params.deptId}`, params.fileFormData),
  update: params => $LOGISTICS.post(`/importEmployee/update`, params)
}

// 导入记录
export const ImportRecord = {
  // 分页
  page: params => $LOGISTICS.post(`/importRecord/page`, params),
  // 下载导入报告
  downloadReport: params => $LOGISTICS.post(`/importRecord/download/report`, params, { responseType: 'arraybuffer' })
}


// 机构装修 Organization Config Controller
export const OrganizationConfig = {
  info: params => $LOGISTICS.post(`/decoration`, params),
  create: params => $LOGISTICS.post(`/decoration/add`, params),
  update: params => $LOGISTICS.post(`/decoration/update`, params)
}


export const labelPage = {
  // 机构学员标签
  labelList: params => $LOGISTICS.post(`/groups/list`, params),
  // 新增机构学员标签
  addlabel: params => $LOGISTICS.post(`/groups/add`, params),
  // 标签分页
  labelPage: params => $LOGISTICS.post(`/groups/page`, params),
  // 修改标签
  update: params => $LOGISTICS.post(`/groups/update`, params),
  // 查询所有标签
  listAll: params => $LOGISTICS.post(`/groups/listAll`, params)
}

export const studentGroup = {
  // 学员单个贴标签
  studentGroup: params => $LOGISTICS.post(`studentGroup/add`, params),
  // 查询标签列表
  detail: params => $LOGISTICS.post(`studentGroup/detail`, params),
  // 查询非禁用所有标签
  list: params => $LOGISTICS.post(`groups/list`, params),
  // 批量添加
  addList: params => $LOGISTICS.post(`studentGroup/addList`, params),
  // 删除标签
  delete: params => $LOGISTICS.post(`studentGroup/delete`, params)
}

// 学员邀请
export const StudentInvite = {
  // 分页
  page: params => $LOGISTICS.post(`/invitation/page`, params),
  // 新增邀请
  add: params => $LOGISTICS.post(`/invitation/add`, params),
  // 详情
  detail: params => $LOGISTICS.post(`/invitation/detail`, params),
  // 启用禁用
  enabled: params => $LOGISTICS.post(`/invitation/batchEnabled`, params)
}

// 机构域名
export const Domain = {
  domain: params => $LOGISTICS.post(`/domain`, params)
}

