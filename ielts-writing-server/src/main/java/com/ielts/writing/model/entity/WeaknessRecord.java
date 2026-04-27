package com.ielts.writing.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("weakness_record")
public class WeaknessRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long essayId;

    private String errorType;

    private String errorCategory;

    private Integer frequency;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}