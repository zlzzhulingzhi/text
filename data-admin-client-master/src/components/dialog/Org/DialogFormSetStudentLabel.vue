<template>
  <el-dialog ref="form" :visible.sync="isLabelDialog" append-to-body title="学员标签"
             :close-on-click-modal="false"
             :before-close="handleClose" width="600px">
    <div class="scroll-label overflow">
      <div class="label-list">
        <el-tag class="label-item" :class="item.flag == 0 ? 'active' : ''" v-for="item in labelList" :key="item.id"
                :title="item.groupName" @click="selectLabel(item)" >
          {{ item.groupName }}
        </el-tag>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="setStudentGroup">确 定</el-button>
    </div>
  </el-dialog>

</template>

<script>
export default {
  name: 'DialogFormSetStudentLabel',
  data() {
    return {
      isLabelDialog: false,
      addApi: '',
      id: '',
      labelList: [],
      selectLabelArr: []
    }
  },
  methods: {
    openLabel(params) {
      this.isLabelDialog = true
      this.selectLabelArr = [];
      this.id = params.id;
      this.addApi = params.addApi
      this.getDetail()
    },
    async getDetail() {
        this.selectLabelArr = [];
        const params = { id: 0 };
        if (this.addApi == 'studentGroup') {
            params.id = this.id
        }
        let {code, data} = await this.$api.studentGroup.detail(params);
        if (code !== 200) return;
        this.labelList = data;
        if (this.addApi == 'studentGroup') {
            this.filterLable(data);
        }
    },
    filterLable(list) {
        list.forEach(item => {
            if (item.flag == 0) {
                this.selectLabelArr.push(item.id);
            }
        });
    },
    handleClose() {
      this.isLabelDialog = false;
    },
    async selectLabel(params) {
        params.flag = params.flag == 1 ?  0 : 1;
        if (params.flag === 1) {
            params.active = false;
            this.selectLabelArr.splice(this.selectLabelArr.indexOf(params.id), 1);
            return;
        }
        this.$set(params, 'active', true);
        this.selectLabelArr.push(params.id);
    },
    async setStudentGroup() {
      let params = {
        groupIds: [], //标签集合id
        studentId: null //学员id
      }
      if (this.addApi === 'addList') { // 批量
          params.studentIdList = this.id;
          params.groupIds = this.selectLabelArr;
      } else {  // 单个
          params.studentId = this.id;
          params.groupIds = this.selectLabelArr;
      }
      let {code} = await this.$api.studentGroup[this.addApi](params)
      if (code !== 200) return
      this.$emit('refreshAction')
      this.handleClose();
    }
  }

}
</script>

<style scoped lang="stylus">   
.scroll-label {
  height: 240px;
}

.label-list {
  flex-direction: row;
  flex-wrap: wrap;
  display: flex;
  width: 100%;
  overflow: auto;
  max-height: 100%;

  .label-item {
    display: block;
    padding-right: 10px;
    padding-left: 10px;
    margin-bottom: 10px;
    border-radius: 4px;
    width: calc((100% - (10px * 15)) / 4);
    margin-left: 5px;
    margin-right: 5px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    cursor pointer
  }

  .active {
    color: #fff;
    background: MAIN_COLOR;
  }
}
</style>

