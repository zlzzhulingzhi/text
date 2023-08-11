<template>
  <TableView class="padding-top-20" :options="options" :params.sync="filterData">
    <div class="flex end-center bg-f padding-left-20 padding-right-20 padding-bottom-20">
      <!-- <UploadImage v-model="newFileUrl" @input="uploadSuccess">
        <el-button type="primary" size="small" icon="el-icon-upload2" :loading="loading.submit">上传图片</el-button>
        <span slot="tips"></span>
      </UploadImage> -->
      <el-button type="primary" size="small" icon="el-icon-upload2" @click="onAdd">上传图片</el-button>
    </div>
    <el-table v-loading="loading.table" :data="tableData" :height="$utils.FullViewHeight(214)">
      <el-table-column prop="fileUrl" label="轮播图">
        <template slot-scope="{row}">
          <el-image class="height-90" :src="row.fileUrl"></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="sort" label="排序" width="100"></el-table-column>
      <el-table-column prop="display" label="是否展示" width="100">
        <template slot-scope="{row}">{{ row.display | display }}</template>
      </el-table-column>
      <el-table-column label="操作" width="130">
        <div class="flex" slot-scope="{row}">
          <!-- <UploadImage v-model="row.fileUrl" @input="uploadSuccess($event, row)">
            <el-button type="text" size="small">重新上传</el-button>
            <span slot="tips"></span>
          </UploadImage> -->
          <el-button type="text" size="small" @click="onEdit(row)">编辑</el-button>
          <el-button class="margin-left-12" type="text" size="small" @click="onDelete(row)">删除</el-button>
        </div>
      </el-table-column>
    </el-table>
    <DialogUploadBanner ref="DialogUploadBanner" @success="getTableData"></DialogUploadBanner>
  </TableView>
</template>

<script>
import DialogUploadBanner from '@/components/dialog/DialogUploadBanner.vue'
import UploadImage from '@/components/upload/UploadImage.vue'

const name = '轮播图'

export default {
  name: 'Banner',
  components: {
    DialogUploadBanner,
    UploadImage
  },
  data() {
    return {
      loading: {
        table: false,
        submit: false
      },
      options: {
        total: 0
      },
      filterData: {},
      tableData: [],
      timer: null,
      newFileUrl: null
    }
  },
  computed: {
    params() {
      return {
        ...this.filterData
      }
    }
  },
  watch: {
    params: {
      handler() {
        clearTimeout(this.timer)
        this.timer = setTimeout(() => {
          this.getTableData()
        }, 300)
      },
      immediate: true
    }
  },
  methods: {
    async getTableData() {
      this.loading.table = true
      let { code, data } = await this.$api.ScreenManage.pageBanner(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records || []
      this.options.total = data.total
    },
    onAdd() {
      this.$refs.DialogUploadBanner.open()
    },
    onEdit(row) {
      this.$refs.DialogUploadBanner.open({
        type: 'Update',
        formData: row
      })
    },
    onDelete(row) {
      this.$confirm(`是否删除该${name}？`, '提示', {
        type: 'warning'
      }).then(async () => {
        let { code } = await this.$api.ScreenManage.deleteBanner({
          idList: [row.id]
        })
        if (code !== 200) return false
        this.$msg.Delete(name)
        this.getTableData()
      }).catch(() => {})
    },
    // async uploadSuccess(fileUrl, item) {
    //   this.loading.submit = true
    //   if (item) {
    //     let { code } = await this.$api.ScreenManage.updateBanner({
    //       ...item,
    //       fileUrl
    //     })
    //     this.loading.submit = false
    //     if (code !== 200) return false
    //     this.$msg.Update(name)
    //   } else {
    //     let { code } = await this.$api.ScreenManage.addBanner({
    //       display: 1,
    //       duration: 5,
    //       fileType: 'pic',
    //       sort: this.tableData[this.tableData.length - 1].sort + 1,
    //       fileUrl
    //     })
    //     this.loading.submit = false
    //     if (code !== 200) return false
    //     this.$msg.Create(name)
    //   }
    //   this.getTableData()
    // }
  }
}
</script>

<style lang="stylus" scoped>

</style>