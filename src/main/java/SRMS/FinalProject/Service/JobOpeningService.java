package SRMS.FinalProject.Service;



import SRMS.FinalProject.Repository.JobOpeningRepository;

import SRMS.FinalProject.Repository.TechnologyRepository;
import SRMS.FinalProject.UserEntity.JobOpenings;
import SRMS.FinalProject.UserEntity.Technology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class JobOpeningService {

    @Autowired
    private JobOpeningRepository jobOpeningsRepository;

    @Autowired
    private TechnologyRepository technologyRepository;


    public JobOpenings addJobOpening(JobOpenings jobOpening) {
        boolean exists = jobOpeningsRepository.existsByJobDescription(jobOpening.getJobDescription());

        if (exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Job opening already exists with the same job description");
        }

        // Fetch Technology entity by id before saving
        if (jobOpening.getTechnology() != null && jobOpening.getTechnology().getTech_id() != null) {
            Technology tech = technologyRepository.findById(jobOpening.getTechnology().getTech_id())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Technology not found"));
            jobOpening.setTechnology(tech);
        } else {
            jobOpening.setTechnology(null);
        }

        return jobOpeningsRepository.save(jobOpening);
    }

    public JobOpenings updateJobOpening(Long id, JobOpenings updatedJobOpening) {
        return jobOpeningsRepository.findById(id).map(job -> {
            job.setJobDescription(updatedJobOpening.getJobDescription());
            job.setYearOfExperience(updatedJobOpening.getYearOfExperience());
            job.setJobType(updatedJobOpening.getJobType());

            if (updatedJobOpening.getTechnology() != null && updatedJobOpening.getTechnology().getTech_id() != null) {
                Technology tech = technologyRepository.findById(updatedJobOpening.getTechnology().getTech_id())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Technology not found"));
                job.setTechnology(tech);
            } else {
                job.setTechnology(null);
            }

            return jobOpeningsRepository.save(job);
        }).orElseThrow(() -> new RuntimeException("Job Opening not found"));
    }


    public Page<JobOpenings> getAllJobOpenings(Pageable pageable) {
        return jobOpeningsRepository.findAll(pageable);
    }

    public Optional<JobOpenings> getJobOpeningById(Long id) {
        return jobOpeningsRepository.findById(id);
    }


    public boolean deleteJobOpening(Long id) {
        if (jobOpeningsRepository.existsById(id)) {
            jobOpeningsRepository.deleteById(id);
            return true;
        }
        return false;
    }
}