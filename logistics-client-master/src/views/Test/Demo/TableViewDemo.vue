<template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:other>
      <div class="margin-bottom-8" :style="{order: -1}">
        <el-button>公共操作按钮区域（例如，新增、导出全部数据，导入）</el-button>
        <el-button>只有1个按钮，放筛选区域左边</el-button>
      </div>
    </template>
    <template v-slot:main>
      <div class="margin-bottom-8 " :style="{order: -1}">筛选条件区域 开始</div>
      <el-button class="margin-bottom-8 margin-right-8" :style="{order: -1}" size="small">其他筛选按钮</el-button>
      <el-button class="margin-bottom-8" :style="{order: 101,marginLeft:0}" size="small">其他筛选按钮（右）</el-button>
      <div class="margin-bottom-8 " :style="{order: 101}">筛选条件区域 结束</div>
    </template>
    <template v-slot:side>
      <div class="">搜索条件区域，可换行</div>
    </template>

    <div class="margin-top-16 flex between-center">
      <div>统计信息放这里，没有统计信息，批量操作居右</div>
      <div>
        <el-button>对表格操作、例如批量操作、批量导出</el-button>
      </div>
    </div>

    <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(298)">
      <template v-slot:empty>
        <Results></Results>
      </template>

      <el-table-column type="selection" width="50" ></el-table-column>
      <el-table-column type="index" label="序号" width="55" ></el-table-column>

      <el-table-column label="名称" prop="name" min-width="88"></el-table-column>

      <el-table-column label="操作" width="88"  fixed="right">
        <template slot-scope="{row}">
          <el-button type="text" size="small">提交</el-button>
          <el-button type="text" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </TableView>
</template>

<script>
import TableView from '@/components/pages/TableView'

export default {
  name: 'TableViewDemo',
  components: {
    TableView
  },
  data() {
    return {
      tableData: [],
      filterData: {},
      timer: {
        table: null
      },
      options: {
        total: 100,
        main: {
          course: {
            label: '课程',
            options: [
              {id: 1, name: '语文'},
              {id: 2, name: '数学'},
              {id: 3, name: '英语'},
              {id: 4, name: '化学'}
            ],
            clearable: false,
            value: 1
          },
          sex: {
            value: 1
          },
          tradeDate: {
            order: 1
          },
          date: {}
        },
        side: {
          name: {
            label: '姓名',
            value: '默认值'
          },
          phone: {
            label: '号码'
          }
        },
        labelWidth: 0
      }
    }
  },
  computed: {
    params() {
      return {
        ...this.filterData,
        other: '其他参数或者覆盖参数'
      }
    }
  },
  watch: {
    params: {
      deep: true,
      immediate: true,
      handler(val) {
        console.log(val)
        clearTimeout(this.timer.table)
        this.timer.table = setTimeout(() => {
          // 监听对象时，需要使用定时器消除重复请求
          this.$message.success('ComponentsStandard页面 发出请求')
        }, 100)
      }
    }
  }
}
</script>

<style scoped>

</style>
