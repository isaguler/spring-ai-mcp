package com.isaguler.spring_ai_mcp.controller;

import com.isaguler.spring_ai_mcp.service.WebSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mcp")
public class WebSearchController {

    private final WebSearchService webSearchService;

    public WebSearchController(WebSearchService webSearchService) {
        this.webSearchService = webSearchService;
    }

    @GetMapping("/search")
    public ResponseEntity<String> search(@RequestParam(value = "message", defaultValue = "Does Spring AI supports the Model Context Protocol? Please provide some references.") String message) {
        return ResponseEntity.ok(webSearchService.searchFromWeb(message));
    }
}
