package com.takima.race.runner.controllers;

import com.takima.race.runner.entities.Race;
import com.takima.race.runner.entities.Runner;
import com.takima.race.runner.services.RegistrationService;
import com.takima.race.runner.services.RunnerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/runners")
public class RunnerController {
    private final RunnerService runnerService;
    private final RegistrationService registrationService;

    public RunnerController(RunnerService runnerService, RegistrationService registrationService) {
        this.runnerService = runnerService;
        this.registrationService = registrationService;
    }

    @GetMapping
    public List<Runner> getAll() {
        return runnerService.getAll();
    }

    @GetMapping("/{id}")
    public Runner getById(@PathVariable Long id) {
        return runnerService.getById(id);
    }

    @GetMapping("/{id}/races")
    public List<Race> getRacesByRunner(@PathVariable Long id) {
        return registrationService.getRacesByRunner(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Runner create(@RequestBody Runner runner){return runnerService.create(runner);}

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){ runnerService.delete(id);}

    @PutMapping("/{id}")
    public Runner update(@PathVariable Long id, @RequestBody Runner runner){return runnerService.update(id, runner);}

}

