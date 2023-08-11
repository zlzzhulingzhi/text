const {$QUESTION} = require('@/api/config')

// 模板
export const ModuleNameTemplate = {
  get: params => $QUESTION.get(`get`, params),
  post: params => $QUESTION.post(`post`, params),
  delete: params => $QUESTION.delete(`delete`, params),
  put: params => $QUESTION.put(`put`, params),
  excel: params => $QUESTION.get(`post`, {params, responseType: 'arraybuffer'}) // 下载 excel 文件
}


export const QuestionType = {
  page: params => $QUESTION.post(`/questionType/page`, params),
  detail: params => $QUESTION.post(`/questionType/detail`, params),
  disable: params => $QUESTION.post(`/questionType/disable`, params),
  add: params => $QUESTION.post(`/questionType/add`, params),
  update: params => $QUESTION.post(`/questionType/update`, params),
  selectOptionList: params => $QUESTION.post(`/questionType/selectOptionList`, params)
}

export const Category = {
  detail: params => $QUESTION.post(`/category/detail`, params),
  update: params => $QUESTION.post(`/category/update`, params),
  delete: params => $QUESTION.post(`/category/delete`, params),
  batchEnabled: params => $QUESTION.post(`/category/enable`, params),
  // selectOptionList: params => $QUESTION.post(`/category/selectOptionList`, params),
  create: params => $QUESTION.post(`/category/add`, params),
  page: params => $QUESTION.post(`/category/page`, params),
  childrenList: params => $QUESTION.post(`/category/children/list`, params),
  questionCount: params => $QUESTION.post(`/category/question/count`, params),
  paperCount: params => $QUESTION.post(`/category/paper/count`, params)
}

export const Question = {
  search: params => $QUESTION.post(`/question/search`, params),
  smartChoose: params => $QUESTION.post(`/question/smartChoose`, params),
  create: params => $QUESTION.post(`/question/add`, params),
  update: params => $QUESTION.post(`/question/update`, params),
  detail: params => $QUESTION.post(`/question/detail`, params),
  page: params => $QUESTION.post(`/question/page`, params),
  excelQuestionBatchSave: params => $QUESTION.post(`/question/import/batchSave`, params),
  excelParse: params => $QUESTION.post(`/question/import/parse`, params.fileFormData),
  countCategory: params => $QUESTION.post(`/question/count/category`, params),
  searchCategory: params => $QUESTION.post(`/question/search/category`, params),
  categoryAll: params => $QUESTION.post(`/question/category/all`, params),
  count: params => $QUESTION.post(`/question/count`, params),
  enable: params => $QUESTION.post(`/question/enable`, params),
  delete: params => $QUESTION.post(`/question/delete`, params),
  group: params => $QUESTION.post(`/question/group`, params),
  category: params => $QUESTION.post(`/question/category`, params),
  downloadReport: params => $QUESTION.post(`/question/import/download/report`, params, {responseType: 'arraybuffer'}),
  importRecord: params => $QUESTION.post(`/question/import/record/page`, params)
}

export const Difficulty = {
  disable: params => $QUESTION.post(`/difficulty/disable`, params),
  selectOptionList: params => $QUESTION.post(`/difficulty/selectOptionList`, params)
}


export const Paper = {
  add: params => $QUESTION.post(`/paper/add`, params),
  update: params => $QUESTION.post(`/paper/update`, params),
  detail: params => $QUESTION.post(`/paper/detail`, params),
  delete: params => $QUESTION.post(`/paper/delete`, params),
  enable: params => $QUESTION.post(`/paper/enable`, params),
  page: params => $QUESTION.post(`/paper/page`, params),
  search: params => $QUESTION.post(`/paper/search`, params),
  category: params => $QUESTION.post(`/paper/category`, params),
  categoryAll: params => $QUESTION.post(`/paper/category/all`, params)
}


export const QuestionBasket = {
  add: params => $QUESTION.post(`/question/basket/add`, params),
  questionIdList: params => $QUESTION.post(`/question/basket/questionIdList`, params),
  detail: params => $QUESTION.post(`/question/basket/detail`, params),
  groupCount: params => $QUESTION.post(`/question/basket/groupCount`, params),
  remove: params => $QUESTION.post(`/question/basket/remove`, params),
  empty: params => $QUESTION.post(`/question/basket/empty`, params)
}