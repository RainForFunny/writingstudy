<template>
  <div class="writing-editor">
    <el-input
      v-model="content"
      type="textarea"
      :rows="15"
      placeholder="在此处开始写作..."
      resize="vertical"
    />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useWritingStore } from '../../stores/writingStore'

const writingStore = useWritingStore()

const content = computed({
  get: () => writingStore.content,
  set: (val) => writingStore.setContent(val)
})
</script>

<style scoped>
.writing-editor :deep(.el-textarea__inner) {
  min-height: 400px;
  font-size: 15px;
  line-height: 1.8;
  padding: 16px;
  font-family: 'Georgia', 'Times New Roman', serif;
}
</style>