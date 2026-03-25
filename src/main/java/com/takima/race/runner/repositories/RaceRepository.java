package com.takima.race.runner.repositories;

import com.takima.race.runner.entities.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RaceRepository extends JpaRepository<Race, Long> {
    Race findRacesById(Long id);
    void deleteRacesById(Long id);
    @Query("SELECT COUNT(r) FROM Registration r WHERE r.race.id=:id_race")
    Long CountRegistrationByRace(@Param("id_race") Long id_race);
    List<Race> findByLocation(String location);
}
