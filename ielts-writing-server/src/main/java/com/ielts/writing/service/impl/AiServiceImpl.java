package com.ielts.writing.service.impl;

import com.ielts.writing.config.AiConfig;
import com.ielts.writing.model.dto.request.AiAssistRequest;
import com.ielts.writing.model.dto.request.ReviewRequest;
import com.ielts.writing.model.dto.response.AiAssistResponse;
import com.ielts.writing.model.dto.response.ReviewResponse;
import com.ielts.writing.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final AiConfig aiConfig;
    private final RestTemplate restTemplate;

    @Override
    public AiAssistResponse assist(AiAssistRequest request) {
        String prompt = buildAssistPrompt(request);
        log.info("AI prompt: {}", prompt);
        String reply = callDeepSeek(prompt);
        log.info("AI reply: {}", reply);
        return AiAssistResponse.builder()
                .assistText(reply)
                .stage(request.getAssistType().name())
                .build();
    }

    @Override
    public AiAssistResponse continueWriting(AiAssistRequest request) {
        String prompt;

        if (request.getCurrentContent() == null || request.getCurrentContent().isBlank()) {
            prompt = String.format("""
                    你是一名雅思写作教练。用户正在写一篇雅思大作文，题目如下：

                    【题目】
                    %s

                    请为这个题目写一个开头段（约50-80词），要求：
                    1. 符合题目表述
                    2. 表明文章立场或概述主要观点
                    3. 使用雅思高分句式和词汇
                    4. 直接返回开头段内容，不要额外解释
                    """, request.getTopicContent());
        } else {
            prompt = String.format("""
                    你是一名雅思写作教练。用户正在写一篇雅思大作文，题目如下：

                    【题目】
                    %s

                    【当前已写内容】
                    %s

                    请根据已写内容，给出后续写作的续写建议（2-3句话），要求：
                    1. 与当前内容自然衔接
                    2. 符合雅思议论文的论证逻辑
                    3. 直接返回续写内容，不要额外解释
                    """, request.getTopicContent(), request.getCurrentContent());
        }
        log.info("AI prompt: {}", prompt);
        String reply = callDeepSeek(prompt);
        log.info("AI reply: {}", reply);
        return AiAssistResponse.builder()
                .assistText(reply)
                .stage("CONTINUE")
                .build();
    }

    @Override
    public String generateRandomTopic() {
        String prompt = """
                你是一名雅思写作出题专家。请随机生成一个雅思大作文题目（Task 2），要求：
                1. 题目完整，包含双方观点或同意/不同意等题型
                2. 话题应涵盖教育、科技、环境、社会、文化等不同领域
                3. 直接返回题目文本，不要任何额外解释或格式标记
                """;
        return callDeepSeek(prompt);
    }

    @Override
    public ReviewResponse review(ReviewRequest request) {
        String prompt = String.format("""
                你是一名资深的雅思写作考官。请对以下雅思大作文进行批改评分，并严格按照下面的JSON格式返回结果，不要添加额外的文字：

                【题目】
                %s

                【文章】
                %s


                请按以下维度评分（满分9分）：
                - TA (Task Achievement): 任务完成度
                - CC (Coherence and Cohesion): 连贯与衔接
                - LR (Lexical Resource): 词汇资源
                - GRA (Grammatical Range and Accuracy): 语法多样性与准确性

                返回格式（严格JSON）：
                {
                  "scores": { "ta": 6.5, "cc": 6.0, "lr": 6.5, "gra": 7.0 },
                  "overallComment": "整体评价文本",
                  "annotations": [
                    {
                      "type": "GRAMMAR|VOCABULARY|LOGIC",
                      "severity": "HIGH|MEDIUM|LOW",
                      "position": "错误位置描述",
                      "original": "原文",
                      "suggestion": "修改建议",
                      "explanation": "解释说明"
                    }
                  ],
                  "upgrade05": "+0.5分的升级版本段落",
                  "upgrade10": "+1.0分的升级版本段落"
                }

                注意：
                1. annotations 数组中请列出文章中具体的错误或可改进之处
                2. upgrade05 和 upgrade10 分别是将分数提高0.5和1.0后的改写段落
                3. 严格按照JSON格式返回，不要包含```json等标记
                """, request.getTopicContent(), request.getContent());

        log.info("AI点评 prompt: {}", prompt);
        String reply = callDeepSeek(prompt);
        log.info("AI点评 reply: {}", reply);
        return parseReviewResponse(reply);
    }

    /**
     * 调用 DeepSeek API
     */
    private String callDeepSeek(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(aiConfig.getApiKey());

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", aiConfig.getModel());
        requestBody.put("messages", List.of(message));
        requestBody.put("max_tokens", aiConfig.getMaxTokens());
        requestBody.put("temperature", aiConfig.getTemperature());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    aiConfig.getApiUrl(), request, Map.class);

            if (response.getBody() != null && response.getBody().containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    Map<String, Object> msg = (Map<String, Object>) choice.get("message");
                    return (String) msg.get("content");
                }
            }
            log.warn("DeepSeek API returned unexpected response: {}", response.getBody());
            return "抱歉，AI服务暂时无法响应，请稍后重试。";
        } catch (Exception e) {
            log.error("调用DeepSeek API失败", e);
            return "AI服务请求失败：" + e.getMessage();
        }
    }

    /**
     * 构建写作辅助提示词
     */
    private String buildAssistPrompt(AiAssistRequest request) {
        String instruction = switch (request.getAssistType()) {
            case THINKING -> """
                    你是一名雅思写作教练。请针对以下题目，提供审题思路分析，包括：
                    1. 题目关键词解析
                    2. 辩论方向（正反方观点）
                    3. 推荐的结构框架
                    4. 写作注意事项
                    
                    重要要求：
                    - 直接输出具体内容，不要有任何开场白（如"好的"、"我来帮你"等）
                    - 不要有任何结束语（如"希望对你有帮助"、"加油"等）
                    - 不要复述题目原文
                    - 只返回核心的审题分析内容
                    """;
            case TEMPLATE -> """
                    你是一名雅思写作教练。请针对以下题目，提供一个完整的段落模板（开头段、主体段、结尾段），
                    要求：
                    1. 使用雅思高分句式
                    2. 标注可替换的词汇和短语
                    3. 每个段落给出2-3个不同写法
                    
                    重要要求：
                    - 直接输出模板内容，不要有任何开场白或结束语
                    - 不要添加"以下是模板"、"希望对你有帮助"等多余文字
                    - 只返回模板本身
                    """;
            case VOCABULARY -> """
                    你是一名雅思写作教练。请针对以下题目，提供相关的词汇表达，包括：
                    1. 主题相关的高频词汇
                    2. 同义替换词
                    3. 学术化表达
                    4. 搭配和短语
                    
                    重要要求：
                    - 直接列出词汇和表达，不要有任何开场白或结束语
                    - 不要添加解释性文字（如"以下是一些词汇"）
                    - 只返回词汇内容本身
                    """;
            case TRANSITION -> """
                    你是一名雅思写作教练。请提供雅思写作中常用的衔接过渡表达，包括：
                    1. 表示递进/转折/因果/举例/总结的连接词和短语
                    2. 每个分类给出5个以上表达
                    3. 附上例句说明用法
                    
                    重要要求：
                    - 直接列出衔接表达，不要有任何开场白或结束语
                    - 不要添加"以下是"、"希望能帮到你"等多余文字
                    - 只返回具体的衔接表达内容
                    """;
            default -> "请提供雅思写作相关的帮助和建议。";
        };


        String content = request.getCurrentContent();
        if (content != null && !content.isBlank()) {
            return instruction + "\n\n【题目】\n" + request.getTopicContent()
                    + "\n\n【用户已写内容】\n" + content;
        }
        return instruction + "\n\n【题目】\n" + request.getTopicContent();
    }

    /**
     * 解析点评返回的JSON
     */
    private ReviewResponse parseReviewResponse(String json) {
        try {
            // 尝试从返回文本中提取JSON
            String jsonStr = json;
            int start = json.indexOf('{');
            int end = json.lastIndexOf('}');
            if (start >= 0 && end > start) {
                jsonStr = json.substring(start, end + 1);
            }

            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> map = mapper.readValue(jsonStr, Map.class);

            // 解析scores
            Map<String, Object> scoresMap = (Map<String, Object>) map.get("scores");
            ReviewResponse.ScoreInfo scores = ReviewResponse.ScoreInfo.builder()
                    .ta(asDouble(scoresMap.get("ta")))
                    .cc(asDouble(scoresMap.get("cc")))
                    .lr(asDouble(scoresMap.get("lr")))
                    .gra(asDouble(scoresMap.get("gra")))
                    .build();

            // 解析annotations
            List<Map<String, Object>> annList = (List<Map<String, Object>>) map.get("annotations");
            List<ReviewResponse.AnnotationInfo> annotations = new ArrayList<>();
            if (annList != null) {
                for (Map<String, Object> ann : annList) {
                    annotations.add(ReviewResponse.AnnotationInfo.builder()
                            .type((String) ann.get("type"))
                            .severity((String) ann.get("severity"))
                            .position((String) ann.get("position"))
                            .original((String) ann.get("original"))
                            .suggestion((String) ann.get("suggestion"))
                            .explanation((String) ann.get("explanation"))
                            .build());
                }
            }

            return ReviewResponse.builder()
                    .scores(scores)
                    .overallComment((String) map.get("overallComment"))
                    .annotations(annotations)
                    .upgrade05((String) map.get("upgrade05"))
                    .upgrade10((String) map.get("upgrade10"))
                    .build();
        } catch (Exception e) {
            log.error("解析AI点评响应失败: {}", json, e);
            // 返回默认响应
            return ReviewResponse.builder()
                    .scores(ReviewResponse.ScoreInfo.builder().ta(0.0).cc(0.0).lr(0.0).gra(0.0).build())
                    .overallComment("AI点评解析失败，请稍后重试。原始返回：" + json)
                    .annotations(Collections.emptyList())
                    .build();
        }
    }

    private Double asDouble(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return 0.0;
    }
}