
---

# 雅思写作智能教练 - 产品需求文档

## 一、产品概述

### 1.1 产品定位
面向雅思备考者的英文写作练习工具，核心价值是**在写作全流程中提供智能辅助**——从审题、段落到词汇、点评，帮助用户在练习中提升写作能力，而非仅仅提供一个AI批改工具。

### 1.2 核心差异化
- 不只批改，更注重**写作过程中的实时辅助**
- 点评采用**分层批注**，可视可交互
- 带有**记忆功能**，追踪弱点，越用越懂用户

---

## 二、功能模块

### 模块一：主题选择

| 功能点 | 描述 |
|--------|------|
| 手动输入主题 | 用户可自行输入或粘贴雅思作文题目 |
| AI随机出题 | 点击按钮，从内置题库（覆盖教育、科技、环境等常考话题）中随机抽取一道Task 2题目 |
| 题库扩展 | 后续可支持选择话题分类后出题，或接入历年真题库 |

### 模块二：写作区

| 功能点 | 描述 |
|--------|------|
| 文本编辑区 | 主写作区域，支持自由输入和编辑 |
| 字数统计 | 实时显示当前字数（后续可扩展） |

### 模块三：写作过程智能辅助

在写作卡壳时，用户可通过工具栏按钮获取不同维度的帮助。所有辅助内容展示在下方的提示框中。

#### 3.1 审题思路
- **触发**：用户点击「🧠 审题思路」按钮
- **内容**：AI提供中文思路框架，包括论证角度建议、观点方向等
- **场景**：读完题目后不知道从何下手

#### 3.2 段落模板
- **触发**：用户点击「📄 段落模板」按钮
- **内容**：AI提供英文句型模板（如引言段、主体段、结论段），用户可填空式完成
- **场景**：知道观点但不会用英文展开

#### 3.3 词汇表达
- **触发**：用户点击「📚 词汇表达」按钮
- **内容**：AI根据当前主题，给出2-3个雅思级别的英文表达，标注正式程度和适用场景
- **场景**：想表达某个意思但找不到合适的词

#### 3.4 衔接过渡
- **触发**：用户点击「🔗 衔接过渡」按钮
- **内容**：AI生成承上启下的过渡句选项
- **场景**：段落之间不知道如何自然衔接

#### 3.5 续写提示
- **触发**：用户点击「✨ 续写提示」按钮
- **智能识别写作阶段**：
  - 完全空白 → 提供完整开头段落
  - 刚写几句 → 提供正文论点展开
  - 写到一半 → 提供深入论证内容
  - 快完成时 → 提供结尾段落
- **交互**：
  - 提示在下方以绿色边框展示
  - 可点击「采用此续写」将内容自动追加到文章末尾
  - 可点击「换一个版本」重新生成
  - 按钮有脉冲动画，吸引注意力
  - 采用成功后按钮有短暂反馈动画

### 模块四：AI点评体系

写完后点击「完成写作·AI点评」，展示三层结构化反馈：

#### 4.1 第一层：四项评分概览
- 基于雅思官方四项评分标准：
  - **TA**（Task Achievement 任务完成度）
  - **CC**（Coherence and Cohesion 连贯与衔接）
  - **LR**（Lexical Resource 词汇资源）
  - **GRA**（Grammatical Range and Accuracy 语法范围与准确性）
- 以**预估分数区间**形式呈现（如 ≈6.5），并标注"AI评估仅供参考"

#### 4.2 第二层：错误批注
按问题类型分类标注：
- **语法错误**（红色标注）：指出具体错误，给出正确写法和规则说明
- **词汇不当**（黄色标注）：指出问题，给出更地道的替换词建议
- **逻辑/连贯**（蓝色标注）：指出论证不足或衔接问题，给出改进方向

#### 4.3 第三层：升级润色
- AI基于原文生成优化版本
- 提供两个档次：**+0.5分版**和**+1.0分版**
- 用户可对照学习，直观感受不同分数段的差异

### 模块五：归档与弱点追踪

| 功能点 | 描述 |
|--------|------|
| 文章归档 | 完成的文章及AI点评完整保存，再次打开可查看 |
| 弱点清单 | AI提取每篇文章中反复出现的错误类型，生成个人弱点记录 |
| 写作提醒 | 新写作时侧边栏提示"你上次常犯冠词错误，请注意"（后续版本） |
| 弱点图谱 | 积累到一定数量后，可视化展示个人写作弱点分布（后续版本） |

### 模块六：扩展写作模式

| 模式 | 描述 |
|------|------|
| 限时写作模式 | 模拟真实考场，Task 1限时20分钟，Task 2限时40分钟，带倒计时 |
| 逐段挑战模式 | 写一段 → AI点评该段 → 修改 → 进入下一段，适合初学者 |

---

## 三、交互设计要点

| 场景 | 设计要点 |
|------|----------|
| 辅助提示展示 | 统一下方弹出提示框，不同类型用不同颜色/图标区分 |
| 续写提示 | 绿色边框突出显示，附带「采用」和「换版本」操作按钮 |
| 按钮状态反馈 | 续写按钮有脉冲动画，采用后有短暂成功反馈 |
| 点评面板 | 初始隐藏，提交后展开，分区展示评分、批注、润色 |
| 清空操作 | 一键清空写作区和点评面板，重新开始 |

---

## 四、后续版本规划

| 优先级 | 功能 | 说明 |
|--------|------|------|
| P0 | 接入真实AI接口 | 替换当前模拟数据，实现真正的智能辅助和点评 |
| P1 | 用户账户与数据持久化 | 支持注册登录，作文云端存储，多设备同步 |
| P1 | 弱点追踪可视化 | 个人弱点图谱、历史错误统计 |
| P2 | 限时写作模式 | 倒计时功能，模拟考试环境 |
| P2 | 逐段挑战模式 | 分步写作+逐段点评 |
| P2 | 点评深度调节 | 初级/高级用户不同粒度的批改 |
| P3 | 雅思真题题库接入 | 收录更多真题，支持话题分类筛选 |

---

## 五、注意事项

1. AI评分的准确性：以预估分数区间呈现，明确标注AI评估仅供参考，避免用户产生误解
2. 点评颗粒度：点评深度可调节，避免一次指出过多问题打击初学者信心
3. 隐私与数据安全：用户写作内容涉及可能的考试素材，需确保数据安全存储
4. 产品边界说明：本产品为写作练习辅助工具，评分不等同于真实雅思考官评分

---
基于您的需求文档和 SpringBoot + Vue 技术栈，以下是完整的技术架构规划。

---

# 雅思写作智能教练 - 技术架构文档

## 一、架构总览

```
┌─────────────────────────────────────────────────────────┐
│                    前端 (Vue 3)                          │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐   │
│  │ 写作模块  │ │ 点评模块  │ │ 归档模块  │ │ 用户模块  │   │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘   │
│         │            │            │            │        │
│         └────────────┴────────────┴────────────┘        │
│                          │                              │
│                    Axios / HTTP                         │
└──────────────────────────┼──────────────────────────────┘
                           │
                    ┌──────┴──────┐
                    │  Nginx 反向  │
                    │  代理/静态   │
                    └──────┬──────┘
                           │
┌──────────────────────────┼──────────────────────────────┐
│                    后端 (SpringBoot)                     │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐   │
│  │Controller│ │Service   │ │Repository│ │AI Client │   │
│  │  REST    │ │  业务逻辑 │ │  数据访问 │ │ API调用  │   │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘   │
│                                              │          │
│                                         OpenAI / Claude  │
└──────────────────────────────────────────────────────────┘
                           │
                    ┌──────┴──────┐
                    │   MySQL     │
                    │  数据库     │
                    └─────────────┘
```

---

## 二、技术选型

| 层级 | 技术 | 版本建议 | 说明 |
|------|------|----------|------|
| 前端框架 | Vue 3 | 3.4+ | Composition API |
| 构建工具 | Vite | 5.x | 快速开发构建 |
| UI组件库 | Element Plus | 2.x | 成熟稳定的Vue3组件库 |
| 状态管理 | Pinia | 2.x | Vue3官方推荐 |
| 路由 | Vue Router | 4.x | 前端路由管理 |
| HTTP客户端 | Axios | 1.x | 请求拦截、错误处理 |
| 富文本编辑器 | Tiptap / Quill | - | 写作区编辑（可选） |
| 后端框架 | SpringBoot | 3.x | Java 17+ |
| ORM | MyBatis-Plus | 3.5+ | 简化CRUD |
| 数据库 | MySQL | 8.0+ | 关系型数据库 |
| 缓存 | Redis | 7.x | 可选，用于会话/热点数据 |
| 认证 | Spring Security + JWT | - | 用户认证与授权 |
| AI SDK | OpenAI官方SDK / 自定义HTTP | - | 对接GPT/Claude |
| API文档 | Knife4j (Swagger) | - | 接口文档自动生成 |

---

## 三、项目结构规划

### 3.1 前端项目结构

```
ielts-writing-web/
├── public/
├── src/
│   ├── api/                          # 接口请求层
│   │   ├── index.js                  # Axios实例，拦截器配置
│   │   ├── essay.js                  # 文章相关接口
│   │   ├── ai.js                     # AI辅助相关接口
│   │   └── user.js                   # 用户相关接口
│   │
│   ├── views/                        # 页面视图
│   │   ├── WritingPage.vue           # 写作主页面（核心）
│   │   ├── ArchivePage.vue           # 文章归档列表
│   │   ├── ArchiveDetail.vue         # 归档文章详情
│   │   ├── WeaknessPage.vue          # 个人弱点图谱
│   │   └── LoginPage.vue             # 登录/注册页
│   │
│   ├── components/                   # 可复用组件
│   │   ├── writing/                  # 写作模块组件
│   │   │   ├── TopicSelector.vue     # 主题选择器
│   │   │   ├── WritingEditor.vue     # 写作编辑器
│   │   │   ├── AssistToolbar.vue     # 辅助工具栏
│   │   │   ├── ContinueHint.vue      # 续写提示面板
│   │   │   └── HelperPanel.vue       # 通用辅助提示面板
│   │   │
│   │   ├── review/                   # 点评模块组件
│   │   │   ├── ReviewPanel.vue       # 点评面板（容器）
│   │   │   ├── ScoreOverview.vue     # 四项评分概览
│   │   │   ├── AnnotationBlock.vue   # 错误批注展示
│   │   │   └── UpgradeBlock.vue      # 升级润色展示
│   │   │
│   │   └── common/                   # 通用组件
│   │       └── LoadingSkeleton.vue   # 加载骨架屏
│   │
│   ├── stores/                       # Pinia 状态管理
│   │   ├── writingStore.js           # 写作状态（主题、内容、辅助信息）
│   │   ├── reviewStore.js            # 点评状态
│   │   └── userStore.js              # 用户状态
│   │
│   ├── router/                       # 路由配置
│   │   └── index.js
│   │
│   ├── utils/                        # 工具函数
│   │   ├── constants.js              # 常量定义（话题分类等）
│   │   └── format.js                 # 格式化工具
│   │
│   ├── App.vue
│   └── main.js
│
├── vite.config.js
└── package.json
```

### 3.2 后端项目结构

```
ielts-writing-server/
├── src/main/java/com/ielts/writing/
│   ├── IeltsWritingApplication.java          # 启动类
│   │
│   ├── config/                               # 配置类
│   │   ├── SecurityConfig.java               # Spring Security配置
│   │   ├── AiConfig.java                     # AI接口配置（API Key等）
│   │   ├── WebMvcConfig.java                 # CORS等Web配置
│   │   └── MyBatisPlusConfig.java            # MyBatis-Plus配置
│   │
│   ├── controller/                           # 控制器层
│   │   ├── EssayController.java              # 文章CRUD相关接口
│   │   ├── AiController.java                 # AI辅助/点评接口
│   │   ├── UserController.java               # 用户相关接口
│   │   └── TopicController.java              # 主题/题库接口
│   │
│   ├── service/                              # 业务逻辑层
│   │   ├── EssayService.java
│   │   ├── impl/EssayServiceImpl.java
│   │   ├── AiService.java                    # AI调用核心业务
│   │   ├── impl/AiServiceImpl.java
│   │   ├── ReviewService.java                # 点评解析业务
│   │   └── impl/ReviewServiceImpl.java
│   │
│   ├── repository/                           # 数据访问层
│   │   ├── EssayRepository.java              # 继承MyBatis-Plus BaseMapper
│   │   ├── UserRepository.java
│   │   └── TopicRepository.java
│   │
│   ├── model/                                # 数据模型
│   │   ├── entity/                           # 数据库实体
│   │   │   ├── User.java
│   │   │   ├── Essay.java
│   │   │   ├── EssayReview.java              # 点评记录
│   │   │   ├── WeaknessRecord.java           # 弱点记录
│   │   │   └── Topic.java
│   │   │
│   │   ├── dto/                              # 数据传输对象
│   │   │   ├── request/
│   │   │   │   ├── EssaySaveRequest.java
│   │   │   │   ├── AiAssistRequest.java      # AI辅助请求
│   │   │   │   └── ReviewRequest.java
│   │   │   │
│   │   │   └── response/
│   │   │       ├── AiAssistResponse.java
│   │   │       ├── ReviewResponse.java
│   │   │       └── EssayArchiveResponse.java
│   │   │
│   │   └── enums/
│   │       ├── AssistType.java               # 辅助类型枚举
│   │       ├── EssayStatus.java              # 文章状态
│   │       └── TopicCategory.java            # 话题分类
│   │
│   ├── ai/                                   # AI模块（核心）
│   │   ├── AiClient.java                     # AI接口封装
│   │   ├── prompt/                           # Prompt模板
│   │   │   ├── AssistPromptBuilder.java      # 辅助提示词构建
│   │   │   ├── ReviewPromptBuilder.java      # 点评提示词构建
│   │   │   └── ContinuePromptBuilder.java    # 续写提示词构建
│   │   │
│   │   └── parser/                           # AI返回结果解析
│   │       ├── ReviewParser.java             # 点评结果解析
│   │       └── AssistParser.java             # 辅助结果解析
│   │
│   └── exception/                            # 异常处理
│       ├── GlobalExceptionHandler.java
│       └── BusinessException.java
│
├── src/main/resources/
│   ├── application.yml                       # 主配置
│   ├── application-dev.yml                   # 开发环境
│   └── db/
│       └── migration/                        # 数据库迁移脚本
│           └── V1__init.sql
│
└── pom.xml
```

---

## 四、数据库设计

### 4.1 ER关系图（简化）

```
┌──────────┐       ┌──────────┐       ┌──────────────┐
│   User   │1─────*│  Essay   │1─────1│ EssayReview  │
└──────────┘       └──────────┘       └──────────────┘
                          │
                          │*
                    ┌─────┴──────┐
                    │ WeaknessRecord│
                    └────────────┘

┌──────────┐
│  Topic   │ (题库，独立)
└──────────┘
```

### 4.2 核心表结构

#### 用户表 (user)
```sql
CREATE TABLE `user` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(100),
  `avatar_url` VARCHAR(255),
  `target_score` DECIMAL(2,1),          -- 目标分数
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE
);
```

#### 文章表 (essay)
```sql
CREATE TABLE `essay` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `topic_id` BIGINT,                     -- 关联题库ID
  `topic_content` TEXT NOT NULL,         -- 题目内容（冗余存储，方便查看）
  `content` MEDIUMTEXT,                  -- 用户写作内容
  `word_count` INT DEFAULT 0,            -- 字数
  `status` VARCHAR(20) DEFAULT 'DRAFT',  -- DRAFT / SUBMITTED / REVIEWED
  `writing_mode` VARCHAR(20),            -- FREE / TIMED / STEP_BY_STEP
  `time_limit` INT,                      -- 限时写作的时间限制(分钟)
  `started_at` DATETIME,                 -- 开始写作时间
  `submitted_at` DATETIME,               -- 提交时间
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE,
  INDEX idx_user_id (user_id),
  INDEX idx_status (status)
);
```

#### 点评表 (essay_review)
```sql
CREATE TABLE `essay_review` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `essay_id` BIGINT NOT NULL UNIQUE,     -- 一篇文章一条点评
  `score_ta` DECIMAL(2,1),               -- 任务完成度
  `score_cc` DECIMAL(2,1),               -- 连贯与衔接
  `score_lr` DECIMAL(2,1),               -- 词汇资源
  `score_gra` DECIMAL(2,1),              -- 语法范围
  `overall_comment` TEXT,                -- 总体评价
  `annotations` JSON,                    -- 批注详情（JSON格式存储）
  `upgrade_05` MEDIUMTEXT,               -- +0.5分润色版
  `upgrade_10` MEDIUMTEXT,               -- +1.0分润色版
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

`annotations` JSON字段设计：
```json
[
  {
    "type": "GRAMMAR",           // GRAMMAR / VOCABULARY / LOGIC
    "severity": "HIGH",           // HIGH / MEDIUM / LOW
    "position": "段落2，第3句",    // 定位信息
    "original": "原文内容",
    "suggestion": "修改建议",
    "explanation": "规则说明"
  }
]
```

#### 弱点记录表 (weakness_record)
```sql
CREATE TABLE `weakness_record` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `essay_id` BIGINT,
  `error_type` VARCHAR(50) NOT NULL,     -- 错误类型：ARTICLE_MISUSE / S_V_AGREEMENT 等
  `error_category` VARCHAR(20),          -- 大类：GRAMMAR / VOCABULARY / LOGIC
  `frequency` INT DEFAULT 1,             -- 累计次数
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_user_category (user_id, error_category)
);
```

#### 题库表 (topic)
```sql
CREATE TABLE `topic` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `category` VARCHAR(50),                -- 话题分类：EDUCATION / TECHNOLOGY / ENVIRONMENT ...
  `content` TEXT NOT NULL,               -- 题目原文
  `task_type` VARCHAR(10) DEFAULT 'T2',  -- T1 / T2
  `difficulty` VARCHAR(10),              -- EASY / MEDIUM / HARD
  `is_active` TINYINT DEFAULT 1,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

---

## 五、核心API设计

### 5.1 写作相关接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/essays` | 创建/保存文章 |
| PUT | `/api/essays/{id}` | 更新文章内容（自动保存） |
| GET | `/api/essays/{id}` | 获取文章详情（含点评） |
| GET | `/api/essays/archive` | 获取归档列表（分页） |
| DELETE | `/api/essays/{id}` | 删除文章 |

### 5.2 AI辅助接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/ai/assist` | 请求写作辅助（审题/模板/词汇/衔接） |
| POST | `/api/ai/continue` | 请求续写提示 |
| POST | `/api/ai/review` | 提交文章进行AI点评 |

**请求/响应示例：**

```
POST /api/ai/continue
Request:
{
  "essayId": 123,           // 可选，已有文章则传
  "currentContent": "...",  // 当前写作内容
  "topicContent": "...",    // 作文题目
}

Response:
{
  "code": 200,
  "data": {
    "continueText": "On the one hand, supporters ...",
    "stage": "BODY_EXPAND"   // 识别的写作阶段
  }
}
```

```
POST /api/ai/review
Request:
{
  "essayId": 123,
  "content": "完整作文内容...",
  "topicContent": "..."
}

Response:
{
  "code": 200,
  "data": {
    "scores": {
      "ta": 6.5, "cc": 6.0, "lr": 6.5, "gra": 7.0
    },
    "overallComment": "整体评价...",
    "annotations": [
      {
        "type": "GRAMMAR",
        "severity": "HIGH",
        "original": "...",
        "suggestion": "...",
        "explanation": "..."
      }
    ],
    "upgrade05": "...",
    "upgrade10": "..."
  }
}
```

### 5.3 题库接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/topics/random` | 随机获取题目 |
| GET | `/api/topics/random?category=EDUCATION` | 按分类随机获取 |
| GET | `/api/topics/categories` | 获取话题分类列表 |

---

## 六、AI集成核心设计

### 6.1 Prompt构建策略

```java
// 以续写提示为例
public class ContinuePromptBuilder {
    
    public String build(String topicContent, String currentContent) {
        String stage = detectStage(currentContent); // 判断写作阶段
        
        return """
            你是一位雅思写作考官。用户正在练习雅思Task 2写作。
            
            题目：%s
            
            用户当前已写内容：
            %s
            
            用户当前处于【%s】阶段，请根据阶段提供续写提示：
            - 如果完全空白：提供完整的开头段（英文）
            - 如果刚写开头：提供第一个主体段的展开
            - 如果写到一半：提供第二个主体段的论证
            - 如果快完成：提供结尾段
            
            要求：
            1. 用英文给出续写内容
            2. 符合雅思写作规范
            3. 词汇和句式适合6.5-7.5分水平
            """.formatted(topicContent, currentContent, stage);
    }
}
```

### 6.2 返回结果结构化解析

AI返回的点评需要从自由文本中解析出结构化数据。策略：
- **方案A（推荐）**：要求AI以JSON格式返回，直接解析
- **方案B**：用特定分隔符标记不同区域，按规则切割解析
- **错误处理**：解析失败时，降级为纯文本展示

---

## 七、数据流示意

### 7.1 写作辅助流程

```
用户点击"续写提示"
        │
        ▼
前端发送当前内容和题目 → 后端AiController
        │
        ▼
AiService.buildPrompt() → 调用AI接口
        │
        ▼
解析AI返回 → 返回续写内容给前端
        │
        ▼
前端展示续写提示 ─→ 用户点击"采用" ─→ 追加入编辑区
```

### 7.2 点评流程

```
用户点击"完成写作·AI点评"
        │
        ▼
前端发送完整内容 → 后端AiController
        │
        ▼
AiService.reviewEssay()
  ├── 调用AI接口进行评分和批注
  ├── 解析返回结果
  ├── 保存Essay + EssayReview到数据库
  └── 提取错误类型 → 更新WeaknessRecord
        │
        ▼
返回完整点评数据 → 前端渲染点评面板
```

---

## 八、后续迭代注意事项

1. **AI调用异步化**：点评接口耗时可能较长（5-15秒），建议使用异步处理 + 轮询或WebSocket通知结果
2. **自动保存**：写作过程中定时自动保存（如每30秒），防止内容丢失
3. **限流**：AI接口调用需要做频率限制，防止滥用
4. **Prompt版本管理**：不同版本的Prompt模板建议独立管理，方便A/B测试和迭代
5. **缓存策略**：题库数据可缓存；AI返回的热门话题辅助内容可考虑缓存以降低成本

---

## 九、开发建议优先级

| 阶段 | 内容 | 预计耗时 |
|------|------|----------|
| Phase 1 | 项目搭建 + 数据库 + 用户认证 | 2-3天 |
| Phase 2 | 写作主页面（选题 + 编辑器 + 辅助按钮UI） | 3-4天 |
| Phase 3 | AI接口集成（辅助 + 续写 + 点评） | 3-5天 |
| Phase 4 | 点评面板渲染 + 归档列表 | 2-3天 |
| Phase 5 | 弱点追踪 + 样式优化 | 2-3天 |

---