package com.ielts.writing.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EssayArchiveResponse {
    private Long id;
    private String topicContent;
    private Integer wordCount;
    private String status;
    private String writingMode;
    private LocalDateTime createdAt;
    private LocalDateTime submittedAt;
}