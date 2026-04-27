<template>
  <div class="continue-hint">
    <div class="hint-header">
      <span>✨ 续写提示</span>
    </div>
    <div class="hint-body">
      <p>{{ writingStore.continueContent }}</p>
    </div>
    <div class="hint-actions">
      <el-button type="success" size="small" @click="handleAccept">
        ✅ 采用此续写
      </el-button>
      <el-button size="small" @click="handleRefresh">
        🔄 换一个版本
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { useWritingStore } from '../../stores/writingStore'

const writingStore = useWritingStore()

function handleAccept() {
  writingStore.acceptContinue()
}

function handleRefresh() {
  // 触发重新生成续写
  // 实际项目中调用AI接口
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