<template>
  <view>
    <template class="empty" v-if="show == false">
      <u-empty mode="car" icon="http://cdn.uviewui.com/uview/empty/car.png">
        <u-button size="small" type="primary" :style="{ marginTop: 10 + 'px' }" text="查看更多商品">
        </u-button>
      </u-empty>
    </template>
    <template v-if="show == true">
      <view class="goods-detail flex items-center justify-between" v-for="(item, index) in goods" :key="index">
        <view class="flex">
          <view class="flex items-center">
            <u-checkbox-group @change="selected(item)">
              <label>
                <u-checkbox class="selected" color="#555555" :checked="item.flag === true" /><text></text>
              </label>
            </u-checkbox-group>
            <image :src="item.goodsImage" style="width: 150rpx;height: 140rpx;"></image>
          </view>
          <view class="flex flex-col justify-around ml-8">
            <text style="font-size: 25rpx;">尺码：{{ item.size }}</text>
            <text class="text-base text-red-500">￥{{ item.price }}/件</text>
          </view>
        </view>
        <view class="detail-right">
          <text class="subtract" @click="reduce(item)">-</text>
          <text class="num">{{ item.num }}</text>
          <text @click="add(item)" class="add">+</text>
        </view>
      </view>
      <view class="end bg-white w-full fixed flex items-center">
        <view class="end-left flex justify-between">
          <u-checkbox-group @change="selectgoods()">
            <label>
              <u-checkbox :checked="allchecked" />全选
            </label>
          </u-checkbox-group>
          <view class="pt-2">
            总计：<text class="text-red-500 font-bold">￥ {{ totalPrice }}</text>
          </view>
        </view>
        <view class="end-right bg-red-500 text-center text-white">
          结算({{ totalNum }})
        </view>
      </view>
    </template>

  </view>
</template>

<script>
export default {
  data() {
    return {
      show: true,
      allchecked: true,
      goods: [{
        size: "课程-M",
        num: 1,
        flag: true,
        price: 149,
        goodsImage: "//seczone-teach-test-1312651338.cos.ap-guangzhou.myqcloud.com/39/2022-08-25/0463bca05fc8bdbf13301780a7513c57.png",
      },
      {
        size: "课程-xs",
        num: 1,
        flag: true,
        price: 219,
        goodsImage: "//seczone-teach-test-1312651338.cos.ap-guangzhou.myqcloud.com/39/2022-08-25/56325e7019aec925e074eefe6d626316.png",
      },
      {
        size: "课程-L",
        num: 1,
        flag: true,
        price: 240,
        goodsImage: "//seczone-teach-test-1312651338.cos.ap-guangzhou.myqcloud.com/39/2022-08-25/116653e6bbf0f338de1189bd1804d0d9.png",
      },
      {
        size: "课程-XXL",
        num: 1,
        flag: true,
        price: 410,
        goodsImage: "//seczone-teach-test-1312651338.cos.ap-guangzhou.myqcloud.com/39/2022-08-25/116653e6bbf0f338de1189bd1804d0d9.png",
      },
      ],
    }
  },
  methods: {
    goshopping() {
      uni.navigateTo({
        url: '../contact/contact'
      })
    },
    change(e) {
      console.log(e)
    },
    selected(item) {
      item.flag = !item.flag
      if (!item.flag) {
        this.allchecked = false

      } else {
        let every = this.goods.every((value, index) => {
          return value.flag === true
        })
        if (every) {
          this.allchecked = true
        } else {
          this.allchecked = false
        }
      }
    },
    selectgoods() {
      this.allchecked = !this.allchecked
      if (this.allchecked) {
        this.goods.map(item => {
          item.flag = true
        })
      } else {
        this.goods.map(item => {
          item.flag = false
        })
      }
    },
    reduce(item) {
      let num = item.num
      if (num > 1) {
        num -= 1
      } else if (num = 1) {
        uni.showToast({
          title: "该宝贝不能减少了哟~"
        })
      }


      item.num = num
    },
    add(item) {
      let num = item.num
      item.num = num + 1
    }
  },
  computed: {
    totalNum() {
      let totalNum = 0;
      this.goods.map(item => {
        item.flag ? totalNum += item.num : totalNum += 0
      })
      return totalNum
    },

    totalPrice() {
      let totalPrice = 0;
      this.goods.map(item => {
        item.flag ? totalPrice += item.num * item.price : totalPrice += 0
      })
      return totalPrice
    }
  }
}

</script>

<style lang="scss">
.goods {
  line-height: 80rpx;
  background-color: #FFFFFF;

  &-detail {
    padding: 30rpx 15rpx 30rpx 30rpx;
    background-color: #fff;
    border-bottom: 5rpx solid #F1F1F1;

    .detail-left {
      display: flex;

      .goods-left {
        display: flex;
        align-items: center;
      }
    }

    .detail-right {
      text {
        width: 50rpx;
        line-height: 50rpx;
        text-align: center;
        display: inline-block;
        background-color: #F7F7F7;
        margin-right: 10rpx;
      }

      .add {
        color: #FA4305;
        border-radius: 10rpx 30rpx 30rpx 10rpx;
        margin-right: 20rpx;
      }

      .subtract {
        border-radius: 30rpx 10rpx 10rpx 30rpx;
      }
    }
  }
}

.empty {

  top: 220rpx;
  text-align: center;

  &-text {
    color: #808080;
    margin-bottom: 50rpx;
  }

  &-button {
    width: 300rpx;
    height: 90rpx;
    color: orange;
    border: 1rpx solid orange;
    text-align: center;
    line-height: 90rpx;
    border-radius: 48rpx;
  }
}

.end {
  width: 100%;
  height: 90rpx;
  bottom: 100rpx;
  left: 0;

  &-left {
    width: 70%;
    padding: 0 30rpx;

    .end-flex {
      display: flex;
      align-items: center;
    }
  }

  &-right {
    width: 30%;
    line-height: 90rpx;
  }
}
</style>
