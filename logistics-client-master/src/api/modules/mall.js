import store from '@/store'

const {$MALL} = require('@/api/config')

export const Coupon = {
  // 新增优惠券
  add: params => $MALL.post(`/mCouponBack/add`, params),
  // 优惠券列表
  couponList: params => $MALL.post(`/mCouponBack/couponList`, params),

  // 优惠券详情
  detail: params => $MALL.post(`/mCouponBack/detail`, params),
  // 分页获取课程
  pageCourse: params => $MALL.post(`/mCouponBack/pageCourse`, params),
  // 4.1日 获取课程列表
  pageGoods: params => $MALL.post(`/goods/page`, params),
  // 分发前获取学员信息
  pageStudent: params => $MALL.post(`/mCouponBack/pageStudent`, params),
  // 分页查询优惠券领取信息
  pageUser: params => $MALL.post(`/mCouponBack/pageUser`, params),

  // 获取组织列表
  getOrgDept: () => $MALL.post(`/mCouponBack/getOrgDept`, {id: store.state.system.orgId}),
  // 获取角色列表
  getOrgRole: () => $MALL.post(`/mCouponBack/getOrgRole`, {id: store.state.system.orgId}),

  // 分发优惠券
  batchGive: params => $MALL.post(`/mCouponBack/batchGive`, params),

  //修改优惠券状态（可能是h5的？）
  updateStatus: params => $MALL.post(`/mCoupon/updateStatus`, params),

  // 我的优惠券/选择优惠券 （可能是h5的？）
  selectCoupon: params => $MALL.post(`/mCoupon/selectCoupon`, params)
}