<template>
  <transition name="fade">
    <div v-if="writingStore.assistContent" class="helper-panel">
      <div class="helper-header">
        <span class="helper-label">{{ currentLabel }}</span>
        <el-button text size="small" @click="writingStore.setAssist(null, '')">
          ✕
        </el-button>
      </div>
      <div class="helper-content">
        <pre>{{ writingStore.assistContent }}</pre>
      </div>
    </div>
  </transition>
</template>

<script setup>
import { computed } from 'vue'
import { useWritingStore } from '../../stores/writingStore'
import { ASSIST_TYPES } from '../../utils/constants'

const writingStore = useWritingStore()

const currentLabel = computed(() => {
  const type = writingStore.assistType
  const config = ASSIST_TYPES[type]
  return config ? `${config.icon} ${config.label}` : '辅助提示'
})
</script>

<style scoped>
.helper-panel {
  margin-top: 12px;
  border: 1px solid #d9ecff;
  border-radius: 8px;
  background: #f0f9ff;
  overflow: hidden;
}

.helper-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 16px;
  background: #e6f7ff;
  font-weight: 600;
  font-size: 14px;
  color: #409eff;
}

.helper-content {
  padding: 12px 16px;
}

.helper-content pre {
  margin: 0;
  white-space: pre-wrap;
  font-family: inherit;
  font-size: 14px;
  line-height: 1.7;
  color: #303133;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>