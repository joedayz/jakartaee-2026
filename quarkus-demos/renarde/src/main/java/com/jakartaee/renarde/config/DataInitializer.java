package com.jakartaee.renarde.config;

import com.jakartaee.common.entities.Hero;
import com.jakartaee.common.entities.Villain;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

/**
 * Inicializador de datos que crea héroes y villanos de ejemplo.
 */
@ApplicationScoped
public class DataInitializer {
    
    private static final Logger logger = Logger.getLogger(DataInitializer.class.getName());
    
    @Inject
    EntityManager entityManager;
    
    @Transactional
    void onStart(@Observes StartupEvent ev) {
        try {
            logger.info("Inicializando datos de ejemplo para Renarde demo...");
            
            long heroCount = entityManager.createQuery("SELECT COUNT(h) FROM Hero h", Long.class)
                .getSingleResult();
            long villainCount = entityManager.createQuery("SELECT COUNT(v) FROM Villain v", Long.class)
                .getSingleResult();
            
            if (heroCount > 0 || villainCount > 0) {
                logger.info(String.format("Ya existen %d héroes y %d villanos, omitiendo inicialización", 
                        heroCount, villainCount));
                return;
            }
            
            // Crear héroes
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
            
            // Crear villanos
            Villain joker = new Villain("Joker", "Caos, inteligencia criminal", 80);
            joker.setDescription("El payaso príncipe del crimen");
            entityManager.persist(joker);
            
            Villain lexLuthor = new Villain("Lex Luthor", "Inteligencia superior, recursos ilimitados", 88);
            lexLuthor.setDescription("El genio malvado de Metropolis");
            entityManager.persist(lexLuthor);
            
            entityManager.flush();
            
            logger.info("Datos de ejemplo inicializados correctamente para Renarde demo");
        } catch (Exception e) {
            logger.severe("Error durante la inicialización de datos: " + e.getMessage());
        }
    }
}

