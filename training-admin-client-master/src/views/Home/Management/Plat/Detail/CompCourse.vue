<!--章节排课-->
<template>
  <el-card shadow="never" :body-style="{ padding: '16px' }" v-loading="loading">
    <div v-if="liveId">
      <el-page-header class="margin-bottom-12" @back="() => (liveId = null)" v-if="courseInfo.courseManage">
        <span class="font-14" slot="content">{{ lessonName }}</span>
      </el-page-header>
      <CompStartLive :liveId="liveId"></CompStartLive>
    </div>

    <div class="font-14" v-else>
      <div v-for="chapter in courseContent" :key="`c_${chapter.chapterId}`">
        <div class="flex start-center padding-top-8 padding-bottom-8" v-if="/^(1)/.test(courseMode)">
          <i class="padding-right-8 text-c pointer"
             :class="chapter.expand ? 'el-icon-caret-bottom' : 'el-icon-caret-right'"
             @click.stop="chapter.expand = !chapter.expand" v-if="chapter.lessonList && chapter.lessonList.length"></i>
          <span class="text-bold">{{ chapter.chapterName }}</span>
        </div>
        <el-collapse-transition>
          <div v-show="chapter.expand">
            <div class="" v-for="lesson in chapter.lessonList" :key="`l_${lesson.lessonId}`">
              <div class="margin-left-24 padding-top-12 padding-bottom-12" v-if="/^(1|2)/.test(courseMode)">
                {{ lesson.lessonName }}
              </div>
              <div class="flex height-56 padding-left-24 padding-right-24 border-bottom-1 is-hover"
                   v-for="component in lesson.componentList" :key="component.componentId">
                <div class="flex-3 flex between-center" :key="`f_${component.componentId}`">
                  <template v-if="component.componentTypeCode === 'SP'">
                    <div class="pointer flex-1 text-ellipsis"
                         :class="/^(3)/.test(courseMode) ? 'text-bold' : 'text-main'"
                         @click="goToPreview(component.resourceFilePath)">
                      <i class="el-icon-video-camera"></i><span class="margin-left-8">{{
                        component.componentName
                      }}</span>
                    </div>
                    <div>视频时长：{{ $utils.formatDuration(component.resourceFileDuration, ['h', 'm', 's']) || '--' }}</div>
                  </template>
                  <template v-else-if="component.componentTypeCode === 'ZB'">
                    <!-- @click="goToLive(component.resourceFileId, lesson.lessonName)" -->
                    <div class="pointer flex-1 text-ellipsis"
                         :class="/^(3)/.test(courseMode) ? 'text-bold' : 'text-main'">
                      <i class="el-icon-microphone"></i><span class="margin-left-8">{{ component.componentName }}</span>
                    </div>
                    <div class="flex start-center flex-2">
                      <div class="flex-2">直播时间：{{
                          $utils.DateFormat([component.startTime, component.endTime]) || '--'
                        }}
                      </div>
                      <div class="flex-1">{{ component.playbacked == 1 ? '允许回放' : '未允许回放' }}</div>
                      <div class="margin-left-20 flex-1">
                        直播状态：{{ component.playStatus == 0 ? '未开始' : component.playStatus === 1 ? '直播中' : '已结束' }}
                      </div>
                      <div class="flex-1">
                        时长： {{ $utils.formatDuration(component.totalDuration, ['时', '分', '秒']) || '--' }}
                      </div>
                      <div class="width-120 margin-left-56 flex end-center">
                        <!--                        <template>
                                                  <el-button type="text" size="medium" v-if="component.playbacked && component.playbackUrls && component.playbackUrls.length" @click="goToPlayback(component.playbackUrls[0])">查看回放</el-button>
                                                </template>
                                                <el-button type="primary" size="small" v-if="component.playStatus !== 2" @click="goToLive(component.resourceFileId, lesson.lessonName)">开始直播</el-button>-->
                        <el-button type="primary" size="small" @click="goToLiveDetail(component, lesson)">直播详情
                        </el-button>
                      </div>
                    </div>
                  </template>
                </div>
              </div>
            </div>
          </div>
        </el-collapse-transition>
      </div>
    </div>
    <DialogPreview ref="DialogPreview"></DialogPreview>
    <DialogPlayback ref="DialogPlayback"></DialogPlayback>
  </el-card>
</template>

<script>
import DialogPreview from '@/components/dialog/Course/DialogPreview'
import DialogPlayback from '@/components/dialog/Course/DialogPlayback'
import CompStartLive from './CompStartLive.vue'

export default {
  components: {
    DialogPreview,
    DialogPlayback,
    CompStartLive
  },
  data() {
    return {
      loading: false,
      courseContent: [],
      liveId: null,
      lessonName: '',
      courseMode: null
    }
  },
  props: {
    courseId: {
      type: [String, Number]
    },
    courseInfo: {
      type: Object,
      default: () => {
      }
    }
  },
  watch: {
    courseInfo: {
      deep: true,
      handler(val) {
        if (val.courseManage) {
          this.getContent()
        } else {
          this.liveId = this.courseInfo.liveId
        }
      }
    }
  },
  methods: {
    // 课程章节信息
    async getContent() {
      if (!this.courseId) return
      this.loading = true
      let {code, data} = await this.$api.Course.contentDetail({
        id: this.courseId
      })
      this.loading = false
      if (code !== 200) return false
      this.courseContent = data.chapterList.map(item => {
        return {
          ...item,
          expand: true
        }
      })
      this.courseMode = data.courseMode // 章节模式
    },
    // 预览视频
    goToPreview(url) {
      if (!url) return
      this.$refs.DialogPreview.open({
        url
      })
    },
    // 进入直播
    goToLive(liveId, lessonName) {
      this.liveId = liveId
      this.lessonName = lessonName
    },
    // 查看回放
    goToPlayback(url) {
      if (!url) return
      this.$refs.DialogPlayback.open({
        url
      })
    },

    // 操作 - 进入直播详情
    goToLiveDetail(component) {
      this.$router.push({
        name: 'CourseLiveDetail',
        query: {
          basicLiveId: component.resourceFileId,
          courseId: this.$route.query.id
        }
      })
    }
  }
}
</script>

<style lang="stylus" scoped>
.border-bottom-1
  border-bottom: 1px solid #eee

.is-hover:hover
  background #EBF2FF
  border-radius 2px 2px 2px 2px
  color #1D61F2
</style>
