<template>
  <u-count-down :time="time" format="DD天HH:mm:ss.S" :millisecond="millisecond" @change="onChange" @finish="onFinsh">
    <view class="flex items-center" :class="customClass" v-if="/^simple/.test(mode)">
      <text class="mr-1" v-if="timeData.days">{{ timeData.days }}天</text>
      <text>{{ timeData.hours >= 10 ? timeData.hours : '0' + timeData.hours }}</text>
      <text class="px-1">:</text>
      <text>{{ timeData.minutes >= 10 ? timeData.minutes : '0' + timeData.minutes }}</text>
      <text class="px-1">:</text>
      <text>{{ timeData.seconds >= 10 ? timeData.seconds : '0' + timeData.seconds }}</text>
      <template v-if="millisecond">
        <text class="px-1">.</text>
        <text>{{ Math.floor(timeData.milliseconds / 100) }}</text>
      </template>
    </view>
    <view class="flex items-center text-sm" v-else>
      <text class="mr-1" v-if="timeData.days">{{ timeData.days }}天</text>
      <view class="flex justify-center bg-error text-white rounded item">
        <text>{{ timeData.hours >= 10 ? timeData.hours : '0' + timeData.hours }}</text>
      </view>
      <text class="px-1">:</text>
      <view class="flex justify-center bg-error text-white rounded item">
        <text>{{ timeData.minutes >= 10 ? timeData.minutes : '0' + timeData.minutes }}</text>
      </view>
      <text class="px-1">:</text>
      <view class="flex justify-center bg-error text-white rounded item">
        <text>{{ timeData.seconds >= 10 ? timeData.seconds : '0' + timeData.seconds }}</text>
      </view>
      <template v-if="millisecond">
        <text class="px-1">.</text>
        <view class="flex justify-center bg-error text-white rounded item">
          <text>{{ Math.floor(timeData.milliseconds / 100) }}</text>
        </view>
      </template>
    </view>
  </u-count-down>
</template>

<script>
export default {
  name: 'uni-countdown',
  props: {
    endTime: {
      type: [String, Number],
      required: true
    },
    millisecond: {
      type: Boolean,
      default: false
    },
    mode: {
      type: String
    },
    customClass: {
      type: String
    }
  },
  data() {
    return {
      time: 0,
      timeData: { days: 0, hours: 0, minutes: 0, seconds: 0, milliseconds: 0 }
    }
  },
  watch: {
    endTime: {
      deep: true,
      handler(val) {
        if (!val) return false
        if (typeof(val) === 'number') this.time = val > 0 ? val : 0
        if (typeof(val) === 'string') this.time = this.$utils.getTimeDiff(null, this.endTime)
      },
      immediate: true
    }
  },
  methods: {
    onChange(e) {
      this.timeData = e
    },
    onFinsh() {
      if (!this.time) return false
      this.$emit('timeup')
    }
  }
}
</script>

<style lang="scss" scoped>
.item {
  width: 22px;
  height: 22px;
}
</style>