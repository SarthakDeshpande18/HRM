package SRMS.FinalProject.Controller;


import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.DTO.QuestionAnswersRequestDTO;
import SRMS.FinalProject.Service.QuestionAnswerService;
import SRMS.FinalProject.UserEntity.QuestionAnswersEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//
@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionsAnswersController {

    private final QuestionAnswerService service;

    @PostMapping("/filter")
    public ResponseEntity<ApiResponse<?>> filterQuestions(@RequestBody QuestionAnswersRequestDTO request) {
        ApiResponse<?> response = service.filterQuestions(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("add")

    public ResponseEntity<ApiResponse<?>> addQuestions(@RequestBody List<QuestionAnswersEntity> questions) {
        ApiResponse<?> response = service.addQuestions(questions);
        return ResponseEntity.ok(response);
    }



    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateQuestion(
            @PathVariable Long id,
            @RequestBody QuestionAnswersEntity question) {
        ApiResponse<?> response = service.updateQuestionById(id, question);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteQuestion(@PathVariable Long id) {
        ApiResponse<?> response = service.deleteQuestionById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
}
}

