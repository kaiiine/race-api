package com.takima.race.runner.controllers;

import com.takima.race.runner.entities.Race;
import com.takima.race.runner.services.RaceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/races")
public class RaceController {
    private final RaceService racesService;

    public RaceController(RaceService racesService) {
        this.racesService = racesService;
    }

    @GetMapping
    public List<Race> getAll(@RequestParam(required = false) String location){
        return racesService.getAll(location);
    }

    @GetMapping("/{id}")
    public Race getRacesById(@PathVariable Long id){
        return racesService.getRacesById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Race postRaces(@RequestBody Race race){
        return racesService.create(race);
    }

    @PutMapping("/{id}")
    public Race updateRaces(@PathVariable Long id, @RequestBody Race race){
        return racesService.update(id, race);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRaces(@PathVariable Long id){
        racesService.delete(id);
    }


}
