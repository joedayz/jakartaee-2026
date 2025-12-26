package com.jakartaee.renarde.controller;

import io.quarkiverse.renarde.Controller;

/**
 * Controlador principal de la aplicación usando Renarde.
 * 
 * Renarde extiende la clase Controller que proporciona:
 * - Convenciones automáticas de rutas
 * - Utilidades para renderizar templates
 * - Manejo de flash messages
 * - Redirecciones simplificadas
 * - Integración con Hibernate/Panache
 */
public class Application extends Controller {
    
    /**
     * Página de inicio.
     * Renarde automáticamente mapea este método a GET /
     * y busca el template en templates/Application/index.html
     */
    public void index() {
        // Renderiza templates/Application/index.html automáticamente
    }
}

