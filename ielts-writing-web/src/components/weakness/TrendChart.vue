<template>
  <div class="chart-container">
    <h3 class="chart-title">📈 分数变化趋势</h3>
    <canvas ref="chartRef"></canvas>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { Chart, CategoryScale, LinearScale, PointElement, LineElement, Filler, Tooltip, Legend } from 'chart.js'

Chart.register(CategoryScale, LinearScale, PointElement, LineElement, Filler, Tooltip, Legend)

const props = defineProps({
  scoreHistory: {
    type: Array,
    default: () => []
  }
})

const chartRef = ref(null)
let chartInstance = null

const DIMENSION_COLORS = {
  ta: { bg: 'rgba(64, 158, 255, 0.1)', border: '#409eff', label: 'TA' },
  cc: { bg: 'rgba(103, 194, 58, 0.1)', border: '#67c23a', label: 'CC' },
  lr: { bg: 'rgba(230, 162, 60, 0.1)', border: '#e6a23c', label: 'LR' },
  gra: { bg: 'rgba(245, 108, 108, 0.1)', border: '#f56c6c', label: 'GRA' }
}

function renderChart() {
  if (chartInstance) {
    chartInstance.destroy()
  }
  if (!chartRef.value) return

  const history = props.scoreHistory || []

  const ctx = chartRef.value.getContext('2d')
  if (!ctx) return

  const datasets = Object.entries(DIMENSION_COLORS).map(([key, color]) => ({
    label: color.label,
    data: history.map((h) => h[key] || 0),
    borderColor: color.border,
    backgroundColor: color.bg,
    borderWidth: 2,
    pointRadius: 4,
    pointHoverRadius: 6,
    tension: 0.3,
    fill: false
  }))

  chartInstance = new Chart(ctx, {
    type: 'line',
    data: {
      labels: history.map((h) => h.label || ''),
      datasets
    },
    options: {
      responsive: true,
      interaction: {
        intersect: false,
        mode: 'index'
      },
      scales: {
        y: {
          min: 0,
          max: 9,
          ticks: {
            stepSize: 1,
            font: { size: 11 }
          }
        },
        x: {
          ticks: {
            font: { size: 11 },
            maxRotation: 45
          }
        }
      },
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
            label: (context) => `${context.dataset.label}: ${context.raw}`
          }
        }
      }
    }
  })
}

onMounted(renderChart)
watch(() => props.scoreHistory, renderChart, { deep: true })
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