<template>
  <el-card :style="{height: $utils.FullViewHeight(32)}">

    <!--上传图片通用组件-->
    <div class="height-0 overflow">
      <ImageUpload ref="ImageUpload" isEdit :autoUpload="true" :width="120" :height="60" :options="{
        fixedNumber: [5, 2],
        autoCropWidth: 686,
        autoCropHeight: 270,
      }" @input="onSaveImage">
        <div slot="tips"></div>
      </ImageUpload>
    </div>

    <!-- 选择图片使用处 -->
    <el-select v-model="picGoal" size="small" placeholder="请选择" class="margin-right-8 margin-bottom-8">
      <el-option
        v-for="item in picGoals"
        :key="item.id"
        :label="item.name"
        :value="item.id">
      </el-option>
    </el-select>

    <el-button type="primary" size="small" icon="el-icon-plus" @click="onSelectFile" class="margin-bottom-8 margin-top-8" v-if="images.length<=6">上传图片</el-button>
    <span class="font-12 margin-left-16 bg-global-background" v-if="(images&&images.length)">支持 .png,.jpg,.jpeg,.gif,.webp 格式图片，大小不超过 5M</span>

    <div v-if="!(images&&images.length)" class="height-90 width-100p bg-global-background flex column center-center">
      <span>上传图片展示操作区域</span>
      <span class="font-12">支持 .png,.jpg,.jpeg,.gif,.webp 格式图片，大小不超过 5M</span>
    </div>

    <div class="flex wrap start-start height-90 width-100p" v-else>
      <div v-for="(item, index) in images" :key="index" class="relative images">
        <el-image class="width-120 height-60 margin-right-8" :src="item.imgUrl"></el-image>

        <el-button class="close-btn" type="info" size="mini" circle icon="el-icon-close" @click="onDelete(index, item)"></el-button>
      </div>
    </div>


    <div class="margin-top-60 margin-bottom-8"><b>轮播图预览</b></div>
    <div v-if="!(miniProgramPictures&&miniProgramPictures.length)" class="height-400 width-100p bg-global-background flex center-center"><span>轮播图预览区域</span></div>
    <Banner :list="miniProgramPictures" errorInfo="小程序图片" v-if="miniProgramPictures?.length" class="width-950"></Banner>


  </el-card>
</template>

<script>
import { mapGetters } from 'vuex'
import Banner from '@/components/common/Banner'

export default {
  name: 'PicManage',
  components: {
    Banner
  },
  data() {
    return {
      images: [],
      picGoal: 'activity',
      timer: {
        pic: null
      }
    };
  },

  computed: {
    miniProgramPictures() {
      return this.images.map(item => {
        return {
          filePath: item.imgUrl
        }
      })
    },
    ...mapGetters({
      picGoals: 'common/picGoals'
    }),
    params() {
      return {
        current: 1,
        size: 999,
        section: this.picGoal
      } 
    }
  },
  watch: {
    params: {
      immediate: true,
      deep: true,
      handler () {
        clearTimeout(this.timer.pic)
        this.timer.pic = setTimeout(() => {
          this.getPictures()
        }, 300)
      }
    }
  },
  methods: {
    // 操作 - 获取图片
    async getPictures() {
      let {code, data} = await this.$api.PictureManage.page(this.params)
      if(code === 200) {
        this.images = data.records
      }
    },
    // 操作 - 调用上传图片
    onSelectFile() {
      this.$refs.ImageUpload.onSelectFile()
    },
    // 操作 - 保存图片回调
    async onSaveImage(image) {
      // this.images.push(image)
      let {code} = await this.$api.PictureManage.add({
        imgUrl: image,
        section: this.picGoal
      })
      if(code === 200) {
        this.getPictures()
        this.$msg('新增图片')
      }
    },
    //操作 - 删除图片
    async onDelete(index, item) {
      await this.$confirm('确认删除图片吗', {
        title: '删除确认',
        type: 'warning'
      })
      let {code} = await this.$api.PictureManage.delete({
        idList: [item.id]
      })
      if(code === 200) {
        this.getPictures()
        this.$msg('删除图片')
      }
    }
  },
};
</script>

<style lang="stylus" scoped>
.images
  .close-btn
    position absolute
    right 10px
    top 2px
    padding 0
    width 18px
    height 18px
    font-size 12px
    display none


  &:hover
    .close-btn 
      display block

.height-400
  height 400px

.width-950
  width 950px
</style>