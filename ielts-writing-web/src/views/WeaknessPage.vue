<template>
  <div class="weakness-page">
    <header class="header">
      <el-button text @click="$router.push('/')">← 返回写作</el-button>
      <h1>🎯 个人弱点追踪</h1>
    </header>

    <div class="weakness-content">
      <el-empty description="暂无弱点记录，完成写作并获取AI点评后将自动生成" />
    </div>
  </div>
</template>

<style scoped>
.weakness-page {
  min-height: 100vh;
  background: #f5f7fa;
}

.header {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 24px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.header h1 {
  margin: 0;
  font-size: 20px;
}

.weakness-content {
  max-width: 900px;
  margin: 40px auto;
  padding: 0 20px;
}
</style>