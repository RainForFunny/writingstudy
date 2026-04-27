<template>
  <div id="app">
    <router-view />
    <ReviewPanel />
  </div>
</template>

<script setup>
import ReviewPanel from './components/review/ReviewPanel.vue'
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  background-color: #f5f7fa;
  color: #303133;
}

#app {
  min-height: 100vh;
}
</style>
