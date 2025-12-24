package com.jakartaee.jsonprocessing.resource;

import com.jakartaee.jsonprocessing.service.JsonProcessingService;
import jakarta.inject.Inject;
import jakarta.json.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

/**
 * Recurso REST que demuestra características de JSON-Processing.
 * Muestra JsonObject, JsonArray, streaming API, querying, y transformaciones.
 */
@Path("/api/json-processing")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JsonProcessingResource {
    
    @Inject
    JsonProcessingService jsonProcessingService;
    
    /**
     * Crea un JsonObject programáticamente.
     * GET /api/json-processing/hero/object
     */
    @GET
    @Path("/hero/object")
    public Response createHeroJsonObject() {
        JsonObject hero = jsonProcessingService.createHeroJsonObject();
        return Response.ok(hero).build();
    }
    
    /**
     * Crea un JsonArray de héroes.
     * GET /api/json-processing/heroes/array
     */
    @GET
    @Path("/heroes/array")
    public Response createHeroesJsonArray() {
        JsonArray heroes = jsonProcessingService.createHeroesJsonArray();
        return Response.ok(heroes).build();
    }
    
    /**
     * Convierte JsonObject a String formateado.
     * GET /api/json-processing/hero/object/formatted
     */
    @GET
    @Path("/hero/object/formatted")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getFormattedJsonObject() {
        JsonObject hero = jsonProcessingService.createHeroJsonObject();
        String formatted = jsonProcessingService.jsonObjectToString(hero);
        return Response.ok(formatted).build();
    }
    
    /**
     * Parsea un JSON string a JsonObject.
     * POST /api/json-processing/parse
     */
    @POST
    @Path("/parse")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response parseJsonString(String jsonString) {
        JsonObject jsonObject = jsonProcessingService.parseJsonString(jsonString);
        return Response.ok(jsonObject).build();
    }
    
    /**
     * Query JSON usando JsonPointer.
     * GET /api/json-processing/query?path=/name
     */
    @GET
    @Path("/query")
    public Response queryJson(@QueryParam("path") @DefaultValue("/name") String path) {
        JsonObject hero = jsonProcessingService.createHeroJsonObject();
        JsonValue result = jsonProcessingService.queryJson(hero, path);
        return Response.ok(result).build();
    }
    
    /**
     * Modifica JSON usando JsonPointer.
     * PATCH /api/json-processing/modify?path=/powerLevel&value=98
     */
    @PATCH
    @Path("/modify")
    public Response modifyJson(
            @QueryParam("path") @DefaultValue("/powerLevel") String path,
            @QueryParam("value") @DefaultValue("98") int value) {
        JsonObject hero = jsonProcessingService.createHeroJsonObject();
        JsonValue newValue = Json.createValue(value);
        JsonObject modified = jsonProcessingService.modifyJson(hero, path, newValue);
        return Response.ok(modified).build();
    }
    
    /**
     * Transforma JSON usando JsonPatch.
     * POST /api/json-processing/transform
     */
    @POST
    @Path("/transform")
    public Response transformJson(JsonArray patchOperations) {
        JsonObject original = jsonProcessingService.createHeroJsonObject();
        JsonObject transformed = jsonProcessingService.transformJson(original, patchOperations);
        return Response.ok(transformed).build();
    }
    
    /**
     * Obtiene operaciones de patch de ejemplo.
     * GET /api/json-processing/patch/example
     */
    @GET
    @Path("/patch/example")
    public Response getPatchExample() {
        JsonArray patchOps = jsonProcessingService.createPatchOperations();
        return Response.ok(patchOps).build();
    }
    
    /**
     * Parsea JSON usando streaming API.
     * POST /api/json-processing/stream/parse
     */
    @POST
    @Path("/stream/parse")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response parseJsonStreaming(String jsonString) {
        Map<String, Object> result = jsonProcessingService.parseJsonStreaming(jsonString);
        return Response.ok(result).build();
    }
    
    /**
     * Genera JSON usando streaming API.
     * GET /api/json-processing/stream/generate
     */
    @GET
    @Path("/stream/generate")
    @Produces(MediaType.TEXT_PLAIN)
    public Response generateJsonStreaming() {
        String json = jsonProcessingService.generateJsonStreaming();
        return Response.ok(json).build();
    }
    
    /**
     * Filtra héroes por nivel de poder mínimo.
     * GET /api/json-processing/heroes/filter?minPowerLevel=90
     */
    @GET
    @Path("/heroes/filter")
    public Response filterHeroes(@QueryParam("minPowerLevel") @DefaultValue("90") int minPowerLevel) {
        JsonArray heroes = jsonProcessingService.createHeroesJsonArray();
        JsonArray filtered = jsonProcessingService.filterHeroesByPowerLevel(heroes, minPowerLevel);
        return Response.ok(filtered).build();
    }
    
    /**
     * Crea un JSON complejo con múltiples niveles.
     * GET /api/json-processing/complex
     */
    @GET
    @Path("/complex")
    public Response createComplexJson() {
        JsonObject complex = jsonProcessingService.createComplexJson();
        return Response.ok(complex).build();
    }
    
    /**
     * Convierte JSON complejo a String formateado.
     * GET /api/json-processing/complex/formatted
     */
    @GET
    @Path("/complex/formatted")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getFormattedComplexJson() {
        JsonObject complex = jsonProcessingService.createComplexJson();
        String formatted = jsonProcessingService.jsonObjectToString(complex);
        return Response.ok(formatted).build();
    }
    
    /**
     * Información sobre características de JSON-Processing demostradas.
     * GET /api/json-processing/info
     */
    @GET
    @Path("/info")
    public Response getInfo() {
        return Response.ok(Map.of(
            "features", java.util.List.of(
                "JsonObject - Create and manipulate JSON objects",
                "JsonArray - Create and manipulate JSON arrays",
                "JsonObjectBuilder - Build objects programmatically",
                "JsonArrayBuilder - Build arrays programmatically",
                "JsonReader - Parse JSON strings",
                "JsonWriter - Write JSON with formatting",
                "JsonPointer - Query JSON (RFC 6901)",
                "JsonPatch - Transform JSON (RFC 6902)",
                "JsonParser - Streaming parser for large JSON",
                "JsonGenerator - Streaming generator for large JSON",
                "Filtering and transformation",
                "Merging JSON objects"
            ),
            "apis", Map.of(
                "Object Model API", "JsonObject, JsonArray, JsonValue",
                "Streaming API", "JsonParser, JsonGenerator",
                "Pointer API", "JsonPointer for querying",
                "Patch API", "JsonPatch for transformations"
            )
        )).build();
    }
}

