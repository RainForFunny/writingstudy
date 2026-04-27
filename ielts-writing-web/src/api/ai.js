import request from './index'

export function getAiAssist(data) {
  return request.post('/ai/assist', data)
}

export function getAiContinue(data) {
  return request.post('/ai/continue', data)
}

export function submitAiReview(data) {
  return request.post('/ai/review', data)
}

export function getRandomTopic() {
  return request.get('/ai/random-topic')
}
