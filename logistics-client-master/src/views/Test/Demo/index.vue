<template>
  <div>
    <!--不继续开发-->
    <el-tabs v-model="activeName">
      <el-tab-pane label="Captcha" name="Captcha">
        <Captcha :code="'5823'" :width="120" :height="48"></Captcha>
      </el-tab-pane>

      <el-tab-pane label="DynamicBackground" name="DynamicBackground">
        <DynamicBackground class="bg-background" append-to-body>
          <div :style="{height: `calc(100vh - 51px)`}"></div>
        </DynamicBackground>
      </el-tab-pane>

      <el-tab-pane label="Results" name="Results">
        <Results></Results>

        <Results type="NotFound" alt="自定义提示">
          <el-button>自定义内容</el-button>
        </Results>

        <Results :type="{
          src: 'https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png',
          alt:'百度'
        }"></Results>
      </el-tab-pane>

      <el-tab-pane label="RouterAlive" name="RouterAlive">
        <RouterAlive></RouterAlive>
      </el-tab-pane>

      <el-tab-pane label="SideMenu" name="SideMenu">
        <SideMenu :style="{height: `calc(100vh - 51px)`}" :sideMenuList="[
            {name:'Test',meta:{title:'开发页',icon:'el-icon-help'}},
            {name:'Menu',meta:{title:'菜单',icon:'el-icon-menu'},children:[
                {name:'Demo',meta:{title:'Demo页面',icon:'el-icon-document'}}
            ]}
        ]"></SideMenu>
      </el-tab-pane>

      <el-tab-pane label="WechatCode" name="WechatCode">
        <WechatCode appId="wxff447f63362a0dd2" domain="https://cbd.armpc.com/#/Wechat/Login"
                    :query="{token:123}"></WechatCode>
      </el-tab-pane>

      <el-tab-pane label="TableView" name="TableView">
        <TableViewDemo></TableViewDemo>
      </el-tab-pane>

      <el-tab-pane label="DialogFormTemplate" name="DialogFormTemplate">
        <el-button @click="$refs.DialogFormTemplate.open({
          type:'AppCreate'
        })">
          新增应用
        </el-button>
        <el-button @click="$refs.DialogFormTemplate.open({
          type:'AppEdit',
          formData: {test:'编辑值'},
        })">
          编辑应用
        </el-button>

        <DialogFormTemplate ref="DialogFormTemplate" @success="()=>{'请求表格数据'}"></DialogFormTemplate>
      </el-tab-pane>

      <el-tab-pane label="DialogSelectTemplateDemo" name="DialogSelectTemplateDemo">
        <DialogSelectTemplateDemo></DialogSelectTemplateDemo>
      </el-tab-pane>

      <el-tab-pane label="ImageClip" name="ImageClip">
        <ImageClip ref="ImageClip" :width="80" :height="80" :zoom="4"
                   :src="require('@/assets/live/img_2.png')"></ImageClip>
        <el-button @click="onDownload">下载</el-button>


        <div class="width-400 height-400">
          <VueCropper ref="cropper" :img="require('@/assets/live/img_2.png')"
                      autoCrop fixed centerBox :autoCropWidth="80" :autoCropHeight="80"
                      outputType="png"
          ></VueCropper>
        </div>

        <el-button @click="onVueCropperDownload">下载</el-button>

        <ImageUpload v-model="image"></ImageUpload>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import {VueCropper} from 'vue-cropper'
import Captcha from '@/components/common/Captcha'
import DynamicBackground from '@/components/common/DynamicBackground'
import Results from '@/components/common/Results'
import RouterAlive from '@/components/common/RouterAlive'
import SideMenu from '@/components/common/SideMenu'
import WechatCode from '@/components/common/WechatCode'
import TableViewDemo from './TableViewDemo'
import DialogFormTemplate from '@/components/dialog/DialogFormTemplate'
import DialogSelectTemplateDemo from './DialogSelectTemplateDemo'
import ImageClip from '@/components/form/ImageClip'
import ImageUpload from '@/components/form/ImageUpload'

export default {
  name: 'Demo',
  components: {
    Captcha,
    DynamicBackground,
    Results,
    RouterAlive,
    SideMenu,
    WechatCode,
    TableViewDemo,
    DialogFormTemplate,
    DialogSelectTemplateDemo,
    ImageClip,
    VueCropper,
    ImageUpload
  },
  data() {
    return {
      activeName: 'ImageClip',

      image: null
    }
  },
  methods: {
    async onDownload() {
      let fileData = await this.$refs.ImageClip.getFileData()
      this.$utils.DownloadBase(fileData)
    },
    async onVueCropperDownload() {
      // getCropBlob || getCropData
      this.$refs.cropper.getCropData(fileData => {
        this.$utils.DownloadBase(fileData)
      })
    },
  }
}
</script>

<style scoped lang="stylus">
.height-400
  height 400px
</style>