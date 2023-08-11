import HttpRequest from './HttpRequest'

// 节省API上的一个流程 - 不需要对改该文件进行操作，只需要保证 环境配置中的格式
for (const envKey in process.env) {
  let match = envKey.match(/^VUE_APP_(\w+)_API$/)
  if (match) {
    module.__proto__.exports[`$${match[1]}`] = new HttpRequest({
      baseURL: process.env[envKey]
    })
  }
}