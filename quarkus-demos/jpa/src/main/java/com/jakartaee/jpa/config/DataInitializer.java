package com.jakartaee.jpa.config;

import com.jakartaee.jpa.entity.HeroJPA;
import com.jakartaee.jpa.entity.Mission;
import com.jakartaee.jpa.entity.Mission.MissionStatus;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.logging.Logger;

/**
 * Inicializador de datos para la demo de JPA.
 * Crea héroes y misiones de ejemplo al iniciar la aplicación.
 * Demuestra el uso de EntityManager y relaciones JPA.
 */
@ApplicationScoped
public class DataInitializer {
    
    private static final Logger logger = Logger.getLogger(DataInitializer.class.getName());
    
    @Inject
    EntityManager entityManager;
    
    @Transactional
    void onStart(@Observes StartupEvent ev) {
        try {
            logger.info("Inicializando datos de ejemplo para JPA demo...");
            
            // Verificar si ya hay datos
            Long heroCount = entityManager.createQuery("SELECT COUNT(h) FROM HeroJPA h", Long.class)
                    .getSingleResult();
            
            if (heroCount > 0) {
                logger.info(String.format("Ya existen %d héroes, omitiendo inicialización", heroCount));
                return;
            }
            
            // Crear héroes usando EntityManager.persist()
            HeroJPA superman = new HeroJPA("Superman", "Super fuerza, vuelo, visión de rayos X", 95);
            superman.setDescription("El último hijo de Krypton");
            entityManager.persist(superman);
            
            HeroJPA batman = new HeroJPA("Batman", "Inteligencia superior, artes marciales", 85);
            batman.setDescription("El caballero oscuro de Gotham");
            entityManager.persist(batman);
            
            HeroJPA wonderWoman = new HeroJPA("Wonder Woman", "Super fuerza, vuelo, lazo de la verdad", 90);
            wonderWoman.setDescription("Princesa amazona");
            entityManager.persist(wonderWoman);
            
            HeroJPA flash = new HeroJPA("Flash", "Super velocidad", 88);
            flash.setDescription("El hombre más rápido del mundo");
            entityManager.persist(flash);
            
            HeroJPA greenLantern = new HeroJPA("Green Lantern", "Anillo de poder, creación de constructos", 87);
            greenLantern.setDescription("Portador del anillo de poder");
            entityManager.persist(greenLantern);
            
            // Forzar sincronización con la BD
            entityManager.flush();
            
            // Crear misiones para los héroes (demostrando relaciones)
            Mission mission1 = new Mission("Salvar Metropolis", "Detener a Lex Luthor", superman);
            mission1.setStatus(MissionStatus.COMPLETED);
            mission1.setCompleted(true);
            superman.addMission(mission1);
            entityManager.persist(mission1);
            
            Mission mission2 = new Mission("Detener el Joker", "Prevenir el caos en Gotham", batman);
            mission2.setStatus(MissionStatus.IN_PROGRESS);
            batman.addMission(mission2);
            entityManager.persist(mission2);
            
            Mission mission3 = new Mission("Proteger Themyscira", "Defender la isla de invasores", wonderWoman);
            mission3.setStatus(MissionStatus.COMPLETED);
            mission3.setCompleted(true);
            wonderWoman.addMission(mission3);
            entityManager.persist(mission3);
            
            Mission mission4 = new Mission("Detener Reverse Flash", "Prevenir alteración de la línea temporal", flash);
            mission4.setStatus(MissionStatus.PENDING);
            flash.addMission(mission4);
            entityManager.persist(mission4);
            
            Mission mission5 = new Mission("Proteger el Sector 2814", "Defender el sector asignado", greenLantern);
            mission5.setStatus(MissionStatus.IN_PROGRESS);
            greenLantern.addMission(mission5);
            entityManager.persist(mission5);
            
            // Otra misión para Superman
            Mission mission6 = new Mission("Detener a Doomsday", "Prevenir destrucción masiva", superman);
            mission6.setStatus(MissionStatus.COMPLETED);
            mission6.setCompleted(true);
            superman.addMission(mission6);
            entityManager.persist(mission6);
            
            entityManager.flush();
            
            logger.info("Datos de ejemplo inicializados correctamente para JPA demo");
            logger.info(String.format("Creados %d héroes y %d misiones", 5, 6));
        } catch (Exception e) {
            logger.severe("Error al inicializar datos JPA: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

