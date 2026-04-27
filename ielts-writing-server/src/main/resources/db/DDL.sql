-- ============================================
-- 雅思写作智能教练 - 数据库DDL
-- Version: 1.0
-- ============================================

CREATE DATABASE IF NOT EXISTS `ielts_writing` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

USE `ielts_writing`;

-- ============================================
-- 用户表
-- ============================================
CREATE TABLE `user` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `avatar_url` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
  `target_score` DECIMAL(2,1) DEFAULT NULL COMMENT '目标分数',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

-- ============================================
-- 文章表
-- ============================================
CREATE TABLE `essay` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '文章ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `topic_id` BIGINT DEFAULT NULL COMMENT '关联题库ID',
  `topic_content` TEXT NOT NULL COMMENT '题目内容（冗余存储）',
  `content` MEDIUMTEXT DEFAULT NULL COMMENT '用户写作内容',
  `word_count` INT DEFAULT 0 COMMENT '字数',
  `status` VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态：DRAFT草稿/SUBMITTED已提交/REVIEWED已点评',
  `writing_mode` VARCHAR(20) DEFAULT 'FREE' COMMENT '写作模式：FREE自由/TIMED限时/STEP_BY_STEP逐段',
  `time_limit` INT DEFAULT NULL COMMENT '限时写作的时间限制(分钟)',
  `started_at` DATETIME DEFAULT NULL COMMENT '开始写作时间',
  `submitted_at` DATETIME DEFAULT NULL COMMENT '提交时间',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_status` (`status`),
  CONSTRAINT `fk_essay_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章表';

-- ============================================
-- 点评表
-- ============================================
CREATE TABLE `essay_review` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '点评ID',
  `essay_id` BIGINT NOT NULL UNIQUE COMMENT '文章ID（一篇文章一条点评）',
  `score_ta` DECIMAL(2,1) DEFAULT NULL COMMENT 'Task Achievement 任务完成度',
  `score_cc` DECIMAL(2,1) DEFAULT NULL COMMENT 'Coherence and Cohesion 连贯与衔接',
  `score_lr` DECIMAL(2,1) DEFAULT NULL COMMENT 'Lexical Resource 词汇资源',
  `score_gra` DECIMAL(2,1) DEFAULT NULL COMMENT 'Grammatical Range and Accuracy 语法范围与准确性',
  `overall_comment` TEXT DEFAULT NULL COMMENT '总体评价',
  `annotations` JSON DEFAULT NULL COMMENT '批注详情（JSON格式）',
  `upgrade_05` MEDIUMTEXT DEFAULT NULL COMMENT '+0.5分润色版',
  `upgrade_10` MEDIUMTEXT DEFAULT NULL COMMENT '+1.0分润色版',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  CONSTRAINT `fk_review_essay` FOREIGN KEY (`essay_id`) REFERENCES `essay` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='点评表';

-- ============================================
-- 弱点记录表
-- ============================================
CREATE TABLE `weakness_record` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `essay_id` BIGINT DEFAULT NULL COMMENT '关联文章ID',
  `error_type` VARCHAR(50) NOT NULL COMMENT '错误类型：ARTICLE_MISUSE冠词误用/S_V_AGREEMENT主谓不一致/等',
  `error_category` VARCHAR(20) DEFAULT NULL COMMENT '错误大类：GRAMMAR语法/VOCABULARY词汇/LOGIC逻辑',
  `frequency` INT DEFAULT 1 COMMENT '累计出现次数',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  INDEX `idx_user_category` (`user_id`, `error_category`),
  CONSTRAINT `fk_weakness_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_weakness_essay` FOREIGN KEY (`essay_id`) REFERENCES `essay` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='弱点记录表';

-- ============================================
-- 题库表
-- ============================================
CREATE TABLE `topic` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '题目ID',
  `category` VARCHAR(50) DEFAULT NULL COMMENT '话题分类：EDUCATION教育/TECHNOLOGY科技/ENVIRONMENT环境/...',
  `content` TEXT NOT NULL COMMENT '题目原文',
  `task_type` VARCHAR(10) DEFAULT 'T2' COMMENT '任务类型：T1/T2',
  `difficulty` VARCHAR(10) DEFAULT 'MEDIUM' COMMENT '难度：EASY简单/MEDIUM中等/HARD困难',
  `is_active` TINYINT DEFAULT 1 COMMENT '是否启用：1启用/0禁用',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  INDEX `idx_category` (`category`),
  INDEX `idx_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='题库表';

-- ============================================
-- 初始题库数据（IELTS Task 2 示例题目）
-- ============================================
INSERT INTO `topic` (`category`, `content`, `task_type`, `difficulty`, `is_active`) VALUES
('EDUCATION', 'Some people think that universities should focus on providing academic knowledge and research, while others believe they should prioritize practical skills for employment. Discuss both views and give your own opinion.', 'T2', 'MEDIUM', 1),
('TECHNOLOGY', 'In the modern world, technology is becoming more prevalent in our daily lives. Some people argue that this development has made our lives more complicated and stressful. To what extent do you agree or disagree?', 'T2', 'MEDIUM', 1),
('ENVIRONMENT', 'Climate change is one of the biggest challenges facing the world today. Some people believe that individual actions can make a difference, while others think only governments and large corporations can address this issue. Discuss both views and give your opinion.', 'T2', 'HARD', 1),
('SOCIETY', 'In many countries, the gap between the rich and the poor is widening. What problems does this cause, and what measures can be taken to address this issue?', 'T2', 'MEDIUM', 1),
('HEALTH', 'Some people believe that the government should be responsible for ensuring that citizens maintain a healthy lifestyle, while others think it is a personal responsibility. Discuss both views and give your own opinion.', 'T2', 'MEDIUM', 1),
('TECHNOLOGY', 'The internet has transformed the way people communicate and access information. However, some people argue that it has led to a decline in face-to-face interaction. Do the advantages of the internet outweigh the disadvantages?', 'T2', 'EASY', 1),
('EDUCATION', 'Some people believe that children should be taught how to manage money at school. Others argue that this is the responsibility of parents. Discuss both views and give your opinion.', 'T2', 'EASY', 1),
('ENVIRONMENT', 'The increase in the production of consumer goods is causing damage to the natural environment. What are the causes of this, and what solutions can you suggest?', 'T2', 'MEDIUM', 1),
('SOCIETY', 'In some cultures, children are often told that they can achieve anything if they try hard enough. What are the advantages and disadvantages of this message?', 'T2', 'MEDIUM', 1),
('TECHNOLOGY', 'Some people think that artificial intelligence will replace most human jobs in the future. Others believe that AI will create new job opportunities. Discuss both views and give your own opinion.', 'T2', 'HARD', 1);