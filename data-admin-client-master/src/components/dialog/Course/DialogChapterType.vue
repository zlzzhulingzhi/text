

<template>
  <el-dialog :visible.sync="visible" append-to-body title="选择章节模式" :close-on-click-modal="false" :before-close="close" width="300px">
    <el-radio-group v-model="chapterType">
      <div class="margin-top-10" v-for="item in chapterTypeList" :key="item.id">
        <el-radio :label="item.type">
          {{ item.name }}
        </el-radio>
      </div>
    </el-radio-group>
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="submit">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'DialogChapterType',
  data() {
    return {
      visible: false,
      chapterType: 1,
    }
  },
  computed: {
    ...mapState('common', {
      chapterTypeList: 'chapterTypeList'
    })
  },
  methods: {
    open(type = 0) {
      this.visible = true;
      this.chapterType = type;
      return new Promise((resolve, reject) => {
        this.$once('handle', (type, val) => {
          if (type == 'success') {
            resolve(val)
          } else {
            reject('取消')
          }
        })
      })
    },
    submit() {
      this.$emit('handle', 'success', this.chapterType);
      this.close();

    },
    close() {
      this.$emit('handle', 'close')
      this.visible = false;
    }
  }
}
</script>

<style lang="stylus" scoped>

</style>