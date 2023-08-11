import store from '@/store'

const {$ORG} = require('@/api/config')

// dept-controller Dept Controller
export const Dept = {
  create: params => $ORG.post(`/dept/add`, params),
  childrenList: params => $ORG.post(`dept/children/list`, params),
  adminTreeList: params => $ORG.post(`/dept/admin/tree-list`, params),
  delete: params => $ORG.post(`/dept/delete`, params),
  detail: params => $ORG.post(`/dept/detail`, params),
  batchEnabled: params => $ORG.post(`dept/batch-enabled`, params),
  page: params => $ORG.post(`/dept/page`, params),
  treeList: params => $ORG.post(`/dept/tree-list`, params),
  update: params => $ORG.post(`/dept/update`, params),
  studentCount: params => $ORG.post(`/dept/student/count`, params),
  employeeCount: params => $ORG.post(`/dept/employee/count`, params)
}


// organization-role-controller Organization Role Controller
export const OrganizationRole = {
  create: params => $ORG.post(`/organizationRole/add`, params),
  adminList: params => $ORG.post(`/organizationRole/admin/list`, params),
  delete: params => $ORG.post(`/organizationRole/delete`, params),
  detail: params => $ORG.post(`/organizationRole/detail`, params),
  batchEnabled: params => $ORG.post(`organizationRole/batch-enabled`, params),
  page: params => $ORG.post(`/organizationRole/page`, params),
  list: params => $ORG.post(`/organizationRole/list`, params),
  update: params => $ORG.post(`/organizationRole/update`, params)
}


// 机构管理 Organization Controller
export const Organization = {
  create: params => $ORG.post(`/organization/add`, params),
  delete: params => $ORG.post(`/organization/delete`, params),
  detail: params => $ORG.post(`/organization/detail`, params),
  detailAdmin: params => $ORG.post(`/organization/admin/detail`, params),
  batchEnabled: params => $ORG.post(`organization/batch-enabled`, params),
  list: params => $ORG.post(`/organization/list`, params),
  page: params => $ORG.post(`/organization/page`, params),
  update: params => $ORG.post(`/organization/update`, params),
  adminUpdate: params => $ORG.post(`/organization/admin/update`, params),
  categoryList: params => $ORG.post(`/organization/category/list`, params),
  orgDeptTree: params => $ORG.post(`/organization/org-dept-tree`, params)
}


// 机构菜单管理 Organization Menu Controller
export const OrganizationMenu = {
  create: params => $ORG.post(`/organizationMenu/add`, params),
  delete: params => $ORG.post(`/organizationMenu/delete`, params),
  detail: params => $ORG.post(`/organizationMenu/detail`, params),
  page: params => $ORG.post(`/organizationMenu/page`, params),
  adminList: params => $ORG.post(`/organizationMenu/admin/list`, params),
  list: params => $ORG.post(`/organizationMenu/list`, params),
  update: params => $ORG.post(`/organizationMenu/update`, params)
}


// 机构角色管理 Employee Role Controller
export const EmployeeRole = {
  create: params => $ORG.post(`/employeeRole/add`, params),
  delete: params => $ORG.post(`/employeeRole/delete`, params),
  detail: params => $ORG.post(`/employeeRole/detail`, params),
  page: params => $ORG.post(`/employeeRole/page`, params),
  list: params => $ORG.post(`/employeeRole/list`, params),
  update: params => $ORG.post(`/employeeRole/update`, params)
}


// 职工用户管理 Employee Controller
export const Employee = {
  create: params => $ORG.post(`/employee/add`, params),
  delete: params => $ORG.post(`/employee/delete`, params),
  detail: params => $ORG.post(`/employee/detail`, params),
  page: params => $ORG.post(`/employee/page`, params),
  resetPassword: params => $ORG.post(`/employee/reset-password`, params),
  list: params => $ORG.post(`/employee/list`, params),
  batchAdminEnabled: params => $ORG.post(`/employee/admin/batch-enabled`, params),
  adminCreate: params => $ORG.post(`/employee/admin/add`, params),
  adminUpdate: params => $ORG.post(`/employee/admin/update`, params),
  batchCreate: params => $ORG.post(`/employee/batch/add`, params),
  batchAdminCreate: params => $ORG.post(`/employee/batch/admin/add`, params),
  adminList: params => $ORG.post(`/employee/admin/list`, params),
  pageAdmin: params => $ORG.post(`/employee/admin/page`, params),
  update: params => $ORG.post(`/employee/update`, params),
  updateRole: params => $ORG.post(`/employee/update-role`, params),
  loginPermission: params => $ORG.post(`employee/login-permission`, params),
  // 创建学员账号
  createStudent: params => $ORG.post(`/employee/create-student`, params),
  categoryCount: params => $ORG.post(`/employee/category/count`, params)
}

// 学员管理
export const Student = {
  // 分页
  page: params => $ORG.post(`student/page`, params),
  // 平台分页
  pageAdmin: params => $ORG.post(`/student/admin/page`, params),
  // 详情
  detail: params => $ORG.post(`student/detail`, params),
  // 新增
  add: params => $ORG.post(`student/add`, params),
  // 修改
  update: params => $ORG.post(`student/update`, params),
  // 删除
  delete: params => $ORG.post(`student/delete`, params),
  // 导入预览
  importPreview: params => $ORG.post(`student/import/preview`, params.fileFormData),
  // 导入保存
  importSave: params => $ORG.post(`student/import/save`, params),
  // 导出（暂无）
  export: params => $ORG.post(``, params),
  // 学员组织批量
  studentDeptAdd: params => $ORG.post(`studentDept/add`, params),
  // 机构管理员组织树形列表显示
  deptTreeList: params => $ORG.post(`dept/tree-list`, params),
  // 新学员详情
  details: params => $ORG.post(`student/details`, params),
  categoryCount: params => $ORG.post(`/student/category/count`, params),

  dDetail: params => $ORG.post(`/student/details`, params),
}


// 组织管理 Employee Dept Controller
// export const EmployeeDept = {
//   create: params => $ORG.post(`/employeeDept/add`, params),
//   delete: params => $ORG.post(`/employeeDept/delete`, params),
//   detail: params => $ORG.post(`/employeeDept/detail`, params),
//   page: params => $ORG.post(`/employeeDept/page`, params),
//   update: params => $ORG.post(`/employeeDept/update`, params)
// }


// 讲师管理 Lecturer Controller
export const Lecturer = {
  create: params => $ORG.post(`/lecturer/add`, params),
  delete: params => $ORG.post(`/lecturer/delete`, params),
  page: params => $ORG.post(`/lecturer/page`, params),
  update: params => $ORG.post(`/lecturer/update`, params),
}


// 职工导入管理 Import Employee Controller
export const ImportEmployee = {
  create: params => $ORG.post(`/importEmployee/add`, params),
  delete: params => $ORG.post(`/importEmployee/delete`, params),
  adminDownload: params => $ORG.post(`/importEmployee/admin/download`, params, {responseType: 'arraybuffer'}),
  detail: params => $ORG.post(`/importEmployee/detail`, params),
  download: params => $ORG.post(`/importEmployee/download`, params, {responseType: 'arraybuffer'}),
  page: params => $ORG.post(`/importEmployee/page`, params),
  adminPreview: params => $ORG.post(`/importEmployee/admin/preview?orgId=${params.orgId}&deptId=${params.deptId}`, params.fileFormData),
  preview: params => $ORG.post(`/importEmployee/preview?orgId=${params.orgId}&deptId=${params.deptId}`, params.fileFormData),
  update: params => $ORG.post(`/importEmployee/update`, params)
}

// // 导入记录
// export const ImportRecord = {
//   // 分页
//   page: params => $ORG.post(`/importRecord/page`, params),
//   // 下载导入报告
//   downloadReport: params => $ORG.post(`/importRecord/download/report`, params, {responseType: 'arraybuffer'})
// }


// 机构装修 Organization Config Controller
// export const OrganizationConfig = {
//   info: params => $ORG.post(`/decoration`, params),
//   create: params => $ORG.post(`/decoration/add`, params),
//   update: params => $ORG.post(`/decoration/update`, params)
// }

// 机构首页
export const OrgIndexPage = {
  // // 机构列表
  // orgList: params => $ORG.post(`org-list`, params),
  // 员工权限
  loginPermission: params => $ORG.post(`login-permission`, params)
}

// export const labelPage = {
//   // 机构学员标签
//   labelList: params => $ORG.post(`/groups/list`, params),
//   // 新增机构学员标签
//   addlabel: params => $ORG.post(`/groups/add`, params),
//   // 标签分页
//   labelPage: params => $ORG.post(`/groups/page`, params),
//   // 修改标签
//   update: params => $ORG.post(`/groups/update`, params),
//   // 查询所有标签
//   listAll: params => $ORG.post(`/groups/listAll`, params)
// }

// export const studentGroup = {
//   // 学员单个贴标签
//   studentGroup: params => $ORG.post(`studentGroup/add`, params),
//   // 查询标签列表
//   detail: params => $ORG.post(`studentGroup/detail`, params),
//   // 查询非禁用所有标签
//   list: params => $ORG.post(`groups/list`, params),
//   // 批量添加
//   addList: params => $ORG.post(`studentGroup/addList`, params),
//   // 删除标签
//   delete: params => $ORG.post(`studentGroup/delete`, params)
// }

// 学员邀请
// export const StudentInvite = {
//   // 分页
//   page: params => $ORG.post(`/invitation/page`, params),
//   // 新增邀请
//   add: params => $ORG.post(`/invitation/add`, params),
//   // 详情
//   detail: params => $ORG.post(`/invitation/detail`, params),
//   // 启用禁用
//   enabled: params => $ORG.post(`/invitation/batchEnabled`, params)
// }

// 机构域名
// export const Domain = {
//   domain: params => $ORG.post(`/domain`, params)
// }