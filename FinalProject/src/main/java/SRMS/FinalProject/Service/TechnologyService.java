package SRMS.FinalProject.Service;

import SRMS.FinalProject.Repository.TechnologyRepository;
import SRMS.FinalProject.UserEntity.Technology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class TechnologyService {
//
    @Autowired
    private TechnologyRepository technologyRepository;

    public Technology addTechnology(Technology technology) {
        try {
            boolean exists = technologyRepository.existsByTechName(technology.getTechName());

            if (exists) {
                throw new RuntimeException("Technology already exists");
            }

            return technologyRepository.save(technology);
        } catch (Exception e) {
            System.err.println("Error in addTechnology: ");
            throw e;
        }
    }

    public List<Technology> getAllTechnologies() {
        try {
            return technologyRepository.findAll();
        } catch (Exception e) {
            System.err.println("Error in getAllTechnologies: " + e.getMessage());
            throw e;
        }
    }

    public Optional<Technology> getTechnologyById(Long id) {
        try {
            return technologyRepository.findById(id);
        } catch (Exception e) {
            System.err.println("Error in getTechnologyById: " + e.getMessage());
            throw e;
        }
    }

    public Technology updateTechnology(Long id, Technology updatedTech) {
        try {
            return technologyRepository.findById(id).map(tech -> {
                tech.setTechName(updatedTech.getTechName());
                tech.setTechDescription(updatedTech.getTechDescription());
                return technologyRepository.save(tech);
            }).orElseThrow(() -> new RuntimeException("Technology not found"));
        } catch (Exception e) {
            System.err.println("Error in updateTechnology: " + e.getMessage());
            throw e;
        }
    }

    public boolean deleteTechnology(Long id) {
        try {
            if (technologyRepository.existsById(id)) {
                technologyRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error in deleteTechnology: " + e.getMessage());
            throw e;
        }
    }
}