# Mercado-Sankhya

# Backend – Catálogo de Produtos e Checkout

Api de catálogo de produtos desenvolvida por <https://www.linkedin.com/in/pehennu/>. 
Este serviço foi feito em **Java + Spring Boot** e utiliza um banco de dados relacional **H2 em memória** para facilitar testes e demonstração.

---

## **1️⃣ Como rodar**

1. Certifique-se de ter **Java 17+** instalado.
2. Clone o repositório:
```bash
git clone <https://github.com/pehennu/Mercado-Sankhya>
```
3. Rode a aplicação:

```bash
./mvnw spring-boot:run
```
ou abra em sua IDE de preferência.

> Quando a aplicação iniciar, o H2 será populado automaticamente com alguns produtos de exemplo.

4. A API estará disponível em:

```
http://localhost:8080/api/v1
```

---

## **2. Endpoints**

### **Produtos**

- **GET /products**
- Parâmetros opcionais:
    - `search`: filtro por nome (case-insensitive, parcial)
    - `page`: página (default 0)
    - `size`: quantidade por página (default 10)

**Exemplo:**

```
GET http://localhost:8080/api/v1/products?search=café&page=0&size=5
```

Retorna uma página de produtos com DTO:

```json
{
  "content": [
    {
      "id": 1,
      "name": "Café Torrado 500g",
      "price": 18.90,
      "stock": 5,
      "active": true
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1
}
```

---

### **Pedidos**

- **POST /orders**
- Corpo:

```json
{
  "items": [
    {"productId": 1, "quantity": 2},
    {"productId": 3, "quantity": 1}
  ]
}
```

- **Respostas:**
    - **201 Created:** pedido criado com sucesso, retorna todos os itens, total e ID do pedido
    - **409 Conflict:** algum item sem estoque suficiente, retorna lista de produtos indisponíveis

---

## **3. Banco de dados H2**

- **População automática** na inicialização via `DataInitializer`:

| Nome                  | Preço | Estoque |
| --------------------- | ----- | ------- |
| Café Torrado 500g     | 18.90 | 5       |
| Filtro de Papel nº103 | 7.50  | 10      |
| Garrafa Térmica 1L    | 79.90 | 2       |
| Açúcar Mascavo 1kg    | 16.00 | 0       |
| Caneca Inox 300ml     | 29.00 | 8       |

- Para acessar o console do H2:
  ```
  http://localhost:8080/h2-console
  ```
    - JDBC URL: `jdbc:h2:mem:marketdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`
    - Usuário: `sa`
    - Senha: (vazio)

---

## **4. Top 3 produtos mais vendidos**

**Query SQL:**

```sql
SELECT p.name, SUM(oi.quantity) AS total_vendido
FROM order_items oi
JOIN products p ON oi.product_id = p.id
GROUP BY p.name
ORDER BY total_vendido DESC
LIMIT 3;
```

**EXPLAIN do H2:**

```sql
EXPLAIN SELECT p.name, SUM(oi.quantity) AS total_vendido
FROM order_items oi
JOIN products p ON oi.product_id = p.id
GROUP BY p.name
ORDER BY total_vendido DESC
LIMIT 3;
```

> Mostra como o H2 otimiza a junção e agregação dos pedidos.

---

## **5. Estratégia de atomicidade e rollback**

No checkout (`POST /orders`), a operação é **totalmente transacional**:

- Se algum item não tiver estoque suficiente, **nenhum pedido é criado** e o cliente recebe 409 Conflict.
- O uso de `` garante rollback automático caso qualquer erro aconteça, preservando consistência.
- Controle de concorrência com `` no produto evita que dois pedidos simultâneos vendam o mesmo estoque.

> Em resumo: ou o pedido é criado inteiro, ou nada é criado. Sem riscos de estoque negativo.

---

## **6. Observações finais**

- DTOs foram usados para separar **entidades do banco da API**, garantindo encapsulamento e flexibilidade para o frontend.
- Qualquer exceção de validação ou estoque insuficiente é retornada de forma **limpa e compreensível**.


## ** Como rodar o frontend**

1. **Pré-requisitos**
  - [Node.js](https://nodejs.org/) v16+
  - [Angular CLI](https://angular.io/cli) instalado globalmente:
    ```bash
    npm install -g @angular/cli
    ```

2. **Acessar a pasta do frontend**
   ```bash
   cd Mercado-Sankhya/frontend
   ```

3. **Instalar dependências**
   ```bash
   npm install
   ```

4. **Configurar URL da API (opcional)**
   Editar `src/environments/environment.ts`:
   ```typescript
   export const environment = {
     production: false,
     apiUrl: 'http://localhost:8080'
   };
   ```

5. **Rodar servidor de desenvolvimento**
   ```bash
   ng serve
   ```
   Acesse: [http://localhost:4200](http://localhost:4200)

---

## **5️⃣ Observações finais**
- O frontend espera o backend em `http://localhost:8080`
- DTOs foram usados para separar entidades do banco da API
- Checkout é transacional: ou cria o pedido inteiro ou nada

