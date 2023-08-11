<!--机构课程-->
<template>
  <view>
    <template v-if="listData.length">
      <CardCourse :item="item" v-for="item in listData" :key="item.id" @click="goToDetail(item)">
        <template #action>
          <view @click.stop="goToStudent(item)">
            <u-button type="primary" shape="circle" size="small">预报学员</u-button>
          </view>
        </template>
      </CardCourse>
    </template>
    <u-loading-icon v-else-if="loading.list"></u-loading-icon>
    <uni-empty v-else></uni-empty>
  </view>
</template>

<script>
import CardCourse from '@/components/views/CardCourse.vue'

export default {
  name: 'OrgCourse',
  components: {
    CardCourse
  },
  data() {
    return {
      loading: {
        list: false
      },
      params: {
        current: 1,
        size: 10
      },
      total: 0,
      listData: []
    }
  },
  onLoad() {
    this.getList();
  },
  onReachBottom() {
    if (this.listData.length >= this.total) return false;
    this.params.current = ++this.params.current;
    this.getList();
  },
  methods: {
    async getList() {
      this.loading.list = true;
      let { code, data } = await this.$api.Course.orgPage(this.params);
      this.loading.list = false;
      if (code !== 200) return false;
      if (data.records && data.records.length) {
        let list = data.records;
        this.listData = this.listData.concat(list);
      }
      this.total = data.total;
    },
    goToStudent(item) {
      this.goToPage({ route: 'ReserveStudent', params: { orgId: item.orgId, courseId: item.id } });
    },
    goToPage(item) {
      if (!item.route) return false
      uni.$u.route(this.$utils.getRoutePath(item.route), item.params);
    },
    async goToDetail(item) {
      uni.$u.route(this.$utils.getRoutePath('ActivityDetail'), {
        id: item.id
      })
    },
  },
}
</script>

<style lang="scss" scoped>

</style>