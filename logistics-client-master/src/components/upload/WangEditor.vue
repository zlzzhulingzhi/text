<template>
  <div id="editor"></div>
</template>

<script>
import WangEditor from 'wangeditor'
import CosRequest from '@/lib/cos.js'

let editor = null

export default {
  props: {
    content: {
      type: String,
      required: true
    },
    destroy: {
      type: Boolean,
      default: false
    },
    disable: {
      type: Boolean,
      default: false
    }
  },
  watch: {
    content(val) {
      this.setContent(val)
    },
    destroy(val) {
      if (val) editor.destroy()
    },
    disable(val) {
      if (val) editor.disable()
    }
  },
  mounted() {
    editor = new WangEditor('#editor')
    editor.config.zIndex = 500
    editor.config.menus = [
      'head',
      'fontSize',
      // 'fontName',
      'bold',
      'italic',
      'underline',
      'strikeThrough',
      'indent',
      // 'lineHeight',
      'foreColor',
      'backColor',
      'link',
      'list',
      'todo',
      'justify',
      'quote',
      // 'emoticon',
      'image',
      // 'video',
      'table',
      // 'code',
      'splitLine',
      'undo',
      'redo'
    ]
    editor.config.showFullScreen = false
    editor.config.uploadImgMaxSize = 5 * 1024 * 1024
    editor.config.uploadImgMaxLength = 1
    editor.config.customUploadImg = function(resultFiles, insertImgFn) {
      const cos = new CosRequest()
      cos.uploadFile({
        file: resultFiles[0],
        onSuccess: (url) => {
          insertImgFn(url)
        },
        onError: (err) => {
          this.$message.error(err)
        }
      })
    }
    editor.create()
  },
  methods: {
    setContent(content) {
      editor.txt.html(content)
    },
    getContent() {
      return editor.txt.html()
    },
    getText() {
      return editor.txt.text()
    }
  }
}
</script>

<style lang="stylus" scoped>

</style>