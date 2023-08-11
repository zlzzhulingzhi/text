# logistics-mp-client

## Dependencies

* [Vue.js](https://cn.vuejs.org/v2/guide/)
* [uni-app](https://uniapp.dcloud.net.cn/)
* [uView](https://www.uviewui.com/components/intro.html)
* [uni-simple-router](https://hhyang.cn/v2/start/quickstart.html)
* [tailwindcss](https://tailwindcss.com/)

## Introduction

### 小程序页面传参说明

id: 主键，根据页面不同
psn: 课程详情页查询个人课程详情标识
org: 机构ID（orgId）

### 注意事项

1.微信小程序最低基础库 2.21.2
2.微信小程序请求、获取网络图片信息、打开外链需配置 request、download、业务域名白名单
3.微信小程序进行页面连续跳转会报错并阻碍流程，第二步跳转请等待页面完全渲染