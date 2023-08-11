

<template >
    <el-popover width="484" placement="bottom" trigger="click" >
        <div class="flex padding-12 between-center">
            <div class="width-240 height-290 border-1 bg-f">
                <div class="overflow height-135">
                     <el-image v-if="statistic.coverUrl" class="width-100p" :src="statistic.coverUrl" ></el-image>
<!--                    <img style="width: 238px;" :src="statistic.coverUrl">-->
                </div>
                <div class="padding-16">
                    <div class="font-14 text-3 width-100p text-ellipsis">
                        课程名称: {{ statistic.courseName }}
                    </div>
                    <div class="font-14 text-6 margin-top-60" style="color: #FF4B5B"> ¥ {{ courseInfo.coursePrice / 100 | number }}</div>
                    </div>
                </div>
            <div class="share width-180 height-290 border-1 padding-18 text-center">
                <div class="qr-code width-144 padding-10">
                    <QrcodeVue :value="playerUrl" :size="124"></QrcodeVue>
                </div>
                <div class="padding-top-4 text-3 font-14 text-center">扫码查看手机端</div>
                <el-button type="primary" size="mini" class="margin-top-16" @click="fowordAction">查看电脑端</el-button>
                <el-button type="text font-14" @click="onCopyUrl">复制链接</el-button>
            </div>
        </div>
        <el-button v-if="isBtnType" size="mini" type="primary" slot="reference">分享查看</el-button>
        <el-button v-else type="text" slot="reference" class="text-6" style="font-weight: initial;width: 100%; text-align: start;">分享查看</el-button>
    </el-popover>
</template>

<script>
import QrcodeVue from 'qrcode.vue'
import { mapState } from 'vuex'
    export default {
        name: 'popoverShare',
        components: { QrcodeVue },
        props: {
            isBtnType: {
                type: Boolean,
                default: true
            },
            statistic: {
                type: Object,
                default: ()=>{}
            },
            courseInfo: {
                type: Object,
                default: ()=>{}
            }
        },
        data() {
            return {}
        },
        computed: {
            ...mapState('system', {
                orgId: 'orgId',
                innerDomain: 'innerDomain'
            }),
            playerUrl() {
                return `${this.innerDomain}/#/Course/Detail?id=${this.courseInfo.id}`
            },
            
        },
        methods: {
            onCopyUrl() {
                this.$utils.CopyToClipboard(this.playerUrl);
            },
            fowordAction() {
                window.open(this.playerUrl, Date.now()+'_bank')
            }
        }
    } 

</script>

<style lang="stylus" scoped>
    .border-1 {
        border-radius: 4px
        border: 1px solid #eee
    }
    .height-290 {
        height: 290px;
    }
    .height-135 {
        height: 135px;
    }
    .qr-code {
        height: 144px
        border-radius: 4px
        border: 1px dashed #eee;
    }
</style>