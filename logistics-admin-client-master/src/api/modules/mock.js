import {
  ClassroomManageData,

  applyStatisticsData,
  moneyStatisticsData,
  ApplyPubData,
  
} from '@/lib/storage'

class MockAPI {
  constructor({ DataCache, BaseData, Fields, FilterFunc }) {
    if (!DataCache.get()) DataCache.set(BaseData)

    this.Fields = Fields
    this.FilterFunc = FilterFunc
    this.DataCache = DataCache

    this.create = this.create.bind(this)
    this.delete = this.delete.bind(this)
    this.detail = this.detail.bind(this)
    this.page = this.page.bind(this)
    this.update = this.update.bind(this)
  }

  create (params) {
    let { DataCache } = this
    let list = DataCache.get() || []
    DataCache.set([
      {
        id: Date.now(),
        ...params
      },
      ...list
    ])
    return {
      code: 200,
      data: null
    }
  }

  delete (params) {
    let { DataCache } = this
    let list = DataCache.get() || []
    let fIndex = list.findIndex(item => item.id === params.idList[0])
    if (fIndex > -1) list.splice(fIndex, 1)
    DataCache.set(list)
    return {
      code: 200,
      data: null
    }
  }

  detail (params) {
    let { DataCache } = this
    let list = DataCache.get() || []
    let fItem = list.find(item => item.id === params.id)
    return {
      code: 200,
      data: fItem
    }
  }

  page (params) {
    let { DataCache, Fields, FilterFunc } = this
    let list = DataCache.get() || []

    Fields.forEach(field => {
      if (params[field] !== undefined && params[field] !== null) list = list.filter(item => new RegExp(params[field]).test(item[field]))
    })

    if (FilterFunc) list = FilterFunc(params, list)

    let records = list.slice((params.current - 1) * params.size, params.current * params.size)

    // console.log(JSON.parse(JSON.stringify(records)))

    return {
      code: 200,
      data: {
        records,
        total: list.length
      }
    }
  }

  update (params) {
    let { DataCache } = this
    let list = DataCache.get() || []
    let fItem = list.find(item => item.id === params.id)

    for (const paramsKey in params) {
      fItem[paramsKey] = params[paramsKey]
    }
    DataCache.set(list)
    return {
      code: 200,
      data: null
    }
  }
}


export const ClassroomManageAPI = new MockAPI({
  DataCache: ClassroomManageData,
  BaseData: require('../json/classroom.json'),
  Fields: [
    'orgName',
    'classCode',
    'classType',
    'classStatus'
  ]
})


export const ApplyStatisicsAPI = new MockAPI({
  DataCache: applyStatisticsData,
  BaseData: require('../json/applyStatistics.json'),
  Fields: [
    'fundingObject',
    'inspectStatus'
  ],
  FilterFunc (params, list) {
    // let mList = applyStatisticsData.get() || []
    let { startDate, endDate, inspectStatus, fundingObject } = params
    if (startDate && endDate) {
      startDate = new Date(startDate).getTime()
      endDate = new Date(endDate).getTime()
      list = list.filter(a => {
        let inTime = new Date(a.createTime).getTime()
        return startDate <= inTime && inTime <= endDate
      })
    }
    return list
  }
})


export const moneyStatisicsAPI = new MockAPI({
  DataCache: moneyStatisticsData,
  BaseData: require('../json/moneyStatistics.json'),
  Fields: [
    'uuid',
    'contacts',
  ],
  FilterFunc (params, list) {
    // console.log('filterFunc = ', list);
    return list;
  }
})


// 资助申请评审
export const ApplyPubAPI = new MockAPI({
  DataCache: ApplyPubData,
  BaseData: require('../json/ApplyPub.json'),
  Fields: [

  ]
})


