<template>
  <div class="chart-container">
    <h3 class="chart-title">🧩 错误类型分布</h3>
    <canvas ref="chartRef"></canvas>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { Chart, ArcElement, Tooltip, Legend } from 'chart.js'

Chart.register(ArcElement, Tooltip, Legend)

const props = defineProps({
  errorDistribution: {
    type: Array,
    default: () => []
  }
})

const chartRef = ref(null)
let chartInstance = null

const CATEGORY_COLORS = {
  GRAMMAR: { bg: 'rgba(245, 108, 108, 0.7)', border: '#f56c6c' },
  VOCABULARY: { bg: 'rgba(230, 162, 60, 0.7)', border: '#e6a23c' },
  LOGIC: { bg: 'rgba(64, 158, 255, 0.7)', border: '#409eff' }
}

const CATEGORY_LABELS = {
  GRAMMAR: '语法错误',
  VOCABULARY: '词汇不当',
  LOGIC: '逻辑/连贯'
}

function renderChart() {
  if (chartInstance) {
    chartInstance.destroy()
  }
  if (!chartRef.value) return

  const data = props.errorDistribution && props.errorDistribution.length > 0
    ? props.errorDistribution
    : [
        { category: 'GRAMMAR', frequency: 0 },
        { category: 'VOCABULARY', frequency: 0 },
        { category: 'LOGIC', frequency: 0 }
      ]

  const ctx = chartRef.value.getContext('2d')
  if (!ctx) return

  chartInstance = new Chart(ctx, {
    type: 'pie',
    data: {
      labels: data.map((d) => CATEGORY_LABELS[d.category] || d.category),
      datasets: [{
        data: data.map((d) => d.frequency),
        backgroundColor: data.map((d) => CATEGORY_COLORS[d.category]?.bg || 'rgba(144, 147, 153, 0.7)'),
        borderColor: data.map((d) => CATEGORY_COLORS[d.category]?.border || '#909399'),
        borderWidth: 2
      }]
    },
    options: {
      responsive: true,
      plugins: {
        legend: {
          position: 'bottom',
          labels: {
            padding: 16,
            font: { size: 12 },
            usePointStyle: true
          }
        },
        tooltip: {
          callbacks: {
            label: (context) => {
              const total = context.dataset.data.reduce((a, b) => a + b, 0)
              const value = context.raw
              const pct = total > 0 ? ((value / total) * 100).toFixed(1) : 0
              return `${context.label}: ${value} 次 (${pct}%)`
            }
          }
        }
      }
    }
  })
}

onMounted(renderChart)
watch(() => props.errorDistribution, renderChart, { deep: true })
</script>

<style scoped>
.chart-container {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.chart-title {
  margin: 0 0 16px;
  font-size: 16px;
  color: #303133;
}
</style>