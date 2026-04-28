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
        <h3 class="review-card-title">📊 AI 点评报告</h3>

        <ScoreOverview :scores="scoresData" />

        <div class="overall-comment">
          <h4 class="section-title">总体评价</h4>
          <p>{{ review.overallComment }}</p>
        </div>

        <AnnotationBlock :annotations="annotationList" />
        <UpgradeBlock 
          :upgrade05="review.upgrade05 || ''" 
          :upgrade10="review.upgrade10 || ''" 
        />

        <p class="score-tip">* AI评估仅供参考，不等同于真实雅思考官评分</p>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getEssay, getEssayReview } from '../api/essay'
import ScoreOverview from '../components/review/ScoreOverview.vue'
import AnnotationBlock from '../components/review/AnnotationBlock.vue'
import UpgradeBlock from '../components/review/UpgradeBlock.vue'

const route = useRoute()
const loading = ref(false)
const essay = ref(null)
const review = ref(null)

const scoresData = computed(() => {
  if (!review.value) return {}
  return {
    ta: review.value.scoreTa,
    cc: review.value.scoreCc,
    lr: review.value.scoreLr,
    gra: review.value.scoreGra
  }
})

const annotationList = computed(() => {
  if (!review.value || !review.value.annotations) return []
  try {
    return typeof review.value.annotations === 'string'
      ? JSON.parse(review.value.annotations)
      : review.value.annotations
  } catch {
    return []
  }
})

onMounted(async () => {
  await fetchDetail()
})

async function fetchDetail() {
  loading.value = true
  try {
    const essayRes = await getEssay(route.params.id)
    if (essayRes.code === 200) {
      essay.value = essayRes.data
    }

    const reviewRes = await getEssayReview(route.params.id)
    if (reviewRes.code === 200 && reviewRes.data) {
      review.value = reviewRes.data
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

.review-card-title {
  font-size: 18px;
  margin: 0 0 20px;
  color: #303133;
}

.overall-comment {
  margin-bottom: 24px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 12px;
  color: #303133;
}

.overall-comment p {
  font-size: 14px;
  line-height: 1.7;
  color: #606266;
}

.score-tip {
  font-size: 12px;
  color: #c0c4cc;
  margin-top: 16px;
}
</style>
