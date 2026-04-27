<template>
  <div class="upgrade-block">
    <h3 class="section-title">升级润色</h3>
    <div class="upgrade-grid">
      <div class="upgrade-item">
        <div class="upgrade-badge">+0.5分版</div>
        <div class="upgrade-content">
          <pre>{{ upgrade05 }}</pre>
        </div>
      </div>
      <div class="upgrade-item">
        <div class="upgrade-badge upgrade-10">+1.0分版</div>
        <div class="upgrade-content">
          <pre>{{ upgrade10 }}</pre>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  upgrade05: { type: String, default: '' },
  upgrade10: { type: String, default: '' }
})
</script>

<style scoped>
.upgrade-block {
  margin-bottom: 24px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
  color: #303133;
}

.upgrade-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.upgrade-item {
  border: 1px solid #ebeef5;
  border-radius: 8px;
  overflow: hidden;
}

.upgrade-badge {
  padding: 6px 12px;
  background: #e6f7ff;
  color: #409eff;
  font-weight: 600;
  font-size: 13px;
}

.upgrade-badge.upgrade-10 {
  background: #f0f5ff;
  color: #722ed1;
}

.upgrade-content {
  padding: 12px;
}

.upgrade-content pre {
  margin: 0;
  white-space: pre-wrap;
  font-family: inherit;
  font-size: 14px;
  line-height: 1.7;
  color: #303133;
}
</style>