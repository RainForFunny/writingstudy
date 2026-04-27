<template>
  <div class="assist-toolbar">
    <el-button-group>
      <el-button
        v-for="btn in assistButtons"
        :key="btn.type"
        :type="activeType === btn.type ? 'primary' : 'default'"
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
import { useWritingStore } from '../../stores/writingStore'
import { useReviewStore } from '../../stores/reviewStore'
import { getAiAssist, submitAiReview } from '../../api/ai'

const writingStore = useWritingStore()
const reviewStore = useReviewStore()

const activeType = ref(null)
const submitting = ref(false)

const assistButtons = [
  { type: 'THINKING', label: '审题思路', icon: '🧠' },
  { type: 'TEMPLATE', label: '段落模板', icon: '📄' },
  { type: 'VOCABULARY', label: '词汇表达', icon: '📚' },
  { type: 'TRANSITION', label: '衔接过渡', icon: '🔗' },
  { type: 'CONTINUE', label: '续写提示', icon: '✨' }
]

async function handleAssist(type) {
  activeType.value = type

  if (type === 'CONTINUE') {
    // 续写提示由 ContinueHint 组件处理
    // 这里触发续写请求
    try {
      const res = await getAiAssist({
        assistType: type,
        topicContent: writingStore.topicContent,
        currentContent: writingStore.content
      })
      if (res.code === 200) {
        writingStore.setContinue(res.data.continueText || '这是AI生成的续写内容示例。根据您的写作阶段，AI会自动提供合适的续写建议。')
      }
    } catch (e) {
      writingStore.setContinue('由于AI服务暂未接入，这是示例续写内容。您可以在后续版本中配置AI API密钥后使用完整功能。')
    }
  } else {
    // 其他辅助类型
    try {
      const res = await getAiAssist({
        assistType: type,
        topicContent: writingStore.topicContent,
        currentContent: writingStore.content
      })
      if (res.code === 200) {
        writingStore.setAssist(type, res.data.assistContent)
      }
    } catch (e) {
      const demoAssists = {
        THINKING: '审题思路示例：\n1. 题目要求讨论双方观点并给出自己的意见\n2. 关键词：governments, railways, roads\n3. 论证角度：经济效益、环境影响、社会便利性',
        TEMPLATE: '段落模板示例：\n【引言段模板】\nIt is widely debated whether... While some argue that..., others believe that... This essay will discuss both perspectives before presenting my own view.',
        VOCABULARY: '词汇表达示例：\n1. infrastructure (n.) 基础设施 - 正式，适用于讨论城市建设\n2. allocate funds (v. phrase) 分配资金 - 正式，适用于政府预算话题\n3. sustainable development (n. phrase) 可持续发展 - 正式，适用于环境与发展话题',
        TRANSITION: '衔接过渡示例：\n1. On the one hand, proponents of... argue that...\n2. However, it is also important to consider...\n3. In conclusion, while both views have merit, I believe that...'
      }
      writingStore.setAssist(type, demoAssists[type] || 'AI辅助功能将在后续版本中接入。')
    }
  }
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
      reviewStore.setReview(res.data)
    }
  } catch (e) {
    // 模拟点评数据
    reviewStore.setReview({
      scores: {
        ta: 6.5,
        cc: 6.0,
        lr: 6.5,
        gra: 7.0
      },
      overallComment: '文章整体结构清晰，论点明确。在论证深度和语言多样性方面还有提升空间。建议加强段落内部论证的连贯性，适当使用更丰富的词汇和复杂句式。',
      annotations: [
        {
          type: 'GRAMMAR',
          severity: 'MEDIUM',
          original: '...there are many people thinks that...',
          suggestion: '...there are many people who think that...',
          explanation: '定语从句中缺少关系代词who/that'
        },
        {
          type: 'VOCABULARY',
          severity: 'LOW',
          original: 'good',
          suggestion: 'beneficial / advantageous',
          explanation: '"good"过于口语化，建议使用更正式的学术表达'
        }
      ],
      upgrade05: 'On the one hand, proponents of investing in railways argue that trains are more environmentally friendly compared to road transport...',
      upgrade10: 'It is an undeniable fact that transportation infrastructure plays a pivotal role in a country\'s economic development. When it comes to the allocation of government funds, there is a perennial debate between investing in railways or roads...'
    })
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