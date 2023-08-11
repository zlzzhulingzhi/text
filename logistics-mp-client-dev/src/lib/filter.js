export default {
  install(Vue) {
    Vue.filter('price', filterPrice)
  }
}

// 金额处理（分 → 元）
const filterPrice = (price) => {
  if (!price) return 0
  return price / 100
}