package com.ielts.writing.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 弱点追踪概览响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeaknessOverviewResponse {

    /** 综述信息 */
    private Summary summary;

    /** 四大维度分数趋势（用于折线图） */
    private List<ScoreTrendItem> scoreTrend;

    /** 错误类型分布（用于饼图/柱状图） */
    private List<ErrorTypeStat> errorTypeStats;

    /** 各维度得分平均值（用于雷达图） */
    private DimensionScores dimensionScores;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private int totalEssays;            // 总评测文章数
        private int totalErrors;            // 总错误数
        private String mostCommonType;      // 最常见错误类型
        private String mostCommonCategory;  // 最常见错误大类
        private String overallAdvice;       // 综合建议
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScoreTrendItem {
        private LocalDateTime date;
        private Double ta;
        private Double cc;
        private Double lr;
        private Double gra;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorTypeStat {
        private String errorType;
        private String errorCategory;
        private String label;       // 中文标签
        private int frequency;
        private LocalDateTime lastOccurredAt;
        private String suggestion;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DimensionScores {
        private Double avgTa;
        private Double avgCc;
        private Double avgLr;
        private Double avgGra;
    }
}