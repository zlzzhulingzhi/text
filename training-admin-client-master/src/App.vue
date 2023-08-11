<template>
  <RouterAlive id="app"></RouterAlive>
</template>

<script>
export default {
  name: 'App',
  async created() {
    console.log(`%c App created`, 'color: #ed5702;')
  }
}
</script>