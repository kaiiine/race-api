package com.takima.race.runner.services;

import com.takima.race.runner.entities.Race;
import com.takima.race.runner.entities.Registration;
import com.takima.race.runner.repositories.RegistrationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final RunnerService runnerService;
    private final RaceService racesService;

    public RegistrationService(RegistrationRepository registrationRepository, RunnerService runnerService, RaceService racesService) {
        this.registrationRepository = registrationRepository;
        this.runnerService = runnerService;
        this.racesService = racesService;
    }

    public List<Registration> getAll(){
        return registrationRepository.findAll();
    }

    public List<Registration> getRegistrationByRace(Long id_race){
        return registrationRepository.getRegistrationByRace_Id(id_race);
    }

    public List<Registration> getRegistrationByRunner(Long id_runner){
        return registrationRepository.getRegistrationByRunner_Id(id_runner);
    }

    public List<Race> getRacesByRunner(Long id_runner) {
        return registrationRepository.getRacesByRunner(id_runner);
    }

    public Registration getRegistrationById(Long id){
        return registrationRepository.getRegistrationById(id);
    }

    public Registration create(Long id_race, Long id_runner){
        Registration register = new Registration();
        register.setRace(racesService.getRacesById(id_race));
        register.setRunner(runnerService.getById(id_runner));
        register.setRegistrationDate(LocalDate.now());
        return registrationRepository.save(register);
    }

    public void deleteRegistrationByRunner(Long id_runner, Long id_race){
        registrationRepository.deleteRegistrationByRunnerAndRace(id_runner, id_race);
    }


}
