package SRMS.FinalProject.Service;

import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.DTO.ProfileDTO;
import SRMS.FinalProject.Repository.EducationRepository;
import SRMS.FinalProject.Repository.ExperienceRepository;
import SRMS.FinalProject.Repository.PositionsRepository;
import SRMS.FinalProject.Repository.UserRepository;
import SRMS.FinalProject.UserEntity.Education;
import SRMS.FinalProject.UserEntity.Experience;
import SRMS.FinalProject.UserEntity.Positions;
import SRMS.FinalProject.UserEntity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
//
//@Service
//public class PositionsService {
//
//    @Autowired
//    private PositionsRepository positionsRepository;
//
//    public Positions addPosition(Positions position) {
//        boolean exists = positionsRepository.existsByPositionName(position.getPositionName());
//
//        if (exists) {
//            throw new RuntimeException("Position already exists");
//        }
//
//        return positionsRepository.save(position);
//    }
//
//    public List<Positions> getAllPositions() {
//        return positionsRepository.findAll();
//    }
//
//    public Optional<Positions> getPositionById(Long id) {
//        return positionsRepository.findById(id);
//    }
//
//    public Positions updatePosition(Long id, Positions updatedPosition) {
//        return positionsRepository.findById(id).map(position -> {
//            position.setPositionName(updatedPosition.getPositionName());
//            position.setPositionDescription(updatedPosition.getPositionDescription());
//            return positionsRepository.save(position);
//        }).orElseThrow(() -> new RuntimeException("Position not found"));
//    }
//
//    public boolean deletePosition(Long id) {
//        if (positionsRepository.existsById(id)) {
//            positionsRepository.deleteById(id);
//            return true;
//        }
//        return false;
//    }
//}

@Service
public class PositionsService {

    @Autowired
    private PositionsRepository positionsRepository;

    public Positions addPosition(Positions position) {
        boolean exists = positionsRepository.existsByPositionName(position.getPositionName());

        if (exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Position with name '" + position.getPositionName() + "' already exists.");
        }

        return positionsRepository.save(position);
    }

    public Page<Positions> getAllPositions(Pageable pageable) {
        return positionsRepository.findAll(pageable);
    }

    public Optional<Positions> getPositionById(Long id) {
        return positionsRepository.findById(id);
    }

    public Positions updatePosition(Long id, Positions updatedPosition) {
        return positionsRepository.findById(id).map(position -> {
            position.setPositionName(updatedPosition.getPositionName());
            position.setPositionDescription(updatedPosition.getPositionDescription());
            return positionsRepository.save(position);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Position not found"));
    }

    public boolean deletePosition(Long id) {
        if (positionsRepository.existsById(id)) {
            positionsRepository.deleteById(id);
            return true;
        }
        return false;
    }
}


