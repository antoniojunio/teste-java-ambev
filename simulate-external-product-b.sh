#!/bin/bash

# Script para simular consultas do Produto Externo B

API_URL="http://localhost:8080/api/orders"

echo "Simulando consultas do Produto Externo B..."

# Consultar pedido por ID
echo "Consultando pedido ID 1:"
curl -X GET "$API_URL/1" \
  -H "Content-Type: application/json"

echo -e "\n\n"

# Listar todos os pedidos
echo "Listando todos os pedidos:"
curl -X GET "$API_URL?page=0&size=10" \
  -H "Content-Type: application/json"

echo -e "\n\n"

# Filtrar por status PROCESSED
echo "Filtrando pedidos por status PROCESSED:"
curl -X GET "$API_URL/status/PROCESSED?page=0&size=10" \
  -H "Content-Type: application/json"

echo -e "\n\nSimulação concluída!"

