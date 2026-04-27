package com.ielts.writing.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EssaySaveRequest {

    private Long id;

    private Long topicId;

    @NotBlank(message = "题目内容不能为空")
    private String topicContent;

    private String content;

    private String status;

    private String writingMode;

    private Integer timeLimit;
}
