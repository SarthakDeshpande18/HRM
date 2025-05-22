package SRMS.FinalProject.Service;

import SRMS.FinalProject.DTO.UserTechnologyDTO;
import SRMS.FinalProject.Repository.TechnologyRepository;
import SRMS.FinalProject.Repository.UserTechnologyRepository;
import SRMS.FinalProject.UserEntity.Technology;
import SRMS.FinalProject.UserEntity.UserTechnology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TechnologyService {

    @Autowired
    private TechnologyRepository technologyRepository;
    @Autowired
    private UserTechnologyRepository userTechnologyRepository;

    public Technology addTechnology(Technology technology) {
        try {
            boolean exists = technologyRepository.existsByTechName(technology.getTechName());

            if (exists) {
                return null;  // Indicate duplicate entry
            }

            return technologyRepository.save(technology);
        } catch (Exception e) {
            System.err.println("Error in addTechnology: ");
            throw e;
        }
    }

    public Page<Technology> getAllTechnologies(Pageable pageable) {
        try {
            return technologyRepository.findAll(pageable);
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

    public Page<UserTechnologyDTO> getAllUserTechnology(Pageable pageable) {
        try {
            return userTechnologyRepository.findAllUserTechIds(pageable);
        } catch (Exception e) {
            System.err.println("Error in getAllUserTechnologyIds: " + e.getMessage());
            throw e;
        }
    }



    // Get user-technology mappings by user ID
    public Page<UserTechnologyDTO> getUserTechnologyDTOsByUserId(Long userId, Pageable pageable) {
        try {
            return userTechnologyRepository.findByUserIdCustom(userId, pageable);
        } catch (Exception e) {
            System.err.println("Error in getUserTechnologyDTOsByUserId: " + e.getMessage());
            throw e;
        }
    }

}
