#!/bin/bash

# Script para validar todas las especificaciones de Jakarta EE en un proyecto
# Uso: ./validate-all-specs.sh [runtime]

set -e

RUNTIME=${1:-"quarkus"}

echo "🔍 Validando todas las especificaciones de Jakarta EE..."
echo "Runtime: $RUNTIME"
echo ""

# Lista de specs a validar
SPECS=(
    "annotations"
    "batch"
    "cdi"
    "jax-rs"
    "json-processing"
    "json-binding"
    "bean-validation"
    "jpa"
    "transactions"
)

if [ "$RUNTIME" = "wildfly" ]; then
    # Agregar specs solo de WildFly Platform
    SPECS+=(
        "activation"
        "authentication"
        "authorization"
    )
fi

VALID_COUNT=0
INVALID_COUNT=0

for spec in "${SPECS[@]}"; do
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "Validando: Jakarta $spec"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    
    if ./validate-spec.sh "$spec" "$RUNTIME" > /dev/null 2>&1; then
        echo "✅ Jakarta $spec: OK"
        ((VALID_COUNT++))
    else
        echo "❌ Jakarta $spec: FALLO"
        ((INVALID_COUNT++))
    fi
    echo ""
done

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "📊 Resumen de Validación"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "✅ Válidas: $VALID_COUNT"
echo "❌ Inválidas: $INVALID_COUNT"
echo "📦 Total: ${#SPECS[@]}"
echo ""

if [ $INVALID_COUNT -eq 0 ]; then
    echo "🎉 ¡Todas las especificaciones están disponibles!"
    exit 0
else
    echo "⚠️  Algunas especificaciones no están disponibles"
    exit 1
fi

