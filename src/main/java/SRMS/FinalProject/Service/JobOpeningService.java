package SRMS.FinalProject.Service;



import SRMS.FinalProject.Repository.JobOpeningRepository;

import SRMS.FinalProject.UserEntity.JobOpenings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

//@Service
//public class JobOpeningService {
//
//    @Autowired
//    private JobOpeningRepository jobOpeningsRepository;
//
//    public JobOpenings addJobOpening(JobOpenings jobOpening) {
//        try {
//            boolean exists = jobOpeningsRepository.existsByJobDescriptionAndYearOfExperienceAndJobType(
//                    jobOpening.getJobDescription(),
//                    jobOpening.getYearOfExperience(),
//                    jobOpening.getJobType()
//            );
//
//            if (exists) {
//                return null;  // Job opening already exists
//            }
//
//            return jobOpeningsRepository.save(jobOpening);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public Page<JobOpenings> getAllJobOpenings(Pageable pageable) {
//        return jobOpeningsRepository.findAll(pageable);
//    }
//
//    public Optional<JobOpenings> getJobOpeningById(Long id) {
//        return jobOpeningsRepository.findById(id);
//    }
//
//    public JobOpenings updateJobOpening(Long id, JobOpenings updatedJobOpening) {
//        return jobOpeningsRepository.findById(id).map(job -> {
//            job.setJobDescription(updatedJobOpening.getJobDescription());
//            job.setYearOfExperience(String.valueOf(updatedJobOpening.getYearOfExperience()));
//            job.setJobType(updatedJobOpening.getJobType());
//            return jobOpeningsRepository.save(job);
//        }).orElseThrow(() -> new RuntimeException("Job Opening not found"));
//    }
//
//    public boolean deleteJobOpening(Long id) {
//        if (jobOpeningsRepository.existsById(id)) {
//            jobOpeningsRepository.deleteById(id);
//            return true;
//        }
//        return false;
//    }
//}

@Service
public class JobOpeningService {

    @Autowired
    private JobOpeningRepository jobOpeningsRepository;

    public JobOpenings addJobOpening(JobOpenings jobOpening) {
        // Check for duplicates based only on jobDescription
        boolean exists = jobOpeningsRepository.existsByJobDescription(jobOpening.getJobDescription());

        if (exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Job opening already exists with the same job description");
        }

        return jobOpeningsRepository.save(jobOpening);
    }

    public Page<JobOpenings> getAllJobOpenings(Pageable pageable) {
        return jobOpeningsRepository.findAll(pageable);
    }

    public Optional<JobOpenings> getJobOpeningById(Long id) {
        return jobOpeningsRepository.findById(id);
    }

    public JobOpenings updateJobOpening(Long id, JobOpenings updatedJobOpening) {
        return jobOpeningsRepository.findById(id).map(job -> {
            job.setJobDescription(updatedJobOpening.getJobDescription());
            job.setYearOfExperience(updatedJobOpening.getYearOfExperience());
            job.setJobType(updatedJobOpening.getJobType());
            return jobOpeningsRepository.save(job);
        }).orElseThrow(() -> new RuntimeException("Job Opening not found"));
    }

    public boolean deleteJobOpening(Long id) {
        if (jobOpeningsRepository.existsById(id)) {
            jobOpeningsRepository.deleteById(id);
            return true;
        }
        return false;
    }
}