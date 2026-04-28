<template>
  <transition name="slide-up">
    <div v-if="reviewStore.showReview" class="review-panel-overlay">
      <div class="review-panel">
        <div class="panel-header">
          <h2>📊 AI 点评报告</h2>
          <div class="header-actions">
            <el-button type="primary" :loading="saving" @click="handleSave">
              💾 保存文章
            </el-button>
            <el-button text @click="reviewStore.clearReview()">
              ✕ 关闭
            </el-button>
          </div>
        </div>
        <div class="panel-body" v-loading="reviewStore.loading">
          <ScoreOverview :scores="reviewStore.scores" />
          
          <div class="overall-comment">
            <h3 class="section-title">总体评价</h3>
            <p>{{ reviewStore.overallComment }}</p>
          </div>

          <AnnotationBlock :annotations="reviewStore.annotations" />
          <UpgradeBlock 
            :upgrade05="reviewStore.upgrade05" 
            :upgrade10="reviewStore.upgrade10" 
          />
        </div>
      </div>
    </div>
  </transition>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useReviewStore } from '../../stores/reviewStore'
import { saveEssayWithReview } from '../../api/essay'
import ScoreOverview from './ScoreOverview.vue'
import AnnotationBlock from './AnnotationBlock.vue'
import UpgradeBlock from './UpgradeBlock.vue'

const reviewStore = useReviewStore()
const saving = ref(false)

async function handleSave() {
  saving.value = true
  try {
    const res = await saveEssayWithReview({
      topicContent: reviewStore.topicContent,
      content: reviewStore.essayContent,
      scores: reviewStore.scores,
      overallComment: reviewStore.overallComment,
      annotations: reviewStore.annotations,
      upgrade05: reviewStore.upgrade05,
      upgrade10: reviewStore.upgrade10
    })
    if (res.code === 200) {
      ElMessage.success('文章及AI点评已保存')
      reviewStore.clearReview()
    } else {
      ElMessage.error('保存失败，请重试')
    }
  } catch (e) {
    console.error('保存文章失败:', e)
    ElMessage.error('保存失败，请稍后重试')
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.review-panel-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.4);
  z-index: 100;
  display: flex;
  align-items: flex-end;
  justify-content: center;
}

.review-panel {
  width: 100%;
  max-width: 900px;
  max-height: 85vh;
  background: #fff;
  border-radius: 16px 16px 0 0;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 24px;
  border-bottom: 1px solid #ebeef5;
}

.panel-header h2 {
  margin: 0;
  font-size: 18px;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.panel-body {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
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

.slide-up-enter-active,
.slide-up-leave-active {
  transition: all 0.3s ease;
}

.slide-up-enter-from,
.slide-up-leave-to {
  opacity: 0;
  transform: translateY(100%);
}
</style>