package SRMS.FinalProject.Service;

import SRMS.FinalProject.Repository.TechnologyRepository;
import SRMS.FinalProject.UserEntity.Technology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TechnologyService {

    @Autowired
    private TechnologyRepository technologyRepository;

    public Technology addTechnology(Technology technology) {
        return technologyRepository.save(technology);
    }

    public List<Technology> getAllTechnologies() {
        return technologyRepository.findAll();
    }

    public Optional<Technology> getTechnologyById(Long id) {
        return technologyRepository.findById(id);
    }

    public Technology updateTechnology(Long id, Technology updatedTech) {
        return technologyRepository.findById(id).map(tech -> {
            tech.setTechName(updatedTech.getTechName());
            tech.setTechDescription(updatedTech.getTechDescription());
            return technologyRepository.save(tech);
        }).orElseThrow(() -> new RuntimeException("Technology not found"));
    }

    public void deleteTechnology(Long id) {
        technologyRepository.deleteById(id);
    }
}
