package com.takima.race.runner.controllers;

import com.takima.race.runner.entities.Registration;
import com.takima.race.runner.services.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registration")
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public List<Registration> getAll(){
        return null;
    }

    @GetMapping("/runners/{id_runner}")
    public List<Registration> getRegistrationByRunner(@PathVariable Long id_runner){
        return registrationService.getRegistrationByRunner(id_runner);
    }

    @GetMapping("/races/{id_race}")
    public List<Registration> getRegistrationByRace(@PathVariable Long id_race){
        return registrationService.getRegistrationByRace(id_race);
    }

    @PostMapping("/{id_race}")
    @ResponseStatus(HttpStatus.CREATED)
    public Registration postRegistration(@PathVariable Long id_race, @RequestBody Long id_runner){
        return registrationService.create(id_race, id_runner);
    }

    @DeleteMapping("/{id_race}/{id_runner}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRegistration(@PathVariable Long id_race, @PathVariable Long id_runner){
        registrationService.deleteRegistrationByRunner(id_runner, id_race);
    }


}
