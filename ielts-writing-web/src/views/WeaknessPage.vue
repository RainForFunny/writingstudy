<template>
  <div class="weakness-page">
    <header class="header">
      <el-button text @click="$router.push('/')">← 返回写作</el-button>
      <h1>🎯 个人弱点追踪</h1>
    </header>

    <div class="weakness-content" v-if="loading">
      <el-skeleton :rows="6" animated />
    </div>

    <div class="weakness-content" v-else-if="!hasData">
      <el-empty description="暂无弱点记录，完成写作并获取AI点评后将自动生成">
        <template #image>
          <el-icon :size="80" color="#c0c4cc">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2Z" fill="currentColor" opacity="0.3"/>
            </svg>
          </el-icon>
        </template>
        <el-button type="primary" @click="$router.push('/')">去完成一篇写作</el-button>
      </el-empty>
    </div>

    <div class="weakness-content" v-else>
      <!-- 综述面板 -->
      <el-card class="overview-card" shadow="never">
        <div class="overview-inner">
          <div class="overview-stats">
            <div class="stat-item">
              <span class="stat-value">{{ overview.totalEssays }}</span>
              <span class="stat-label">已归档文章</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ overview.totalWeakness }}</span>
              <span class="stat-label">累计弱点数</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ overview.totalErrors }}</span>
              <span class="stat-label">总错误次数</span>
            </div>
            <div class="stat-item">
              <span class="stat-value score">{{ overview.avgScore }}</span>
              <span class="stat-label">平均分</span>
            </div>
          </div>
          <p class="overview-summary" v-if="overview.summary">
            {{ overview.summary }}
          </p>
          <p class="overview-empty-summary" v-else>
            暂无综述，完成更多写作后将为您生成综合评估。
          </p>
        </div>
      </el-card>

      <!-- 图表区域 -->
      <div class="charts-grid">
        <RadarChart :scores="overview.avgScores" />
        <PieChart :error-distribution="overview.errorDistribution" />
      </div>

      <div class="charts-full" v-if="overview.scoreHistory && overview.scoreHistory.length > 0">
        <TrendChart :score-history="overview.scoreHistory" />
      </div>

      <!-- 弱点清单 -->
      <el-card class="weakness-list-card" shadow="never">
        <template #header>
          <div class="card-header">
            <h3>📋 个人弱点清单</h3>
            <el-tag type="info" effect="plain">
              共 {{ overview.detailList.length }} 条
            </el-tag>
          </div>
        </template>

        <el-table :data="overview.detailList" style="width: 100%" empty-text="暂无弱点记录">
          <el-table-column label="弱点类型" min-width="180">
            <template #default="{ row }">
              <div class="weakness-type">
                <el-tag
                  :type="errorTypeTag(row.errorCategory)"
                  size="small"
                  effect="dark"
                >
                  {{ categoryLabel(row.errorCategory) }}
                </el-tag>
                <span class="weakness-name">{{ errorTypeLabel(row.errorType) }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="累计次数" width="120" align="center">
            <template #default="{ row }">
              <el-tag :type="frequencyLevel(row.frequency)" effect="plain" round>
                {{ row.frequency }} 次
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="最近出现" width="160" align="center">
            <template #default="{ row }">
              <span class="weakness-date">{{ formatDate(row.lastOccurredAt) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="改进建议" min-width="200">
            <template #default="{ row }">
              <span class="weakness-tip">{{ row.suggestion || '暂无建议' }}</span>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getWeaknessOverview } from '@/api/weakness'
import RadarChart from '@/components/weakness/RadarChart.vue'
import PieChart from '@/components/weakness/PieChart.vue'
import TrendChart from '@/components/weakness/TrendChart.vue'

const loading = ref(true)
const overview = ref({
  totalEssays: 0,
  totalWeakness: 0,
  totalErrors: 0,
  avgScore: '0.0',
  avgScores: { ta: 0, cc: 0, lr: 0, gra: 0 },
  errorDistribution: [],
  scoreHistory: [],
  detailList: [],
  summary: ''
})

const hasData = computed(() => overview.value.totalEssays > 0)

function transformOverview(raw) {
  // raw comes from backend as: { code: 200, data: { summary, scoreTrend, errorTypeStats, dimensionScores } }
  const data = raw || {}
  const s = data.summary || {}
  const dim = data.dimensionScores || {}
  const avgScore = [dim.avgTa || 0, dim.avgCc || 0, dim.avgLr || 0, dim.avgGra || 0]
  const overallAvg = avgScore.length > 0 ? (avgScore.reduce((a, b) => a + b, 0) / avgScore.length).toFixed(1) : '0.0'

  // Aggregate errorTypeStats into category-based errorDistribution for pie chart
  const categoryMap = {}
  const detailList = []
  ;(data.errorTypeStats || []).forEach((e) => {
    // Build detail list for the table
    detailList.push({
      errorType: e.errorType,
      errorCategory: e.errorCategory,
      frequency: e.frequency,
      label: e.label,
      lastOccurredAt: e.lastOccurredAt || null,
      suggestion: e.suggestion || ''
    })

    // Aggregate by category for pie chart
    const cat = e.errorCategory || 'OTHER'
    if (!categoryMap[cat]) {
      categoryMap[cat] = 0
    }
    categoryMap[cat] += e.frequency
  })

  const errorDistribution = Object.entries(categoryMap).map(([category, frequency]) => ({
    category,
    frequency
  }))

  // Transform scoreTrend -> add label for timeline x-axis
  const scoreHistory = (data.scoreTrend || []).map((item, idx) => {
    const dateStr = item.date ? item.date.substring(0, 10) : ''
    return {
      ta: item.ta || 0,
      cc: item.cc || 0,
      lr: item.lr || 0,
      gra: item.gra || 0,
      label: dateStr || `第${idx + 1}次`
    }
  })

  return {
    totalEssays: s.totalEssays || 0,
    totalWeakness: detailList.length,
    totalErrors: s.totalErrors || 0,
    avgScore: overallAvg,
    avgScores: {
      ta: dim.avgTa || 0,
      cc: dim.avgCc || 0,
      lr: dim.avgLr || 0,
      gra: dim.avgGra || 0
    },
    errorDistribution,
    scoreHistory,
    detailList,
    summary: s.overallAdvice || ''
  }
}

const ERROR_CATEGORY_LABELS = {
  GRAMMAR: '语法',
  VOCABULARY: '词汇',
  LOGIC: '逻辑'
}

const ERROR_TYPE_LABELS = {
  ARTICLE_MISUSE: '冠词误用',
  S_V_AGREEMENT: '主谓一致',
  TENSE_ERROR: '时态错误',
  PREPOSITION_ERROR: '介词误用',
  WORD_CHOICE: '用词不当',
  COLLOCATION_ERROR: '搭配不当',
  TRANSITION_WEAK: '衔接不足',
  ARGUMENT_WEAK: '论证薄弱',
  RUN_ON_SENTENCE: '流水句',
  FRAGMENT: '句子碎片'
}

function errorTypeTag(category) {
  switch (category) {
    case 'GRAMMAR': return 'danger'
    case 'VOCABULARY': return 'warning'
    case 'LOGIC': return 'primary'
    default: return 'info'
  }
}

function categoryLabel(category) {
  return ERROR_CATEGORY_LABELS[category] || category
}

function errorTypeLabel(type) {
  return ERROR_TYPE_LABELS[type] || type
}

function frequencyLevel(freq) {
  if (freq >= 5) return 'danger'
  if (freq >= 3) return 'warning'
  return 'info'
}

function formatDate(dateStr) {
  if (!dateStr) return '-'
  try {
    const d = new Date(dateStr)
    return `${d.getMonth() + 1}/${d.getDate()}`
  } catch {
    return dateStr
  }
}

async function fetchOverview() {
  loading.value = true
  try {
    const res = await getWeaknessOverview()
    if (res.code === 200 && res.data) {
      overview.value = transformOverview(res.data)
    }
  } catch (err) {
    console.error('获取弱点概览失败:', err)
  } finally {
    loading.value = false
  }
}

onMounted(fetchOverview)
</script>

<style scoped>
.weakness-page {
  min-height: 100vh;
  background: #f5f7fa;
}

.header {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 24px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.header h1 {
  margin: 0;
  font-size: 20px;
}

.weakness-content {
  max-width: 1000px;
  margin: 24px auto;
  padding: 0 20px;
}

/* 综述卡片 */
.overview-card {
  margin-bottom: 24px;
  border-radius: 12px;
}

.overview-inner {
  padding: 8px 0;
}

.overview-stats {
  display: flex;
  justify-content: space-around;
  gap: 16px;
  margin-bottom: 16px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
}

.stat-value.score {
  color: #409eff;
}

.stat-label {
  font-size: 13px;
  color: #909399;
}

.overview-summary {
  margin: 0;
  padding: 12px 16px;
  background: #f0f9eb;
  border-radius: 8px;
  color: #67c23a;
  font-size: 14px;
  line-height: 1.6;
}

.overview-empty-summary {
  margin: 0;
  padding: 12px 16px;
  background: #f5f7fa;
  border-radius: 8px;
  color: #909399;
  font-size: 14px;
}

/* 图表网格 */
.charts-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
  margin-bottom: 24px;
}

.charts-full {
  margin-bottom: 24px;
}

/* 弱点清单 */
.weakness-list-card {
  border-radius: 12px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.card-header h3 {
  margin: 0;
  font-size: 16px;
  color: #303133;
}

.weakness-type {
  display: flex;
  align-items: center;
  gap: 8px;
}

.weakness-name {
  font-size: 14px;
  color: #303133;
}

.weakness-date {
  font-size: 13px;
  color: #909399;
}

.weakness-tip {
  font-size: 13px;
  color: #606266;
  line-height: 1.5;
}
</style>