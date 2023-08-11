// 读取module下API并export出去
require.context('./modules', false, /\.js$/).keys().forEach($path => {
  let apiList = require('./modules' + $path.slice(1))
  // 开发环境命名冲突
  if (process.env.NODE_ENV === 'development') {
    let keys = Object.keys(module.__proto__.exports)
    let errorItem = Object.keys(apiList).find(item => keys.includes(item))
    if (errorItem && errorItem !== 'ModuleNameTemplate') {
      alert(`注意！有命名冲突：${errorItem}, 路径：${'./modules' + $path.slice(1)}`)
    }
  }
  Object.assign(module.__proto__.exports, apiList)
})

export default module.__proto__.exports