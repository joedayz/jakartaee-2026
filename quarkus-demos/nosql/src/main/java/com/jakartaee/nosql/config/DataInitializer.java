package com.jakartaee.nosql.config;

import com.jakartaee.nosql.entity.HeroMongo;
import com.jakartaee.nosql.entity.VillainMongo;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import java.time.LocalDateTime;
import java.util.logging.Logger;

/**
 * Inicializador de datos para la demo de NoSQL.
 * Crea héroes y villanos de ejemplo con estructuras complejas de MongoDB:
 * - Documentos anidados (location, secretBase)
 * - Arrays de valores (abilities, allies)
 * - Arrays de documentos anidados (missions)
 */
@ApplicationScoped
public class DataInitializer {
    
    private static final Logger logger = Logger.getLogger(DataInitializer.class.getName());
    
    void onStart(@Observes StartupEvent ev) {
        try {
            logger.info("Inicializando datos de ejemplo para NoSQL demo...");
            
            // Verificar si ya hay datos
            long heroCount = HeroMongo.count();
            long villainCount = VillainMongo.count();
            
            if (heroCount > 0 || villainCount > 0) {
                logger.info(String.format("Ya existen %d héroes y %d villanos, omitiendo inicialización", 
                        heroCount, villainCount));
                return;
            }
            
            // Crear Superman con estructura compleja
            HeroMongo superman = new HeroMongo("Superman", "Super fuerza, vuelo, visión de rayos X", 95);
            superman.description = "El último hijo de Krypton";
            superman.addAbility("Super strength");
            superman.addAbility("Flight");
            superman.addAbility("Heat vision");
            superman.addAbility("X-ray vision");
            
            // Ubicación anidada
            HeroMongo.Location location = new HeroMongo.Location();
            location.city = "Metropolis";
            location.planet = "Earth";
            HeroMongo.Location.Coordinates coords = new HeroMongo.Location.Coordinates();
            coords.latitude = 40.7128;
            coords.longitude = -74.0060;
            location.coordinates = coords;
            superman.location = location;
            
            // Misiones anidadas
            HeroMongo.Mission mission1 = new HeroMongo.Mission();
            mission1.title = "Salvar Metropolis";
            mission1.description = "Detener a Lex Luthor";
            mission1.status = "COMPLETED";
            mission1.createdAt = LocalDateTime.now().minusDays(5);
            mission1.completedAt = LocalDateTime.now().minusDays(4);
            superman.addMission(mission1);
            
            HeroMongo.Mission mission2 = new HeroMongo.Mission();
            mission2.title = "Detener a Doomsday";
            mission2.description = "Prevenir destrucción masiva";
            mission2.status = "PENDING";
            mission2.createdAt = LocalDateTime.now().minusDays(1);
            superman.addMission(mission2);
            
            superman.persist();
            
            // Crear Batman
            HeroMongo batman = new HeroMongo("Batman", "Inteligencia superior, artes marciales", 85);
            batman.description = "El caballero oscuro de Gotham";
            batman.addAbility("Intelligence");
            batman.addAbility("Martial arts");
            batman.addAbility("Technology");
            
            HeroMongo.Location batmanLocation = new HeroMongo.Location();
            batmanLocation.city = "Gotham City";
            batmanLocation.planet = "Earth";
            HeroMongo.Location.Coordinates batmanCoords = new HeroMongo.Location.Coordinates();
            batmanCoords.latitude = 40.7589;
            batmanCoords.longitude = -73.9851;
            batmanLocation.coordinates = batmanCoords;
            batman.location = batmanLocation;
            
            batman.persist();
            
            // Crear Wonder Woman
            HeroMongo wonderWoman = new HeroMongo("Wonder Woman", "Super fuerza, vuelo, lazo de la verdad", 90);
            wonderWoman.description = "Princesa amazona";
            wonderWoman.addAbility("Super strength");
            wonderWoman.addAbility("Flight");
            wonderWoman.addAbility("Lasso of truth");
            
            HeroMongo.Location wwLocation = new HeroMongo.Location();
            wwLocation.city = "Themyscira";
            wwLocation.planet = "Earth";
            wonderWoman.location = wwLocation;
            
            wonderWoman.persist();
            
            // Crear Flash
            HeroMongo flash = new HeroMongo("Flash", "Super velocidad", 88);
            flash.description = "El hombre más rápido del mundo";
            flash.addAbility("Super speed");
            flash.addAbility("Time travel");
            
            HeroMongo.Location flashLocation = new HeroMongo.Location();
            flashLocation.city = "Central City";
            flashLocation.planet = "Earth";
            flash.location = flashLocation;
            
            flash.persist();
            
            // Crear villanos
            VillainMongo joker = new VillainMongo("Joker", "Caos, inteligencia criminal", 80);
            joker.description = "El payaso príncipe del crimen";
            joker.threatLevel = "HIGH";
            joker.addAbility("Chaos");
            joker.addAbility("Intelligence");
            joker.addAlly("Harley Quinn");
            joker.addAlly("Penguin");
            
            VillainMongo.SecretBase jokerBase = new VillainMongo.SecretBase();
            jokerBase.name = "Abandoned Amusement Park";
            jokerBase.location = "Gotham City";
            VillainMongo.SecretBase.Coordinates jokerCoords = new VillainMongo.SecretBase.Coordinates();
            jokerCoords.latitude = 40.7580;
            jokerCoords.longitude = -73.9855;
            jokerBase.coordinates = jokerCoords;
            jokerBase.features.add("Tunnels");
            jokerBase.features.add("Traps");
            joker.secretBase = jokerBase;
            
            joker.persist();
            
            // Crear Darkseid
            VillainMongo darkseid = new VillainMongo("Darkseid", "Super fuerza, rayos Omega, inmortalidad", 98);
            darkseid.description = "El señor oscuro de Apokolips";
            darkseid.threatLevel = "CRITICAL";
            darkseid.addAbility("Omega beams");
            darkseid.addAbility("Immortality");
            darkseid.addAbility("Super strength");
            
            VillainMongo.SecretBase darkseidBase = new VillainMongo.SecretBase();
            darkseidBase.name = "Apokolips";
            darkseidBase.location = "Apokolips Planet";
            darkseidBase.features.add("Fire pits");
            darkseidBase.features.add("Prison camps");
            darkseid.secretBase = darkseidBase;
            
            darkseid.persist();
            
            logger.info("Datos de ejemplo inicializados correctamente para NoSQL demo");
            logger.info(String.format("Creados %d héroes y %d villanos con estructuras complejas de MongoDB", 
                    HeroMongo.count(), VillainMongo.count()));
        } catch (Exception e) {
            logger.severe("Error al inicializar datos NoSQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

