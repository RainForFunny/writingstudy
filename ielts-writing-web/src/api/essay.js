import request from './index'

export function saveEssay(data) {
  return request.post('/essays', data)
}

export function updateEssay(id, data) {
  return request.put(`/essays/${id}`, data)
}

export function getEssay(id) {
  return request.get(`/essays/${id}`)
}

export function getArchiveList(params) {
  return request.get('/essays/archive', { params })
}

export function deleteEssay(id) {
  return request.delete(`/essays/${id}`)
}