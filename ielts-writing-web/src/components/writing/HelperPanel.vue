<template>
  <transition name="fade">
    <div v-if="writingStore.assistContent" class="helper-panel">
      <div class="helper-header">
        <span class="helper-label">{{ currentLabel }}</span>
        <el-button text size="small" @click="writingStore.setAssist(null, '')">
          ✕
        </el-button>
      </div>
      <div class="helper-content markdown-body" v-html="renderedContent" />
    </div>
  </transition>
</template>

<script setup>
import { computed } from 'vue'
import { marked } from 'marked'
import { useWritingStore } from '../../stores/writingStore'
import { ASSIST_TYPES } from '../../utils/constants'

const writingStore = useWritingStore()

const currentLabel = computed(() => {
  const type = writingStore.assistType
  const config = ASSIST_TYPES[type]
  return config ? `${config.icon} ${config.label}` : '辅助提示'
})

const renderedContent = computed(() => {
  if (!writingStore.assistContent) return ''
  return marked.parse(writingStore.assistContent)
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

.markdown-body h1,
.markdown-body h2,
.markdown-body h3,
.markdown-body h4 {
  margin: 8px 0 4px;
  font-size: 15px;
  color: #303133;
}

.markdown-body p {
  margin: 4px 0;
  line-height: 1.7;
  font-size: 14px;
  color: #303133;
}

.markdown-body ul,
.markdown-body ol {
  margin: 4px 0;
  padding-left: 20px;
}

.markdown-body li {
  margin: 2px 0;
  line-height: 1.7;
  font-size: 14px;
  color: #303133;
}

.markdown-body code {
  background: #e8e8e8;
  border-radius: 3px;
  padding: 1px 4px;
  font-size: 13px;
  color: #d63384;
}

.markdown-body pre {
  background: #f5f5f5;
  border-radius: 6px;
  padding: 12px;
  overflow-x: auto;
}

.markdown-body pre code {
  background: none;
  padding: 0;
  color: #303133;
}

.markdown-body strong {
  font-weight: 600;
  color: #409eff;
}

.markdown-body blockquote {
  margin: 8px 0;
  padding: 4px 12px;
  border-left: 3px solid #409eff;
  color: #606266;
  background: #ecf5ff;
  border-radius: 0 4px 4px 0;
}

.markdown-body hr {
  margin: 12px 0;
  border: none;
  border-top: 1px solid #dcdfe6;
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