package com.jakartaee.jsonprocessing.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.*;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonParser;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio que demuestra características de JSON-Processing:
 * - JsonObject y JsonArray
 * - Building JSON programáticamente
 * - Parsing JSON
 * - Streaming API (JsonParser, JsonGenerator)
 * - Querying JSON
 * - Transformaciones
 */
@ApplicationScoped
public class JsonProcessingService {
    
    /**
     * Crea un JsonObject programáticamente usando JsonObjectBuilder.
     */
    public JsonObject createHeroJsonObject() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("name", "Superman")
               .add("power", "Super strength, flight, heat vision")
               .add("powerLevel", 95)
               .add("isActive", true)
               .add("description", "El último hijo de Krypton");
        
        // Agregar un objeto anidado
        JsonObjectBuilder locationBuilder = Json.createObjectBuilder();
        locationBuilder.add("city", "Metropolis")
                       .add("planet", "Earth");
        builder.add("location", locationBuilder);
        
        // Agregar un array
        JsonArrayBuilder abilitiesBuilder = Json.createArrayBuilder();
        abilitiesBuilder.add("Super strength")
                       .add("Flight")
                       .add("Heat vision")
                       .add("X-ray vision");
        builder.add("abilities", abilitiesBuilder);
        
        return builder.build();
    }
    
    /**
     * Crea un JsonArray de héroes.
     */
    public JsonArray createHeroesJsonArray() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        // Primer héroe
        JsonObjectBuilder hero1Builder = Json.createObjectBuilder();
        hero1Builder.add("name", "Superman")
                    .add("powerLevel", 95)
                    .add("power", "Super strength");
        arrayBuilder.add(hero1Builder);
        
        // Segundo héroe
        JsonObjectBuilder hero2Builder = Json.createObjectBuilder();
        hero2Builder.add("name", "Batman")
                    .add("powerLevel", 85)
                    .add("power", "Intelligence");
        arrayBuilder.add(hero2Builder);
        
        // Tercer héroe
        JsonObjectBuilder hero3Builder = Json.createObjectBuilder();
        hero3Builder.add("name", "Wonder Woman")
                    .add("powerLevel", 90)
                    .add("power", "Super strength");
        arrayBuilder.add(hero3Builder);
        
        return arrayBuilder.build();
    }
    
    /**
     * Parsea un JSON string a JsonObject.
     */
    public JsonObject parseJsonString(String jsonString) {
        JsonReader reader = Json.createReader(new StringReader(jsonString));
        JsonObject jsonObject = reader.readObject();
        reader.close();
        return jsonObject;
    }
    
    /**
     * Parsea un JSON array string a JsonArray.
     */
    public JsonArray parseJsonArrayString(String jsonArrayString) {
        JsonReader reader = Json.createReader(new StringReader(jsonArrayString));
        JsonArray jsonArray = reader.readArray();
        reader.close();
        return jsonArray;
    }
    
    /**
     * Convierte JsonObject a String con formato.
     */
    public String jsonObjectToString(JsonObject jsonObject) {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = Json.createWriterFactory(
            Map.of(JsonGenerator.PRETTY_PRINTING, true)
        ).createWriter(writer);
        jsonWriter.writeObject(jsonObject);
        jsonWriter.close();
        return writer.toString();
    }
    
    /**
     * Convierte JsonArray a String con formato.
     */
    public String jsonArrayToString(JsonArray jsonArray) {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = Json.createWriterFactory(
            Map.of(JsonGenerator.PRETTY_PRINTING, true)
        ).createWriter(writer);
        jsonWriter.writeArray(jsonArray);
        jsonWriter.close();
        return writer.toString();
    }
    
    /**
     * Query JSON usando JsonPointer (RFC 6901).
     */
    public JsonValue queryJson(JsonObject jsonObject, String pointerPath) {
        JsonPointer pointer = Json.createPointer(pointerPath);
        return pointer.getValue(jsonObject);
    }
    
    /**
     * Modifica JSON usando JsonPointer.
     */
    public JsonObject modifyJson(JsonObject jsonObject, String pointerPath, JsonValue newValue) {
        JsonPointer pointer = Json.createPointer(pointerPath);
        return pointer.add(jsonObject, newValue);
    }
    
    /**
     * Elimina un campo usando JsonPointer.
     */
    public JsonObject removeFromJson(JsonObject jsonObject, String pointerPath) {
        JsonPointer pointer = Json.createPointer(pointerPath);
        return pointer.remove(jsonObject);
    }
    
    /**
     * Transforma un JsonObject usando JsonPatch (RFC 6902).
     */
    public JsonObject transformJson(JsonObject original, JsonArray patchOperations) {
        JsonPatch patch = Json.createPatch(patchOperations);
        return patch.apply(original);
    }
    
    /**
     * Crea operaciones de patch para actualizar un héroe.
     */
    public JsonArray createPatchOperations() {
        JsonArrayBuilder patchBuilder = Json.createArrayBuilder();
        
        // Operación: reemplazar powerLevel
        JsonObjectBuilder replaceOp = Json.createObjectBuilder();
        replaceOp.add("op", "replace")
                 .add("path", "/powerLevel")
                 .add("value", 98);
        patchBuilder.add(replaceOp);
        
        // Operación: agregar nuevo campo
        JsonObjectBuilder addOp = Json.createObjectBuilder();
        addOp.add("op", "add")
             .add("path", "/newField")
             .add("value", "newValue");
        patchBuilder.add(addOp);
        
        return patchBuilder.build();
    }
    
    /**
     * Usa streaming API para parsear JSON grande.
     * Demuestra JsonParser para procesar JSON de forma eficiente.
     */
    public Map<String, Object> parseJsonStreaming(String jsonString) {
        Map<String, Object> result = new HashMap<>();
        List<String> keys = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        
        JsonParser parser = Json.createParser(new StringReader(jsonString));
        
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            
            switch (event) {
                case KEY_NAME:
                    keys.add(parser.getString());
                    break;
                case VALUE_STRING:
                    if (!keys.isEmpty()) {
                        values.add(parser.getString());
                    }
                    break;
                case VALUE_NUMBER:
                    if (parser.isIntegralNumber()) {
                        values.add(parser.getLong());
                    } else {
                        values.add(parser.getBigDecimal());
                    }
                    break;
                case VALUE_TRUE:
                    values.add(true);
                    break;
                case VALUE_FALSE:
                    values.add(false);
                    break;
                case VALUE_NULL:
                    values.add(null);
                    break;
                default:
                    break;
            }
        }
        parser.close();
        
        // Construir mapa de resultados
        for (int i = 0; i < keys.size() && i < values.size(); i++) {
            result.put(keys.get(i), values.get(i));
        }
        
        return result;
    }
    
    /**
     * Genera JSON usando streaming API (JsonGenerator).
     */
    public String generateJsonStreaming() {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = Json.createGeneratorFactory(
            Map.of(JsonGenerator.PRETTY_PRINTING, true)
        ).createGenerator(writer);
        
        generator.writeStartObject()
                 .write("name", "Superman")
                 .write("powerLevel", 95)
                 .writeStartObject("location")
                     .write("city", "Metropolis")
                     .write("planet", "Earth")
                 .writeEnd()
                 .writeStartArray("abilities")
                     .write("Super strength")
                     .write("Flight")
                     .write("Heat vision")
                 .writeEnd()
                 .writeEnd();
        
        generator.close();
        return writer.toString();
    }
    
    /**
     * Filtra héroes de un JsonArray por nivel de poder mínimo.
     */
    public JsonArray filterHeroesByPowerLevel(JsonArray heroesArray, int minPowerLevel) {
        JsonArrayBuilder filteredBuilder = Json.createArrayBuilder();
        
        for (JsonValue heroValue : heroesArray) {
            if (heroValue.getValueType() == JsonValue.ValueType.OBJECT) {
                JsonObject hero = heroValue.asJsonObject();
                int powerLevel = hero.getInt("powerLevel", 0);
                
                if (powerLevel >= minPowerLevel) {
                    filteredBuilder.add(hero);
                }
            }
        }
        
        return filteredBuilder.build();
    }
    
    /**
     * Combina múltiples JsonObjects.
     */
    public JsonObject mergeJsonObjects(JsonObject obj1, JsonObject obj2) {
        JsonObjectBuilder mergedBuilder = Json.createObjectBuilder(obj1);
        
        // Agregar campos del segundo objeto
        for (Map.Entry<String, JsonValue> entry : obj2.entrySet()) {
            mergedBuilder.add(entry.getKey(), entry.getValue());
        }
        
        return mergedBuilder.build();
    }
    
    /**
     * Crea un JSON complejo con múltiples niveles de anidación.
     */
    public JsonObject createComplexJson() {
        JsonObjectBuilder teamBuilder = Json.createObjectBuilder();
        teamBuilder.add("name", "Justice League")
                   .add("formationDate", "1960-02-01");
        
        JsonArrayBuilder membersBuilder = Json.createArrayBuilder();
        
        // Miembro 1
        JsonObjectBuilder member1 = Json.createObjectBuilder();
        member1.add("name", "Superman")
               .add("powerLevel", 95)
               .add("role", "Leader");
        
        JsonArrayBuilder abilities1 = Json.createArrayBuilder();
        abilities1.add("Super strength").add("Flight").add("Heat vision");
        member1.add("abilities", abilities1);
        membersBuilder.add(member1);
        
        // Miembro 2
        JsonObjectBuilder member2 = Json.createObjectBuilder();
        member2.add("name", "Batman")
               .add("powerLevel", 85)
               .add("role", "Strategist");
        
        JsonArrayBuilder abilities2 = Json.createArrayBuilder();
        abilities2.add("Intelligence").add("Martial arts").add("Technology");
        member2.add("abilities", abilities2);
        membersBuilder.add(member2);
        
        teamBuilder.add("members", membersBuilder);
        
        // Headquarters
        JsonObjectBuilder hqBuilder = Json.createObjectBuilder();
        hqBuilder.add("city", "Washington D.C.")
                 .add("country", "USA");
        
        JsonObjectBuilder coordsBuilder = Json.createObjectBuilder();
        coordsBuilder.add("latitude", new BigDecimal("38.9072"))
                     .add("longitude", new BigDecimal("-77.0369"));
        hqBuilder.add("coordinates", coordsBuilder);
        
        teamBuilder.add("headquarters", hqBuilder);
        
        return teamBuilder.build();
    }
}

