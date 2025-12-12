package com.jakartaee.panache.config;

import com.jakartaee.panache.entity.HeroEntity;
import com.jakartaee.panache.entity.VillainEntity;
import com.jakartaee.panache.repository.HeroRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

/**
 * Inicializador de datos que crea algunos héroes y villanos de ejemplo al iniciar la aplicación.
 * Demuestra el uso de ambos patrones: Repository y Active Record.
 */
@ApplicationScoped
public class DataInitializer {
    
    private static final Logger logger = Logger.getLogger(DataInitializer.class.getName());
    
    @Inject
    HeroRepository heroRepository;
    
    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        logger.info("=== Inicializando datos de ejemplo ===");
        
        // Verificar si ya hay datos
        if (heroRepository.count() > 0 || VillainEntity.count() > 0) {
            logger.info("Ya existen datos en la base de datos, omitiendo inicialización");
            return;
        }
        
        // Crear héroes usando Repository Pattern
        logger.info("Creando héroes usando Repository Pattern...");
        HeroEntity superman = new HeroEntity("Superman", "Super fuerza, vuelo, visión de rayos X", 95);
        superman.description = "El último hijo de Krypton";
        heroRepository.persist(superman);
        
        HeroEntity batman = new HeroEntity("Batman", "Inteligencia superior, artes marciales", 85);
        batman.description = "El caballero oscuro de Gotham";
        heroRepository.persist(batman);
        
        HeroEntity wonderWoman = new HeroEntity("Wonder Woman", "Super fuerza, vuelo, lazo de la verdad", 90);
        wonderWoman.description = "Princesa amazona";
        heroRepository.persist(wonderWoman);
        
        HeroEntity flash = new HeroEntity("Flash", "Super velocidad", 88);
        flash.description = "El hombre más rápido del mundo";
        heroRepository.persist(flash);
        
        HeroEntity greenLantern = new HeroEntity("Green Lantern", "Anillo de poder", 87);
        greenLantern.description = "Portador del anillo de poder verde";
        heroRepository.persist(greenLantern);
        
        // Crear villanos usando Active Record Pattern
        logger.info("Creando villanos usando Active Record Pattern...");
        VillainEntity joker = new VillainEntity("Joker", "Caos, inteligencia criminal", 80);
        joker.description = "El payaso príncipe del crimen";
        joker.threatLevel = "HIGH";
        joker.persist();
        
        VillainEntity lexLuthor = new VillainEntity("Lex Luthor", "Inteligencia, recursos", 75);
        lexLuthor.description = "El genio criminal de Metropolis";
        lexLuthor.threatLevel = "HIGH";
        lexLuthor.persist();
        
        VillainEntity darkseid = new VillainEntity("Darkseid", "Omega beams, inmortalidad", 98);
        darkseid.description = "El señor oscuro de Apokolips";
        darkseid.threatLevel = "CRITICAL";
        darkseid.persist();
        
        VillainEntity sinestro = new VillainEntity("Sinestro", "Anillo de poder amarillo", 85);
        sinestro.description = "El portador del anillo amarillo";
        sinestro.threatLevel = "HIGH";
        sinestro.persist();
        
        VillainEntity brainiac = new VillainEntity("Brainiac", "Inteligencia artificial superior", 90);
        brainiac.description = "El colector de mundos";
        brainiac.threatLevel = "CRITICAL";
        brainiac.persist();
        
        logger.info(String.format("Datos inicializados: %d héroes (Repository Pattern), %d villanos (Active Record Pattern)",
            heroRepository.count(), VillainEntity.count()));
    }
}

