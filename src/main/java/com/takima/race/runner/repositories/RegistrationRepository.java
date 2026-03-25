package com.takima.race.runner.repositories;

import com.takima.race.runner.entities.Race;
import com.takima.race.runner.entities.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    Registration getRegistrationById(Long id);
    List<Registration> getRegistrationByRace_Id(Long id_race);
    List<Registration> getRegistrationByRunner_Id(Long id_runner);
    void deleteRegistrationByRunnerAndRace(Long id_runner, Long id_race);
    @Query("SELECT COUNT(r) FROM Registration r WHERE r.race.id=:id_race")  // J'ai essayé d'appliquer les Query pour apprendre à les utiliser comme vous me l'aviez expliqué
    Long CountRegistrationByRace(@Param("id_race") Long id_race);
    @Query("SELECT r.race FROM Registration r WHERE r.runner.id = :id_runner")
    List<Race> getRacesByRunner(@Param("id_runner") Long id_runner);
}
