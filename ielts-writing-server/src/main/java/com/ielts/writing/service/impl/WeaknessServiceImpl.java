package com.ielts.writing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ielts.writing.model.dto.response.WeaknessOverviewResponse;
import com.ielts.writing.model.entity.Essay;
import com.ielts.writing.model.entity.EssayReview;
import com.ielts.writing.model.entity.WeaknessRecord;
import com.ielts.writing.repository.EssayRepository;
import com.ielts.writing.repository.EssayReviewRepository;
import com.ielts.writing.repository.WeaknessRecordRepository;
import com.ielts.writing.service.WeaknessService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeaknessServiceImpl implements WeaknessService {

    private final WeaknessRecordRepository weaknessRecordRepository;
    private final EssayRepository essayRepository;
    private final EssayReviewRepository essayReviewRepository;

    public WeaknessServiceImpl(WeaknessRecordRepository weaknessRecordRepository,
                               EssayRepository essayRepository,
                               EssayReviewRepository essayReviewRepository) {
        this.weaknessRecordRepository = weaknessRecordRepository;
        this.essayRepository = essayRepository;
        this.essayReviewRepository = essayReviewRepository;
    }

    /**
     * 错误类型 → 中文标签映射
     */
    private static final Map<String, String> ERROR_TYPE_LABELS = new LinkedHashMap<>();
    static {
        ERROR_TYPE_LABELS.put("ARTICLE_MISUSE", "冠词误用");
        ERROR_TYPE_LABELS.put("S_V_AGREEMENT", "主谓不一致");
        ERROR_TYPE_LABELS.put("TENSE_ERROR", "时态错误");
        ERROR_TYPE_LABELS.put("PREPOSITION_ERROR", "介词误用");
        ERROR_TYPE_LABELS.put("PLURAL_ERROR", "单复数错误");
        ERROR_TYPE_LABELS.put("WORD_CHOICE", "词汇选择不当");
        ERROR_TYPE_LABELS.put("COLLOCATION", "搭配不当");
        ERROR_TYPE_LABELS.put("SPELLING", "拼写错误");
        ERROR_TYPE_LABELS.put("RUN_ON_SENTENCE", "断句/流水句");
        ERROR_TYPE_LABELS.put("FRAGMENT", "句子不完整");
        ERROR_TYPE_LABELS.put("PUNCTUATION", "标点错误");
        ERROR_TYPE_LABELS.put("LOGIC_GAP", "逻辑跳跃");
        ERROR_TYPE_LABELS.put("COHESION", "衔接不足");
        ERROR_TYPE_LABELS.put("REDUNDANCY", "冗余表达");
        ERROR_TYPE_LABELS.put("AWKWARD", "表达生硬");
        ERROR_TYPE_LABELS.put("OTHER", "其他错误");
    }

    /**
     * 错误类型 → 大类映射
     */
    private static final Map<String, String> ERROR_CATEGORIES = new HashMap<>();
    static {
        ERROR_CATEGORIES.put("ARTICLE_MISUSE", "GRAMMAR");
        ERROR_CATEGORIES.put("S_V_AGREEMENT", "GRAMMAR");
        ERROR_CATEGORIES.put("TENSE_ERROR", "GRAMMAR");
        ERROR_CATEGORIES.put("PREPOSITION_ERROR", "GRAMMAR");
        ERROR_CATEGORIES.put("PLURAL_ERROR", "GRAMMAR");
        ERROR_CATEGORIES.put("WORD_CHOICE", "VOCABULARY");
        ERROR_CATEGORIES.put("COLLOCATION", "VOCABULARY");
        ERROR_CATEGORIES.put("SPELLING", "VOCABULARY");
        ERROR_CATEGORIES.put("RUN_ON_SENTENCE", "GRAMMAR");
        ERROR_CATEGORIES.put("FRAGMENT", "GRAMMAR");
        ERROR_CATEGORIES.put("PUNCTUATION", "GRAMMAR");
        ERROR_CATEGORIES.put("LOGIC_GAP", "LOGIC");
        ERROR_CATEGORIES.put("COHESION", "LOGIC");
        ERROR_CATEGORIES.put("REDUNDANCY", "GRAMMAR");
        ERROR_CATEGORIES.put("AWKWARD", "VOCABULARY");
        ERROR_CATEGORIES.put("OTHER", "OTHER");
    }

    /**
     * 大类 → 中文标签
     */
    private static final Map<String, String> CATEGORY_LABELS = new HashMap<>();
    static {
        CATEGORY_LABELS.put("GRAMMAR", "语法错误");
        CATEGORY_LABELS.put("VOCABULARY", "词汇问题");
        CATEGORY_LABELS.put("LOGIC", "逻辑问题");
        CATEGORY_LABELS.put("OTHER", "其他");
    }

    @Override
    public void analyzeWeakness(Long userId, Long essayId, EssayReview review) {
        if (review == null || review.getAnnotations() == null) {
            return;
        }

        // 解析annotations JSON，提取错误类型
        // annotations 字段存储为JSON数组: [{"type":"GRAMMAR","severity":"HIGH","original":"...","suggestion":"..."}, ...]
        // 由于annotations是JSON字符串，我们从其中提取errorType模式
        String annotationsJson = null;
        if (review.getAnnotations() instanceof String) {
            annotationsJson = (String) review.getAnnotations();
        } else if (review.getAnnotations() != null) {
            annotationsJson = review.getAnnotations().toString();
        }

        if (annotationsJson == null || annotationsJson.isBlank()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        // 根据annotation中的type字段推断错误类型并入库
        List<String> detectedTypes = detectErrorTypesFromAnnotations(annotationsJson);

        // 对每种检测到的错误类型，创建或更新weakness_record
        for (String errorType : detectedTypes) {
            String category = ERROR_CATEGORIES.getOrDefault(errorType, "OTHER");

            LambdaQueryWrapper<WeaknessRecord> queryWrapper = new LambdaQueryWrapper<WeaknessRecord>()
                    .eq(WeaknessRecord::getUserId, userId)
                    .eq(WeaknessRecord::getErrorType, errorType);

            WeaknessRecord existing = weaknessRecordRepository.selectOne(queryWrapper);

            if (existing != null) {
                // 更新：累计频率，覆盖essay_id（最新出现）
                existing.setFrequency(existing.getFrequency() + 1);
                existing.setEssayId(essayId);
                existing.setLastOccurredAt(now);
                weaknessRecordRepository.updateById(existing);
            } else {
                // 新增
                WeaknessRecord record = new WeaknessRecord();
                record.setUserId(userId);
                record.setEssayId(essayId);
                record.setErrorType(errorType);
                record.setErrorCategory(category);
                record.setFrequency(1);
                record.setLastOccurredAt(now);
                record.setSuggestion(generateErrorSuggestion(errorType));
                weaknessRecordRepository.insert(record);
            }
        }
    }

    @Override
    public WeaknessOverviewResponse getOverview(Long userId) {
        // 1. 获取用户的全部已完成评测的文章
        List<Essay> reviewedEssays = essayRepository.selectList(
                new LambdaQueryWrapper<Essay>()
                        .eq(Essay::getUserId, userId)
                        .eq(Essay::getStatus, "REVIEWED")
                        .orderByAsc(Essay::getCreatedAt)
        );

        // 2. 获取所有弱点记录
        List<WeaknessRecord> allWeaknesses = weaknessRecordRepository.selectList(
                new LambdaQueryWrapper<WeaknessRecord>()
                        .eq(WeaknessRecord::getUserId, userId)
                        .orderByDesc(WeaknessRecord::getFrequency)
        );

        // 3. 构建分数趋势（折线图数据）
        List<WeaknessOverviewResponse.ScoreTrendItem> scoreTrend = new ArrayList<>();
        for (Essay essay : reviewedEssays) {
            EssayReview review = essayReviewRepository.selectOne(
                    new LambdaQueryWrapper<EssayReview>()
                            .eq(EssayReview::getEssayId, essay.getId())
            );
            if (review != null && review.getScoreTa() != null) {
                scoreTrend.add(WeaknessOverviewResponse.ScoreTrendItem.builder()
                        .date(essay.getCreatedAt())
                        .ta(review.getScoreTa().doubleValue())
                        .cc(review.getScoreCc().doubleValue())
                        .lr(review.getScoreLr().doubleValue())
                        .gra(review.getScoreGra().doubleValue())
                        .build());
            }
        }

        // 4. 构建错误类型统计（饼图/柱状图数据）
        Map<String, WeaknessOverviewResponse.ErrorTypeStat> errorStatMap = new LinkedHashMap<>();
        for (WeaknessRecord wr : allWeaknesses) {
            String key = wr.getErrorType();
            if (!errorStatMap.containsKey(key)) {
                errorStatMap.put(key, WeaknessOverviewResponse.ErrorTypeStat.builder()
                        .errorType(wr.getErrorType())
                        .errorCategory(wr.getErrorCategory())
                        .label(ERROR_TYPE_LABELS.getOrDefault(wr.getErrorType(), wr.getErrorType()))
                        .frequency(0)
                        .lastOccurredAt(wr.getLastOccurredAt())
                        .suggestion(wr.getSuggestion())
                        .build());
            }
            WeaknessOverviewResponse.ErrorTypeStat stat = errorStatMap.get(key);
            stat.setFrequency(stat.getFrequency() + wr.getFrequency());
            // 取最新lastOccurredAt
            if (wr.getLastOccurredAt() != null &&
                    (stat.getLastOccurredAt() == null || wr.getLastOccurredAt().isAfter(stat.getLastOccurredAt()))) {
                stat.setLastOccurredAt(wr.getLastOccurredAt());
            }
            // 如果有建议且尚未设置，取第一个
            if (stat.getSuggestion() == null && wr.getSuggestion() != null) {
                stat.setSuggestion(wr.getSuggestion());
            }
        }
        List<WeaknessOverviewResponse.ErrorTypeStat> errorTypeStats = new ArrayList<>(errorStatMap.values());
        // 按频率降序排列
        errorTypeStats.sort((a, b) -> Integer.compare(b.getFrequency(), a.getFrequency()));

        // 5. 计算各维度平均分（雷达图数据）
        double sumTa = 0, sumCc = 0, sumLr = 0, sumGra = 0;
        int count = 0;
        for (Essay essay : reviewedEssays) {
            EssayReview review = essayReviewRepository.selectOne(
                    new LambdaQueryWrapper<EssayReview>()
                            .eq(EssayReview::getEssayId, essay.getId())
            );
            if (review != null && review.getScoreTa() != null) {
                sumTa += review.getScoreTa().doubleValue();
                sumCc += review.getScoreCc().doubleValue();
                sumLr += review.getScoreLr().doubleValue();
                sumGra += review.getScoreGra().doubleValue();
                count++;
            }
        }
        WeaknessOverviewResponse.DimensionScores dimensionScores =
                WeaknessOverviewResponse.DimensionScores.builder()
                        .avgTa(count > 0 ? Math.round(sumTa / count * 10.0) / 10.0 : 0)
                        .avgCc(count > 0 ? Math.round(sumCc / count * 10.0) / 10.0 : 0)
                        .avgLr(count > 0 ? Math.round(sumLr / count * 10.0) / 10.0 : 0)
                        .avgGra(count > 0 ? Math.round(sumGra / count * 10.0) / 10.0 : 0)
                        .build();

        // 6. 构建综述
        int totalErrors = allWeaknesses.stream().mapToInt(WeaknessRecord::getFrequency).sum();
        String mostCommonErrorType = errorTypeStats.isEmpty() ? "" : errorTypeStats.get(0).getErrorType();
        String mostCommonErrorLabel = errorTypeStats.isEmpty() ? "" : errorTypeStats.get(0).getLabel();

        // 统计最常见错误大类
        Map<String, Integer> categoryFreq = new HashMap<>();
        for (WeaknessRecord wr : allWeaknesses) {
            String cat = wr.getErrorCategory();
            categoryFreq.merge(cat, wr.getFrequency(), Integer::sum);
        }
        String mostCommonCategory = categoryFreq.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> CATEGORY_LABELS.getOrDefault(e.getKey(), e.getKey()))
                .orElse("");

        // 生成综合建议
        String overallAdvice = generateAdvice(dimensionScores, mostCommonErrorLabel, mostCommonCategory);

        WeaknessOverviewResponse.Summary summary = WeaknessOverviewResponse.Summary.builder()
                .totalEssays(reviewedEssays.size())
                .totalErrors(totalErrors)
                .mostCommonType(mostCommonErrorLabel)
                .mostCommonCategory(mostCommonCategory)
                .overallAdvice(overallAdvice)
                .build();

        return WeaknessOverviewResponse.builder()
                .summary(summary)
                .scoreTrend(scoreTrend)
                .errorTypeStats(errorTypeStats)
                .dimensionScores(dimensionScores)
                .build();
    }

    /**
     * 从annotations JSON中检测错误类型
     */
    private List<String> detectErrorTypesFromAnnotations(String annotationsJson) {
        List<String> types = new ArrayList<>();
        String upperJson = annotationsJson.toUpperCase();

        // 通过关键词匹配来检测错误类型
        if (upperJson.contains("ARTICLE") || upperJson.contains("冠词") || annotationsJson.contains("the") && annotationsJson.contains("a ")) {
            types.add("ARTICLE_MISUSE");
        }
        if (upperJson.contains("S_V") || upperJson.contains("SUBJECT") || upperJson.contains("VERB AGREEMENT") || upperJson.contains("主谓")) {
            types.add("S_V_AGREEMENT");
        }
        if (upperJson.contains("TENSE") || upperJson.contains("时态")) {
            types.add("TENSE_ERROR");
        }
        if (upperJson.contains("PREPOSITION") || upperJson.contains("介词") || upperJson.contains("FREP")) {
            types.add("PREPOSITION_ERROR");
        }
        if (upperJson.contains("PLURAL") || upperJson.contains("单复数") || upperJson.contains("SINGULAR")) {
            types.add("PLURAL_ERROR");
        }
        if (upperJson.contains("WORD CHOICE") || upperJson.contains("词汇") || upperJson.contains("VOCABULARY") || upperJson.contains("替换")) {
            types.add("WORD_CHOICE");
        }
        if (upperJson.contains("COLLOCATION") || upperJson.contains("搭配")) {
            types.add("COLLOCATION");
        }
        if (upperJson.contains("SPELLING") || upperJson.contains("拼写")) {
            types.add("SPELLING");
        }
        if (upperJson.contains("RUN-ON") || upperJson.contains("RUN ON") || upperJson.contains("流水") || upperJson.contains("断句")) {
            types.add("RUN_ON_SENTENCE");
        }
        if (upperJson.contains("FRAGMENT") || upperJson.contains("不完整")) {
            types.add("FRAGMENT");
        }
        if (upperJson.contains("PUNCTUATION") || upperJson.contains("标点")) {
            types.add("PUNCTUATION");
        }
        if (upperJson.contains("LOGIC") || upperJson.contains("逻辑") || upperJson.contains("论证")) {
            types.add("LOGIC_GAP");
        }
        if (upperJson.contains("COHESION") || upperJson.contains("衔接") || upperJson.contains("CONHESION")) {
            types.add("COHESION");
        }
        if (upperJson.contains("REDUNDANCY") || upperJson.contains("冗余") || upperJson.contains("REPETITION") || upperJson.contains("重复")) {
            types.add("REDUNDANCY");
        }
        if (upperJson.contains("AWKWARD") || upperJson.contains("生硬") || upperJson.contains("NATURAL")) {
            types.add("AWKWARD");
        }

        // 如果没有检测到具体类型，添加一个OTHER
        if (types.isEmpty()) {
            types.add("OTHER");
        }

        return types;
    }

    /**
     * 生成单条错误类型的改进建议
     */
    private String generateErrorSuggestion(String errorType) {
        Map<String, String> suggestions = new LinkedHashMap<>();
        suggestions.put("ARTICLE_MISUSE", "注意冠词a/an/the的使用，特别关注可数名词单数前是否需要冠词。");
        suggestions.put("S_V_AGREEMENT", "检查主语与谓语动词的数是否一致，特别注意单数第三人称。");
        suggestions.put("TENSE_ERROR", "保持时态一致性，注意时间状语对时态的影响。");
        suggestions.put("PREPOSITION_ERROR", "多积累介词固定搭配，如depend on, interested in等。");
        suggestions.put("PLURAL_ERROR", "注意可数名词的复数形式及对应的动词变化。");
        suggestions.put("WORD_CHOICE", "扩大词汇量，使用更精准的词汇替换通用表达。");
        suggestions.put("COLLOCATION", "学习常见词语搭配，如make progress而非do progress。");
        suggestions.put("SPELLING", "写作后仔细检查拼写，注意常见易错词。");
        suggestions.put("RUN_ON_SENTENCE", "合理使用句号和连接词，避免过长的流水句。");
        suggestions.put("FRAGMENT", "确保每个句子都有完整的主谓结构。");
        suggestions.put("PUNCTUATION", "正确使用标点符号，特别注意逗号的用法。");
        suggestions.put("LOGIC_GAP", "加强段落间的逻辑衔接，确保论证链条完整。");
        suggestions.put("COHESION", "使用恰当的连接词增强段落连贯性。");
        suggestions.put("REDUNDANCY", "精简表达，避免重复或多余的修饰语。");
        suggestions.put("AWKWARD", "多阅读范文，培养地道的英语表达习惯。");
        return suggestions.getOrDefault(errorType, "继续加强练习，注意识别和改正此类错误。");
    }

    /**
     * 生成综合建议
     */
    private String generateAdvice(WeaknessOverviewResponse.DimensionScores scores,
                                   String mostCommonError, String mostCommonCategory) {
        StringBuilder advice = new StringBuilder();

        // 找出最弱的维度
        Map<String, Double> dims = new LinkedHashMap<>();
        dims.put("TA（任务完成度）", scores.getAvgTa());
        dims.put("CC（连贯与衔接）", scores.getAvgCc());
        dims.put("LR（词汇资源）", scores.getAvgLr());
        dims.put("GRA（语法范围与准确性）", scores.getAvgGra());

        String weakestDim = dims.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");

        if (mostCommonCategory.contains("语法") || mostCommonCategory.contains("词汇") || mostCommonCategory.contains("逻辑")) {
            advice.append("你的主要薄弱环节在于「")
                    .append(mostCommonCategory)
                    .append("」，其中「")
                    .append(mostCommonError)
                    .append("」出现频率最高。");
        } else {
            advice.append("你的主要薄弱环节在于「")
                    .append(mostCommonError)
                    .append("」。");
        }

        if (!weakestDim.isEmpty()) {
            advice.append("在雅思四项评分中，「")
                    .append(weakestDim)
                    .append("」得分最低，建议重点加强。");
        }

        advice.append("坚持练习，针对性地改进以上问题，写作分数将稳步提升。");

        return advice.toString();
    }
}