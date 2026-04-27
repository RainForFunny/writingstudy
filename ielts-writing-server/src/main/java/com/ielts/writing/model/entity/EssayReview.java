package com.ielts.writing.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("essay_review")
public class EssayReview {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long essayId;

    private Double scoreTa;

    private Double scoreCc;

    private Double scoreLr;

    private Double scoreGra;

    private String overallComment;

    private String annotations;

    private String upgrade05;

    private String upgrade10;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}