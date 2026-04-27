package com.ielts.writing.controller;

import com.ielts.writing.model.dto.request.AiAssistRequest;
import com.ielts.writing.model.dto.request.ReviewRequest;
import com.ielts.writing.model.dto.response.AiAssistResponse;
import com.ielts.writing.model.dto.response.ReviewResponse;
import com.ielts.writing.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    /**
     * 写作辅助（审题思路/段落模板/词汇表达/衔接过渡/续写提示）
     * 前端 /ai/assist 同时用于辅助和续写，通过 assistType 区分：
     * - CONTINUE → 返回 continueText
     * - 其他 → 返回 assistContent
     */
    @PostMapping("/assist")
    public ResponseEntity<Map<String, Object>> assist(@RequestBody AiAssistRequest request) {
        Map<String, Object> data = new HashMap<>();
        data.put("assistType", request.getAssistType());

        if ("CONTINUE".equals(request.getAssistType().toString())) {
            AiAssistResponse response = aiService.continueWriting(request);
            data.put("continueText", response.getAssistText());
            data.put("stage", response.getStage());
        } else {
            AiAssistResponse response = aiService.assist(request);
            data.put("assistContent", response.getAssistText());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", data);
        return ResponseEntity.ok(result);
    }

    /**
     * AI随机出题
     */
    @GetMapping("/random-topic")
    public ResponseEntity<Map<String, Object>> randomTopic() {
        String topic = aiService.generateRandomTopic();

        Map<String, Object> data = new HashMap<>();
        data.put("topicContent", topic);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", data);
        return ResponseEntity.ok(result);
    }

    /**
     * AI智能点评
     */
    @PostMapping("/review")
    public ResponseEntity<Map<String, Object>> review(@RequestBody ReviewRequest request) {
        ReviewResponse reviewResponse = aiService.review(request);

        Map<String, Object> scores = new HashMap<>();
        scores.put("ta", reviewResponse.getScores().getTa());
        scores.put("cc", reviewResponse.getScores().getCc());
        scores.put("lr", reviewResponse.getScores().getLr());
        scores.put("gra", reviewResponse.getScores().getGra());

        List<Map<String, Object>> annotations = new ArrayList<>();
        if (reviewResponse.getAnnotations() != null) {
            for (ReviewResponse.AnnotationInfo ann : reviewResponse.getAnnotations()) {
                Map<String, Object> annMap = new HashMap<>();
                annMap.put("type", ann.getType());
                annMap.put("severity", ann.getSeverity());
                annMap.put("position", ann.getPosition());
                annMap.put("original", ann.getOriginal());
                annMap.put("suggestion", ann.getSuggestion());
                annMap.put("explanation", ann.getExplanation());
                annotations.add(annMap);
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("scores", scores);
        data.put("overallComment", reviewResponse.getOverallComment());
        data.put("annotations", annotations);
        data.put("upgrade05", reviewResponse.getUpgrade05());
        data.put("upgrade10", reviewResponse.getUpgrade10());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", data);
        return ResponseEntity.ok(result);
    }
}