<p align="center">
        <img src="https://avatars.githubusercontent.com/u/109238400?s=400&u=e5b242311297e5a0b1c2a7e4efd42d523c158b59&v=4">
</p>

## Sobre

Essa aplicação tem como base o projeto desenvolvido em: [Freshwarehouse](https://github.com/javatastico/freshWarehouse).
Porém com um requisito extra para realizar gerencia de uma lista de desejos do usuário.


## Requisito desenvolvido

Após inserir um novo produto e o retorno trara uma lista de produtos com as sugestões relacionados aos 
produtudos inseridos

## Documentação

A documentação completa da história de usuário pode ser baixado em: [Documents](https://github.com/dlcdev/freshWarehouse-david/blob/4c421b49151ca7c206c17558efe90b1056937cb6/Documents/David%20Requisito%206%20-%20Template.pdf).


## Como subir o projeto

Ao inicializar o projeto serão criados alguns dados iniciais no banco. Estes dados são gerados por meio do script data.sql que
pode ser encontrado na pasta de resources. Caso não possua nenhum dado no seu banco mesmo após o levantamento do projeto, rode os seguintes comandos em ordem:

```SQL
use fresh_warehouse_db;

INSERT INTO warehouse(address, city, country, number, state) values('Address 1', 'City 1', 'Country 1', 1, 'State 1');
INSERT INTO section(name, id_warehouse, available_space) values('Fresh', 1, 100);
INSERT INTO seller(name) values('Seller Name');
INSERT INTO buyer(name) VALUES ('Buyer Name');
```

Após esta inicialização dos dados básicos, é necessário criar um produto através de uma requisição.
Para criar este produto, primeiramente,
importe no seu Postman a Collection [Documents](https://github.com/dlcdev/freshWarehouse-david/blob/45025004156a211a9d2d9c34b0a87e6bae10f3aa/Documents/collections/Products.json)
, depois disso, abra a requisição de "Post Product" e aperte para enviar. Caso a importação da collection
fornecida não tenha funcionado, realize uma requisição do tipo POST na rota:
http://localhost:8080/api/v1/fresh-products/product passando como body:

```JSON
{
    "name": "Apple",
    "price": 10.00,
    "sectionsId": [1],
    "sellerId": 1
}
```

Após esta requisição já podemos visitar os endpoints do requisito em si.
Inporte a colletcion [Collection test](https://github.com/dlcdev/freshWarehouse-david/blob/45025004156a211a9d2d9c34b0a87e6bae10f3aa/Documents/Suggestion%20Products.postman_collection.json)


## UML

<p align="center">
  <img src="https://github.com/MateusMeli/freshWarehouse-Mateus/blob/feature/US06_Wishlist/Documents/UML.png?raw=true">
</p>