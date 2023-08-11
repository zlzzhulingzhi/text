const { $COURSE } = require("@/api/config");

// 活动
export const Activity = {
  create: (params) => $COURSE.post(`tActivity/add`, params),
  delete: (params) => $COURSE.post(`tActivity/delete`, params),
  detail: (params) => $COURSE.post(`tActivity/detail`, params),
  page: (params) => $COURSE.post(`tActivity/page`, params),
  update: (params) => $COURSE.post(`tActivity/update`, params),
  updateShelfStatus: (params) => $COURSE.post(`tActivity/updateShelfStatus`, params),
};


// 图片管理
export const PictureManage = {
  add: (params) => $COURSE.post(`banner/add`, params),
  delete: (params) => $COURSE.post(`banner/delete`, params),
  page: (params) => $COURSE.post(`banner/page`, params),
  update: (params) => $COURSE.post(`banner/update`, params),
};