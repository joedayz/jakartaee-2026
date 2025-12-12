package com.jakartaee.jakartadata.repository;

import com.jakartaee.common.entities.Villain;
import jakarta.data.repository.Find;
import jakarta.data.repository.Insert;
import jakarta.data.repository.Delete;
import jakarta.data.repository.Repository;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.processing.Pattern;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Jakarta Data para Villains de DC Comics.
 * Usa anotaciones específicas de Jakarta Data como en la demo oficial.
 */
@Transactional
@Repository(dataStore = "DCHeroes")
public interface VillainRepository {

    @Find
    Optional<Villain> findById(Long id);

    @Find
    List<Villain> findByName(@Pattern String name);

    @Find
    List<Villain> findByThreatLevel(String threatLevel);

    @Find
    List<Villain> findAll();

    @Insert
    void save(Villain villain);

    @Delete
    void deleteById(Long id);
}
