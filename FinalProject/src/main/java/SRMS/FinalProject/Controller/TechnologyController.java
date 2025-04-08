package SRMS.FinalProject.Controller;

import SRMS.FinalProject.Service.TechnologyService;
import SRMS.FinalProject.UserEntity.Technology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technologies")
public class TechnologyController {

    @Autowired
    private TechnologyService technologyService;

    @PostMapping
    public ResponseEntity<Technology> createTechnology(@RequestBody Technology technology) {
        return ResponseEntity.ok(technologyService.addTechnology(technology));
    }

    @GetMapping
    public ResponseEntity<List<Technology>> getAllTechnologies() {
        return ResponseEntity.ok(technologyService.getAllTechnologies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Technology> getTechnologyById(@PathVariable Long id) {
        return technologyService.getTechnologyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Technology> updateTechnology(@PathVariable Long id, @RequestBody Technology technology) {
        return ResponseEntity.ok(technologyService.updateTechnology(id, technology));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnology(@PathVariable Long id) {
        technologyService.deleteTechnology(id);
        return ResponseEntity.noContent().build();
    }
}
