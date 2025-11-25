Sistema Mercado 2 Amigos

Sistema de gerenciamento de produtos desenvolvido para a Unidade Curricular de Programação Orientada a Objetos (SADP 2025/1). O sistema permite o cadastro, listagem, edição e exclusão de produtos em um banco de dados MySQL.

## 📋 Pré-requisitos

* Java JDK 17 ou superior.
* MySQL Server instalado e rodando.
* IntelliJ IDEA (ou outra IDE Java).
* Driver JDBC do MySQL (mysql-connector-java) adicionado às bibliotecas do projeto.

## 🚀 Como Rodar o Projeto

### 1. Configuração do Banco de Dados
Execute o seguinte script no seu cliente MySQL (Workbench/DBeaver) para criar o banco:

```sql
CREATE DATABASE mercado2amigos;
USE mercado2amigos;

CREATE TABLE produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco DECIMAL(10, 2) NOT NULL,
    quantidade INT NOT NULL
);
