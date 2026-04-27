package com.ielts.writing.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReviewRequest {

    private Long essayId;

    @NotBlank(message = "题目内容不能为空")
    private String topicContent;

    @NotBlank(message = "文章内容不能为空")
    private String content;
}