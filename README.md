Sales Management System

![Java](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green?style=for-the-badge&logo=springboot)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.x-brightgreen?style=for-the-badge&logo=thymeleaf)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5-purple?style=for-the-badge&logo=bootstrap)
![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellow?style=for-the-badge)

Sistema web de gestão comercial e controle de vendas desenvolvido em Java com o framework Spring Boot. 

O projeto adota uma arquitetura em camadas (MVC) e persistência de dados relacional com Spring Data JPA / Hibernate, integrando um front-end dinâmico renderizado no servidor via Thymeleaf e estilizado com Bootstrap 5.

 Visão Geral & Roadmap do Projeto

O sistema foi modelado para suportar o fluxo completo de uma operação de varejo/distribuição. No estágio atual, a fundação arquitetural foi estabelecida e o módulo de dados base (Estados) está 100% operacional.

[x] **Módulo de Estados (Base Cadastral): CRUD completo (Cadastro, Listagem, Edição e Exclusão) com validações e persistência em banco de dados.
[ ] **Módulo de Funcionários: Gestão de equipe e perfis de acesso.(Em andamento)
[ ] **Módulo de Fornecedores e Produtos: Catálogo de itens e controle de suprimentos.(Em andamento)
[ ] **Módulo de Entradas de Mercadorias: Registro de movimentação de estoque.(Planejado)
[ ] **Módulo de Vendas: Emissão e fechamento de pedidos comerciais.(Planejado)

Tecnologias e Ferramentas

Linguagem:** Java (versão 17 ou superior)
Framework Principal:** Spring Boot
Spring Web: Controladores REST/MVC e manipulação de requisições HTTP (`@Controller`, `@GetMapping`, `@PostMapping`).
Spring Data JPA: Camada de abstração de dados e repositórios (`JpaRepository`).
Hibernate: Mapeamento Objeto-Relacional (ORM) e gerenciamento de entidades (`@Entity`, `@Table`).
Template Engine: Thymeleaf
Front-end: HTML5, CSS3, Bootstrap 5 (formulários e tabelas responsivas)
Gerenciador de Dependências: Apache Maven

Estrutura Arquitetural

O projeto foi dividido em pacotes por domínio de negócio para facilitar a escalabilidade e manutenção:

text
src/main/java/com/projeto/sistema/
├── configuracao/       Configurações de banco de dados e ambiente
├── estado/             Módulo funcional: Entidade, Repositório e Controle de Estados
├── entrada/            Modelagem e fluxo de entrada de estoque
├── fornecedor/         Modelagem e repositórios de fornecedores
├── funcionario/        Modelagem e repositórios de colaboradores
├── principal/          Rotas de navegação geral e dashboard
├── produto/            Modelagem e catálogo de produtos
└── venda/              Modelagem do fluxo de vendas e pedidos
