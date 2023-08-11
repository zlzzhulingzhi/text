<template>
  <view>
    <template v-if="selectClassList.length">
      <view v-for="item, index in selectClassList" :key="index" class="flex flex-col pl-2 pr-2 pt-2"
        @click="onClick(item)">
        <view v-if="item.building && item.floor" class="flex justify-between items-center w-full">
          <text>单元/楼层: </text>
          <text>{{ `${(buildingList.find(v => v.id === item.building) || {}).name}${(floorList.find(v =>
              v.id ===
              item.floor) || {}).name}`
          }}</text>
        </view>
        <view class="flex justify-between items-center w-full">
          <text>房间号：</text>
          <text>{{ item.roomNo }}</text>
        </view>
        <view class="flex justify-between items-center w-full mb-2">
          <text>教室类型：</text>
          <text>{{ classroomList.find(v => v.id === item.roomType).name }}</text>
        </view>
        <view class="w-full line bg-gray-200"></view>
      </view>

    </template>
    <view v-else class="mt-40 p-2">
      <view class="flex justify-between items-center w-full" @click="onShowChooseBuilding">
        <text>单元/楼层</text>
        <u-icon name="arrow-right"
          :label="`${(buildingList.find(v => v.id === form.building) || {}).name}${(floorList.find(v => v.id === form.floor) || {}).name}`"
          labelPos="left" v-if="form.building && form.floor"></u-icon>
        <u-icon name="arrow-right" label="请选择" labelPos="left" v-else></u-icon>
      </view>
      <view class="flex justify-between items-center w-full mt-10" @click="onShowChooseRoomType">
        <text>教室类型</text>
        <u-icon name="arrow-right" :label="(classroomList.find(v => v.id === form.roomType) || {}).name || '请选择'"
          labelPos="left"></u-icon>
      </view>
      <view class="mt-20">
        <u-button type="primary" text="查询" @click="onSeacrh"></u-button>
      </view>

    </view>
  </view>

</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'PopupSelectList',
  props: {
    item: {
      type: Object,
    }
  },
  data() {
    return {
      form: {},
      searchParams: {
        current: 1,
        size: 12
      },
      // 选择教室列表
      selectClassList: [],
      total: 0
    }

  },
  computed: {
    ...mapGetters({
      buildingList: 'common/buildingList',
      floorList: 'common/floorList',
      classroomList: 'common/classroomList',
    })
  },
  methods: {
    onClick(item) {
      this.$emit('click', item)
    },
    onShowChooseBuilding() {
      this.$emit('showChooseBuilding', {});
    },
    onShowChooseRoomType() {
      this.$emit('showChooseRoomType', {});
    },
    confirmBuilding(value) {
      this.form.building = value[0].id
      this.form.floor = value[1].id
      this.$forceUpdate()
    },

    confirmType(value) {
      // console.log('value === ', value)
      this.$set(this.form, 'roomType', value[0].id)
      this.form.roomType = value[0].id
    },
    async onSeacrh() {
      // 查询报事报修关联教室
      // if (!this.form.building) {
      //   return uni.$u.toast('请选择单元/楼层')
      // }
      // if (!this.form.roomType) {
      //   return uni.$u.toast('请选择房间类型')
      // }

      // console.log('this.form 2222=== ', this.form.floor)

      Object.assign(this.searchParams, { ...this.form });

      let { code, data } = await this.$api.WorkOrder.matterClassPage(this.searchParams);
      if (code !== 200) return false;
      if (data.records.length === 0 && this.searchParams.current === 1) return uni.$u.toast('请重新选择，暂无此种房间');
      if (data.records && data.records.length) {
        const list = data.records
        this.selectClassList = this.selectClassList.concat(list)
      }
      this.total = data.total
      // console.log('code ', code);
      // console.log('data == ', data);
      // console.log('this.form == ', this.form);
    },
    onScrollToBottom() {
      // console.log('========滑动到底部了。。。。')
      if (this.selectClassList.length >= this.total) return false;
      this.searchParams.current = ++this.searchParams.current;
      this.onSeacrh();
    },
    clearPopdata() {
      this.selectClassList = []
      this.form = {}
      this.searchParams = { current: 1, size: 12 }
      this.total = 0
    }
  }
}
</script>

<style lang="scss" scoped>
.line {
  height: 0.5px;
}
</style>