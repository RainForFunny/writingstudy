package com.ielts.writing.controller;

import com.ielts.writing.model.dto.response.WeaknessOverviewResponse;
import com.ielts.writing.service.WeaknessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/weakness")
@RequiredArgsConstructor
public class WeaknessController {

    private final WeaknessService weaknessService;

    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getOverview(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        WeaknessOverviewResponse overview = weaknessService.getOverview(userId);
        return ResponseEntity.ok(Map.of("code", 200, "data", overview));
    }
}
