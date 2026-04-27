package com.ielts.writing.service;

import com.ielts.writing.model.dto.request.AiAssistRequest;
import com.ielts.writing.model.dto.request.ReviewRequest;
import com.ielts.writing.model.dto.response.AiAssistResponse;
import com.ielts.writing.model.dto.response.ReviewResponse;

public interface AiService {

    /**
     * 写作辅助（审题思路/段落模板/词汇表达/衔接过渡）
     */
    AiAssistResponse assist(AiAssistRequest request);

    /**
     * 续写提示
     */
    AiAssistResponse continueWriting(AiAssistRequest request);

    /**
     * AI智能点评
     */
    ReviewResponse review(ReviewRequest request);

    /**
     * AI随机出题
     */
    String generateRandomTopic();
}
