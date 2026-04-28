<template>
  <div class="assist-toolbar">
    <el-button-group>
      <el-button
        v-for="btn in assistButtons"
        :key="btn.type"
        :type="activeType === btn.type ? 'primary' : 'default'"
        :loading="loadingType === btn.type"
        :disabled="!!loadingType && loadingType !== btn.type"
        @click="handleAssist(btn.type)"
      >
        {{ btn.icon }} {{ btn.label }}
      </el-button>
    </el-button-group>

    <div class="toolbar-right">
      <el-button
        type="success"
        size="large"
        :loading="submitting"
        @click="handleSubmit"
        :disabled="!writingStore.content"
      >
        ✍️ 完成写作·AI点评
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useWritingStore } from '../../stores/writingStore'
import { useReviewStore } from '../../stores/reviewStore'
import { getAiAssist, submitAiReview } from '../../api/ai'

const writingStore = useWritingStore()
const reviewStore = useReviewStore()

const activeType = ref(null)
const loadingType = ref(null)
const submitting = ref(false)

const assistButtons = [
  { type: 'THINKING', label: '审题思路', icon: '🧠' },
  { type: 'TEMPLATE', label: '段落模板', icon: '📄' },
  { type: 'VOCABULARY', label: '词汇表达', icon: '📚' },
  { type: 'TRANSITION', label: '衔接过渡', icon: '🔗' },
  { type: 'CONTINUE', label: '续写提示', icon: '✨' }
]

async function handleAssist(type) {
  if (loadingType.value) return
  activeType.value = type
  loadingType.value = type

  if (type === 'CONTINUE') {
    try {
      const res = await getAiAssist({
        assistType: type,
        topicContent: writingStore.topicContent,
        currentContent: writingStore.content
      })
      if (res.code === 200 && res.data) {
        writingStore.setContinue(res.data.continueText || '')
      } else {
        ElMessage.error('获取续写提示失败')
      }
    } catch (e) {
      console.error('续写提示请求失败:', e)
      ElMessage.error('AI服务请求失败，请稍后重试')
    }
  } else {
    try {
      const res = await getAiAssist({
        assistType: type,
        topicContent: writingStore.topicContent,
        currentContent: writingStore.content
      })
      if (res.code === 200 && res.data) {
        writingStore.setAssist(type, res.data.assistContent)
      } else {
        ElMessage.error('获取辅助内容失败')
      }
    } catch (e) {
      console.error('AI辅助请求失败:', e)
      ElMessage.error('AI服务请求失败，请稍后重试')
    }
  }

  loadingType.value = null
}

async function handleSubmit() {
  if (!writingStore.content) return
  submitting.value = true
  reviewStore.loading = true

  try {
    const res = await submitAiReview({
      topicContent: writingStore.topicContent,
      content: writingStore.content
    })
    if (res.code === 200) {
      reviewStore.setReview(res.data, writingStore.content, writingStore.topicContent)
    } else {
      ElMessage.error('获取AI点评失败')
    }
  } catch (e) {
    console.error('AI点评请求失败:', e)
    ElMessage.error('AI服务请求失败，请稍后重试')
  } finally {
    submitting.value = false
    reviewStore.loading = false
  }
}
</script>

<style scoped>
.assist-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 0;
  border-top: 1px solid #ebeef5;
  margin-top: 12px;
  flex-wrap: wrap;
  gap: 8px;
}

.toolbar-right {
  flex-shrink: 0;
}
</style>