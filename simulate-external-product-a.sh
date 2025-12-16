#!/bin/bash

# Script para simular envio de pedidos do Produto Externo A

API_URL="http://localhost:8080/api/orders"

echo "Simulando envio de pedidos do Produto Externo A..."

# Pedido 1
curl -X POST "$API_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "externalId": "EXT-001",
    "products": [
      {
        "name": "Produto A",
        "value": 10.50,
        "quantity": 2
      },
      {
        "name": "Produto B",
        "value": 15.75,
        "quantity": 1
      }
    ]
  }'

echo -e "\n\n"

# Pedido 2
curl -X POST "$API_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "externalId": "EXT-002",
    "products": [
      {
        "name": "Produto C",
        "value": 25.00,
        "quantity": 3
      }
    ]
  }'

echo -e "\n\nSimulação concluída!"

