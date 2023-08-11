import HttpRequest from '@/api/request'

const $COS = new HttpRequest({
  baseURL: process.env.VUE_APP_COS_API
})

export const Cos = {
  // COS授权
  cosCredential: params => $COS.post(`/cosCredential`, params)
}