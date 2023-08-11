<template>
  <div class="width-100%">
    <el-tabs v-model="activeTab" @tab-click="handleClick">
      <el-tab-pane v-for="(item, index) in tabsList" :key="index" :label="item.label" :name="item.name">
        <component :is="item.id"></component>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>


<script>
import BasicInfo from './BasicInfo.vue'
import RemainImport from './RemainImport.vue'

export default {
  name: 'DormitoryRemainInfo',
  components: {
    BasicInfo,
    RemainImport
  },
  mixins: [],
  props: {},
  data() {
    return {
      // 选中的tab的name
      activeTab: 'Remain',
      tabsList: [
        {id: 'RemainImport', label: '剩余统计', name: 'Remain'},
        {id: 'BasicInfo', label: '基本信息', name: 'baseInfo'},
      ]
    }
  },
  created() {

  },
  mounted() {},
  computed: {},
  watch: {},
  methods: {
    handleClick() {
      
    }
  }
}
</script>


<style lang="stylus" scoped>

</style>