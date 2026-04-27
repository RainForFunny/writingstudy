package com.ielts.writing.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ielts.writing.model.entity.Topic;
import com.ielts.writing.model.enums.TopicCategory;
import com.ielts.writing.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicRepository topicRepository;

    @GetMapping("/random")
    public ResponseEntity<Map<String, Object>> getRandomTopic(
            @RequestParam(required = false) String category) {
        LambdaQueryWrapper<Topic> wrapper = new LambdaQueryWrapper<Topic>()
                .eq(Topic::getIsActive, 1);
        if (category != null) {
            wrapper.eq(Topic::getCategory, category);
        }
        wrapper.last("ORDER BY RAND() LIMIT 1");

        Topic topic = topicRepository.selectOne(wrapper);

        Map<String, Object> data = new HashMap<>();
        data.put("topic", topic);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", data);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getCategories() {
        List<Map<String, String>> categories = Arrays.stream(TopicCategory.values())
                .map(c -> {
                    Map<String, String> item = new HashMap<>();
                    item.put("code", c.name());
                    item.put("name", c.getDisplayName());
                    return item;
                })
                .collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("categories", categories);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", data);
        return ResponseEntity.ok(result);
    }
}