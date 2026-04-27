<template>
  <div class="topic-selector">
    <el-input
      v-model="topicContent"
      type="textarea"
      :rows="2"
      placeholder="请输入雅思作文题目..."
      @input="handleInput"
    />
    <div class="topic-actions">
      <el-button type="primary" size="small" :loading="loading" @click="handleRandom">
        🎲 AI随机出题
      </el-button>
      <el-button size="small" @click="handleClear">
        🗑️ 清空
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useWritingStore } from '../../stores/writingStore'
import { getRandomTopic } from '../../api/ai'

const writingStore = useWritingStore()
const topicContent = ref(writingStore.topicContent)
const loading = ref(false)

function handleInput() {
  writingStore.setTopic(topicContent.value)
}

async function handleRandom() {
  loading.value = true
  try {
    const res = await getRandomTopic()
    if (res.code === 200 && res.data) {
      const topic = res.data.topicContent
      topicContent.value = topic
      writingStore.setTopic(topic)
    }
  } catch (e) {
    console.error('AI随机出题失败:', e)
  } finally {
    loading.value = false
  }
}

function handleClear() {
  writingStore.clearAll()
  topicContent.value = ''
}
</script>

<style scoped>
.topic-selector {
  margin-bottom: 16px;
}

.topic-actions {
  display: flex;
  gap: 8px;
  margin-top: 8px;
}
</style>