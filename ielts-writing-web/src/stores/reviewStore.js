import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useReviewStore = defineStore('review', () => {
  const showReview = ref(false)
  const loading = ref(false)
  const scores = ref(null)
  const annotations = ref([])
  const upgrade05 = ref('')
  const upgrade10 = ref('')
  const overallComment = ref('')
  const essayContent = ref('')
  const topicContent = ref('')

  function setReview(data, essayContentText, topicText) {
    scores.value = data.scores
    annotations.value = data.annotations || []
    upgrade05.value = data.upgrade05 || ''
    upgrade10.value = data.upgrade10 || ''
    overallComment.value = data.overallComment || ''
    essayContent.value = essayContentText || ''
    topicContent.value = topicText || ''
    showReview.value = true
  }

  function clearReview() {
    showReview.value = false
    loading.value = false
    scores.value = null
    annotations.value = []
    upgrade05.value = ''
    upgrade10.value = ''
    overallComment.value = ''
    essayContent.value = ''
    topicContent.value = ''
  }

  return {
    showReview, loading, scores, annotations,
    upgrade05, upgrade10, overallComment,
    essayContent, topicContent,
    setReview, clearReview
  }
})
