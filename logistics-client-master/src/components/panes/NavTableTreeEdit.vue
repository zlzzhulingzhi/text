<template>
  <div class="nav-tree margin-top-8 ">

    <div class="border-f2">
      <div class="flex between-center padding-left-8  width-100p font-12 text-3 bg-f2 height-44">
        <div class="width-160 flex-shrink-0">导航名称</div>
        <div class="width-360 ">跳转链接</div>
        <div class="width-128 margin-left-8 margin-right-8  flex between-center ">
          <div>默认图标</div>
          <div>选中图标</div>
        </div>
        <div class="padding-right-8 width-140 ">操作</div>
      </div>
      <el-tree
        :data="value"
        :draggable="draggable"
        :allow-drop="allowDrop"
        :allow-drag="allowDrag"
        default-expand-all
      >
        <div
          slot-scope="{node,data}"
          class="flex padding-left-8  between-center width-100p line-height-58 border-bottom-f2"
        >

          <!--标题-->
          <el-input
            class="width-160"
            v-if="data.editable"
            v-model="data.navbarName"
            placeholder="导航名称"
            :maxlength="4"
            show-word-limit
            size="small"
            @keyup.enter.native="onCheck(data)"
            ref="input"
          >
            <el-button
              class="width-32 height-30 padding-0"
              slot="append"
              type="primary"
              icon="el-icon-check"
              @click.stop="onCheck(data)"
            ></el-button>
          </el-input>
          <div
            v-else
            class="font-14 width-160 height-58"
          >
            {{ data.navbarName }}
            <el-button
              class="width-32"
              type="text"
              icon="el-icon-edit"
              size="small"
              @click.stop="onEdit(data)"
              v-if="data.edited"
            ></el-button>
          </div>

          <div
            class="padding-4 width-360 height-58"
            v-if="!(data.children || []).length && data.edited"
          >
            <el-select
              v-model="data.linkType"
              size="small"
              class="width-120"
            >
              <el-option
                v-for="item in list"
                :key="item.id"
                :label="item.navbarName"
                :value="item.linkType"
              ></el-option>
            </el-select>
            <el-input
              v-model="data.linkUrl"
              size="small"
              v-if="data.linkType === 1"
              placeholder="请输入自定义链接"
              class="width-240"
              maxlength="100"
              show-word-limit
            ></el-input>
            <!-- <el-button
            v-else
            size="small"
            class="width-240"
            @click="onSetName(data)"
          >设置导航名称</el-button> -->
          </div>
          <div
            v-else
            class="height-58 line-height-58 width-360 font-14"
          > {{ data.navbarName }}</div>
          <div class="width-120 flex between-center margin-left-8 margin-right-8">
            <div class="relative">
              <UploadImage
                style="align-items:center"
                :width="40"
                :height="40"
                v-model="data.iconUrl"
              >
                <div slot="tips"></div>
              </UploadImage>
              <div class="icon_tip font-10">{{data.iconUrl?'修改':'添加'}}</div>
            </div>
            <div class="relative">
              <UploadImage
                style="align-items:center"
                :width="40"
                :height="40"
                v-model="data.focusIconUrl"
              >
                <div slot="tips"></div>
              </UploadImage>
              <div class="icon_tip font-10">{{data.focusIconUrl?'修改':'添加'}}</div>
            </div>
          </div>
          <div class="padding-right-16 height-58">
            <el-button
              class="text-main text-bold"
              type="text"
              icon="el-icon-rank"
              size="mini"
              v-if="node.level === 1"
              :disabled="!draggable || data.editable || !Boolean(data.edited)"
            >
              拖拽排序
            </el-button>
            <el-button
              class="text-main text-bold"
              type="text"
              icon="el-icon-delete"
              size="mini"
              @click.stop="onDelete(data,node)"
              :disabled="!Boolean(data.edited)"
            >
              删除
            </el-button>
          </div>
        </div>
      </el-tree>
    </div>
    <div class="text-left margin-top-8">
      <el-button
        type="primary"
        size="small"
        icon="el-icon-plus"
        @click="onAdd"
      >添加导航</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "NavTreeEdit",
  props: {
    value: {
      type: Array,
      default() {
        return [];
      },
    },
    list: {
      type: Array,
      default() {
        return [];
      },
    },
  },
  data() {
    return {
      initPermission: "nav:custom",
    };
  },
  computed: {
    draggable() {
      return !this.$utils.ArrayFlat(this.value).some((item) => item.editable);
    },
  },
  methods: {
    onAdd() {
      if (this.value.length >= 5)
        return this.$message.warning("最多可设置5个导航");
      let child = {
        navbarName: null,
        linkType: null,
        disabled: false,
        edited: 1,
        groupCode: "navbarH5",
      };

      this.value.splice(this.value.length - 1, 0, child);

      this.$nextTick(() => {
        try {
          this.onEdit(child);
        } catch (e) {}
      });
    },

    async loadPagesPage() {
      const { code, data } = await this.$api.Pages.page(this.pageParams);
      if (code !== 200) return false;
      // this.options.menu = data.records;
      console.log("loadPagesPage", data.records);
    },
    onCheck(data) {
      if (!data.navbarName) return this.$message.warning("请输入导航名称");
      if (!data.navbarName.trim()) return this.$message.warning("不能输入空格");
      this.$set(data, "editable", !data.editable);
      this.$set(data, "navbarName", data.navbarName);
    },

    onEdit(data) {
      if (!data.edited) return false;
      this.$set(data, "editable", !data.editable);
      this.$set(data, "navbarName", data.navbarName);
    },
    onDelete(data) {
      console.log(data);
      let index = this.value.findIndex((item) => item.id === data.id);
      if (!data.edited) return false;
      this.value.splice(index, 1);
    },
    onSetName(data) {
      if (data.permission !== this.initPermission) {
        this.$set(data, "editable", false);
        this.$set(
          data,
          "name",
          this.list.find((item) => item.permission === data.permission).name
        );
      }
    },

    allowDrop(draggingNode, dropNode, type) {
      if (!dropNode.data.edited) return false;
      if (type === "inner") return false;
      return true;
    },

    allowDrag(node) {
      return node.level === 1 && node.data.edited;
    },
  },
};
</script>

<style scoped lang="stylus">
.nav-tree {
  div {
    flex-shrink: 0;
  }

  >>> .el-tree {
    > .el-tree-node {
      // padding-top 8px
      padding-bottom: 4px;
    }

    .el-tree-node__content>.el-tree-node__expand-icon {
      padding: 0;
    }

    .el-icon-caret-right:before {
      content: '';
    }

    .el-tree-node__content {
      width: 100%;
      height: auto;
    }

    .el-tree-node__children {
      border-radius: 4px;
      background-color: BACKGROUND_COLOR;
    }
  }

  >>> .el-input-group__append {
    padding: 0;
    width: 32px;

    .el-button {
      margin: 0;
    }
  }

  .icon_tip {
    position: absolute;
    bottom: 7px;
    left: -3px;
    width: 47px;
    height: 17px;
    line-height: 17px;
    text-align: center;
    font-size: 10px;
    color: NEUTRAL_COLOR_F;
    background: rgba(0, 0, 0, 0.5);
    border-radius: 0px 0px 4px 4px;
  }

  .border-bottom-f2 {
    border-bottom: 1px solid #f2f2f2;
  }
}
</style>