package SRMS.FinalProject.Controller;



import SRMS.FinalProject.DTO.*;
import SRMS.FinalProject.Repository.AnsweredQuestionRepository;
import SRMS.FinalProject.Service.CustomUserDetails;
import SRMS.FinalProject.util.DatasetDownloader;
import SRMS.FinalProject.util.DecisionTreeEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class InterviewQuestionController {

    @Autowired
    AnsweredQuestionRepository answeredQuestionRepository;

    @PostMapping(value = "/start-interview", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> startInterview(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "datasetUrl", required = false) String datasetUrl,
            @AuthenticationPrincipal CustomUserDetails user) {

        try {
            String datasetPath = null;

            if (file != null && !file.isEmpty()) {
                File tempFile = File.createTempFile("dataset_", ".csv");
                file.transferTo(tempFile);
                datasetPath = tempFile.getAbsolutePath();
            } else if (datasetUrl != null && !datasetUrl.isEmpty()) {
                datasetPath = DatasetDownloader.downloadDataset(datasetUrl);
            } else {
                // Return standardized error using ApiResponse
                ApiResponse<?> response = new ApiResponse<>(401, "Either file or datasetUrl must be provided.", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Initialize and get question
            DecisionTreeEngine.initialize(datasetPath);
            QuestionDTO firstQuestion = DecisionTreeEngine.getNextQuestion(null, user.getUserId(), answeredQuestionRepository);

            ApiResponse<QuestionDTO> successResponse = new ApiResponse<>(200, "Interview started", firstQuestion);
            return ResponseEntity.ok(successResponse);

        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(500, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/next-question")
    public ResponseEntity<?> nextQuestion(@RequestBody AnswerDTO answerDTO, @AuthenticationPrincipal CustomUserDetails user) {
        try {
            QuestionDTO next = DecisionTreeEngine.getNextQuestion(answerDTO, user.getUserId(), answeredQuestionRepository);
            return ResponseEntity.ok(next);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

}
