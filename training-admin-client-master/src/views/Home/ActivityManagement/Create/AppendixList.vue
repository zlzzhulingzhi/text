
<template>
  <div class="border-f2">
    <div class="flex between-center padding-left-8 width-100p font-12 text-3 height-44 border-bottom-1 ">
      <div class="width-50 flex-shrink-0 ">类型</div>
      <div class="width-90p">内容</div>
      <div class="padding-right-8">操作</div>
    </div>
    <el-tree :data="appendixList" :draggable="draggable" :allow-drop="allowDrop" default-expand-all @node-drag-end="setList">
      <template slot-scope="{ node, data }">
        <div class="flex between-center width-100p line-height-64 border-bottom-f2 text-bold-400" >
        <div class="height-64 width-50 font-14 flex column center margin-left-8">{{ data.componentTypeCode == 'SP' ? "视频" : "直播" }}</div>
        <div class="width-80p flex font-14 margin-left-8 margin-right-8 ">
          <div class="relative">
            <div class="line-height-18 text-3 text-ellipsis width-400">
              <span>{{ data.componentName }}</span>
              <span class="margin-left-6 text-9 font-12 bgc" v-if="data.componentTypeCode == 'ZB'">{{ data.playbacked == 1 ? '允许回放' : '未允许回放' }}</span>
            </div>
            <div class="text-9 line-height-16 margin-top-8" v-if="data.componentTypeCode == 'SP'">
              {{ $utils.formatDuration(data.resourceFileDuration, ["时", "分", "秒"]) }}
            </div>
            <div class="text-9 line-height-16 margin-top-8" v-else>{{ $utils.DateFormat([data.startTime,data.endTime]) || '--' }}</div>
          </div>
        </div>
        <div class="height-64 font-14">
          <el-button class="text-main text-bold font-16" type="text" icon="el-icon-edit" size="mini" @click.stop="$emit('updataAttachName', data)" v-if="data.componentTypeCode == 'SP'"></el-button>
          <el-button class="text-main text-bold font-16" type="text" icon="el-icon-edit" size="mini" @click.stop="editAttach(data, node)" v-else></el-button>
          <el-button class="text-main text-bold font-16" type="text" icon="el-icon-rank" @mousedown.native="onDraggable"  size="mini" v-if="node.level === 1">  </el-button>
          <el-button class="text-main text-bold font-16" type="text" icon="el-icon-delete" size="mini" @click.stop="onDelete(data, node)">  </el-button>
        </div>
      </div>
      </template>
    </el-tree>
  </div>
</template>

<script>
export default {
  props: {
    AppendixParams: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      draggable: false
    }
  },
  computed: {
    appendixList() {
      return this.AppendixParams.appendixList;
    },
    componentList() {
      return this.AppendixParams.appendixList.map(item => {
        return item.componentId
      })
    }
  },
  methods: {
    // 编辑附件
    editAttach(row) {
      this.$emit('updataAttachZB', row)
    },
    allowDrop(draggingNode, dropNode, type) {
      if (type !== "inner")  return true
    },
    onDraggable(event) {
      let target = event.target;
      if (/^(BUTTON|SPAN|I)/.test(target.nodeName)) return this.draggable = true;
    },
    setList() {
      this.AppendixParams.sortedIds = this.componentList
      this.$emit('setTreeSort', this.AppendixParams)
      this.draggable = false;
    },
    async onDelete(row) {
      this.$confirm("此操作将永久取消附件关联，是否继续？", "提示", {
        type: "warning",
      }).then(async () => {
        let { code } = await this.$api.Course.deleteComponent({
          idList: [row.componentId]
        })
        if (code !== 200) return false
        this.$msg.Delete("附件")
        this.$emit('getComponentList', this.AppendixParams)
      }).catch(() => {})
    }
  }
}
</script>

<style lang="stylus" scoped>
.text-bold-400 {
  font-weight: 400;
}
.text-main {
  >>>.el-icon-rank {
    transform:rotate(45deg);
  }
}

>>>.is-leaf.el-tree-node__expand-icon.el-icon-caret-right {
  display: none
}

.bgc {
  background-color #f2f2f2
}
</style>