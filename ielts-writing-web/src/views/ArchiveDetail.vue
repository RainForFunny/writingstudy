<template>
  <div class="archive-detail">
    <header class="header">
      <el-button text @click="$router.push('/archive')">← 返回归档</el-button>
      <el-button text @click="$router.push('/')">返回写作</el-button>
    </header>

    <div class="detail-content" v-loading="loading">
      <el-card v-if="essay" class="detail-card">
        <h2 class="essay-topic">{{ essay.topicContent }}</h2>
        <div class="essay-body">
          <pre>{{ essay.content }}</pre>
        </div>
        <div class="essay-meta">
          <span>字数：{{ essay.wordCount }}</span>
          <span>写作模式：{{ modeLabel(essay.writingMode) }}</span>
          <span>{{ formatDate(essay.createdAt) }}</span>
        </div>
      </el-card>

      <el-card v-if="review" class="review-card">
        <h3>📊 AI 点评</h3>
        <div class="review-section">
          <p class="overall">{{ review.overallComment }}</p>
          <p class="score-tip">* AI评估仅供参考</p>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getEssay } from '../api/essay'

const route = useRoute()
const loading = ref(false)
const essay = ref(null)
const review = ref(null)

onMounted(async () => {
  await fetchDetail()
})

async function fetchDetail() {
  loading.value = true
  try {
    const res = await getEssay(route.params.id)
    if (res.code === 200) {
      essay.value = res.data.essay
      review.value = res.data.review
    }
  } catch (e) {
    console.error('获取文章详情失败', e)
  } finally {
    loading.value = false
  }
}

function modeLabel(mode) {
  const map = { FREE: '自由写作', TIMED: '限时写作', STEP_BY_STEP: '逐段挑战' }
  return map[mode] || '自由写作'
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}
</script>

<style scoped>
.archive-detail {
  min-height: 100vh;
  background: #f5f7fa;
}

.header {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 16px 24px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.detail-content {
  max-width: 900px;
  margin: 20px auto;
  padding: 0 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.essay-topic {
  font-size: 16px;
  color: #303133;
  margin-bottom: 16px;
}

.essay-body pre {
  margin: 0;
  white-space: pre-wrap;
  font-family: 'Georgia', 'Times New Roman', serif;
  font-size: 15px;
  line-height: 1.8;
  color: #303133;
}

.essay-meta {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #909399;
  margin-top: 16px;
  padding-top: 12px;
  border-top: 1px solid #ebeef5;
}

.review-section {
  margin-top: 12px;
}

.overall {
  font-size: 14px;
  line-height: 1.7;
  color: #606266;
}

.score-tip {
  font-size: 12px;
  color: #c0c4cc;
  margin-top: 8px;
}
</style>