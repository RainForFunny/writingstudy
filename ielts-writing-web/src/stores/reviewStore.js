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

  function setReview(data) {
    scores.value = data.scores
    annotations.value = data.annotations || []
    upgrade05.value = data.upgrade05 || ''
    upgrade10.value = data.upgrade10 || ''
    overallComment.value = data.overallComment || ''
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
  }

  return {
    showReview, loading, scores, annotations,
    upgrade05, upgrade10, overallComment,
    setReview, clearReview
  }
})