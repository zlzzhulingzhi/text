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
  orgDeptTree: params => $ORG.post(`/organization/org-dept-tree`, params),

  // 机构学员  左侧树结构
  orgTree: params => $ORG.post(`/organization/orgList`, params)
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
  categoryCount: params => $ORG.post(`/student/category/count`, params)
}

// 机构学员
export const orgStudents = {
  page: params => $ORG.post(`/student/plat/page`, params),
  pageClasses: params => $ORG.post(`/student/plat/page-clazz`, params),
  
  detail: params => $ORG.post(`/student/plat/detail`, params)
}

// 机构讲师
export const orgLectures = {
  page: params => $ORG.post(`/lecturer/plat/page`, params),
  pageClasses: params => $ORG.post(`/lecturer/plat/page-clazz`, params),
  detail: params => $ORG.post(`/lecturer/detail`, params),
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

export const UnitUser = {
  page: params => {
    return {
      "code": 200,
      "msg": "操作成功",
      "data": {
        "records": [
          {
            "userId": 111,
            "username": "trx01",
            "password": null,
            "deptId": null,
            "jobId": null,
            "email": "1@qq.com",
            "phone": "15623138615",
            "avatar": null,
            "createTime": "2021-12-28T16:24:12",
            "updateTime": "2021-12-28T16:24:12",
            "lockFlag": "0",
            "delFlag": "0",
            "tenantId": null,
            "tenantName": null,
            "roleList": [
              {
                "roleId": 31,
                "roleName": "机构用户",
                "roleCode": "JIGOU_ROLE",
                "roleDesc": "培训机构,用于基础数据建设",
                "dsType": 4,
                "dsScope": "50",
                "createTime": "2021-11-05T11:41:27",
                "updateTime": "2021-11-05T11:41:27",
                "delFlag": "0",
                "tenantId": null,
                "tenantName": null,
                "roleDepts": null,
                "displayType": 0
              }
            ],
            "deptName": "北京天融信教育科技有限公司",
            "key": null,
            "displayType": null
          },
          {
            "userId": 110,
            "username": "lvmeng",
            "password": null,
            "deptId": null,
            "jobId": null,
            "email": "1@qq.com",
            "phone": "15623261229",
            "avatar": null,
            "createTime": "2021-12-28T16:22:18",
            "updateTime": "2021-12-28T16:22:18",
            "lockFlag": "0",
            "delFlag": "0",
            "tenantId": null,
            "tenantName": null,
            "roleList": [
              {
                "roleId": 31,
                "roleName": "机构用户",
                "roleCode": "JIGOU_ROLE",
                "roleDesc": "培训机构,用于基础数据建设",
                "dsType": 4,
                "dsScope": "50",
                "createTime": "2021-11-05T11:41:27",
                "updateTime": "2021-11-05T11:41:27",
                "delFlag": "0",
                "tenantId": null,
                "tenantName": null,
                "roleDepts": null,
                "displayType": 0
              }
            ],
            "deptName": "神州绿盟武汉科技有限公司",
            "key": null,
            "displayType": null
          },
          {
            "userId": 109,
            "username": "anyu01",
            "password": null,
            "deptId": null,
            "jobId": null,
            "email": "1@qq.com",
            "phone": "13807167726",
            "avatar": null,
            "createTime": "2021-12-28T16:19:08",
            "updateTime": "2021-12-28T16:19:08",
            "lockFlag": "0",
            "delFlag": "0",
            "tenantId": null,
            "tenantName": null,
            "roleList": [
              {
                "roleId": 31,
                "roleName": "机构用户",
                "roleCode": "JIGOU_ROLE",
                "roleDesc": "培训机构,用于基础数据建设",
                "dsType": 4,
                "dsScope": "50",
                "createTime": "2021-11-05T11:41:27",
                "updateTime": "2021-11-05T11:41:27",
                "delFlag": "0",
                "tenantId": null,
                "tenantName": null,
                "roleDepts": null,
                "displayType": 0
              }
            ],
            "deptName": "武汉安域信息安全技术有限公司",
            "key": null,
            "displayType": null
          },
          {
            "userId": 108,
            "username": "qianxin",
            "password": null,
            "deptId": null,
            "jobId": null,
            "email": "1@qq.com",
            "phone": "18986146533",
            "avatar": null,
            "createTime": "2021-12-23T17:18:18",
            "updateTime": "2021-12-23T17:18:18",
            "lockFlag": "0",
            "delFlag": "0",
            "tenantId": null,
            "tenantName": null,
            "roleList": [
              {
                "roleId": 31,
                "roleName": "机构用户",
                "roleCode": "JIGOU_ROLE",
                "roleDesc": "培训机构,用于基础数据建设",
                "dsType": 4,
                "dsScope": "50",
                "createTime": "2021-11-05T11:41:27",
                "updateTime": "2021-11-05T11:41:27",
                "delFlag": "0",
                "tenantId": null,
                "tenantName": null,
                "roleDepts": null,
                "displayType": 0
              }
            ],
            "deptName": "绵阳奇安信科技有限公司",
            "key": null,
            "displayType": null
          },
          {
            "userId": 107,
            "username": "zhongdian",
            "password": null,
            "deptId": null,
            "jobId": null,
            "email": "1@qq.com",
            "phone": "15342554564",
            "avatar": null,
            "createTime": "2021-12-23T16:52:45",
            "updateTime": "2021-12-23T16:55:27",
            "lockFlag": "0",
            "delFlag": "0",
            "tenantId": null,
            "tenantName": null,
            "roleList": [
              {
                "roleId": 31,
                "roleName": "机构用户",
                "roleCode": "JIGOU_ROLE",
                "roleDesc": "培训机构,用于基础数据建设",
                "dsType": 4,
                "dsScope": "50",
                "createTime": "2021-11-05T11:41:27",
                "updateTime": "2021-11-05T11:41:27",
                "delFlag": "0",
                "tenantId": null,
                "tenantName": null,
                "roleDepts": null,
                "displayType": 0
              }
            ],
            "deptName": "中电（武汉）网安教育科技有限公司",
            "key": null,
            "displayType": null
          },
          {
            "userId": 106,
            "username": "kywa01",
            "password": null,
            "deptId": null,
            "jobId": null,
            "email": "1@qq.com",
            "phone": "13465642541",
            "avatar": null,
            "createTime": "2021-11-23T11:12:04",
            "updateTime": "2021-11-23T11:12:04",
            "lockFlag": "0",
            "delFlag": "0",
            "tenantId": null,
            "tenantName": null,
            "roleList": [
              {
                "roleId": 31,
                "roleName": "机构用户",
                "roleCode": "JIGOU_ROLE",
                "roleDesc": "培训机构,用于基础数据建设",
                "dsType": 4,
                "dsScope": "50",
                "createTime": "2021-11-05T11:41:27",
                "updateTime": "2021-11-05T11:41:27",
                "delFlag": "0",
                "tenantId": null,
                "tenantName": null,
                "roleDepts": null,
                "displayType": 0
              }
            ],
            "deptName": "开源网安技术有限公司",
            "key": null,
            "displayType": null
          },
          {
            "userId": 90,
            "username": "sushe",
            "password": null,
            "deptId": null,
            "jobId": null,
            "email": "1@qq.com",
            "phone": "13426546485",
            "avatar": null,
            "createTime": "2021-11-02T10:20:58",
            "updateTime": "2021-11-02T10:20:58",
            "lockFlag": "0",
            "delFlag": "0",
            "tenantId": null,
            "tenantName": null,
            "roleList": [
              {
                "roleId": 30,
                "roleName": "宿舍管理员",
                "roleCode": "sushe",
                "roleDesc": "宿舍管理员",
                "dsType": 2,
                "dsScope": "50",
                "createTime": "2021-11-02T10:22:04",
                "updateTime": "2021-11-02T10:22:04",
                "delFlag": "0",
                "tenantId": null,
                "tenantName": null,
                "roleDepts": null,
                "displayType": 0
              }
            ],
            "deptName": "国家网络安全培训中心",
            "key": null,
            "displayType": null
          },
          {
            "userId": 88,
            "username": "daping",
            "password": null,
            "deptId": null,
            "jobId": null,
            "email": "1@qq.com",
            "phone": "13456655836",
            "avatar": null,
            "createTime": "2021-10-28T10:06:06",
            "updateTime": "2021-10-28T10:06:06",
            "lockFlag": "0",
            "delFlag": "0",
            "tenantId": null,
            "tenantName": null,
            "roleList": [
              {
                "roleId": 28,
                "roleName": "大屏",
                "roleCode": "display",
                "roleDesc": "大屏",
                "dsType": 2,
                "dsScope": "50",
                "createTime": "2021-10-28T10:05:30",
                "updateTime": "2021-10-28T11:12:36",
                "delFlag": "0",
                "tenantId": null,
                "tenantName": null,
                "roleDepts": null,
                "displayType": 0
              }
            ],
            "deptName": "国家网络安全培训中心",
            "key": null,
            "displayType": null
          },
          {
            "userId": 87,
            "username": "test360",
            "password": null,
            "deptId": null,
            "jobId": null,
            "email": "1@qq.com",
            "phone": "13432222222",
            "avatar": null,
            "createTime": "2021-10-15T09:20:31",
            "updateTime": "2021-10-15T09:20:31",
            "lockFlag": "0",
            "delFlag": "0",
            "tenantId": null,
            "tenantName": null,
            "roleList": [
              {
                "roleId": 5,
                "roleName": "管理员",
                "roleCode": "ADMIN_ROLE",
                "roleDesc": "管理员",
                "dsType": 1,
                "dsScope": "50,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144",
                "createTime": "2019-04-22T21:53:38",
                "updateTime": "2021-10-28T09:03:01",
                "delFlag": "0",
                "tenantId": 0,
                "tenantName": null,
                "roleDepts": null,
                "displayType": 0
              }
            ],
            "deptName": null,
            "key": null,
            "displayType": null
          },
          {
            "userId": 4,
            "username": "admin",
            "password": null,
            "deptId": null,
            "jobId": null,
            "email": "admin@163.com",
            "phone": "18627706030",
            "avatar": null,
            "createTime": "2019-04-23T23:29:51",
            "updateTime": "2021-10-08T14:20:56",
            "lockFlag": "0",
            "delFlag": "0",
            "tenantId": null,
            "tenantName": null,
            "roleList": [
              {
                "roleId": 5,
                "roleName": "管理员",
                "roleCode": "ADMIN_ROLE",
                "roleDesc": "管理员",
                "dsType": 1,
                "dsScope": "50,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144",
                "createTime": "2019-04-22T21:53:38",
                "updateTime": "2021-10-28T09:03:01",
                "delFlag": "0",
                "tenantId": 0,
                "tenantName": null,
                "roleDepts": null,
                "displayType": 0
              }
            ],
            "deptName": "国家网络安全培训中心",
            "key": null,
            "displayType": null
          }
        ],
        "total": 10,
        "size": 10,
        "current": 1,
        "orders": [],
        "searchCount": true,
        "pages": 1
      }
    }
  },
  tree:params => {
    return {
      "code": 200,
      "msg": "操作成功",
      "data": [
        {
          "deptId": 50,
          "name": "国家网络安全培训中心",
          "parentId": 0,
          "sort": 0,
          "createTime": "2021-09-26T11:14:21",
          "updateTime": null,
          "delFlag": null,
          "tenantId": null,
          "tenantName": null,
          "parentName": null,
          "level": 0,
          "children": []
        },
        {
          "deptId": 120,
          "name": "北京天融信教育科技有限公司",
          "parentId": 0,
          "sort": 0,
          "createTime": "2021-10-23T01:27:51",
          "updateTime": null,
          "delFlag": null,
          "tenantId": null,
          "tenantName": null,
          "parentName": null,
          "level": 0,
          "children": []
        },
        {
          "deptId": 121,
          "name": "绵阳奇安信科技有限公司",
          "parentId": 0,
          "sort": 0,
          "createTime": "2021-10-23T01:51:38",
          "updateTime": null,
          "delFlag": null,
          "tenantId": null,
          "tenantName": null,
          "parentName": null,
          "level": 0,
          "children": []
        },
        {
          "deptId": 122,
          "name": "中电（武汉）网安教育科技有限公司",
          "parentId": 0,
          "sort": 0,
          "createTime": "2021-10-23T01:56:46",
          "updateTime": null,
          "delFlag": null,
          "tenantId": null,
          "tenantName": null,
          "parentName": null,
          "level": 0,
          "children": []
        },
        {
          "deptId": 123,
          "name": "武汉安域信息安全技术有限公司",
          "parentId": 0,
          "sort": 0,
          "createTime": "2021-10-23T01:57:23",
          "updateTime": null,
          "delFlag": null,
          "tenantId": null,
          "tenantName": null,
          "parentName": null,
          "level": 0,
          "children": []
        },
        {
          "deptId": 124,
          "name": "中国网络安全审查技术与认证中心",
          "parentId": 0,
          "sort": 0,
          "createTime": "2021-10-23T01:57:39",
          "updateTime": null,
          "delFlag": null,
          "tenantId": null,
          "tenantName": null,
          "parentName": null,
          "level": 0,
          "children": []
        },
        {
          "deptId": 125,
          "name": "杭州安恒信息技术股份有限公司",
          "parentId": 0,
          "sort": 0,
          "createTime": "2021-10-23T01:57:48",
          "updateTime": null,
          "delFlag": null,
          "tenantId": null,
          "tenantName": null,
          "parentName": null,
          "level": 0,
          "children": []
        },
        {
          "deptId": 126,
          "name": "开源网安技术有限公司",
          "parentId": 0,
          "sort": 0,
          "createTime": "2021-10-23T01:57:59",
          "updateTime": null,
          "delFlag": null,
          "tenantId": null,
          "tenantName": null,
          "parentName": null,
          "level": 0,
          "children": []
        },
        {
          "deptId": 127,
          "name": "武汉临空港网安基地职业培训学校有限公司",
          "parentId": 0,
          "sort": 0,
          "createTime": "2021-10-23T01:58:11",
          "updateTime": null,
          "delFlag": null,
          "tenantId": null,
          "tenantName": null,
          "parentName": null,
          "level": 0,
          "children": []
        },
        {
          "deptId": 128,
          "name": "神州绿盟武汉科技有限公司",
          "parentId": 0,
          "sort": 0,
          "createTime": "2021-10-23T01:58:22",
          "updateTime": null,
          "delFlag": null,
          "tenantId": null,
          "tenantName": null,
          "parentName": null,
          "level": 0,
          "children": []
        },
        {
          "deptId": 129,
          "name": "中共湖北省委党校",
          "parentId": 0,
          "sort": 0,
          "createTime": "2021-10-23T01:58:36",
          "updateTime": null,
          "delFlag": null,
          "tenantId": null,
          "tenantName": null,
          "parentName": null,
          "level": 0,
          "children": []
        },
        {
          "deptId": 130,
          "name": "武汉网盾科技有限公司",
          "parentId": 0,
          "sort": 0,
          "createTime": "2021-10-23T01:58:44",
          "updateTime": null,
          "delFlag": null,
          "tenantId": null,
          "tenantName": null,
          "parentName": null,
          "level": 0,
          "children": []
        },
        {
          "deptId": 131,
          "name": "湖北省信息系统资质认证中心",
          "parentId": 0,
          "sort": 0,
          "createTime": "2021-10-23T01:58:53",
          "updateTime": null,
          "delFlag": null,
          "tenantId": null,
          "tenantName": null,
          "parentName": null,
          "level": 0,
          "children": []
        },
        {
          "deptId": 132,
          "name": "湖北省商用密码协会/商密在线科技有限公司",
          "parentId": 0,
          "sort": 0,
          "createTime": "2021-10-23T01:59:03",
          "updateTime": null,
          "delFlag": null,
          "tenantId": null,
          "tenantName": null,
          "parentName": null,
          "level": 0,
          "children": []
        },
        {
          "deptId": 133,
          "name": "湖北省大数据中心",
          "parentId": 0,
          "sort": 0,
          "createTime": "2021-10-23T01:59:13",
          "updateTime": null,
          "delFlag": null,
          "tenantId": null,
          "tenantName": null,
          "parentName": null,
          "level": 0,
          "children": []
        },
        {
          "deptId": 134,
          "name": "全国信息安全标准化技术委员会",
          "parentId": 0,
          "sort": 0,
          "createTime": "2021-10-23T01:59:57",
          "updateTime": null,
          "delFlag": null,
          "tenantId": null,
          "tenantName": null,
          "parentName": null,
          "level": 0,
          "children": []
        },
        {
          "deptId": 135,
          "name": "武汉大学继续教育学院",
          "parentId": 0,
          "sort": 0,
          "createTime": "2021-10-23T02:00:09",
          "updateTime": null,
          "delFlag": null,
          "tenantId": null,
          "tenantName": null,
          "parentName": null,
          "level": 0,
          "children": []
        },
        {
          "deptId": 136,
          "name": "（武汉）奇安信科技股份有限公司",
          "parentId": 0,
          "sort": 0,
          "createTime": "2021-10-23T02:00:20",
          "updateTime": null,
          "delFlag": null,
          "tenantId": null,
          "tenantName": null,
          "parentName": null,
          "level": 0,
          "children": []
        },
        {
          "deptId": 137,
          "name": "中共蔡甸区委宣传部",
          "parentId": 0,
          "sort": 0,
          "createTime": "2021-10-23T02:00:29",
          "updateTime": null,
          "delFlag": null,
          "tenantId": null,
          "tenantName": null,
          "parentName": null,
          "level": 0,
          "children": []
        },
        {
          "deptId": 138,
          "name": "市政法委",
          "parentId": 0,
          "sort": 0,
          "createTime": "2021-10-23T02:00:38",
          "updateTime": null,
          "delFlag": null,
          "tenantId": null,
          "tenantName": null,
          "parentName": null,
          "level": 0,
          "children": []
        }
      ]
    }
  }
}