import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useWritingStore = defineStore('writing', () => {
  const essayId = ref(null)
  const topicContent = ref('')
  const content = ref('')
  const wordCount = computed(() => {
    if (!content.value) return 0
    return content.value.replace(/\s/g, '').length
  })
  const assistType = ref(null)      // 当前辅助类型
  const assistContent = ref('')     // 辅助内容
  const continueContent = ref('')   // 续写内容
  const showContinue = ref(false)

  function setTopic(text) {
    topicContent.value = text
  }

  function setContent(text) {
    content.value = text
  }

  function setAssist(type, data) {
    assistType.value = type
    assistContent.value = data
  }

  function setContinue(data) {
    continueContent.value = data
    showContinue.value = true
  }

  function acceptContinue() {
    if (continueContent.value) {
      content.value += '\n' + continueContent.value
    }
    showContinue.value = false
    continueContent.value = ''
  }

  function clearAll() {
    essayId.value = null
    topicContent.value = ''
    content.value = ''
    assistType.value = null
    assistContent.value = ''
    continueContent.value = ''
    showContinue.value = false
  }

  return {
    essayId, topicContent, content, wordCount,
    assistType, assistContent, continueContent, showContinue,
    setTopic, setContent, setAssist, setContinue,
    acceptContinue, clearAll
  }
})