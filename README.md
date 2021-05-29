# blindtest

This project is composed of two programs, the server and the client.

The client is written in TypeScript with the [Angular](https://angular.io)
framework.

The server is written in Kotlin with the [Ktor](https://ktor.io) framework.

## The Components

[Using the server](server/README.md)\
[Using the web client](web-client/README.md)

## Setup

### Automated

Running the [`setup.sh`](setup.sh) does everything for you.
It will create 2 dockers, one with an nginx server and the other
with the game's backend.

#### Dependencies

- Docker
- npm

### Custom

If you have a custom web server configuration, you have to compile
the [web-client](web-client/README.md) and the [server](server/README.md).

#### Dependencies

- maven
- npm

## Running

[`run.sh`](run.sh) will start the dockers and link them if you used
the automatic setup.

## Updating

[`update.sh`](update.sh) will update the dockers and the web client.

/!\ Only use this if you used the automatic setup.
