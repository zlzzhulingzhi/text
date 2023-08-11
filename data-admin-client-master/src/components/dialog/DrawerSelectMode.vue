<template>
  <el-drawer :visible.sync="visible" direction="rtl" :before-close="close" size="600px">
    <template slot="title">
      <div class="text-bold text-3 font-16">{{ drawerInfo.title }}</div>
    </template>
    <div class="flex column between-center height-100p">
      <el-radio-group v-model="value" class="width-100p padding-left-78 padding-right-78">
        <el-radio class="width-100p margin-bottom-24 " :label="item.id" v-for="item in drawerInfo.list" :key="item.id">
          <span class="font-14">{{ item.name || item.label }}</span>
          <div class="margin-top-4 margin-left-24 text-6 font-13" style="white-space: break-spaces;">{{ item.description }}</div>
        </el-radio>
      </el-radio-group>
      <div class="width-100p" style="box-shadow: 2px 0px 6px 1px #E6F3FF;">
        <FormSaveBar class="margin-top-10" @submit="submit" @cancel="close"></FormSaveBar>
      </div>
    </div>
  </el-drawer>
</template>

<script>
export default {
  name: 'DrawerSelectMode',
  data() {
    return {
      visible: false,
      value: null,
      drawerType: 'task',
      typeMapping: {
        task: { title: '选择培训模式', list: this.$store.getters['common/taskMode'] },
        exam: { title: '考试模式选择', list: this.$store.getters['common/examModel'] }
      }
    }
  },
  computed: {
    drawerInfo() {
      return this.typeMapping[this.drawerType] || {}
    }
  },
  methods: {
    open(data = {}) {
      this.drawerType = data.type || 'task'
      this.value = data.value || null
      this.visible = true
    },
    close() {
      this.visible = false
    },
    submit() {
      this.$emit('select', this.value)
      this.close()
    }
  }
}
</script>

<style lang="stylus" scoped>
>>>.el-drawer__header
  border-bottom 1px solid #E6E6E6
  margin-bottom 30px
  padding-bottom 20px
</style>