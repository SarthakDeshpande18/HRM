package SRMS.FinalProject.Service;


import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.DTO.QuestionAnswersRequestDTO;
import SRMS.FinalProject.Repository.QuestionAnswerRepository;
import SRMS.FinalProject.UserEntity.QuestionAnswersEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class QuestionAnswerService {
//
//    private final QuestionAnswerRepository repository;
//
//    // Filter logic for getAll, getById, and getByTechnology (single/multiple)
//    public ApiResponse<?> filterQuestions(QuestionAnswersRequestDTO request) {
//        try {
//            String filter = request.getFilter();
//
//            // Check if the filter is a comma-separated list of technologies (e.g., "java,cpp")
//            if (filter != null && filter.contains(",")) {
//                // Split the filter into a list of technologies
//                List<String> techList = List.of(filter.split(","));
//
//                // Query the database using the technologies list
//                List<QuestionAnswersEntity> results = repository.findByTechnologyIn(techList);
//
//                return results.isEmpty()
//                        ? new ApiResponse<>(404, "No data found for given technologies", null)
//                        : new ApiResponse<>(200, "Fetched by multiple technologies", results);
//            }
//
//            // If filter is not a comma-separated list, you can handle other cases
//            // For example, fetching by a single technology or by ID (if applicable)
//            if ("0".equals(filter)) {
//                return new ApiResponse<>(200, "Fetched all questions", repository.findAll());
//            }
//
//            // If filter is numeric (i.e., an ID), fetch by ID
//            if (filter.matches("\\d+")) {
//                Long id = Long.parseLong(filter);
//                Optional<QuestionAnswersEntity> byId = repository.findById(id);
//                return byId.map(qa -> new ApiResponse<>(200, "Fetched by ID", qa))
//                        .orElseGet(() -> new ApiResponse<>(404, "ID not found", null));
//            }
//
//            // Otherwise, fetch by a single technology
//            List<QuestionAnswersEntity> results = repository.findByTechnology(filter);
//            return results.isEmpty()
//                    ? new ApiResponse<>(404, "No data found for technology: " + filter, null)
//                    : new ApiResponse<>(200, "Fetched by technology", results);
//
//        } catch (Exception e) {
//            return new ApiResponse<>(500, "Internal Server Error: " + e.getMessage(), null);
//        }
//    }
//
//
//    // Add questions
//    public ApiResponse<?> addQuestions(List<QuestionAnswersEntity> incomingQuestions) {
//        if (incomingQuestions == null || incomingQuestions.isEmpty()) {
//            return new ApiResponse<>(400, "No questions provided", null);
//        }
//
//        for (QuestionAnswersEntity q : incomingQuestions) {
//            if (q.getTechnology() == null || q.getTechnology().isEmpty()) {
//                return new ApiResponse<>(400, "Technology field is required for all questions", null);
//            }
//        }
//
//        List<QuestionAnswersEntity> saved = repository.saveAll(incomingQuestions);
//        return new ApiResponse<>(200, "Questions and Answers added", saved);
//    }
//
//    // Update by ID
//    public ApiResponse<?> updateQuestionById(Long id, QuestionAnswersEntity updatedQuestion) {
//        Optional<QuestionAnswersEntity> existing = repository.findById(id);
//        if (existing.isPresent()) {
//            QuestionAnswersEntity qa = existing.get();
//
//            if (updatedQuestion.getTechnology() != null) {
//                qa.setTechnology(updatedQuestion.getTechnology());
//            }
//            if (updatedQuestion.getQuestion() != null) {
//                qa.setQuestion(updatedQuestion.getQuestion());
//            }
//            if (updatedQuestion.getAnswer() != null) {
//                qa.setAnswer(updatedQuestion.getAnswer());
//            }
//
//            QuestionAnswersEntity updated = repository.save(qa);
//            return new ApiResponse<>(200, "Question updated successfully", updated);
//        } else {
//            return new ApiResponse<>(404, "Question ID not found", null);
//        }
//    }
//
//    // Delete by ID
//    public ApiResponse<?> deleteQuestionById(Long id) {
//        if (repository.existsById(id)) {
//            repository.deleteById(id);
//            return new ApiResponse<>(200, "Deleted successfully", null);
//        } else {
//            return new ApiResponse<>(404, "ID not found for deletion", null);
//        }
//    }
//}

@Service
@RequiredArgsConstructor
public class QuestionAnswerService {

    private final QuestionAnswerRepository repository;

    // Public filterQuestions method used by controller
    public ApiResponse<?> filterQuestions(String filter, Pageable pageable) {
        if (filter == null || filter.isEmpty() || filter.equals("All")) {
            return fetchAllQuestions(pageable);
        }

        try {
            Long id = Long.parseLong(filter);
            return fetchById(id);
        } catch (NumberFormatException e) {
            // Not an ID, continue
        }

        if (filter.contains(",")) {
            return fetchByMultipleTechnologies(filter, pageable);
        }

        return fetchBySingleTechnology(filter, pageable);
    }

    private ApiResponse<?> fetchByMultipleTechnologies(String filter, Pageable pageable) {
        List<String> techList = List.of(filter.split(","));
        Page<QuestionAnswersEntity> results = repository.findByTechnologyIn(techList, pageable);

        return results.isEmpty()
                ? new ApiResponse<>(404, "No data found for given technologies", null)
                : new ApiResponse<>(200, "Fetched by multiple technologies", results);
    }

    private ApiResponse<?> fetchAllQuestions(Pageable pageable) {
        Page<QuestionAnswersEntity> results = repository.findAll(pageable);
        return new ApiResponse<>(200, "Fetched all questions", results);
    }

    private ApiResponse<?> fetchById(Long id) {
        Optional<QuestionAnswersEntity> byId = repository.findById(id);
        return byId.map(qa -> new ApiResponse<>(200, "Fetched by ID", qa))
                .orElseGet(() -> new ApiResponse<>(404, "ID not found", null));
    }

    private ApiResponse<?> fetchBySingleTechnology(String filter, Pageable pageable) {
        Page<QuestionAnswersEntity> results = repository.findByTechnology(filter, pageable);
        return results.isEmpty()
                ? new ApiResponse<>(404, "No data found for technology: " + filter, null)
                : new ApiResponse<>(200, "Fetched by technology", results);
    }

    public ApiResponse<?> addQuestions(List<QuestionAnswersEntity> incomingQuestions) {
        if (incomingQuestions == null || incomingQuestions.isEmpty()) {
            return new ApiResponse<>(400, "No questions provided", null);
        }

        for (QuestionAnswersEntity q : incomingQuestions) {
            if (q.getTechnology() == null || q.getTechnology().isEmpty()) {
                return new ApiResponse<>(400, "Technology field is required for all questions", null);
            }
        }

        List<QuestionAnswersEntity> saved = repository.saveAll(incomingQuestions);
        return new ApiResponse<>(200, "Questions and Answers added", saved);
    }

    public ApiResponse<?> updateQuestionById(Long id, QuestionAnswersEntity updatedQuestion) {
        Optional<QuestionAnswersEntity> existing = repository.findById(id);
        if (existing.isPresent()) {
            QuestionAnswersEntity qa = existing.get();

            if (updatedQuestion.getTechnology() != null) {
                qa.setTechnology(updatedQuestion.getTechnology());
            }
            if (updatedQuestion.getQuestion() != null) {
                qa.setQuestion(updatedQuestion.getQuestion());
            }
            if (updatedQuestion.getAnswer() != null) {
                qa.setAnswer(updatedQuestion.getAnswer());
            }

            QuestionAnswersEntity updated = repository.save(qa);
            return new ApiResponse<>(200, "Question updated successfully", updated);
        } else {
            return new ApiResponse<>(404, "Question ID not found", null);
        }
    }

    public ApiResponse<?> deleteQuestionById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return new ApiResponse<>(200, "Deleted successfully", null);
        } else {
            return new ApiResponse<>(404, "ID not found for deletion", null);
        }
    }
}