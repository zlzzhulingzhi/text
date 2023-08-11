const {$DECORATE} = require('@/api/config')


export const File = {
  create: params => $DECORATE.post(`file/add`, params),
  delete: params => $DECORATE.post(`file/delete`, params),
  detail: params => $DECORATE.post(`file/detail`, params),
  page: params => $DECORATE.post(`file/page`, params),
  update: params => $DECORATE.post(`file/update`, params)
}

export const Groups = {
  create: params => $DECORATE.post(`groups/add`, params),
  delete: params => $DECORATE.post(`groups/delete`, params),
  detail: params => $DECORATE.post(`groups/detail`, params),
  page: params => $DECORATE.post(`groups/page`, params),
  update: params => $DECORATE.post(`groups/update`, params)
}

export const Navbar = {
  create: params => $DECORATE.post(`navbar/add`, params),
  delete: params => $DECORATE.post(`navbar/delete`, params),
  detail: params => $DECORATE.post(`navbar/detail`, params),
  init: params => $DECORATE.post(`navbar/init`, params),
  page: params => $DECORATE.post(`navbar/page`, params),
  update: params => $DECORATE.post(`navbar/update`, params),
  selectList: params => $DECORATE.post(`navbar/select/list`, params),
  batchAdd: params => $DECORATE.post(`navbar/batch-add`, params)
}

export const PageDetail = {
  create: params => $DECORATE.post(`pageDetail/add`, params),
  delete: params => $DECORATE.post(`pageDetail/delete`, params),
  detail: params => $DECORATE.post(`pageDetail/detail`, params),
  page: params => $DECORATE.post(`pageDetail/page`, params),
  update: params => $DECORATE.post(`pageDetail/update`, params)
}

export const Pages = {
  create: params => $DECORATE.post(`pages/add`, params),
  delete: params => $DECORATE.post(`pages/delete`, params),
  detail: params => $DECORATE.post(`pages/detail`, params),
  page: params => $DECORATE.post(`pages/page`, params),
  init: params => $DECORATE.post(`pages/init`, params),
  update: params => $DECORATE.post(`pages/update`, params)
}

export const WCourse = {
  create: params => $DECORATE.post(`wCourse/add`, params),
  delete: params => $DECORATE.post(`wCourse/delete`, params),
  detail: params => $DECORATE.post(`wCourse/detail`, params),
  page: params => $DECORATE.post(`wCourse/page`, params),
  selectPage: params => $DECORATE.post(`wCourse/select/page`, params),
  update: params => $DECORATE.post(`wCourse/update`, params),
  batchGooded: params => $DECORATE.post(`wCourse/batch-gooded`, params)
}

export const WLecturer = {
  create: params => $DECORATE.post(`wLecturer/add`, params),
  delete: params => $DECORATE.post(`wLecturer/delete`, params),
  detail: params => $DECORATE.post(`wLecturer/detail`, params),
  page: params => $DECORATE.post(`wLecturer/page`, params),
  selectPage: params => $DECORATE.post(`wLecturer/select/page`, params),
  update: params => $DECORATE.post(`wLecturer/update`, params)
}

export const WOrg = {
  create: params => $DECORATE.post(`wOrg/add`, params),
  delete: params => $DECORATE.post(`wOrg/delete`, params),
  detail: params => $DECORATE.post(`wOrg/detail`, params),
  page: params => $DECORATE.post(`wOrg/page`, params),
  selectList: params => $DECORATE.post(`wOrg/select/list`, params),
  update: params => $DECORATE.post(`wOrg/update`, params)
}

export const Widget = {
  create: params => $DECORATE.post(`widget/add`, params),
  delete: params => $DECORATE.post(`widget/delete`, params),
  detail: params => $DECORATE.post(`widget/detail`, params),
  init: params => $DECORATE.post(`widget/init`, params),
  list: params => $DECORATE.post(`widget/list`, params),
  page: params => $DECORATE.post(`widget/page`, params),
  update: params => $DECORATE.post(`widget/update`, params)
}

export const WNews = {
  create: params => $DECORATE.post(`wNews/add`, params),
  delete: params => $DECORATE.post(`wNews/delete`, params),
  batchStatus: params => $DECORATE.post(`wNews/batch-status`, params),
  detail: params => $DECORATE.post(`wNews/detail`, params),
  page: params => $DECORATE.post(`wNews/page`, params),
  update: params => $DECORATE.post(`wNews/update`, params),
  updateViews: params => $DECORATE.post(`wNews/updateViews`, params)

}

export const WCategory = {
  create: params => $DECORATE.post(`wCategory/add`, params),
  batchEnabled: params => $DECORATE.post(`wCategory/batch-enabled`, params),
  childrenList: params => $DECORATE.post(`wCategory/children/list`, params),
  delete: params => $DECORATE.post(`wCategory/delete`, params),
  detail: params => $DECORATE.post(`wCategory/detail`, params),
  page: params => $DECORATE.post(`wCategory/page`, params),
  tree: params => $DECORATE.post(`wCategory/tree`, params),
  update: params => $DECORATE.post(`wCategory/update`, params)
}

export const Index = {
  basicInfo: params => $DECORATE.post(`index/basicInfo`, params),
  extraCategoryTree: params => $DECORATE.post(`index/extra/category/tree`, params),
  extraLivePage: params => $DECORATE.post(`index/extra/live/page`, params),
  extraCoursePage: params => $DECORATE.post(`index/extra/course/page`, params),
  extraLecturerPage: params => $DECORATE.post(`index/extra/lecturer/page`, params),
  extraNewsPage: params => $DECORATE.post(`index/extra/news/page`, params),
  extraOrgPage: params => $DECORATE.post(`index/extra/org/page`, params),
  pageInfo: params => $DECORATE.post(`index/pageInfo`, params)
}


export const OrgIndexDec = {
  basicInfo: params => $DECORATE.post(`org-index/basicInfo`, params),
  extraCategoryTree: params => $DECORATE.post(`org-index/extra/category/tree`, params),
  extraLivePage: params => $DECORATE.post(`org-index/extra/live/page`, params),
  extraCoursePage: params => $DECORATE.post(`org-index/extra/course/page`, params),
  extraLecturerPage: params => $DECORATE.post(`org-index/extra/lecturer/page`, params),
  extraNewsPage: params => $DECORATE.post(`org-index/extra/news/page`, params),
  extraOrgPage: params => $DECORATE.post(`org-index/extra/org/page`, params),
  pageInfo: params => $DECORATE.post(`org-index/pageInfo`, params)
}


export const OrgDecInfo = {
  info: params => $DECORATE.post(`orgDecInfo/info`, params),
  delete: params => $DECORATE.post(`orgDecInfo/delete`, params),
  page: params => $DECORATE.post(`orgDecInfo/page`, params),
  update: params => $DECORATE.post(`orgDecInfo/update`, params)
}


export const DecFile = {
  create: params => $DECORATE.post(`file/add`, params),
  batchEnabled: params => $DECORATE.post(`file/batch-enabled`, params),
  delete: params => $DECORATE.post(`file/delete`, params),
  detail: params => $DECORATE.post(`file/detail`, params),
  page: params => $DECORATE.post(`file/page`, params),
  update: params => $DECORATE.post(`file/update`, params)
}
