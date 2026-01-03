#!/bin/bash
echo "🧹 Limpiando procesos previos..."
lsof -ti:3000,3001,3002 2>/dev/null | xargs kill -9 2>/dev/null || true
echo "🗑️  Limpiando cache..."
rm -rf .next
echo "🚀 Iniciando servidor de desarrollo..."
pnpm dev
