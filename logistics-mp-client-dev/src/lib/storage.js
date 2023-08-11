import logger from '@/lib/logger'

export class Storage {
  constructor(name) {
    this.name = `${process.env.NODE_ENV === 'production' ? `WAM` : `WAN`}-${name}`
  }
  get() {
    try {
      return uni.getStorageSync(this.name)
    } catch (e) {
      logger.error(`Get StorageSync Exception: ${e}`, logger.TAG.STORAGE)
      return null
    }
  }
  set(data) {
    try {
      uni.setStorageSync(this.name, data)
    } catch (e) {
      logger.error(`Set StorageSync Exception: ${e}`, logger.TAG.STORAGE)
    }
  }
  remove() {
    try {
      uni.removeStorageSync(this.name)
    } catch (e) {
      logger.error(`Remove StorageSync Exception: ${e}`, logger.TAG.STORAGE)
    }
  }
}

export function clearStorage() {
  try {
    console.log('uni.clearStorageSync')
    // uni.clearStorageSync()
  } catch (e) {
    logger.error(`Clear StorageSync Exception: ${e}`, logger.TAG.STORAGE)
  }
}

export const Token = new Storage('Token')
export const UserInfo = new Storage('UserInfo')
export const AuthInfo = new Storage('AuthInfo')
export const Identity = new Storage('Identity')
export const SignTime = new Storage('SignTime')