package ru.sverchkov.backendfortutorials.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sverchkov.backendfortutorials.model.request.TutorialRequest;
import ru.sverchkov.backendfortutorials.service.TutorialService;

import java.util.UUID;

@RestController
@RequestMapping("/api/tutorials")
@CrossOrigin(origins = "*", maxAge = 3600)
@SecurityRequirement(name = "bearerAuth")
public class TutorialController {
    private final TutorialService tutorialService;

    public TutorialController(TutorialService tutorialService) {
        this.tutorialService = tutorialService;
    }

    @GetMapping
    public ResponseEntity<?> getAllTutorials() {
        return ResponseEntity.ok(tutorialService.getAllTutorials());
    }

    @GetMapping("/page/{id}")
    public ResponseEntity<?> getTutorialById(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok(tutorialService.getTutorialById(UUID.fromString(id)));
    }

    @PostMapping
    public ResponseEntity<?> createTutorial(@RequestBody TutorialRequest request) {
        return ResponseEntity.ok(tutorialService.createTutorial(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTutorial(@PathVariable(name = "id") String id, @RequestBody TutorialRequest request) {
        return ResponseEntity.ok(tutorialService.updateTutorial(UUID.fromString(id), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTutorial(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok(tutorialService.deleteTutorial(UUID.fromString(id)));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllTutorials() {
        return ResponseEntity.ok(tutorialService.deleteAllTutorials());
    }

    @GetMapping("/published")
    public ResponseEntity<?> findByPublished() {
        return ResponseEntity.ok(tutorialService.findByPublished());
    }

}
