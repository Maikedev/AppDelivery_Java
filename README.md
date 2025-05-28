# Pizzaria Delivery - Sistema de Gerenciamento

![Java](https://img.shields.io/badge/Java-21-orange)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Swing](https://img.shields.io/badge/Java%20Swing-GUI-yellowgreen)

Um sistema completo para gerenciamento de pedidos de pizzaria com interface gráfica e banco de dados.

## Funcionalidades

- **Área do Cliente**
  - Visualização do cardápio completo
  - Realização de pedidos
  - Jogo de Ping Pong enquanto aguarda

- **Área do Trabalhador**
  - Adição de novos sabores
  - Atualização de status dos pedidos
  - Remoção de pedidos concluídos
  - Acompanhamento de todos os pedidos

## Como Executar

### Pré-requisitos
- Java JDK 21
- MySQL Server 8.0
- MySQL Connector/J (Na pasta lib)
- MySQL Workbench

### Passo a Passo
1. **Configure o banco de dados**:
   ```sql
   CREATE DATABASE pizzaria_db;
   CREATE USER 'pizzaria_user'@'localhost' IDENTIFIED BY 'senha_segura';
   GRANT ALL PRIVILEGES ON pizzaria_db.* TO 'pizzaria_user'@'localhost';
   FLUSH PRIVILEGES;
2. **Importe as tabelas**:
   ```sql
   USE pizzaria_db;
   
   CREATE TABLE sabores (
       id INT AUTO_INCREMENT PRIMARY KEY,
       nome VARCHAR(50) NOT NULL,
       preco DECIMAL(10, 2) NOT NULL
   );
   
   CREATE TABLE pedidos (
       id INT AUTO_INCREMENT PRIMARY KEY,
       sabor_id INT NOT NULL,
       status VARCHAR(20) DEFAULT 'Em espera',
       data_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY (sabor_id) REFERENCES sabores(id)
   );
  
   ```

## 🛠 Tecnologias Utilizadas

| Tecnologia        | Descrição                         |
|-------------------|-----------------------------------|
| IDE               | Visual Studio Code                |
| Java 21           | Linguagem principal do sistema    |
| Java Swing        | Interface gráfica                 |
| MySQL 8.0         | Banco de dados relacional         |
| JDBC              | Conexão com o banco de dados      |

## Estrutura dos Arquivos

```plaintext
pizzaria-delivery/
├── src/
│   ├── AppDelivery.java          # Classe principal (GUI)
│   ├── DatabaseConnection.java   # Conexão com BD
│   └── pingPong/                 # Jogo integrado
│       ├── Pong.java             # Lógica do jogo
│       ├── Bola.java             # Componente do jogo
│       ├── Jogador.java          # Componente do jogo
│       └── Inimigo.java          # Componente do jogo
├── lib/                          # Dependências
│   └── mysql-connector-java-8.0.xx.jar
└── README.md                    # Documentação
```

## Feito Por:
- Maike Martins
- Cauã Castro
- Guilherm Nunes
- Pedro Nonato
