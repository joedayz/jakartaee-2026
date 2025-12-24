package com.jakartaee.transactions.resource;

import com.jakartaee.transactions.entity.Battle;
import com.jakartaee.transactions.entity.PowerTransfer;
import com.jakartaee.transactions.service.TransactionDemoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * Recurso REST que demuestra diferentes características de transacciones.
 */
@Path("/api/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionResource {
    
    @Inject
    TransactionDemoService transactionService;
    
    /**
     * Crea un héroe usando transacción básica.
     * GET /api/transactions/hero/create?name=Superman&power=Super strength&powerLevel=95
     */
    @GET
    @Path("/hero/create")
    public Response createHero(
            @QueryParam("name") String name,
            @QueryParam("power") String power,
            @QueryParam("powerLevel") @DefaultValue("80") int powerLevel) {
        try {
            var hero = transactionService.createHero(name, power, powerLevel);
            return Response.ok(hero).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }
    
    /**
     * Transfiere poder entre héroes.
     * Demuestra transacción con múltiples operaciones atómicas.
     * POST /api/transactions/power-transfer
     */
    @POST
    @Path("/power-transfer")
    public Response transferPower(TransferPowerRequest request) {
        try {
            PowerTransfer transfer = transactionService.transferPower(
                    request.getFromHeroId(),
                    request.getToHeroId(),
                    request.getAmount());
            return Response.ok(transfer).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }
    
    /**
     * Transfiere poder que falla intencionalmente.
     * Demuestra rollback automático.
     * POST /api/transactions/power-transfer/fail
     */
    @POST
    @Path("/power-transfer/fail")
    public Response transferPowerWithFailure(TransferPowerRequest request) {
        try {
            PowerTransfer transfer = transactionService.transferPowerWithFailure(
                    request.getFromHeroId(),
                    request.getToHeroId(),
                    request.getAmount());
            return Response.ok(transfer).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of(
                        "error", "Transaction failed and rolled back",
                        "message", e.getMessage()
                    ))
                    .build();
        }
    }
    
    /**
     * Transfiere poder con rollback manual.
     * POST /api/transactions/power-transfer/manual-rollback
     */
    @POST
    @Path("/power-transfer/manual-rollback")
    public Response transferPowerWithManualRollback(TransferPowerRequest request) {
        try {
            transactionService.transferPowerWithManualRollback(
                    request.getFromHeroId(),
                    request.getToHeroId(),
                    request.getAmount());
            return Response.ok(Map.of("message", "Transfer completed successfully")).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage(), "rolledBack", true))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }
    
    /**
     * Ejecuta una batalla entre héroe y villano.
     * Demuestra transacción compleja con múltiples operaciones.
     * POST /api/transactions/battle
     */
    @POST
    @Path("/battle")
    public Response executeBattle(BattleRequest request) {
        try {
            Battle battle = transactionService.executeBattle(
                    request.getHeroId(),
                    request.getVillainId());
            return Response.ok(battle).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }
    
    /**
     * Obtiene todas las transferencias de poder.
     * GET /api/transactions/transfers
     */
    @GET
    @Path("/transfers")
    public Response getAllTransfers() {
        List<PowerTransfer> transfers = transactionService.getAllTransfers();
        return Response.ok(transfers).build();
    }
    
    /**
     * Obtiene todas las batallas.
     * GET /api/transactions/battles
     */
    @GET
    @Path("/battles")
    public Response getAllBattles() {
        List<Battle> battles = transactionService.getAllBattles();
        return Response.ok(battles).build();
    }
    
    /**
     * Obtiene una transferencia por ID.
     * GET /api/transactions/transfers/{id}
     */
    @GET
    @Path("/transfers/{id}")
    public Response getTransfer(@PathParam("id") Long id) {
        return transactionService.getTransferById(id)
                .map(transfer -> Response.ok(transfer).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Transfer not found"))
                        .build());
    }
    
    /**
     * Obtiene una batalla por ID.
     * GET /api/transactions/battles/{id}
     */
    @GET
    @Path("/battles/{id}")
    public Response getBattle(@PathParam("id") Long id) {
        return transactionService.getBattleById(id)
                .map(battle -> Response.ok(battle).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Battle not found"))
                        .build());
    }
    
    /**
     * Información sobre características de transacciones demostradas.
     * GET /api/transactions/info
     */
    @GET
    @Path("/info")
    public Response getInfo() {
        return Response.ok(Map.of(
            "transactionTypes", Map.of(
                "REQUIRED", "Usa transacción existente o crea nueva (por defecto)",
                "REQUIRES_NEW", "Siempre crea nueva transacción",
                "MANDATORY", "Requiere transacción existente",
                "SUPPORTS", "Usa transacción si existe, sino ejecuta sin transacción",
                "NOT_SUPPORTED", "Suspende transacción existente",
                "NEVER", "Lanza excepción si hay transacción activa"
            ),
            "features", List.of(
                "Automatic rollback on exceptions",
                "Manual rollback with TransactionManager",
                "Transaction timeout",
                "Custom rollback conditions (rollbackOn, dontRollbackOn)",
                "Nested transactions",
                "Atomic operations",
                "Multiple operations in single transaction"
            )
        )).build();
    }
    
    /**
     * DTO para transferencia de poder.
     */
    public static class TransferPowerRequest {
        private Long fromHeroId;
        private Long toHeroId;
        private Integer amount;
        
        public Long getFromHeroId() {
            return fromHeroId;
        }
        
        public void setFromHeroId(Long fromHeroId) {
            this.fromHeroId = fromHeroId;
        }
        
        public Long getToHeroId() {
            return toHeroId;
        }
        
        public void setToHeroId(Long toHeroId) {
            this.toHeroId = toHeroId;
        }
        
        public Integer getAmount() {
            return amount;
        }
        
        public void setAmount(Integer amount) {
            this.amount = amount;
        }
    }
    
    /**
     * DTO para batalla.
     */
    public static class BattleRequest {
        private Long heroId;
        private Long villainId;
        
        public Long getHeroId() {
            return heroId;
        }
        
        public void setHeroId(Long heroId) {
            this.heroId = heroId;
        }
        
        public Long getVillainId() {
            return villainId;
        }
        
        public void setVillainId(Long villainId) {
            this.villainId = villainId;
        }
    }
}

