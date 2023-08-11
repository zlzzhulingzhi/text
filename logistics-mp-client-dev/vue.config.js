const TransformPages = require('uni-read-pages')
const { webpack } = new TransformPages()
const fs = require('fs')

module.exports = {
  transpileDependencies: ['uview-ui'],
  devServer: {
    port: 8088,
    proxy: {
      '/': {
        target: 'https://wa-union-org.armpc.cn/gateway',
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          '^/auth': '/auth',
          '^/basic': '/basic',
          '^/course': '/course',
          '^/logistics': '/logistics',
          '^/train': '/train',
          '^/u-admin': '/u-admin',
          '^/u-auth': '/u-auth'
        }
      }
    }
  },
  configureWebpack: {
    plugins: [
      new webpack.DefinePlugin({
        ROUTES: webpack.DefinePlugin.runtimeValue(() => {
					const tfPages = new TransformPages({
						includes: ['path', 'name', 'meta']
					})
					return JSON.stringify(tfPages.routes)
				}, true)
      })
    ]
  },
  chainWebpack: config => {
    config.plugin('define').tap(args => {
      if (args[0]['process.env'].VUE_APP_MODE) {
        args[0]['process.env'].NODE_ENV = args[0]['process.env'].VUE_APP_MODE

        let doc = fs.readFileSync('.env.testing', 'utf-8')
        let arr = Array.from(doc.matchAll(/^VUE_APP_.+/gm)).map(item => item[0].split(/\s*=\s*/))
        arr.forEach(item => {
          args[0]['process.env'][item[0]] = item[1]
        })
      }
      return args
    })
  }
}