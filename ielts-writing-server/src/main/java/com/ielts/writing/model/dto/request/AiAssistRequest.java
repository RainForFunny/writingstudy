package com.ielts.writing.model.dto.request;

import com.ielts.writing.model.enums.AssistType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AiAssistRequest {

    private Long essayId;

    @NotNull(message = "辅助类型不能为空")
    private AssistType assistType;

    @NotBlank(message = "题目内容不能为空")
    private String topicContent;

    private String currentContent;
}