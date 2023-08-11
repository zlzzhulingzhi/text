<template>
  <div>
    <el-table :data="value">
      <el-table-column
        label="导航名称"
        width="180"
      >
        <template v-slot="{row}">
          <el-input
            ref="input"
            v-if='row.editable'
            v-model="row.navbarName"
            :maxlength="4"
          >
            <el-button
              class="width-32 padding-0"
              slot="append"
              type="primary"
              icon="el-icon-check"
              @click.stop="onCheck(row)"
            ></el-button>
          </el-input>
          <div
            v-else
            class="font-14 width-240"
          >
            {{ row.navbarName }}
            <el-button
              class="width-32"
              type="text"
              icon="el-icon-edit"
              size="small"
              @click.stop="onEdit(row)"
              v-if="row.edited"
            ></el-button>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        label="跳转链接"
        width="500"
      >
        <template
          v-slot="{row}"
          style="flex"
        >
          <span
            v-if="!row.edited"
            class="font-14 width-240 padding-right-8"
          >
            {{ row.navbarName }}</span>
          <el-select
            v-else
            v-model="row.linkType"
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
            v-model="row.linkUrl"
            size="small"
            v-if="row.linkType === 1"
            placeholder="请输入自定义链接"
            class="width-240"
            maxlength="100"
            show-word-limit
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column
        label="默认图标"
        width="100"
      >
        <template v-slot="{row}">
          <UploadImage
            :width="40"
            :height="40"
            v-model="row.iconUrl"
          >
            <div slot="tips"></div>

          </UploadImage>
          <div class="icon_tip font-10">{{row.iconUrl?'修改':'添加'}}</div>
        </template>
      </el-table-column>
      <el-table-column
        label="选中图标"
        width="100"
      >
        <template v-slot="{row}">
          <UploadImage
            :width="40"
            :height="40"
            v-model="row.focusIconUrl"
          >
            <div slot="tips"></div>
          </UploadImage>
          <div class="icon_tip font-10">{{row.iconUrl?'修改':'添加'}}</div>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template v-slot="{row,$index}">
          <i
            class="el-icon-delete "
            :class="{'color-a':!row.edited,pointer:row.edited}"
            @click="onDelete(row,$index)"
          ></i>
          <!-- <el-input v-model="row.name" /> -->
        </template>
      </el-table-column>

    </el-table>
    <div class="text-left margin-top-32">
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
import { mapState } from "vuex";
export default {
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
  created() {
    this.loadPagesPage();
  },
  computed: {
    ...mapState("system", {
      userInfo: "userInfo",
    }),
    pageParams() {
      return {
        current: 1,
        size: 10,
        pageType: 2,
        orgId: this.userInfo.orgId,
        enabled: 1,
      };
    },
  },
  methods: {
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
    onDelete(data, index) {
      if (!data.edited) return false;
      this.value.splice(index, 1);
    },
    onAdd() {
      if (this.value.length >= 5)
        return this.$message.warning("最多可设置5个导航");
      let child = {
        navbarName: "",
        linkType: null,
        disabled: false,
        edited: 1,
        groupCode: "navbarH5",
        editable: false,
      };
      this.value.splice(this.value.length - 1, 0, child);

      this.$nextTick(() => {
        try {
          this.onEdit(child);
        } catch (e) {}
      });
    },
  },
};
</script>

<style  scoped lang="stylus">
.color-a {
  color: #aaa;
}

.icon_tip {
  position: absolute;
  bottom: 7px;
  left: 7px;
  width: 47px;
  height: 17px;
  line-height: 17px;
  text-align: center;
  font-size: 10px;
  color: NEUTRAL_COLOR_F;
  background: rgba(0, 0, 0, 0.5);
  border-radius: 0px 0px 4px 4px;
}
</style>