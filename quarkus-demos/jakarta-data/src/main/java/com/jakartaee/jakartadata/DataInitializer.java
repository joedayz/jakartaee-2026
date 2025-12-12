package com.jakartaee.jakartadata;

import com.jakartaee.common.entities.Hero;
import com.jakartaee.common.entities.Villain;
import com.jakartaee.jakartadata.repository.HeroRepository;
import com.jakartaee.jakartadata.repository.VillainRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

/**
 * Inicializador de datos de ejemplo.
 * Crea algunos Heroes y Villains de DC Comics al iniciar la aplicación.
 */
@ApplicationScoped
public class DataInitializer {
    
    @Inject
    HeroRepository heroRepository;
    
    @Inject
    VillainRepository villainRepository;
    
    @PostConstruct
    @Transactional
    public void init() {
        // Solo inicializar si no hay datos
        List<Hero> heroes = heroRepository.findAll();
        if (heroes.isEmpty()) {
            initializeHeroes();
        }
        List<Villain> villains = villainRepository.findAll();
        if (villains.isEmpty()) {
            initializeVillains();
        }
    }
    
    private void initializeHeroes() {
        heroRepository.save(new Hero("Superman", "Super fuerza, vuelo, visión de rayos X", 95));
        heroRepository.save(new Hero("Batman", "Inteligencia, artes marciales, tecnología", 85));
        heroRepository.save(new Hero("Wonder Woman", "Super fuerza, vuelo, lazo de la verdad", 90));
        heroRepository.save(new Hero("Flash", "Super velocidad", 88));
        heroRepository.save(new Hero("Green Lantern", "Anillo de poder", 87));
        heroRepository.save(new Hero("Aquaman", "Control del agua, comunicación marina", 82));
        heroRepository.save(new Hero("Cyborg", "Tecnología avanzada, interfaz cibernética", 80));
        heroRepository.save(new Hero("Martian Manhunter", "Cambio de forma, telepatía", 92));
    }
    
    private void initializeVillains() {
        villainRepository.save(new Villain("Joker", "Caos, inteligencia criminal", 80));
        villainRepository.save(new Villain("Lex Luthor", "Inteligencia, recursos", 75));
        villainRepository.save(new Villain("Darkseid", "Omega beams, inmortalidad", 98));
        villainRepository.save(new Villain("Sinestro", "Anillo de poder amarillo", 85));
        villainRepository.save(new Villain("Brainiac", "Inteligencia artificial superior", 90));
        villainRepository.save(new Villain("Doomsday", "Fuerza destructiva, adaptación", 95));
        villainRepository.save(new Villain("Bane", "Fuerza sobrehumana, estrategia", 78));
        
        // Establecer niveles de amenaza usando búsqueda por nombre exacto
        List<Villain> darkseid = villainRepository.findByName("Darkseid");
        if (!darkseid.isEmpty()) {
            Villain v = darkseid.get(0);
            v.setThreatLevel("CRITICAL");
            villainRepository.save(v);
        }
        
        List<Villain> doomsday = villainRepository.findByName("Doomsday");
        if (!doomsday.isEmpty()) {
            Villain v = doomsday.get(0);
            v.setThreatLevel("CRITICAL");
            villainRepository.save(v);
        }
        
        List<Villain> brainiac = villainRepository.findByName("Brainiac");
        if (!brainiac.isEmpty()) {
            Villain v = brainiac.get(0);
            v.setThreatLevel("HIGH");
            villainRepository.save(v);
        }
        
        List<Villain> sinestro = villainRepository.findByName("Sinestro");
        if (!sinestro.isEmpty()) {
            Villain v = sinestro.get(0);
            v.setThreatLevel("HIGH");
            villainRepository.save(v);
        }
    }
}
