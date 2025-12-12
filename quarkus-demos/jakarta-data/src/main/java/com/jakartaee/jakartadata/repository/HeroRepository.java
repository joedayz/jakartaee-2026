package com.jakartaee.jakartadata.repository;

import com.jakartaee.common.entities.Hero;
import jakarta.data.repository.Find;
import jakarta.data.repository.Insert;
import jakarta.data.repository.Delete;
import jakarta.data.repository.Repository;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.processing.Pattern;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Jakarta Data para Heroes de DC Comics.
 * Usa anotaciones específicas de Jakarta Data como en la demo oficial.
 */
@Transactional
@Repository(dataStore = "DCHeroes")
public interface HeroRepository {

    @Find
    Optional<Hero> findById(Long id);

    @Find
    List<Hero> findByName(@Pattern String name);

    @Find
    List<Hero> findByIsActiveTrue();

    @Find
    List<Hero> findAll();

    @Insert
    void save(Hero hero);

    @Delete
    void deleteById(Long id);
}
