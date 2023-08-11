import HttpRequest from '@/api/request'

const $BASIC = new HttpRequest({
  baseURL: process.env.VUE_APP_BASIC_API
})

export const Dict = {
  list: params => $BASIC.post(`/dict/list`, params)
}