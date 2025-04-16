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
        try {
            boolean exists = jobOpeningsRepository.existsByJobDescriptionAndYearOfExperienceAndJobType(
                    jobOpening.getJobDescription(),
                    jobOpening.getYearOfExperience(),
                    jobOpening.getJobType()
            );

            if (exists) {
                //throw new RuntimeException("Job Opening with the same details already exists");
            return null;
            }


            return jobOpeningsRepository.save(jobOpening);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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