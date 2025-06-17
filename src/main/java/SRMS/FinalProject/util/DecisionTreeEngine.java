package SRMS.FinalProject.util;

import SRMS.FinalProject.DTO.AnswerDTO;
import SRMS.FinalProject.DTO.QuestionDTO;
import SRMS.FinalProject.Repository.AnsweredQuestionRepository;
import SRMS.FinalProject.UserEntity.AnsweredQuestionEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


//public class DecisionTreeEngine {
//
//    private static List<QuestionDTO> questions = new ArrayList<>();
//    private static int currentIndex = 0;
//
//
//    public static QuestionDTO getNextQuestion(AnswerDTO answerDTO, Long userId, AnsweredQuestionRepository answeredRepo) {
//        if (answerDTO != null && "Yes".equalsIgnoreCase(answerDTO.getAnswer())) {
//            QuestionDTO lastQuestion = questions.get(currentIndex - 1);
//            AnsweredQuestionEntity entity = new AnsweredQuestionEntity();
//            entity.setUserId(userId);
//            entity.setQuestion(lastQuestion.getQuestion());
//            entity.setAnswer("Yes");
//            entity.setAnsweredAt(LocalDateTime.now());
//            answeredRepo.save(entity);
//        }
//
//        // Fetch previously answered questions
//        List<String> answeredYesQuestions = answeredRepo.findByUserId(userId).stream()
//                .map(AnsweredQuestionEntity::getQuestion)
//                .collect(Collectors.toList());
//
//        while (currentIndex < questions.size()) {
//            QuestionDTO next = questions.get(currentIndex++);
//            if (!answeredYesQuestions.contains(next.getQuestion())) {
//                return next;
//            }
//        }
//
//        return null;
//    }
//
//
//
////public static QuestionDTO getNextQuestion(AnswerDTO answerDTO) {
////    // Always move forward — whether the answer is Yes or No
////    if (currentIndex < questions.size()) {
////        return questions.get(currentIndex++);
////    }
////    return null; // No more questions left
////}
//
//    public static void initialize(String filePath) {
//        questions.clear();
//        currentIndex = 0;
//
//        try {
//            ProcessBuilder pb = new ProcessBuilder("python", "D:/HRM/scripts/decision_tree.py", filePath);
//            pb.redirectErrorStream(true);
//            Process process = pb.start();
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            StringBuilder output = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                output.append(line);
//            }
//
//            int exitCode = process.waitFor();
//            if (exitCode != 0) {
//                throw new RuntimeException("Python script failed with exit code " + exitCode + ": " + output);
//            }
//
//            // Debug print to verify Python output
//            System.out.println("Python output: " + output.toString());
//
//            ObjectMapper mapper = new ObjectMapper();
//            // Explicit type reference for JSON parsing
//            List<Map<String, String>> extractedQA = mapper.readValue(output.toString(), new TypeReference<List<Map<String, String>>>() {
//            });
//
//            for (Map<String, String> qa : extractedQA) {
//                String questionId = qa.get("questionId");
//                String question = qa.getOrDefault("question", "No question provided");
//                String answer = qa.getOrDefault("answer", "No answer provided");
//                questions.add(new QuestionDTO(questionId, question, List.of("Yes", "No"), answer));
//            }
//
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to process dataset via Python: " + e.getMessage());
//        }
//
//    }
//}


public class DecisionTreeEngine {

    private static List<QuestionDTO> questions = new ArrayList<>();
    private static int currentIndex = 0;

    public static QuestionDTO getNextQuestion(AnswerDTO answerDTO, Long userId, AnsweredQuestionRepository answeredRepo) {
        if (answerDTO != null && "Yes".equalsIgnoreCase(answerDTO.getAnswer())) {
            // Get the last question that was shown
            QuestionDTO lastQuestion = questions.get(currentIndex - 1);
            AnsweredQuestionEntity entity = new AnsweredQuestionEntity();
            entity.setUserId(userId);
            entity.setQuestionId(lastQuestion.getQuestionId()); // store questionId
            entity.setQuestion(lastQuestion.getQuestion());
            entity.setAnswer("Yes");
            entity.setAnsweredAt(LocalDateTime.now());
            answeredRepo.save(entity);
        }

        // Get list of already answered "Yes" questionIds for this user
        List<String> answeredYesQuestionIds = answeredRepo.findByUserId(userId).stream()
                .filter(a -> "Yes".equalsIgnoreCase(a.getAnswer()))
                .map(AnsweredQuestionEntity::getQuestionId)
                .collect(Collectors.toList());

        // Loop until we find a question that hasn't been answered "Yes"
        while (currentIndex < questions.size()) {
            QuestionDTO next = questions.get(currentIndex++);
            if (!answeredYesQuestionIds.contains(next.getQuestionId())) {
                return next;
            }
        }

        return null; // All questions answered with Yes or exhausted
    }

    public static void initialize(String filePath) {
        questions.clear();
        currentIndex = 0;

        try {
            ProcessBuilder pb = new ProcessBuilder("python", "E:/HRM/scripts/decision_tree.py", filePath);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Python script failed with exit code " + exitCode + ": " + output);
            }

            // Debug print to verify Python output
            System.out.println("Python output: " + output);

            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, String>> extractedQA = mapper.readValue(output.toString(), new TypeReference<List<Map<String, String>>>() {});

            for (Map<String, String> qa : extractedQA) {
                String questionId = qa.getOrDefault("questionId", UUID.randomUUID().toString()); // fallback to UUID
                String question = qa.getOrDefault("question", "No question provided");
                String answer = qa.getOrDefault("answer", "No answer provided");
                questions.add(new QuestionDTO(questionId, question, List.of("Yes", "No"), answer));
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to process dataset via Python: " + e.getMessage());
        }
    }
}
