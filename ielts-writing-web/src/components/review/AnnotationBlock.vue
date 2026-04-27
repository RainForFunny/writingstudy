<template>
  <div class="annotation-block">
    <h3 class="section-title">错误批注</h3>
    <div class="annotation-list">
      <div v-for="(item, index) in annotations" :key="index" class="annotation-item">
        <div class="annotation-tag">
          <el-tag :type="tagType(item.type)" size="small">
            {{ typeLabel(item.type) }}
          </el-tag>
          <el-tag v-if="item.severity === 'HIGH'" type="danger" size="small" effect="dark">
            重要
          </el-tag>
        </div>
        <div class="annotation-original">
          <span class="label">原文：</span>{{ item.original }}
        </div>
        <div class="annotation-suggestion">
          <span class="label">建议：</span>{{ item.suggestion }}
        </div>
        <div v-if="item.explanation" class="annotation-explanation">
          <span class="label">说明：</span>{{ item.explanation }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  annotations: {
    type: Array,
    default: () => []
  }
})

function tagType(type) {
  const map = { GRAMMAR: 'danger', VOCABULARY: 'warning', LOGIC: 'primary' }
  return map[type] || 'info'
}

function typeLabel(type) {
  const map = { GRAMMAR: '语法错误', VOCABULARY: '词汇不当', LOGIC: '逻辑/连贯' }
  return map[type] || type
}
</script>

<style scoped>
.annotation-block {
  margin-bottom: 24px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
  color: #303133;
}

.annotation-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.annotation-item {
  padding: 12px 16px;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  background: #fff;
}

.annotation-tag {
  display: flex;
  gap: 6px;
  margin-bottom: 8px;
}

.annotation-original,
.annotation-suggestion,
.annotation-explanation {
  font-size: 14px;
  line-height: 1.6;
  margin-top: 4px;
}

.label {
  font-weight: 600;
  color: #909399;
}
</style>