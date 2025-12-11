package com.ace.designservice.controller;


import com.ace.designservice.dto.GenerateDesignRequest;
import com.ace.designservice.dto.GenerateDesignResponse;
import com.ace.designservice.service.PromptService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/design")
public class DesignController {

    private final PromptService promptService;

    public DesignController(PromptService promptService) {
        this.promptService = promptService;
    }

    @PostMapping("/generate-prompt")
    public ResponseEntity<GenerateDesignResponse> generatePrompt(
            @Valid @RequestBody GenerateDesignRequest request) {

        GenerateDesignResponse response = promptService.generatePrompt(request);
        return ResponseEntity.ok(response);
    }
}
