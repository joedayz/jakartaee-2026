#!/bin/bash

# Script para validar que una especificación de Jakarta EE está disponible
# Uso: ./validate-spec.sh [spec-name] [runtime]
# Ejemplo: ./validate-spec.sh batch quarkus

set -e

SPEC_NAME=$1
RUNTIME=${2:-"quarkus"}  # Default a quarkus

if [ -z "$SPEC_NAME" ]; then
    echo "❌ Error: Debes especificar el nombre de la spec"
    echo "Uso: $0 [spec-name] [runtime]"
    echo "Ejemplos:"
    echo "  $0 batch quarkus"
    echo "  $0 batch wildfly"
    echo "  $0 annotations quarkus"
    exit 1
fi

echo "🔍 Validando Jakarta $SPEC_NAME en $RUNTIME..."

# Mapeo de nombres de specs a paquetes Java
declare -A SPEC_PACKAGES=(
    ["annotations"]="jakarta.annotation"
    ["activation"]="jakarta.activation"
    ["authentication"]="jakarta.security.auth.message"
    ["authorization"]="jakarta.security.jacc"
    ["batch"]="jakarta.batch"
    ["cdi"]="jakarta.enterprise"
    ["jax-rs"]="jakarta.ws.rs"
    ["json-processing"]="jakarta.json"
    ["json-binding"]="jakarta.json.bind"
    ["bean-validation"]="jakarta.validation"
    ["jpa"]="jakarta.persistence"
    ["transactions"]="jakarta.transaction"
)

PACKAGE=${SPEC_PACKAGES[$SPEC_NAME]}

if [ -z "$PACKAGE" ]; then
    echo "⚠️  No se encontró mapeo para '$SPEC_NAME'"
    echo "Paquetes conocidos: ${!SPEC_PACKAGES[@]}"
    exit 1
fi

echo "📦 Paquete esperado: $PACKAGE"

# Buscar en dependencias Maven
if [ -f "pom.xml" ]; then
    echo "📋 Verificando dependencias en pom.xml..."
    
    if grep -q "$PACKAGE" pom.xml || grep -q "jakarta-$SPEC_NAME" pom.xml; then
        echo "✅ Dependencia encontrada en pom.xml"
    else
        echo "⚠️  Dependencia no encontrada en pom.xml"
        echo "   Busca: $PACKAGE o jakarta-$SPEC_NAME"
    fi
fi

# Verificar en classpath (si está compilado)
if [ -d "target/classes" ]; then
    echo "🔎 Verificando clases compiladas..."
    
    # Buscar archivos .class que contengan el paquete
    if find target/classes -name "*.class" -exec strings {} \; | grep -q "$PACKAGE"; then
        echo "✅ Clases de $PACKAGE encontradas en classpath"
    else
        echo "⚠️  Clases de $PACKAGE no encontradas en classpath"
    fi
fi

# Verificar imports en código fuente
if [ -d "src" ]; then
    echo "📝 Verificando imports en código fuente..."
    
    IMPORT_COUNT=$(find src -name "*.java" -exec grep -l "import $PACKAGE" {} \; | wc -l | tr -d ' ')
    
    if [ "$IMPORT_COUNT" -gt 0 ]; then
        echo "✅ Encontrados $IMPORT_COUNT archivo(s) usando $PACKAGE"
    else
        echo "⚠️  No se encontraron imports de $PACKAGE en código fuente"
    fi
fi

# Validación específica por runtime
case $RUNTIME in
    quarkus)
        echo "🚀 Validación específica para Quarkus..."
        
        if [ -f "pom.xml" ]; then
            if grep -q "quarkus" pom.xml; then
                echo "✅ Proyecto Quarkus detectado"
                
                # Verificar extensión específica si existe
                if [ "$SPEC_NAME" = "batch" ] && grep -q "quarkus-batch" pom.xml; then
                    echo "✅ Extensión quarkus-batch encontrada"
                fi
            fi
        fi
        ;;
    
    wildfly)
        echo "🐉 Validación específica para WildFly..."
        
        if [ -f "pom.xml" ]; then
            if grep -q "wildfly" pom.xml || grep -q "jboss" pom.xml; then
                echo "✅ Proyecto WildFly detectado"
            fi
        fi
        ;;
esac

echo ""
echo "✅ Validación completada para Jakarta $SPEC_NAME"
echo ""
echo "📚 Próximos pasos:"
echo "   1. Crea un test que use clases de $PACKAGE"
echo "   2. Ejecuta el test para verificar funcionalidad"
echo "   3. Revisa la documentación de la spec en:"
echo "      https://jakarta.ee/specifications/$SPEC_NAME/"

