<template>
  <Results class="width-100p height-100p bg-background" type="Disabled" :alt="$route.query.msg" :imageSize="320">
    <el-button class="width-80" type="primary" icon="el-icon-back" size="small" @click="$router.back()">返回</el-button>
  </Results>
</template>