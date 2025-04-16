package SRMS.FinalProject.Service;


import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.DTO.QuestionAnswersRequestDTO;
import SRMS.FinalProject.Repository.QuestionAnswerRepository;
import SRMS.FinalProject.UserEntity.QuestionAnswersEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class QuestionAnswerService {
//
    private final QuestionAnswerRepository repository;

    public ApiResponse<?> handleAction(QuestionAnswersRequestDTO request) {
        switch (request.getFilter()) {
            case "getAll":
                return new ApiResponse<>(200, "Fetched all questions", repository.findAll());

            case "getById":
                Optional<QuestionAnswersEntity> byId = repository.findById(request.getId());
                return byId.map(qa -> new ApiResponse<>(200, "Fetched by ID", qa))
                        .orElseGet(() -> new ApiResponse<>(404, "ID not found", null));

            case "getByTechnology":
                List<QuestionAnswersEntity> results = repository.findByTechnology(request.getTechnology());

                if (results.isEmpty()) {
                    return new ApiResponse<>(404, "No data found for the given technology", null);
                } else {
                    return new ApiResponse<>(200, "Fetched by technology", results);
                }

            case "add":
                if ("add".equalsIgnoreCase(request.getFilter())) {
                    List<QuestionAnswersEntity> incomingQuestions = request.getQuestions();

                    if (incomingQuestions == null || incomingQuestions.isEmpty()) {
                        return new ApiResponse<>(400, "No questions provided", null);
                    }

                    // Ensure technology is present in each entity
                    for (QuestionAnswersEntity q : incomingQuestions) {
                        if (q.getTechnology() == null || q.getTechnology().isEmpty()) {
                            return new ApiResponse<>(400, "Technology field is required for all questions", null);
                        }
                    }

                    List<QuestionAnswersEntity> saved = repository.saveAll(incomingQuestions);
                    return new ApiResponse<>(200, "Questions and Answers are added", saved);
                }

//                List<QuestionAnswersEntity> saved = repository.saveAll(request.getQuestions());
//                return new ApiResponse<>(200, "Questions and Answers are added", saved);

            case "updateById":

                if ("updateById".equalsIgnoreCase(request.getFilter())) {
                    if (request.getQuestion() != null && request.getQuestion().getId() != null) {
                        Optional<QuestionAnswersEntity> existing = repository.findById(request.getQuestion().getId());
                        if (existing.isPresent()) {
                            QuestionAnswersEntity qa = existing.get();

                            // Update only if non-null values are provided
                            if (request.getQuestion().getTechnology() != null) {
                                qa.setTechnology(request.getQuestion().getTechnology());
                            }
                            if (request.getQuestion().getQuestion() != null) {
                                qa.setQuestion(request.getQuestion().getQuestion());
                            }
                            if (request.getQuestion().getAnswer() != null) {
                                qa.setAnswer(request.getQuestion().getAnswer());
                            }

                            QuestionAnswersEntity updated = repository.save(qa);
                            return new ApiResponse<>(200, "Question updated successfully", updated);
                        } else {
                            return new ApiResponse<>(404, "Question ID not found", null);
                        }
                    }
                    return new ApiResponse<>(400, "Invalid data for update", null);
                }


            case "deleteById":
                if (repository.existsById(request.getId())) {
                    repository.deleteById(request.getId());
                    return new ApiResponse<>(200, "Deleted", null);
                } else {
                    return new ApiResponse<>(404, "ID not found for deletion", null);
                }

            default:
                return new ApiResponse<>(400, "Unknown action", null);

        }
    }
}