package com.ielts.writing.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private ScoreInfo scores;
    private String overallComment;
    private List<AnnotationInfo> annotations;
    private String upgrade05;
    private String upgrade10;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScoreInfo {
        private Double ta;
        private Double cc;
        private Double lr;
        private Double gra;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnnotationInfo {
        private String type;
        private String severity;
        private String position;
        private String original;
        private String suggestion;
        private String explanation;
    }
}