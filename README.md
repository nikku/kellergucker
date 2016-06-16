# kellergucker

A stock value tracker application.


## Features

* Configure shares + triggers
* Monitor stock prices __TBD__
* Notify on trigger reached __TBD__


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