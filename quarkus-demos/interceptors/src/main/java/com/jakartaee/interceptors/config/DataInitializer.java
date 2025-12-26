package com.jakartaee.interceptors.config;

import com.jakartaee.common.entities.Hero;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

/**
 * Inicializador de datos que crea héroes de ejemplo.
 * Este bean será interceptado por @Monitored para demostrar @PostConstruct.
 */
@ApplicationScoped
@Monitored
public class DataInitializer {
    
    private static final Logger logger = Logger.getLogger(DataInitializer.class.getName());
    
    @Inject
    EntityManager entityManager;
    
    @Transactional
    void onStart(@Observes StartupEvent ev) {
        try {
            logger.info("Inicializando datos de ejemplo para Interceptors demo...");
            
            long count = entityManager.createQuery("SELECT COUNT(h) FROM Hero h", Long.class)
                .getSingleResult();
            
            if (count > 0) {
                logger.info(String.format("Ya existen %d héroes en la base de datos, omitiendo inicialización", count));
                return;
            }
            
            Hero superman = new Hero("Superman", "Super fuerza, vuelo, visión de rayos X", 95);
            superman.setDescription("El último hijo de Krypton");
            entityManager.persist(superman);
            
            Hero batman = new Hero("Batman", "Inteligencia superior, artes marciales", 85);
            batman.setDescription("El caballero oscuro de Gotham");
            entityManager.persist(batman);
            
            Hero wonderWoman = new Hero("Wonder Woman", "Super fuerza, vuelo, lazo de la verdad", 90);
            wonderWoman.setDescription("Princesa amazona");
            entityManager.persist(wonderWoman);
            
            Hero flash = new Hero("Flash", "Super velocidad", 88);
            flash.setDescription("El hombre más rápido del mundo");
            entityManager.persist(flash);
            
            Hero greenLantern = new Hero("Green Lantern", "Anillo de poder, creación de constructos", 87);
            greenLantern.setDescription("Portador del anillo de poder");
            entityManager.persist(greenLantern);
            
            Hero aquaman = new Hero("Aquaman", "Comunicación con vida marina, fuerza sobrehumana", 82);
            aquaman.setDescription("Rey de Atlantis");
            entityManager.persist(aquaman);
            
            entityManager.flush();
            
            logger.info("Datos de ejemplo inicializados correctamente para Interceptors demo");
        } catch (Exception e) {
            logger.severe("Error durante la inicialización de datos: " + e.getMessage());
        }
    }
}

