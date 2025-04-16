package SRMS.FinalProject.Service;



import SRMS.FinalProject.Repository.JobOpeningRepository;
import SRMS.FinalProject.UserEntity.JobOpenings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class JobOpeningService {

    @Autowired
    private JobOpeningRepository jobOpeningsRepository;

    public JobOpenings addJobOpening(JobOpenings jobOpening) {
        boolean exists = jobOpeningsRepository.existsByJobDescription(jobOpening.getJobDescription()) ||
                jobOpeningsRepository.existsByJobType(jobOpening.getJobType());

        if (exists) {
            throw new RuntimeException("Job type already exists");
        }

        return jobOpeningsRepository.save(jobOpening);
    }


    public List<JobOpenings> getAllJobOpenings() {
        return jobOpeningsRepository.findAll();
    }

    public Optional<JobOpenings> getJobOpeningById(Long id) {
        return jobOpeningsRepository.findById(id);
    }

    public JobOpenings updateJobOpening(Long id, JobOpenings updatedJobOpening) {
        return jobOpeningsRepository.findById(id).map(job -> {
            job.setJobDescription(updatedJobOpening.getJobDescription());
            job.setYear_Of_Experience(updatedJobOpening.getYear_Of_Experience());
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