package com.clientbank.max.resources;

import com.clientbank.max.entities.Employer;
import com.clientbank.max.services.EmployerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employers")
public class EmployerController {

    private final EmployerService employerService;


    @GetMapping
    public List<Employer> findAll() {
        return employerService.findAll();
    }

    @GetMapping("/{id}")
    public Employer getOne (@PathVariable (name = "id") Long id) {
        return employerService.getOne(id);
    }

    @PostMapping
    public Employer save(@RequestBody Employer employer) {
        return employerService.save(employer);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable (name = "id") Long id) {
        return employerService.deleteById(id);
    }
}
