package com.takima.race.runner.services;

import com.takima.race.runner.entities.Race;
import com.takima.race.runner.repositories.RaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RaceService {
    private final RaceRepository racesRepository;

    public RaceService(RaceRepository racesRepository) {
        this.racesRepository = racesRepository;
    }

    public List<Race> getAll(String location){
        if (location != null && !location.isBlank()) {
            return racesRepository.findByLocation(location);
        }

        return racesRepository.findAll();
    }

    public Race getRacesById(Long id){
        return racesRepository.findRacesById(id);
    }

    public Race create(Race races){
        return racesRepository.save(races);
    }

    public Race update(Long id, Race race){
        Race oldRace = getRacesById(id);
        oldRace.setDate(race.getDate());
        oldRace.setLocation(race.getLocation());
        oldRace.setName(race.getName());
        oldRace.setMaxParticipants(race.getMaxParticipants());
        return racesRepository.save(oldRace);
    }

    public void delete(Long id){
        racesRepository.deleteRacesById(id);
    }
}
