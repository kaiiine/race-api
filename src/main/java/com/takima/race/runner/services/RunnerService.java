package com.takima.race.runner.services;

import com.takima.race.runner.entities.Runner;
import com.takima.race.runner.repositories.RunnerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RunnerService {

    private final RunnerRepository runnerRepository;

    public RunnerService(RunnerRepository runnerRepository) {
        this.runnerRepository = runnerRepository;
    }

    public List<Runner> getAll() {
        return runnerRepository.findAll();
    }

    public Runner getById(Long id) {
        return runnerRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Runner %s not found", id)
                )
        );
    }

    public Runner create(Runner runner){
        return runnerRepository.save(runner);
    }

    public void delete(Long id){
        runnerRepository.deleteById(id);
    }

    public Runner update(long id, Runner runner){
        Runner oldRunner = getById(id);
        oldRunner.setLastName(runner.getLastName());
        oldRunner.setFirstName(runner.getFirstName());
        oldRunner.setEmail(runner.getEmail());
        oldRunner.setAge(runner.getAge());
        return runnerRepository.save(oldRunner);
    }
}
