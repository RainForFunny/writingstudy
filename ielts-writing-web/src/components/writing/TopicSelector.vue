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
      <el-button type="primary" size="small" @click="handleRandom">
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

const writingStore = useWritingStore()
const topicContent = ref(writingStore.topicContent)

function handleInput() {
  writingStore.setTopic(topicContent.value)
}

function handleRandom() {
  const demoTopics = [
    'Some people think that governments should spend more money on railways rather than roads. Others believe the opposite. Discuss both views and give your own opinion.',
    'In many countries, people are living in a "throw-away" society where they use things once and then discard them. What are the causes of this problem? What measures can be taken to solve it?',
    'Some people believe that unpaid community service should be a compulsory part of high school programs. To what extent do you agree or disagree?',
    'The development of technology has changed the way people interact with each other. Do the advantages outweigh the disadvantages?',
    'Many people believe that formal education should start at an early age. Others think children should play and learn through play. Discuss both views and give your opinion.'
  ]
  const random = demoTopics[Math.floor(Math.random() * demoTopics.length)]
  topicContent.value = random
  writingStore.setTopic(random)
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