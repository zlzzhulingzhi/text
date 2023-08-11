module.exports = {
  presets: [
    '@vue/cli-plugin-babel/preset'
  ],
  env: {
    development: {},
    production: {
      plugins: [
        ['transform-remove-console', {'exclude': ['error', 'warn']}] // 清除console打印
      ]
    }
  }
}