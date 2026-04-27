package com.ielts.writing.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ielts.writing.model.dto.request.EssaySaveRequest;
import com.ielts.writing.model.dto.response.EssayArchiveResponse;
import com.ielts.writing.model.entity.Essay;
import com.ielts.writing.model.entity.EssayReview;
import com.ielts.writing.service.EssayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/essays")
@RequiredArgsConstructor
public class EssayController {

    private final EssayService essayService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createEssay(Authentication authentication,
                                                            @RequestBody EssaySaveRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        Essay essay = essayService.saveEssay(userId, request);

        Map<String, Object> data = new HashMap<>();
        data.put("id", essay.getId());
        data.put("status", essay.getStatus());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "保存成功");
        result.put("data", data);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateEssay(Authentication authentication,
                                                            @PathVariable Long id,
                                                            @RequestBody EssaySaveRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        Essay essay = essayService.updateEssay(id, userId, request);

        Map<String, Object> data = new HashMap<>();
        data.put("id", essay.getId());
        data.put("wordCount", essay.getWordCount());
        data.put("status", essay.getStatus());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "更新成功");
        result.put("data", data);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getEssay(Authentication authentication,
                                                         @PathVariable Long id) {
        Long userId = (Long) authentication.getPrincipal();
        Essay essay = essayService.getEssayDetail(id, userId);

        Map<String, Object> data = new HashMap<>();
        data.put("id", essay.getId());
        data.put("topicContent", essay.getTopicContent());
        data.put("topicId", essay.getTopicId());
        data.put("content", essay.getContent());
        data.put("wordCount", essay.getWordCount());
        data.put("status", essay.getStatus());
        data.put("writingMode", essay.getWritingMode());
        data.put("createdAt", essay.getCreatedAt());
        data.put("updatedAt", essay.getUpdatedAt());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", data);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/archive")
    public ResponseEntity<Map<String, Object>> getArchiveList(Authentication authentication,
                                                               @RequestParam(defaultValue = "1") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        Long userId = (Long) authentication.getPrincipal();
        IPage<EssayArchiveResponse> essayPage = essayService.getArchiveList(userId, page, size);

        Map<String, Object> data = new HashMap<>();
        data.put("records", essayPage.getRecords());
        data.put("total", essayPage.getTotal());
        data.put("page", essayPage.getCurrent());
        data.put("size", essayPage.getSize());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", data);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteEssay(Authentication authentication,
                                                            @PathVariable Long id) {
        Long userId = (Long) authentication.getPrincipal();
        essayService.deleteEssay(id, userId);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "删除成功");
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/review")
    public ResponseEntity<Map<String, Object>> getEssayReview(Authentication authentication,
                                                               @PathVariable Long id) {
        Long userId = (Long) authentication.getPrincipal();
        essayService.getEssayDetail(id, userId); // verify ownership
        EssayReview review = essayService.getEssayReview(id);

        Map<String, Object> data = new HashMap<>();
        if (review != null) {
            data.put("scoreTa", review.getScoreTa());
            data.put("scoreCc", review.getScoreCc());
            data.put("scoreLr", review.getScoreLr());
            data.put("ScoreGra", review.getScoreGra());
            data.put("overallComment", review.getOverallComment());
            data.put("annotations", review.getAnnotations());
            data.put("upgrade05", review.getUpgrade05());
            data.put("upgrade10", review.getUpgrade10());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", data);
        return ResponseEntity.ok(result);
    }
}