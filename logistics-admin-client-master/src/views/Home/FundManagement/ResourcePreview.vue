<template>
  <el-dialog custom-class="preview-dialog" :visible.sync="visible" width="1000px" top="5vh" append-to-body :close-on-click-modal="false" @close="close">
    <div :style="{height: $utils.FullViewHeight(150)}" class="overflow-auto">
      
      <iframe v-if="isIframe" ref="iframeWin" :src="url" frameborder="0" width="1000px" height="720" allowfullscreen></iframe>
      <video v-else width="100%" height="720" ref="videoElement" id="videoElement" controls></video>
      <!--图片展示-->
      <viewer v-if="isPicture" :images="images" @inited="inited" class="viewer" :options="{hide:hiddenImage}" ref="viewer" >
          <template #default="scope">
              <img v-for="src in scope.images" :src="src" :key="src">
              {{scope.options}}
          </template>
      </viewer>
     
    </div> 
  </el-dialog>
</template>

<script>
// flv文件播放
import flvjs from 'flv.js/dist/flv.js'
import CosRequest from '@/lib/cos.js'
import url from 'url'
import querystring from 'querystring'
import { mapState } from 'vuex'


import 'viewerjs/dist/viewer.css'
import { component as Viewer } from "v-viewer"


let cos = null

export default {
  name: 'ResourcePreview',
  data() {
    return {
        music: '',
      visible: false,
      type: null,
      url: null,
      flvPlayer: null,
      isIframe: true,
      images: [],
      isPicture: false,
      value: ''
    }
  },
   components: {
      Viewer
    },
  computed: {
    ...mapState('config', {
      COSProcessType: 'COSProcessType'
    })
  },
  created() {
    cos = new CosRequest()
  },
  methods: {
      inited (viewer) {
        this.$viewer = viewer
      },
      hiddenImage(viewer) {
        this.visible = false;
      },
    // 打开弹窗
    async open(data = {}) {
        data.url = decodeURIComponent(data.attachUrl);
        if (!data.url) return this.$message.warning('无效链接')
        this.visible = true;
        this.isIframe = true;
        const fileUrl = await cos.getObjectUrl(/^http(s)?:\/\/.+/.test(data.url) ? data.url : `https:${data.url}`)
        const suffix  = data.url.match(/[^\.]\w*$/)[0]
        if (this.COSProcessType.includes(suffix)) {
            let parse = url.parse(fileUrl)
            this.url = url.format({
                protocol: parse.protocol,
                hostname: parse.hostname,
                pathname: parse.pathname,
                query: {
                    'ci-process': 'doc-preview',
                    'dstType': 'html',
                    ...querystring.parse(parse.query),
                }   
            })
        } else {
            if (data.fileType === 'picture') {
                this.isPicture = true;
                this.images = [fileUrl];
                 this.url = '';
                this.$nextTick(()=>{
                    // this.visible= false;
                    this.isPicture = true;
                    this.$viewer.show();
                    
                })
            } else {
                if (data.remark === 'flv') {
                    this.isIframe = false;
                    this.flvplay(fileUrl);
                }  else {
                    this.url = fileUrl;
                }
            }
        }
    },
    // flv格式播放
   flvplay(fileUrl) {
       this.$nextTick(()=> {
           if (flvjs.isSupported()) {
                this.flvPlayer = flvjs.createPlayer({
                    type: 'flv',
                    url: fileUrl
                })
                this.flvPlayer.attachMediaElement(this.$refs.videoElement)
                this.flvPlayer.load()
            }
       })
   },
    // 关闭弹窗
    close() {
      if (this.url !== '') this.url = '';
      if (this.flvPlayer) {
          this.flvPlayer.destroy();
      }
      this.isPicture = false
      this.visible = false;
    }
  },
}
</script>

<style lang="stylus">
  .preview-dialog
    &.el-dialog
      background transparent
      box-shadow none

      .el-dialog__header
        border-bottom none
        margin-bottom 8px

        .el-dialog__headerbtn
          right 0

          .el-dialog__close
            color #fff
            font-size 1.2rem

      .el-dialog__body
        padding 10px 0
</style>