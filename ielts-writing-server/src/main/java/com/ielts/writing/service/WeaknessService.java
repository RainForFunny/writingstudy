package com.ielts.writing.service;

import com.ielts.writing.model.dto.response.WeaknessOverviewResponse;
import com.ielts.writing.model.entity.EssayReview;

/**
 * 弱点追踪服务
 */
public interface WeaknessService {

    /**
     * 根据AI点评结果分析并更新弱点记录
     */
    void analyzeWeakness(Long userId, Long essayId, EssayReview review);

    /**
     * 获取用户的弱点追踪概览
     */
    WeaknessOverviewResponse getOverview(Long userId);
}