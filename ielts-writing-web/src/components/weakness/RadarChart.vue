<template>
  <div class="chart-container">
    <h3 class="chart-title">📊 四项评分维度分析</h3>
    <canvas ref="chartRef"></canvas>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { Chart, RadialLinearScale, PointElement, LineElement, Filler, Tooltip, Legend } from 'chart.js'

Chart.register(RadialLinearScale, PointElement, LineElement, Filler, Tooltip, Legend)

const props = defineProps({
  scores: {
    type: Object,
    default: () => ({ ta: 0, cc: 0, lr: 0, gra: 0 })
  }
})

const chartRef = ref(null)
let chartInstance = null

function renderChart() {
  if (chartInstance) {
    chartInstance.destroy()
  }
  if (!chartRef.value) return

  const ctx = chartRef.value.getContext('2d')
  if (!ctx) return

  chartInstance = new Chart(ctx, {
    type: 'radar',
    data: {
      labels: ['TA（任务完成度）', 'CC（连贯与衔接）', 'LR（词汇资源）', 'GRA（语法范围与准确性）'],
      datasets: [{
        label: '得分',
        data: [
          props.scores.ta || 0,
          props.scores.cc || 0,
          props.scores.lr || 0,
          props.scores.gra || 0
        ],
        backgroundColor: 'rgba(64, 158, 255, 0.2)',
        borderColor: 'rgba(64, 158, 255, 0.8)',
        borderWidth: 2,
        pointBackgroundColor: 'rgba(64, 158, 255, 1)',
        pointBorderColor: '#fff',
        pointBorderWidth: 1,
        pointRadius: 5
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: true,
      scales: {
        r: {
          min: 0,
          max: 9,
          ticks: {
            stepSize: 1,
            font: { size: 11 }
          },
          pointLabels: {
            font: { size: 12 }
          }
        }
      },
      plugins: {
        legend: { display: false },
        tooltip: {
          callbacks: {
            label: (context) => `得分: ${context.raw}`
          }
        }
      }
    }
  })
}

onMounted(renderChart)
watch(() => props.scores, renderChart, { deep: true })
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