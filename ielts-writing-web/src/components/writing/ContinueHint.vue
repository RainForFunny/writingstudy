<template>
  <div class="continue-hint">
    <div class="hint-header">
      <span>✨ 续写提示</span>
    </div>
    <div class="hint-body">
      <p>{{ writingStore.continueContent }}</p>
    </div>
    <div class="hint-actions">
      <el-button type="success" size="small" :loading="refreshing" @click="handleAccept">
        ✅ 采用此续写
      </el-button>
      <el-button size="small" :loading="refreshing" @click="handleRefresh">
        🔄 换一个版本
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useWritingStore } from '../../stores/writingStore'
import { getAiAssist } from '../../api/ai'

const writingStore = useWritingStore()
const refreshing = ref(false)

function handleAccept() {
  writingStore.acceptContinue()
}

async function handleRefresh() {
  if (refreshing.value) return
  refreshing.value = true
  try {
    const res = await getAiAssist({
      assistType: 'CONTINUE',
      topicContent: writingStore.topicContent,
      currentContent: writingStore.content
    })
    if (res.code === 200 && res.data) {
      // 替换续写内容，不清除 showContinue 状态
      writingStore.continueContent = res.data.continueText || ''
    }
  } catch (e) {
    console.error('重新生成续写失败:', e)
  } finally {
    refreshing.value = false
  }
}
</script>

<style scoped>
.continue-hint {
  border: 2px solid #67c23a;
  border-radius: 8px;
  background: #f0f9eb;
  overflow: hidden;
  animation: pulse-border 2s infinite;
}

@keyframes pulse-border {
  0%, 100% {
    box-shadow: 0 0 0 0 rgba(103, 194, 58, 0.3);
  }
  50% {
    box-shadow: 0 0 0 6px rgba(103, 194, 58, 0);
  }
}

.hint-header {
  padding: 8px 16px;
  background: #e1f3d8;
  font-weight: 600;
  font-size: 14px;
  color: #67c23a;
}

.hint-body {
  padding: 12px 16px;
  font-size: 14px;
  line-height: 1.7;
  color: #303133;
}

.hint-actions {
  display: flex;
  gap: 8px;
  padding: 8px 16px 12px;
}
</style>