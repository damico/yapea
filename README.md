yapea
=====

Yet Another Picture Encryption Application

=====
O que é: 

Uma aplicação simples, para Android que permite a encriptação de imagens. Esta aplicação é gratuita e de código-fonte aberto (GPL v2). Esta aplicação foi desenvolvida no laboratório de P&D da empresa TIX11.

=====
Pré-requisitos de funcionamento:

Smartphone Android versão maior ou igual 4.0.

=====
O que significa YAPEA?

É um acrônimo das seguintes palavras da língua inglesa: Yet Another Picture  Encryption Application.
A tradução para a língua portuguesa é: Mais uma Aplicação de Encriptação de Imagens.

=====
Como é feita a encriptação de imagens na Yapea?

Através de criptografia simétrica, nos algoritmos AES (CBC/PKCS5Padding) ou Blowfish (CFB/NoPadding). Os vetores de inicialização são gerados através da coleta de dados únicos do smartphone.

=====
Qual é o tipo de chave criptográfica?

A chave é de 256 bits, derivada de uma senha criada pelo usuário. A derivação é feita por PBKDF2, e o salt para derivação é gerado através da coleta de dados únicos do smartphone.
A chave criptográfica é armazenada em arquivo de configuração, para ser verificada na primeira utilização do aplicativo, quando o smartphone é ligado. Uma vez, verificada a chave, a mesma é encriptada e armazenada em memória, porém a qualquer momento, o usuário pode escolher apagar o cache de memória que contém a chave.

=====
Características gerais:

Resetar a aplicação: O usuário pode a qualquer momento apagar todos os dados da aplicação, inclusive os arquivos de configuração.
Pânico: O usuário pode configurar uma senha de pânico, para que quando digitada, a mesma apague todas as imagens encriptadas armazenadas.
Linguagens: A aplicação está traduzida para as línguas portuguesa e inglesa.

=====
Capturas de Tela:

https://picasaweb.google.com/113557199789179991340/Yapea?authuser=0&feat=directlink

=====
Autor:

José Ricardo de Oliveira Damico <damico@tix11.com>