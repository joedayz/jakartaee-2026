package com.jakartaee.jakartadata.config;

import com.jakartaee.jakartadata.entity.HeroPanacheEntity;
import com.jakartaee.jakartadata.entity.VillainPanacheEntity;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import java.util.logging.Logger;

/**
 * Inicializador de datos para entidades Panache Next.
 * Demuestra el uso de Active Record Pattern para crear datos iniciales.
 */
@ApplicationScoped
public class PanacheNextDataInitializer {
    
    private static final Logger logger = Logger.getLogger(PanacheNextDataInitializer.class.getName());
    
    @Transactional
    void onStart(@Observes StartupEvent ev) {
        try {
            logger.info("Inicializando datos de ejemplo para Panache Next demo...");
            
            // Verificar si ya hay datos
            long heroCount = HeroPanacheEntity.count();
            long villainCount = VillainPanacheEntity.count();
            
            if (heroCount > 0 || villainCount > 0) {
                logger.info(String.format("Ya existen %d héroes y %d villanos Panache, omitiendo inicialización", 
                        heroCount, villainCount));
                return;
            }
            
            // Crear héroes usando Active Record Pattern
            HeroPanacheEntity superman = new HeroPanacheEntity();
            superman.name = "Superman";
            superman.power = "Super fuerza, vuelo, visión de rayos X";
            superman.powerLevel = 95;
            superman.description = "El último hijo de Krypton";
            superman.isActive = true;
            superman.persist(); // Active Record Pattern
            
            HeroPanacheEntity batman = new HeroPanacheEntity();
            batman.name = "Batman";
            batman.power = "Inteligencia superior, artes marciales";
            batman.powerLevel = 85;
            batman.description = "El caballero oscuro de Gotham";
            batman.isActive = true;
            batman.persist();
            
            HeroPanacheEntity wonderWoman = new HeroPanacheEntity();
            wonderWoman.name = "Wonder Woman";
            wonderWoman.power = "Super fuerza, vuelo, lazo de la verdad";
            wonderWoman.powerLevel = 90;
            wonderWoman.description = "Princesa amazona";
            wonderWoman.isActive = true;
            wonderWoman.persist();
            
            HeroPanacheEntity flash = new HeroPanacheEntity();
            flash.name = "Flash";
            flash.power = "Super velocidad";
            flash.powerLevel = 88;
            flash.description = "El hombre más rápido del mundo";
            flash.isActive = true;
            flash.persist();
            
            HeroPanacheEntity greenLantern = new HeroPanacheEntity();
            greenLantern.name = "Green Lantern";
            greenLantern.power = "Anillo de poder, creación de constructos";
            greenLantern.powerLevel = 87;
            greenLantern.description = "Portador del anillo de poder";
            greenLantern.isActive = true;
            greenLantern.persist();
            
            // Crear villanos usando Active Record Pattern
            VillainPanacheEntity joker = new VillainPanacheEntity();
            joker.name = "Joker";
            joker.power = "Caos, inteligencia criminal";
            joker.powerLevel = 80;
            joker.description = "El payaso príncipe del crimen";
            joker.threatLevel = "HIGH";
            joker.isActive = true;
            joker.persist();
            
            VillainPanacheEntity lexLuthor = new VillainPanacheEntity();
            lexLuthor.name = "Lex Luthor";
            lexLuthor.power = "Inteligencia, recursos";
            lexLuthor.powerLevel = 75;
            lexLuthor.description = "El genio criminal de Metropolis";
            lexLuthor.threatLevel = "HIGH";
            lexLuthor.isActive = true;
            lexLuthor.persist();
            
            VillainPanacheEntity darkseid = new VillainPanacheEntity();
            darkseid.name = "Darkseid";
            darkseid.power = "Super fuerza, rayos Omega, inmortalidad";
            darkseid.powerLevel = 98;
            darkseid.description = "El señor oscuro de Apokolips";
            darkseid.threatLevel = "CRITICAL";
            darkseid.isActive = true;
            darkseid.persist();
            
            VillainPanacheEntity deathstroke = new VillainPanacheEntity();
            deathstroke.name = "Deathstroke";
            deathstroke.power = "Fuerza sobrehumana, habilidades de combate";
            deathstroke.powerLevel = 82;
            deathstroke.description = "El asesino perfecto";
            deathstroke.threatLevel = "HIGH";
            deathstroke.isActive = true;
            deathstroke.persist();
            
            logger.info("Datos de ejemplo inicializados correctamente para Panache Next demo");
        } catch (Exception e) {
            logger.severe("Error al inicializar datos Panache Next: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

