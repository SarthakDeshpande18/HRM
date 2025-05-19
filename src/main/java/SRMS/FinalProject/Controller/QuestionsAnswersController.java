package SRMS.FinalProject.Controller;


import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.DTO.QuestionAnswersRequestDTO;
import SRMS.FinalProject.Service.QuestionAnswerService;
import SRMS.FinalProject.UserEntity.QuestionAnswersEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionsAnswersController {

    private final QuestionAnswerService service;
    @PostMapping
    public ResponseEntity<ApiResponse<?>> filterQuestions(
            @RequestBody QuestionAnswersRequestDTO request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        ApiResponse<?> response = service.filterQuestions(request.getFilter(), pageable);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("add")
    public ResponseEntity<ApiResponse<?>> addQuestion(@RequestBody QuestionAnswersEntity question) {
        ApiResponse<?> response = service.addQuestions(Collections.singletonList(question));
        return ResponseEntity.ok(response);
    }


    @PutMapping("/update")
    public ResponseEntity<ApiResponse<?>> updateQuestion(@RequestBody QuestionAnswersEntity question) {
        Long id = question.getId();
        if (id == null) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "ID is required in request body", null));
        }
        ApiResponse<?> response = service.updateQuestionById(id, question);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<?>> deleteQuestion(@RequestBody Map<String, Long> request) {
        Long id = request.get("id");
        if (id == null) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "ID is required in request body", null));
        }
        ApiResponse<?> response = service.deleteQuestionById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}