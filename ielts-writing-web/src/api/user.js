import request from './index'

export function login(data) {
  return request.post('/users/login', data)
}

export function register(data) {
  return request.post('/users/register', data)
}

export function getUserInfo() {
  return request.get('/users/me')
}