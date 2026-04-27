<template>
  <div class="archive-page">
    <header class="header">
      <el-button text @click="$router.push('/')">← 返回写作</el-button>
      <h1>📚 文章归档</h1>
    </header>

    <div class="archive-list" v-loading="loading">
      <el-empty v-if="!loading && essayList.length === 0" description="暂无归档文章" />
      
      <el-card v-for="essay in essayList" :key="essay.id" class="essay-card">
        <div class="essay-info">
          <div class="essay-topic">{{ essay.topicContent }}</div>
          <div class="essay-meta">
            <span>字数：{{ essay.wordCount }}</span>
            <span>状态：{{ statusLabel(essay.status) }}</span>
            <span>{{ formatDate(essay.createdAt) }}</span>
          </div>
        </div>
        <div class="essay-actions">
          <el-button type="primary" @click="$router.push(`/archive/${essay.id}`)">
            查看详情
          </el-button>
          <el-button type="danger" @click="handleDelete(essay.id)">
            删除
          </el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getArchiveList, deleteEssay } from '../api/essay'

const loading = ref(false)
const essayList = ref([])

onMounted(async () => {
  await fetchList()
})

async function fetchList() {
  loading.value = true
  try {
    const res = await getArchiveList({ page: 1, pageSize: 20 })
    if (res.code === 200) {
      essayList.value = res.data.records || []
    }
  } catch (e) {
    console.error('获取归档列表失败', e)
  } finally {
    loading.value = false
  }
}

function statusLabel(status) {
  const map = { DRAFT: '草稿', SUBMITTED: '已提交', REVIEWED: '已点评' }
  return map[status] || status
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleDateString('zh-CN')
}

async function handleDelete(id) {
  try {
    await deleteEssay(id)
    essayList.value = essayList.value.filter(e => e.id !== id)
  } catch (e) {
    console.error('删除失败', e)
  }
}
</script>

<style scoped>
.archive-page {
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

.archive-list {
  max-width: 900px;
  margin: 20px auto;
  padding: 0 20px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.essay-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.essay-topic {
  font-size: 14px;
  color: #303133;
  margin-bottom: 8px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.essay-meta {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #909399;
}

.essay-actions {
  display: flex;
  gap: 8px;
  flex-shrink: 0;
}
</style>