package com.ielts.writing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ielts.writing.exception.BusinessException;
import com.ielts.writing.model.dto.request.EssaySaveRequest;
import com.ielts.writing.model.dto.request.EssaySaveWithReviewRequest;
import com.ielts.writing.model.dto.response.EssayArchiveResponse;
import com.ielts.writing.model.entity.Essay;
import com.ielts.writing.model.entity.EssayReview;
import com.ielts.writing.repository.EssayRepository;
import com.ielts.writing.repository.EssayReviewRepository;
import com.ielts.writing.service.EssayService;
import com.ielts.writing.service.WeaknessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EssayServiceImpl implements EssayService {

    private final EssayRepository essayRepository;
    private final EssayReviewRepository essayReviewRepository;
    private final WeaknessService weaknessService;

    @Override
    public Essay saveEssay(Long userId, EssaySaveRequest request) {
        Essay essay = Essay.builder()
                .userId(userId)
                .topicContent(request.getTopicContent())
                .topicId(request.getTopicId())
                .content(request.getContent())
                .wordCount(countWords(request.getContent()))
                .status("DRAFT")
                .writingMode(request.getWritingMode() != null ? request.getWritingMode() : "FREE")
                .startedAt(LocalDateTime.now())
                .build();

        essayRepository.insert(essay);
        return essay;
    }

    @Override
    public Essay updateEssay(Long essayId, Long userId, EssaySaveRequest request) {
        Essay essay = essayRepository.selectById(essayId);
        if (essay == null || !essay.getUserId().equals(userId)) {
            throw new BusinessException("文章不存在或无权限操作");
        }

        essay.setContent(request.getContent());
        essay.setWordCount(countWords(request.getContent()));
        if (request.getStatus() != null) {
            essay.setStatus(request.getStatus());
            if ("SUBMITTED".equals(request.getStatus())) {
                essay.setSubmittedAt(LocalDateTime.now());
            }
        }

        essayRepository.updateById(essay);
        return essay;
    }

    @Override
    public Essay getEssayDetail(Long essayId, Long userId) {
        Essay essay = essayRepository.selectById(essayId);
        if (essay == null || !essay.getUserId().equals(userId)) {
            throw new BusinessException("文章不存在或无权限查看");
        }
        return essay;
    }

    @Override
    public IPage<EssayArchiveResponse> getArchiveList(Long userId, int page, int size) {
        Page<Essay> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Essay> wrapper = new LambdaQueryWrapper<Essay>()
                .eq(Essay::getUserId, userId)
                .in(Essay::getStatus, "SUBMITTED", "REVIEWED")
                .orderByDesc(Essay::getUpdatedAt);

        IPage<Essay> essayPage = essayRepository.selectPage(pageParam, wrapper);

        return essayPage.convert(essay -> EssayArchiveResponse.builder()
                .id(essay.getId())
                .topicContent(essay.getTopicContent())
                .wordCount(essay.getWordCount())
                .status(essay.getStatus())
                .writingMode(essay.getWritingMode())
                .createdAt(essay.getCreatedAt())
                .submittedAt(essay.getSubmittedAt())
                .build());
    }

    @Override
    public void deleteEssay(Long essayId, Long userId) {
        Essay essay = essayRepository.selectById(essayId);
        if (essay == null || !essay.getUserId().equals(userId)) {
            throw new BusinessException("文章不存在或无权限删除");
        }
        essayRepository.deleteById(essayId);
    }

    @Override
    @Transactional
    public Essay saveEssayWithReview(Long userId, EssaySaveWithReviewRequest request) {
        // 1. Save essay
        Essay essay = Essay.builder()
                .userId(userId)
                .topicContent(request.getTopicContent())
                .content(request.getContent())
                .wordCount(countWords(request.getContent()))
                .status("REVIEWED")
                .writingMode(request.getWritingMode() != null ? request.getWritingMode() : "FREE")
                .startedAt(LocalDateTime.now())
                .submittedAt(LocalDateTime.now())
                .build();

        essayRepository.insert(essay);

        // 2. Save review
        Map<String, Double> scores = request.getScores();
        EssayReview review = new EssayReview();
        review.setEssayId(essay.getId());
        if (scores != null) {
            review.setScoreTa(scores.get("ta"));
            review.setScoreCc(scores.get("cc"));
            review.setScoreLr(scores.get("lr"));
            review.setScoreGra(scores.get("gra"));
        }
        review.setOverallComment(request.getOverallComment());

        // Serialize annotations & upgrades to JSON string
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            if (request.getAnnotations() != null) {
                review.setAnnotations(mapper.writeValueAsString(request.getAnnotations()));
            }
            review.setUpgrade05(request.getUpgrade05());
            review.setUpgrade10(request.getUpgrade10());
        } catch (Exception e) {
            throw new BusinessException("序列化点评数据失败");
        }

        essayReviewRepository.insert(review);

        // 3. Trigger weakness analysis
        weaknessService.analyzeWeakness(userId, essay.getId(), review);

        return essay;
    }

    @Override
    public EssayReview getEssayReview(Long essayId) {
        return essayReviewRepository.selectOne(
                new LambdaQueryWrapper<EssayReview>().eq(EssayReview::getEssayId, essayId));
    }

    private int countWords(String content) {
        if (content == null || content.isBlank()) {
            return 0;
        }
        return content.trim().split("\\s+").length;
    }
}