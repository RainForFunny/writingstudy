import request from './index'

/**
 * 获取弱点追踪概览数据
 */
export function getWeaknessOverview() {
  return request.get('/api/weakness/overview')
}