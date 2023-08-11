<template>
  <view class="h-full">
    <map
      class="w-full h-full"
      :scale="18"
      :latitude="destiLocation.latitude"
      :longitude="destiLocation.longitude"
      :markers="markers"
      :circles="circles"
    ></map>
    <view
      class="map-info w-full h-10 bg-white flex items-center justify-center"
      @click="touchRefresh"
    >
      <text class="text-sm text-red-500">
        你当前不在打卡区域，请到达后再试
      </text>
    </view>
    <!-- <view
      class="map-info-test w-60 h-10 flex items-center justify-end"
      @click="touchRefresh"
    >
      <text class="text-sm text-red-500">
        {{ `当前坐标：${myLoactionInfo.longitude}-${myLoactionInfo.latitude}` }}
      </text>
    </view> -->
  </view>
</template>

<script>
export default {
  name: "LocationMap",
  props: {
    // 我的位置
    myLoactionInfo: {
      type: Object,
    },
    // 我的目的地
    destiLocation: {
      type: Object,
    },
    // 打卡范围
    signDistance: {
      type: Number,
    },
  },
  data() {
    return {};
  },
  computed: {
    // 当前路线
    polyline() {
      let { latitude: myLat, longitude: myLon } = this.myLoactionInfo;
      let { latitude: destiLat, longitude: destiLon } = this.destiLocation;
      return [
        {
          points: [
            {
              latitude: myLat,
              longitude: myLon,
            },
            {
              latitude: destiLat,
              longitude: destiLon,
            },
          ],
          color: "#31c27c",
          width: 10,
          arrowLine: true,
          borderWidth: 2, //线的边框宽度，还有很多参数，请看文档
        },
      ];
    },
    // 签到半径
    circles() {
      let { latitude, longitude } = this.destiLocation;
      return [
        {
          longitude,
          latitude,
          fillColor: "#FF00004A",
          color: "#FF0000",
          radius: this.signDistance,
          strokeWidth: 1,
        },
      ];
    },
    // 当前定位标记点
    markers() {
      let { latitude: destiLat, longitude: destiLon } = this.destiLocation;
      let { latitude: myLat, longitude: myLon } = this.myLoactionInfo;
      return [
        {
          id: 1,
          longitude: myLon,
          latitude: myLat,
          iconPath: "../../../static/my_location.png",
          width: 32,
          height: 32,
        },
        {
          id: 2,
          latitude: destiLat,
          longitude: destiLon,
          iconPath: "../../../static/location.png",
          width: 32,
          height: 32,
        },
      ];
    },
  },

  mounted() {},
  methods: {
    touchRefresh() {
      this.$emit("refresh");
    },
  },
};
</script>

<style lang="scss" scoped>
.map-info {
  position: absolute;
  bottom: 100px;
}

.map-info-test {
  position: absolute;
  top: 60px;
  right: 10px;
}
</style>