package SRMS.FinalProject.Controller;


import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.DTO.QuestionAnswersRequestDTO;
import SRMS.FinalProject.Service.QuestionAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//
@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionsAnswersController {

    private final QuestionAnswerService service;

    @PostMapping
    public ApiResponse<?> handleRequest(@RequestBody QuestionAnswersRequestDTO request) {
        return service.handleAction(request);
    }
}

