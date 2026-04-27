<template>
  <div class="score-overview">
    <h3 class="section-title">四项评分概览</h3>
    <div class="score-grid">
      <div v-for="item in scoreItems" :key="item.key" class="score-item">
        <div class="score-label">{{ item.label }}</div>
        <div class="score-value">≈{{ scores[item.key] }}</div>
      </div>
    </div>
    <p class="score-tip">* AI评估仅供参考，不等同于真实雅思考官评分</p>
  </div>
</template>

<script setup>
defineProps({
  scores: {
    type: Object,
    required: true
  }
})

const scoreItems = [
  { key: 'ta', label: 'TA 任务完成度' },
  { key: 'cc', label: 'CC 连贯与衔接' },
  { key: 'lr', label: 'LR 词汇资源' },
  { key: 'gra', label: 'GRA 语法范围与准确性' }
]
</script>

<style scoped>
.score-overview {
  margin-bottom: 24px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
  color: #303133;
}

.score-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
}

.score-item {
  text-align: center;
  padding: 16px 8px;
  border-radius: 8px;
  background: #f5f7fa;
}

.score-label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 8px;
}

.score-value {
  font-size: 28px;
  font-weight: 700;
  color: #409eff;
}

.score-tip {
  margin-top: 8px;
  font-size: 12px;
  color: #c0c4cc;
}
</style>