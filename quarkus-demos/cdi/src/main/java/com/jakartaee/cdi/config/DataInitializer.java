package com.jakartaee.cdi.config;

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
 * Inicializador de datos que crea algunos héroes y villanos de ejemplo al iniciar la aplicación.
 * Demuestra el uso de @Observes para StartupEvent.
 */
@ApplicationScoped
public class DataInitializer {
    
    private static final Logger logger = Logger.getLogger(DataInitializer.class.getName());
    
    @Inject
    EntityManager entityManager;
    
    @Transactional
    void onStart(@Observes StartupEvent ev) {
        try {
            logger.info("Inicializando datos de ejemplo para CDI demo...");
            
            // Verificar si ya hay datos
            long heroCount = entityManager.createQuery("SELECT COUNT(h) FROM Hero h", Long.class)
                .getSingleResult();
            long villainCount = entityManager.createQuery("SELECT COUNT(v) FROM Villain v", Long.class)
                .getSingleResult();
            
            if (heroCount > 0 || villainCount > 0) {
                logger.info(String.format("Ya existen %d héroes y %d villanos, omitiendo inicialización", 
                        heroCount, villainCount));
                return;
            }
            
            // Crear héroes de ejemplo
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
            
            // Crear villanos de ejemplo
            Villain joker = new Villain("Joker", "Caos, inteligencia criminal", 80);
            joker.setDescription("El payaso príncipe del crimen");
            joker.setThreatLevel("HIGH");
            entityManager.persist(joker);
            
            Villain lexLuthor = new Villain("Lex Luthor", "Inteligencia, recursos", 75);
            lexLuthor.setDescription("El genio criminal de Metropolis");
            lexLuthor.setThreatLevel("HIGH");
            entityManager.persist(lexLuthor);
            
            Villain darkseid = new Villain("Darkseid", "Super fuerza, rayos Omega, inmortalidad", 98);
            darkseid.setDescription("El señor oscuro de Apokolips");
            darkseid.setThreatLevel("CRITICAL");
            entityManager.persist(darkseid);
            
            Villain deathstroke = new Villain("Deathstroke", "Fuerza sobrehumana, habilidades de combate", 82);
            deathstroke.setDescription("El asesino perfecto");
            deathstroke.setThreatLevel("HIGH");
            entityManager.persist(deathstroke);
            
            entityManager.flush();
            
            logger.info("Datos de ejemplo inicializados correctamente para CDI demo");
        } catch (Exception e) {
            logger.severe("Error al inicializar datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

