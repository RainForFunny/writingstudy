package com.ielts.writing.controller;

import com.ielts.writing.model.dto.request.AiAssistRequest;
import com.ielts.writing.model.dto.request.ReviewRequest;
import com.ielts.writing.model.dto.response.AiAssistResponse;
import com.ielts.writing.model.dto.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    /**
     * 写作辅助（审题/模板/词汇/衔接）
     */
    @PostMapping("/assist")
    public ResponseEntity<Map<String, Object>> assist(@RequestBody AiAssistRequest request) {
        // Phase 1: return mock data, will integrate real AI in Phase 3
        Map<String, Object> data = new HashMap<>();
        data.put("assistType", request.getAssistType());
        data.put("content", "这里是" + request.getAssistType() + "辅助内容（模拟数据）");

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", data);
        return ResponseEntity.ok(result);
    }

    /**
     * 续写提示
     */
    @PostMapping("/continue")
    public ResponseEntity<Map<String, Object>> continueWriting(@RequestBody AiAssistRequest request) {
        Map<String, Object> data = new HashMap<>();
        data.put("continueText", "On the one hand, proponents of this view argue that...");
        data.put("stage", "BODY_EXPAND");

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", data);
        return ResponseEntity.ok(result);
    }

    /**
     * AI点评
     */
    @PostMapping("/review")
    public ResponseEntity<Map<String, Object>> review(@RequestBody ReviewRequest request) {
        Map<String, Object> scores = new HashMap<>();
        scores.put("ta", 6.5);
        scores.put("cc", 6.0);
        scores.put("lr", 6.5);
        scores.put("gra", 7.0);

        List<Map<String, Object>> annotations = new ArrayList<>();
        Map<String, Object> ann = new HashMap<>();
        ann.put("type", "GRAMMAR");
        ann.put("severity", "HIGH");
        ann.put("position", "第2段，第3句");
        ann.put("original", "There are many reason...");
        ann.put("suggestion", "There are many reasons...");
        ann.put("explanation", "主语是复数，谓语动词应用复数形式");
        annotations.add(ann);

        Map<String, Object> data = new HashMap<>();
        data.put("scores", scores);
        data.put("overallComment", "整体来看，文章结构清晰，论点充分。需要在语法准确性和词汇多样性方面进一步提升。");
        data.put("annotations", annotations);
        data.put("upgrade05", "This is the +0.5 upgraded version...");
        data.put("upgrade10", "This is the +1.0 upgraded version...");

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", data);
        return ResponseEntity.ok(result);
    }
}