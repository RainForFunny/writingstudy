package com.ielts.writing.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
@TableName("essay")
public class Essay {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long topicId;

    private String topicContent;

    private String content;

    private Integer wordCount;

    private String status;

    private String writingMode;

    private Integer timeLimit;

    private LocalDateTime startedAt;

    private LocalDateTime submittedAt;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}