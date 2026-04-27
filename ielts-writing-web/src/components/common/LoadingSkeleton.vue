<template>
  <div class="loading-skeleton">
    <el-skeleton animated :count="rows" :throttle="500" />
  </div>
</template>

<script setup>
defineProps({
  rows: { type: Number, default: 3 }
})
</script>

<style scoped>
.loading-skeleton {
  padding: 16px;
}
</style>