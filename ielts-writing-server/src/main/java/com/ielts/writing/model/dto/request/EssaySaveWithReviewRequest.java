package com.ielts.writing.model.dto.request;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class EssaySaveWithReviewRequest {
    private String topicContent;
    private String content;
    private String writingMode;

    // Review data
    private Map<String, Double> scores;
    private String overallComment;
    private List<Map<String, Object>> annotations;
    private String upgrade05;
    private String upgrade10;
}