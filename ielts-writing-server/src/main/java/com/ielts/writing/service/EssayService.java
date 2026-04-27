package com.ielts.writing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ielts.writing.model.dto.request.EssaySaveRequest;
import com.ielts.writing.model.dto.response.EssayArchiveResponse;
import com.ielts.writing.model.entity.Essay;
import com.ielts.writing.model.entity.EssayReview;

public interface EssayService {
    Essay saveEssay(Long userId, EssaySaveRequest request);
    Essay updateEssay(Long essayId, Long userId, EssaySaveRequest request);
    Essay getEssayDetail(Long essayId, Long userId);
    IPage<EssayArchiveResponse> getArchiveList(Long userId, int page, int size);
    void deleteEssay(Long essayId, Long userId);
    EssayReview getEssayReview(Long essayId);
}