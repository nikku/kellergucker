# kellergucker

A stock value tracker application.


## Features

* Configure shares + triggers
* Monitor stock prices
* Notify via Email on trigger reached
* Show overview page __TBD__

See [docs](./docs) for detailed usage instructions.


## Development

Use [amvn](https://github.com/nikku/amvn) for awesome life-reload experience during development.

```
npm install
npm run env:up
npm run dev
```


## Building

```
mvn clean package
```


## Running

```
DB_MIGRATE=true java -jar target/app-$VERSION-fat.jar
```


### Configuration

Specify application configuration parameters via environment entries:

```
DB_URL=...           # postgres JDBC url
DB_MIGRATE=false     # true to migrate app on start

PRODUCTION=false     # running in production?
ADMIN_KEY=null       # secret for creating new users
PORT=8080            # application port
```