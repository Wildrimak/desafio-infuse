# Desafio Infuse

Este é o projeto do **Desafio Infuse**.

## 📦 Requisitos

- Git
- Docker
- Docker Compose

## 🚀 Instruções para rodar o projeto

1. Clone **o repositório (backend)** em sua máquina local:

```bash
git clone https://github.com/Wildrimak/desafio-infuse.git
```

2. Clone também o repositório do **frontend**:

```bash
git clone https://github.com/Wildrimak/desafio-infuse-frontend.git
```

> Ambos os projetos precisam estar disponíveis localmente para a aplicação funcionar corretamente.

3. Acesse o diretório do backend:

```bash
cd desafio-infuse/infra
```

4. Suba os containers com Docker Compose:

```bash
docker compose up -d
```

5. Agora, basta abrir o navegador e acessar:

```
http://localhost
```

A aplicação estará disponível na **porta 80**.

## 🛠 Tecnologias

- Java 21 + Spring Boot
- PostgreSQL (via Docker)
- Angular (frontend separado)

## 📂 Estrutura dos repositórios

- [Backend - desafio-infuse](https://github.com/Wildrimak/desafio-infuse)
- [Frontend - desafio-infuse-frontend](https://github.com/Wildrimak/desafio-infuse-frontend)
