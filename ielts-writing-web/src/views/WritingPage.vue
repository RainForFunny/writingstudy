<template>
  <div class="writing-page">
    <!-- 顶部导航 -->
    <header class="header">
      <div class="header-left">
        <h1 class="logo">雅思写作智能教练</h1>
      </div>
      <div class="header-right">
        <el-button text @click="$router.push('/archive')">文章归档</el-button>
        <el-button text @click="$router.push('/weakness')">弱点追踪</el-button>
        <template v-if="isLoggedIn">
          <el-dropdown @command="handleLogout">
            <span class="user-name">{{ username }}</span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <el-button v-else text @click="$router.push('/')">登录</el-button>
      </div>
    </header>

    <div class="main-container">
      <!-- 左侧：写作区 -->
      <div class="writing-area">
        <!-- 主题选择 -->
        <TopicSelector />

        <!-- 写作编辑器 -->
        <WritingEditor />

        <!-- 字数统计 -->
        <div class="word-count" v-if="writingStore.content">
          字数：<strong>{{ writingStore.wordCount }}</strong>
        </div>

        <!-- 辅助工具条 -->
        <AssistToolbar />

        <!-- 辅助提示面板 -->
        <HelperPanel />
      </div>

      <!-- 右侧：AI提示区 -->
      <div class="side-panel">
        <!-- 续写提示 -->
        <ContinueHint v-if="writingStore.showContinue" />
      </div>
    </div>

    <!-- AI点评面板 -->
    <ReviewPanel />
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useWritingStore } from '../stores/writingStore'
import { useUserStore } from '../stores/userStore'
import TopicSelector from '../components/writing/TopicSelector.vue'
import WritingEditor from '../components/writing/WritingEditor.vue'
import AssistToolbar from '../components/writing/AssistToolbar.vue'
import HelperPanel from '../components/writing/HelperPanel.vue'
import ContinueHint from '../components/writing/ContinueHint.vue'
import ReviewPanel from '../components/review/ReviewPanel.vue'

const router = useRouter()
const writingStore = useWritingStore()
const userStore = useUserStore()
const isLoggedIn = computed(() => !!userStore.token)
const username = computed(() => userStore.userInfo?.username || '')

function handleLogout() {
  userStore.clearToken()
  router.push('/')
}

onMounted(() => {
  if (userStore.token && !userStore.userInfo) {
    userStore.fetchUserInfo()
  }
})
</script>

<style scoped>
.writing-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 24px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  z-index: 10;
}

.logo {
  font-size: 20px;
  font-weight: 600;
  color: #409eff;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-name {
  color: #409eff;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
}

.main-container {
  display: flex;
  flex: 1;
  padding: 20px;
  gap: 20px;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
}

.writing-area {
  flex: 1;
  min-width: 0;
}

.side-panel {
  width: 360px;
  flex-shrink: 0;
}

.word-count {
  text-align: right;
  padding: 8px 0;
  color: #909399;
  font-size: 14px;
}
</style>