
<template>
  <el-tree ref="tree"
           :data="treeData"
           draggable
           :options="{}"
           v-if="/^(1|2)/.test(courseMode)"
           default-expand-all
           @node-drop="handleDrop"
           :allow-drop="allowDrop"
           :props="{ children: 'lessonList', label: 'lessonName' }"
           highlight-current
           node-key="id"
           @node-click="initChapterCentent">
    <div class="custom-tree-node flex"
         slot-scope="{ data, node }">
      <slot name="side"
            :data="data"
            :node="node"></slot>
    </div>
  </el-tree>
</template>

<script >
  export default {
    name: 'tree',
    props: {
      listData: {
        type: Array,
        default: () => []
      },
      courseMode: {
        type: Number,
        default: 1
      }
    },
    data() {
      return {
        timer: null
      }
    },
    computed: {
      treeData() {
        if (!this.listData.length) return []
        let listData = this.listData.map(item => {
          return {
            ...item,
            id: item.chapterId,
            lessonList: item.lessonList.map(element => {
              return {
                ...element,
                id: element.lessonId
              }
            })
          }
        })
        if (this.courseMode === 2) return listData[0].lessonList
        return listData
      }
    },
    watch: {
      treeData: {
        deep: true,
        handler(newVal) {
          if (newVal && newVal.length) {
            if (this.timer) clearTimeout(this.timer)
            this.timer = setTimeout(() => {
              if (newVal[0].lessonList && newVal[0].lessonList.length) {
                this.$refs.tree.setCurrentKey(newVal[0].lessonList[0].id)
                this.initChapterCentent(newVal[0].lessonList[0])
              } else {
                this.$refs.tree.setCurrentKey(newVal[0].id)
                this.initChapterCentent(newVal[0])
              }
            }, 300)
          }
        }
      }
    },
    methods: {
      initChapterCentent(item) {
        this.$emit('initChapterCentent', item)
      },
      // 判断当前拖拽的节点是否可以被放置
      allowDrop(draggingNode, dropNode, type) {
        let draggingData = draggingNode.data
        let dropData = dropNode.data
        if (
          draggingNode.level === 1 &&
          dropNode.level === 1 &&
          type !== 'inner'
        ) {
          return true
        } else if (draggingNode.level === 2 && dropNode.level === 2) {
          if (draggingData.chapterId === dropData.chapterId && type !== 'inner')
            return true
        }
      },
      // 节点拖拽成功触发
      handleDrop(draggingNode, dropNode, dropType) {
        // 最优解
        // 获取树下面的节点
        let chapterList = this.$refs.tree.data
        // 判断是否为节节点还是章节点
        console.log(this.$refs.tree);
        if (draggingNode.data.lessonId) {
          // 节拖拽
          let lessonList =
            chapterList.find(
              item => item.chapterId === draggingNode.data.chapterId
            ).lessonList || chapterList
          this.setParams(
            'second',
            draggingNode.data,
            lessonList.map(item => item.lessonId)
          )        
        } else {
          // 章拖拽
          this.setParams(
            'first',
            draggingNode.data,
            (chapterList = chapterList.map(item => item.chapterId))
          )
        }
      },
      setParams(level, data, list) {
        let params = {
          sortedIds: list,
          chapterId: data.chapterId,
          lessonId: data.lessonId,
          courseId: '',
          level: level
        }
        // this.setTreeSort(params)
        this.$emit('setTreeSort', params)
      }
    }
  }
</script>

<style lang="stylus" scoped>
  .width-170 {
    width: 170px;
    display: inline-block;
    vertical-align: middle;
  }

  .width-200 {
    width: 200px;
    display: inline-block;
    vertical-align: middle;
  }

  >>>.el-tree-node {
    &:focus>.el-tree-node__content {
      background: #EBF2FF;
      color: #1D61F2;
    }

    .el-tree-node__content {
      min-height: 36px;
      height: auto;
      color: #333;
      font-weight: 500;
      padding-right: 10px;

      &:hover {
        background-color: #EBF2FF;
        color: #1D61F2;
      }
    }
  }

  >>>.el-tree-node__children {
    .el-tree-node {
      &:focus>.el-tree-node__content {
        color: #1D61F2;
      }

      max-height: 36px;

      .el-tree-node__content {
        height: 36px;
        padding-left: 24px !important;
        padding-right: 10px;
        font-weight: 400;
        color: #666;

        &:hover {
          background-color: #EBF2FF;
          color: #1D61F2;
        }

        .el-tree-node__expand-icon.el-icon-caret-right {
          display: none;
        }
      }
    }
  }

  >>>.el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
    background-color: #EBF2FF;
    color: #1D61F2;
  }
</style>