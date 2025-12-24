package com.jakartaee.transactions.service;

import com.jakartaee.common.entities.Hero;
import com.jakartaee.common.entities.Villain;
import com.jakartaee.transactions.entity.Battle;
import com.jakartaee.transactions.entity.PowerTransfer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Servicio que demuestra diferentes características de transacciones:
 * - @Transactional básico
 * - Tipos de transacciones (REQUIRED, REQUIRES_NEW, etc.)
 * - Rollback automático y manual
 * - Transacciones con múltiples operaciones
 * - Manejo de excepciones
 * - Timeout de transacciones
 */
@ApplicationScoped
public class TransactionDemoService {
    
    private static final Logger logger = Logger.getLogger(TransactionDemoService.class.getName());
    
    @Inject
    EntityManager entityManager;
    
    @Inject
    TransactionManager transactionManager;
    
    /**
     * Transacción básica con @Transactional.
     * REQUIRED es el valor por defecto.
     */
    @Transactional
    public Hero createHero(String name, String power, Integer powerLevel) {
        Hero hero = new Hero(name, power, powerLevel);
        entityManager.persist(hero);
        entityManager.flush();
        return hero;
    }
    
    /**
     * Transacción REQUIRED (por defecto).
     * Si ya existe una transacción, la usa. Si no, crea una nueva.
     */
    @Transactional(Transactional.TxType.REQUIRED)
    public Hero createHeroRequired(String name, String power, Integer powerLevel) {
        Hero hero = new Hero(name, power, powerLevel);
        entityManager.persist(hero);
        return hero;
    }
    
    /**
     * Transacción REQUIRES_NEW.
     * Siempre crea una nueva transacción, incluso si ya existe una.
     * Útil para operaciones que deben ejecutarse independientemente.
     */
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void logOperation(String message) {
        // Esta operación siempre se ejecuta en una nueva transacción
        // Incluso si se llama desde otra transacción
        logger.info("Logging operation: " + message);
    }
    
    /**
     * Transacción MANDATORY.
     * Requiere que exista una transacción activa.
     * Lanza excepción si no hay transacción.
     */
    @Transactional(Transactional.TxType.MANDATORY)
    public void updateHeroPower(Long heroId, Integer newPowerLevel) {
        Hero hero = entityManager.find(Hero.class, heroId);
        if (hero != null) {
            hero.setPowerLevel(newPowerLevel);
            entityManager.merge(hero);
        }
    }
    
    /**
     * Transacción SUPPORTS.
     * Si hay transacción, la usa. Si no, ejecuta sin transacción.
     */
    @Transactional(Transactional.TxType.SUPPORTS)
    public Hero findHero(Long id) {
        return entityManager.find(Hero.class, id);
    }
    
    /**
     * Transacción NOT_SUPPORTED.
     * Suspende cualquier transacción existente y ejecuta sin transacción.
     */
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public String readOnlyOperation(Long heroId) {
        Hero hero = entityManager.find(Hero.class, heroId);
        return hero != null ? hero.getName() : "Not found";
    }
    
    /**
     * Transacción NEVER.
     * Lanza excepción si hay una transacción activa.
     */
    @Transactional(Transactional.TxType.NEVER)
    public String nonTransactionalOperation(Long heroId) {
        // Esta operación no debe ejecutarse dentro de una transacción
        Hero hero = entityManager.find(Hero.class, heroId);
        return hero != null ? hero.getName() : "Not found";
    }
    
    /**
     * Transferencia de poder entre héroes.
     * Demuestra transacción con múltiples operaciones que deben ser atómicas.
     */
    @Transactional
    public PowerTransfer transferPower(Long fromHeroId, Long toHeroId, Integer amount) {
        // 1. Validar que ambos héroes existen
        Hero fromHero = entityManager.find(Hero.class, fromHeroId);
        Hero toHero = entityManager.find(Hero.class, toHeroId);
        
        if (fromHero == null || toHero == null) {
            throw new IllegalArgumentException("One or both heroes not found");
        }
        
        // 2. Validar que el héroe origen tiene suficiente poder
        if (fromHero.getPowerLevel() < amount) {
            throw new IllegalArgumentException("Insufficient power level");
        }
        
        // 3. Crear registro de transferencia
        PowerTransfer transfer = new PowerTransfer(fromHeroId, toHeroId, amount);
        entityManager.persist(transfer);
        
        // 4. Actualizar niveles de poder (operaciones atómicas)
        fromHero.setPowerLevel(fromHero.getPowerLevel() - amount);
        toHero.setPowerLevel(toHero.getPowerLevel() + amount);
        
        entityManager.merge(fromHero);
        entityManager.merge(toHero);
        
        // 5. Marcar transferencia como completada
        transfer.setStatus(PowerTransfer.TransferStatus.COMPLETED);
        transfer.setCompletedAt(LocalDateTime.now());
        
        entityManager.flush();
        
        return transfer;
    }
    
    /**
     * Transferencia de poder que falla y hace rollback automático.
     * Demuestra rollback automático cuando se lanza una excepción.
     */
    @Transactional
    public PowerTransfer transferPowerWithFailure(Long fromHeroId, Long toHeroId, Integer amount) {
        Hero fromHero = entityManager.find(Hero.class, fromHeroId);
        Hero toHero = entityManager.find(Hero.class, toHeroId);
        
        if (fromHero == null || toHero == null) {
            throw new IllegalArgumentException("One or both heroes not found");
        }
        
        // Crear registro de transferencia
        PowerTransfer transfer = new PowerTransfer(fromHeroId, toHeroId, amount);
        entityManager.persist(transfer);
        
        // Actualizar niveles de poder
        fromHero.setPowerLevel(fromHero.getPowerLevel() - amount);
        toHero.setPowerLevel(toHero.getPowerLevel() + amount);
        
        entityManager.merge(fromHero);
        entityManager.merge(toHero);
        
        // Simular un error que causa rollback
        throw new RuntimeException("Simulated error - transaction will rollback");
    }
    
    /**
     * Rollback manual usando TransactionManager.
     */
    @Transactional
    public void transferPowerWithManualRollback(Long fromHeroId, Long toHeroId, Integer amount) {
        try {
            Hero fromHero = entityManager.find(Hero.class, fromHeroId);
            Hero toHero = entityManager.find(Hero.class, toHeroId);
            
            if (fromHero == null || toHero == null) {
                transactionManager.setRollbackOnly();
                return;
            }
            
            // Validar condiciones de negocio
            if (amount > 50) {
                // Rollback manual si la cantidad es muy grande
                transactionManager.setRollbackOnly();
                throw new IllegalArgumentException("Transfer amount too large");
            }
            
            PowerTransfer transfer = new PowerTransfer(fromHeroId, toHeroId, amount);
            entityManager.persist(transfer);
            
            fromHero.setPowerLevel(fromHero.getPowerLevel() - amount);
            toHero.setPowerLevel(toHero.getPowerLevel() + amount);
            
            entityManager.merge(fromHero);
            entityManager.merge(toHero);
            
        } catch (Exception e) {
            // Marcar para rollback si hay error
            transactionManager.setRollbackOnly();
            throw e;
        }
    }
    
    /**
     * Batalla entre héroe y villano.
     * Demuestra transacción compleja con múltiples operaciones.
     */
    @Transactional
    public Battle executeBattle(Long heroId, Long villainId) {
        Hero hero = entityManager.find(Hero.class, heroId);
        Villain villain = entityManager.find(Villain.class, villainId);
        
        if (hero == null || villain == null) {
            throw new IllegalArgumentException("Hero or villain not found");
        }
        
        // Crear registro de batalla
        Battle battle = new Battle(heroId, villainId);
        battle.setHeroPowerBefore(hero.getPowerLevel());
        battle.setVillainPowerBefore(villain.getPowerLevel());
        
        entityManager.persist(battle);
        
        // Simular batalla y calcular resultado
        int heroPower = hero.getPowerLevel();
        int villainPower = villain.getPowerLevel();
        
        int powerDifference = Math.abs(heroPower - villainPower);
        int powerExchanged = Math.min(powerDifference, 10); // Máximo 10 puntos
        
        if (heroPower > villainPower) {
            // Héroe gana
            battle.setResult(Battle.BattleResult.HERO_WIN);
            hero.setPowerLevel(heroPower + powerExchanged);
            villain.setPowerLevel(villainPower - powerExchanged);
        } else if (villainPower > heroPower) {
            // Villano gana
            battle.setResult(Battle.BattleResult.VILLAIN_WIN);
            hero.setPowerLevel(heroPower - powerExchanged);
            villain.setPowerLevel(villainPower + powerExchanged);
        } else {
            // Empate
            battle.setResult(Battle.BattleResult.DRAW);
        }
        
        battle.setHeroPowerAfter(hero.getPowerLevel());
        battle.setVillainPowerAfter(villain.getPowerLevel());
        battle.setPowerExchanged(powerExchanged);
        battle.setEndedAt(LocalDateTime.now());
        
        entityManager.merge(hero);
        entityManager.merge(villain);
        entityManager.merge(battle);
        
        entityManager.flush();
        
        return battle;
    }
    
    /**
     * Transacción con timeout.
     * La transacción se cancela si excede el tiempo especificado.
     */
    @Transactional(timeout = 5) // 5 segundos
    public void longRunningOperation() throws InterruptedException {
        // Simular operación larga
        Thread.sleep(6000); // 6 segundos - excederá el timeout
    }
    
    /**
     * Transacción con rollbackOn y dontRollbackOn.
     * Especifica qué excepciones causan rollback.
     */
    @Transactional(rollbackOn = {IllegalArgumentException.class, RuntimeException.class},
                   dontRollbackOn = {IllegalStateException.class})
    public void transferWithCustomRollback(Long fromHeroId, Long toHeroId, Integer amount) {
        Hero fromHero = entityManager.find(Hero.class, fromHeroId);
        
        if (fromHero == null) {
            // IllegalArgumentException causa rollback
            throw new IllegalArgumentException("Hero not found");
        }
        
        // IllegalStateException NO causa rollback
        if (amount < 0) {
            throw new IllegalStateException("Negative amount - no rollback");
        }
        
        // Resto de la lógica...
    }
    
    /**
     * Transacción anidada usando REQUIRES_NEW.
     * La transacción interna se ejecuta independientemente.
     */
    @Transactional
    public void nestedTransactionExample(Long heroId) {
        Hero hero = entityManager.find(Hero.class, heroId);
        
        // Esta llamada crea una nueva transacción
        logOperation("Processing hero: " + hero.getName());
        
        // Si esta transacción hace rollback, el log anterior NO se revierte
        // porque se ejecutó en REQUIRES_NEW
    }
    
    /**
     * Obtiene todas las transferencias de poder.
     */
    public List<PowerTransfer> getAllTransfers() {
        return entityManager.createQuery(
            "SELECT pt FROM PowerTransfer pt ORDER BY pt.createdAt DESC", 
            PowerTransfer.class
        ).getResultList();
    }
    
    /**
     * Obtiene todas las batallas.
     */
    public List<Battle> getAllBattles() {
        return entityManager.createQuery(
            "SELECT b FROM Battle b ORDER BY b.startedAt DESC", 
            Battle.class
        ).getResultList();
    }
    
    /**
     * Obtiene una transferencia por ID.
     */
    public Optional<PowerTransfer> getTransferById(Long id) {
        PowerTransfer transfer = entityManager.find(PowerTransfer.class, id);
        return Optional.ofNullable(transfer);
    }
    
    /**
     * Obtiene una batalla por ID.
     */
    public Optional<Battle> getBattleById(Long id) {
        Battle battle = entityManager.find(Battle.class, id);
        return Optional.ofNullable(battle);
    }
}

