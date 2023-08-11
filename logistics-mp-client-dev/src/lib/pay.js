export const Payment = (payInfo) => {
  return new Promise((resolve) => {
    // #ifdef MP-WEIXIN
    uni.requestPayment({
      provider: 'wxpay',
      timeStamp: payInfo.timeStamp,
      nonceStr: payInfo.nonceStr,
      package: payInfo.packageValue,
      signType: payInfo.signType,
      paySign: payInfo.paySign,
      success: (res) => {
        resolve({
          status: 'success',
          msg: JSON.stringify(res)
        })
      },
      fail: (err) => {
        resolve({
          status: 'fail',
          msg: err.errMsg
        })
      }
    })
    // #endif

    // #ifndef MP-WEIXIN
    setTimeout(() => {
      resolve({
        status: 'error',
        msg: '此平台暂不支持在线支付'
      })
    }, 0)
    // #endif
  })
}