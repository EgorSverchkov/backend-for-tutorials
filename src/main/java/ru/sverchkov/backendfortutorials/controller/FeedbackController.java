package ru.sverchkov.backendfortutorials.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sverchkov.backendfortutorials.model.request.FeedbackRequest;
import ru.sverchkov.backendfortutorials.service.FeedbackService;

@RestController
@RequestMapping("/api/feedback")
@Slf4j
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeedbackById(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok(feedbackService.deleteById(id));
    }

    @PostMapping
    public ResponseEntity<?> createFeedback(@RequestBody FeedbackRequest feedbackRequest) {
        return ResponseEntity.ok(feedbackService.createFeedback(feedbackRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveFeedbacks() {
       return ResponseEntity.ok(feedbackService.getAllActive());
    }

    @GetMapping("/all/byuser/{userId}")
    public ResponseEntity<?> getAllByUser(@PathVariable(name = "userId") String userId) {
        return ResponseEntity.ok(feedbackService.getAllByUser(userId));
    }

    @GetMapping("/all/bytutorial/{tutorialId}")
    public ResponseEntity<?> getAllByTutorial(@PathVariable(name = "tutorialId") String tutorialId) {
        return ResponseEntity.ok(feedbackService.getAllByTutorial(tutorialId));
    }

}
