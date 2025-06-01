![Texto alternativo](https://a7.net.br/api/files?src=%2Fuploads%2F2023%2F11%2Fbf27711c502fb6b8c6d39b701.png&quality=80&width=1080)

# Sistema de Biblioteca

Um sistema simples desenvolvido em Java com Swing para o desafio da Alpha7 Software para cadastro e gerenciamento de livros,
autores e editoras.

Contém uma interface gráfica amigável e utiliza Hibernate para persistência de dados com um banco H2 em memória.

A principal proposta dos códigos é demonstrar a capacidade de criar uma aplicação Java com uma interface gráfica 
funcional e interativa e clean, além de integrar com APIs externas para facilitar o cadastro de livros.

### Usabilidades 
- Foi integrado em todas as telas de CRUD (livros, editoras e autores) a possibilidade de pesquisar por qualquer 
campo da entidade carregada na tabela (DTO's) que não possuem a anotação `@NotFilterable`, permitindo uma busca rápida e eficiente;


- Em cada linha das tabelas, foram adicionados botões de ação para editar e excluir os registros;


- As telas de CRUD também contam com botões (acima da tabela) para incluir novos registros e atualizar a tabela;


- Na tela de livros, foi implementada a funcionalidade de importar livros via arquivo CSV,
  atualizando os dados caso o livro já exista no banco de dados. Devido à implementação modular, essa funcionalidade 
pode ser facilmente adaptada para outras entidades (autores e editoras), já que para elas são usadas um de DTO 
específico para importação de CSV (como a `CSVImportableBookDTO` que implementam a interface `CSVImportable`), onde conta com
implementações de anotações para definir os campos que serão importados e como serão tratados. Exemplo:
  - `@CsvProperties(csvConverter = ConverterListPublishers.class)`;



- Na tela com o formulário de cadastro de livros, foi implementada a funcionalidade de buscar informações do livro via ISBN, 
  utilizando a API do Open Library. Ao informar o ISBN e o campo perder o foco, os dados do livro são preenchidos automaticamente, 
  facilitando o cadastro. Caso algum campo não seja retornado pela API, ele permanece em branco para o usuário preencher manualmente.


- Foi implementada um campo reutilizável de multisseletor para selecionar múltiplos autores, editoras, e livros semelhantes,
  permitindo uma experiência de usuário mais fluida e intuitiva. Esse componente pode ser facilmente reutilizado em outras telas 
  que necessitem de seleção múltipla de entidades.


- Na pasta `src/main/resources`, foram adicionados:
  - Arquivo de configuração do banco de dados H2 (hibernate.cfg.xml) para facilitar a conexão e configuração do banco de dados;
- Pasta com os ícones utilizados na interface gráfica (icons) para manter a consistência visual da aplicação;
- CSV de exemplo (livros.csv) para demonstrar a funcionalidade de importação de livros via arquivo CSV.

![Java](https://img.shields.io/badge/Java-8-blue?style=flat-square&logo=java&logoColor=white)
![Java](https://img.shields.io/badge/Java-007396?style=flat-square&logo=java&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-007396?style=flat-square&logo=java&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=flat-square&logo=hibernate&logoColor=white)
![H2 Database](https://img.shields.io/badge/H2%20Database-4FA94D?style=flat-square&logo=h2database&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat-square&logo=apache-maven&logoColor=white)




## 📚 Sobre o Projeto

📚 Requisitos de Funcionalidade
- Criar a entidade/tabela Livro com os seguintes campos:
  - Título
  - Autores
  - Data de Publicação
  - ISBN
  - Editora
  - Livros Semelhantes (relacionamento)

- Utilizar JPA/Hibernate para persistência dos dados.

- Criar cadastro de livros com:

  - Tela de listagem (com botões: Incluir, Editar, Excluir)
  - Telas separadas para incluir, editar e excluir livros
  - Criar pesquisa de livros por qualquer campo do banco (filtro por título, autor, ISBN etc.)
  - Interfaces gráficas feitas com Java Swing

🌐 Integração com WebService (ISBN)
   - Permitir cadastro automático por ISBN usando API:
     - Exemplo: https://openlibrary.org/isbn/9780140328721.json

   - Preencher os dados retornados automaticamente
   - Campos ausentes na resposta podem ficar em branco

📁 Importação de Arquivo
   - Importar dados de livros via arquivo:
   - Formato: .CSV
   - Se o livro já existir, atualizar os dados

✅ Tecnologias mínimas exigidas
   - Java
   - Swing (interface)
   - SQL (banco relacional)
   - Hibernate

## 🚀 Tecnologias Utilizadas

- Java 8
- Swing (GUI)
- Maven
- Hibernate (JPA)
- H2 Database (banco em memória)

## 💻 Funcionalidades

   - Tabela interativa com botões de ação (editar e excluir)
   - Componentes reutilizáveis para formulários e tabelas
   - ‘Interface’ modular com navegação por botões superiores

