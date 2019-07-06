# DISCIPLINA: Banco de Dados 2
# ALUNO: 17409065
##### ARQUIVOS
<span style="color:red">
https://github.com/BrighterMind/UNICESUMAR/blob/master/BD2_MAPA.pdf
https://github.com/BrighterMind/UNICESUMAR/blob/master/BD2_MAPA.md
</span>
<BR>

# ENUNCIADO

* A equipe de desenvolvimento de uma empresa de software irá desenvolver um sistema de vendas, onde iremos precisar controlar os produtos vendidos, para quem foi vendido e em qual data, assim como as comissões dos vendedores. Para isto, contratou você para criar o banco de dados e os requisitos funcionais necessários para atender as regras do negócio. 

* Este sistema será utilizado por: 
    - Coordenadores, com permissão de cadastrar clientes, produtos e vendedores.
    - Faturistas com acesso apenas de visualização dos cadastros de clientes, produtos e vendedores, porém com acesso ao cadastro de vendas realizadas.

* Segue diagrama com o modelo de dados do projeto.

![enter image description here](https://raw.githubusercontent.com/BrighterMind/UNICESUMAR/master/BD2_MAPA.png)

Com base nas informações acima, execute as seguintes tarefas:

1. Crie o Schema e as tabelas conforme o diagrama apresentado.

3. Crie uma função para calcular e retornar a comissão do vendedor.
    - A função precisa receber o valor total de venda do item (valor_venda) e o percentual de comissão do vendedor (percentual_comissao).
    - A função irá retornar o valor da comissão do vendedor calculada (valor_venda * (percentual_comissao / 100.0)).

4. Crie um comando de consulta em SQL que retorne a comissão dos vendedores por produto:
    - Id do vendedor.
    - Nome do vendedor.
    - Id do produto.
    - Nome do produto.
    - Quantidade vendida.
    - Valor vendido.
    - Percentual de comissão.
    - Valor de comissão.

5. Altere a tabela “vendas_itens”, adicionando as colunas:
    - valor total do item (valor_total) como Decimal(12,2)
    - percentual de comissão do item (perc_comissao) como Decimal(4,2)
    - valor de comissão do item (valor_comissao) como Decimal(12,2)

6. Agora que temos as colunas “valor_total”, “perc_comissao” e “valor_comissao”, precisamos que o banco de dados calcule estes valores quando um item for inserido. Crie uma trigger que calcule e salve os campos “valor_total”, “perc_comissão” e “valor_comissao” conforme abaixo:
    - valor_total = quantidade * valor_unitario.
    - perc_comissao = buscar o percentual da comissão do cadastro de vendedor.
    - valor_comissao = valor_total * (perc_comissao / 100.0) ou utilizar a função criada no item “b”.

7. Crie 1 usuário coordenador com permissão de leitura, gravação e exclusão nas tabelas clientes, produtos e vendedores. Este usuário pode apenas visualizar as vendas emitidas.

# RESPOSTAS

## Script MySQL

### 1
    CREATE SCHEMA UNICESUMAR_BD2_17409065;

    CREATE TABLE UNICESUMAR_BD2_17409065.PRODUTOS
    (
        ID              INT         NOT NULL
       ,NOME        VARCHAR( 100 )  NOT NULL
       ,DESCRICAO      TEXT             NULL
       ,VALOR_VENDA DECIMAL( 12,2 ) NOT NULL
    );

    ALTER TABLE UNICESUMAR_BD2_17409065.PRODUTOS
    ADD PRIMARY KEY ( ID );


    CREATE TABLE UNICESUMAR_BD2_17409065.CLIENTES
    (
        ID              INT        NOT NULL
       ,NOME        VARCHAR( 100 ) NOT NULL
       ,TELEFONE    VARCHAR(  20 )     NULL
       ,CELULAR     VARCHAR(  20 ) NOT NULL
       ,EMAIL       VARCHAR( 100 )     NULL
       ,ENDERECO    VARCHAR( 100 )     NULL
       ,NUMERO          INT            NULL
       ,COMPLEMENTO VARCHAR(  50 )     NULL
       ,BAIRRO      VARCHAR( 100 )     NULL
       ,CIDADE      VARCHAR( 100 )     NULL
       ,CEP         VARCHAR(  10 )     NULL
    );

    ALTER TABLE UNICESUMAR_BD2_17409065.CLIENTES
    ADD PRIMARY KEY ( ID );


    CREATE TABLE UNICESUMAR_BD2_17409065.VENDEDORES
    (
        ID                INT         NOT NULL
       ,NOME          VARCHAR( 100 )  NOT NULL
       ,CELULAR       VARCHAR(  20 )  NOT NULL
       ,EMAIL         VARCHAR( 100 )      NULL
       ,PERC_COMISSAO DECIMAL( 4, 2 )     NULL
    );

    ALTER TABLE UNICESUMAR_BD2_17409065.VENDEDORES
    ADD PRIMARY KEY ( ID );


    CREATE TABLE UNICESUMAR_BD2_17409065.VENDAS
    (
        ID           INT NOT NULL
       ,DATA_VENDA  DATE NOT NULL
       ,ID_CLIENTE   INT NOT NULL
       ,ID_VENDEDOR  INT NOT NULL
    );

    ALTER TABLE UNICESUMAR_BD2_17409065.VENDAS
    ADD PRIMARY KEY ( ID );

    ALTER TABLE UNICESUMAR_BD2_17409065.VENDAS
    ADD CONSTRAINT CLIENTES_FK_VENDAS
    FOREIGN KEY ( ID_CLIENTE )
    REFERENCES UNICESUMAR_BD2_17409065.CLIENTES ( ID );

    ALTER TABLE UNICESUMAR_BD2_17409065.VENDAS
    ADD CONSTRAINT VENDEDORES_FK_VENDAS
    FOREIGN KEY ( ID_VENDEDOR )
    REFERENCES UNICESUMAR_BD2_17409065.VENDEDORES ( ID );


    CREATE TABLE UNICESUMAR_BD2_17409065.VENDAS_ITENS
    (
        ID                 INT          NOT NULL
       ,ID_VENDAS          INT              NULL
       ,ID_PRODUTOS        INT              NULL
       ,QUANTIDADE     DECIMAL( 12, 2 )     NULL
       ,VALOR_UNITARIO DECIMAL( 12, 2 )     NULL
    );

    ALTER TABLE UNICESUMAR_BD2_17409065.VENDAS_ITENS
    ADD PRIMARY KEY ( ID );

    ALTER TABLE UNICESUMAR_BD2_17409065.VENDAS_ITENS
    ADD CONSTRAINT VENDAS_FK_VENDAS_ITENS
    FOREIGN KEY ( ID_VENDAS )
    REFERENCES UNICESUMAR_BD2_17409065.VENDAS ( ID );

    ALTER TABLE UNICESUMAR_BD2_17409065.VENDAS_ITENS
    ADD CONSTRAINT PRODUTOS_FK_VENDAS_ITENS
    FOREIGN KEY ( ID_PRODUTOS )
    REFERENCES UNICESUMAR_BD2_17409065.PRODUTOS ( ID );

### 2
    DELIMITER $$
    CREATE FUNCTION UNICESUMAR_BD2_17409065.CALCULAR_COMISSAO_VENDEDOR
    (
        VLR_TOTAL_VENDA_ITEM       DECIMAL( 12, 2 )
       ,PERC_COMISSAO_VENDEDOR     DECIMAL( 4, 2 )
    )
    RETURNS DECIMAL( 12, 2 )
    DETERMINISTIC
    BEGIN
        DECLARE V_VLR_RETORNO DECIMAL( 12, 2 );

        SET V_VLR_RETORNO = 0;

        SELECT ( VLR_TOTAL_VENDA_ITEM * ( PERC_COMISSAO_VENDEDOR / 100 ) ) AS VLR_COMISSAO_VENDEDOR
          INTO V_VLR_RETORNO
          FROM DUAL;

        RETURN V_VLR_RETORNO;
    END$$
    DELIMITER ;

### 3
    SELECT     TB01.ID             AS ID_DO_VENDEDOR
              ,TB01.NOME           AS NOME_DO_VENDEDOR
              ,TB04.ID             AS ID_DO_PRODUTO
              ,TB04.NOME           AS NOME_DO_PRODUTO
              ,TB03.QUANTIDADE     AS QUANTIDADE_VENDIDA
              ,TB03.VALOR_UNITARIO AS VALOR_VENDIDO
              ,TB01.PERC_COMISSAO  AS PERCENTUAL_DE_COMISSAO
              ,UNICESUMAR_BD2_17409065.CALCULAR_COMISSAO_VENDEDOR
               ( 
                    ( TB03.QUANTIDADE * TB03.VALOR_UNITARIO )
                   ,TB01.PERC_COMISSAO 
               ) AS COMISSAO_VENDEDOR
          FROM UNICESUMAR_BD2_17409065.VENDEDORES       AS TB01
    INNER JOIN UNICESUMAR_BD2_17409065.VENDAS           AS TB02
            ON TB02.ID_VENDEDOR = TB01.ID
    INNER JOIN UNICESUMAR_BD2_17409065.VENDAS_ITENS     AS TB03
            ON TB03.ID_VENDAS = TB02.ID
    INNER JOIN UNICESUMAR_BD2_17409065.PRODUTOS         AS TB04
            ON TB04.ID = TB03.ID_PRODUTOS;

### 4
    ALTER TABLE UNICESUMAR_BD2_17409065.VENDAS_ITENS
    ADD
    (
        VALOR_TOTAL      DECIMAL( 12, 2 ) NULL
       ,PERC_COMISSAO    DECIMAL(  4, 2 ) NULL
       ,VALOR_COMISSAO   DECIMAL( 12, 2 ) NULL
    );

### 5
    DELIMITER $$
    CREATE TRIGGER UNICESUMAR_BD2_17409065.TRG_CALCULA_CAMPOS
    AFTER INSERT ON UNICESUMAR_BD2_17409065.VENDAS_ITENS
    FOR EACH ROW
    BEGIN
        UPDATE UNICESUMAR_BD2_17409065.VENDAS_ITENS AS TB00
           SET TB00.VALOR_TOTAL = ( TB00.QUANTIDADE * TB00.VALOR_UNITARIO )
              ,TB00.PERC_COMISSAO =
               (
                SELECT     TB01.PERC_COMISSAO
                      FROM UNICESUMAR_BD2_17409065.VENDEDORES       AS TB01
                INNER JOIN UNICESUMAR_BD2_17409065.VENDAS           AS TB02
                        ON TB02.ID_VENDEDOR = TB01.ID
                INNER JOIN UNICESUMAR_BD2_17409065.VENDAS_ITENS     AS TB03
                        ON TB03.ID_VENDAS = TB02.ID
                     WHERE 1 = 1
                       AND TB03.ID = TB00.ID
                       AND TB03.ID_VENDAS = TB00.ID_VENDAS
                       AND TB03.ID_PRODUTOS = TB00.ID_PRODUTOS
               )
              ,TB00.VALOR_COMISSAO = UNICESUMAR_BD2_17409065.CALCULAR_COMISSAO_VENDEDOR
               ( 
                    TB00.VALOR_TOTAL
                   ,TB00.PERC_COMISSAO
               );
    END$$
    DELIMITER ;

### 6
    CREATE USER 'coordenador'@'localhost' IDENTIFIED BY 'senha_coordenador';

    GRANT SELECT, INSERT, UPDATE, DELETE
    ON UNICESUMAR_BD2_17409065.CLIENTES
    TO 'coordenador'@'localhost';

    GRANT SELECT, INSERT, UPDATE, DELETE
    ON UNICESUMAR_BD2_17409065.PRODUTOS
    TO 'coordenador'@'localhost';

    GRANT SELECT, INSERT, UPDATE, DELETE
    ON UNICESUMAR_BD2_17409065.VENDEDORES
    TO 'coordenador'@'localhost';

    GRANT SELECT
    ON UNICESUMAR_BD2_17409065.VENDAS
    TO 'coordenador'@'localhost';

    GRANT SELECT
    ON UNICESUMAR_BD2_17409065.VENDAS_ITENS
    TO 'coordenador'@'localhost';
