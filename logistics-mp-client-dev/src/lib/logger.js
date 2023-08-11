const showLog = process.env.NODE_ENV !== 'production'

export default {
  info(log, tag = this.TAG.APP, show) {
    if (showLog || show) {
      console.log(`%c${getTime()} %c[${tag}]%c ${log}`, 'color:blue', 'color:green', 'color:black')
    }
  },
  warn(log, tag = this.TAG.APP, show) {
    if (showLog || show) {
      console.warn(`%c${getTime()} %c[${tag}]%c ${log}`, 'color:orange', 'color:olive', 'color:black')
    }
  },
  error(log, tag = this.TAG.APP, show) {
    if (showLog || show) {
      console.error(`%c${getTime()} %c[${tag}]%c ${log}`, 'color:red', 'color:orange', 'color:black')
    }
  },
  TAG: {
    APP: 'App',
    AUTH: 'Auth',
    COS: 'COS',
    LAUNCH: 'Launch',
    REQUEST: 'Request',
    STORAGE: 'Storage',
    VUE: 'Vue'
  }
}

function getTime() {
  const date = new Date()
  const millis = date.getMilliseconds()
  return `${uni.$u.timeFormat(date, 'yyyy-mm-dd hh:MM:ss')}.${millis}`
}