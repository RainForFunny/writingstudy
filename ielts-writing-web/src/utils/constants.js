// 话题分类
export const TOPIC_CATEGORIES = [
  { label: '教育', value: 'EDUCATION' },
  { label: '科技', value: 'TECHNOLOGY' },
  { label: '环境', value: 'ENVIRONMENT' },
  { label: '社会', value: 'SOCIETY' },
  { label: '政府', value: 'GOVERNMENT' },
  { label: '媒体', value: 'MEDIA' },
  { label: '文化', value: 'CULTURE' },
  { label: '健康', value: 'HEALTH' },
  { label: '犯罪', value: 'CRIME' },
  { label: '其他', value: 'OTHER' }
]

// 辅助类型
export const ASSIST_TYPES = {
  THINKING: { label: '审题思路', icon: '🧠', color: '#409eff' },
  TEMPLATE: { label: '段落模板', icon: '📄', color: '#67c23a' },
  VOCABULARY: { label: '词汇表达', icon: '📚', color: '#e6a23c' },
  TRANSITION: { label: '衔接过渡', icon: '🔗', color: '#909399' },
  CONTINUE: { label: '续写提示', icon: '✨', color: '#67c23a' }
}

// 文章状态
export const ESSAY_STATUS = {
  DRAFT: 'DRAFT',
  SUBMITTED: 'SUBMITTED',
  REVIEWED: 'REVIEWED'
}

// 写作模式
export const WRITING_MODES = {
  FREE: 'FREE',
  TIMED: 'TIMED',
  STEP_BY_STEP: 'STEP_BY_STEP'
}

// 错误类型枚举
export const ERROR_TYPES = {
  GRAMMAR: { label: '语法错误', color: '#f56c6c' },
  VOCABULARY: { label: '词汇不当', color: '#e6a23c' },
  LOGIC: { label: '逻辑/连贯', color: '#409eff' }
}

// 写作阶段检测关键词
export const WRITING_STAGES = {
  BLANK: 'BLANK',           // 完全空白
  INTRO: 'INTRO',           // 刚写开头
  BODY_PARTIAL: 'BODY_PARTIAL', // 写到一半
  NEAR_COMPLETE: 'NEAR_COMPLETE' // 快完成
}