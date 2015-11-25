# 概要

Spring BootとORMにjOOQを使用して開発した簡単な検索アプリケーションです。

**環境**

* Java 1.8.0_60
* Spring-Boot 1.3.0.M5
 * spring-boot-starter-jooq
* jOOQ 3.6.2
* MySQL 5.6.25


## 事前準備

### データベースの作成

データベースにMySQL 5.6.25を使用します。

* データベース名： actor_db
* ユーザー: actor_user / actor_pass

```
create database if not exists actor_db;

create user 'actor_user'@'localhost' identified by 'actor_pass';

grant all on actor_db.* to 'actor_user'@'localhost';
```

### テーブル、初期データおよびjavaコードの生成

下記のコマンドを実行するとテーブルと初期データ作成し、テーブルからjavaコードを生成します。

```
> mvn clean generate-sources -Pgenerate
```

#### sql-maven-plugin

テーブルの作成やデータの投入はmavenのsql-maven-pluginを使用します。

プラグインによって実行されるsqlは`src\main\resources\sql`ディレクトリに格納しています。

* 01-schema.sql
* 02-data.sql


#### jooq-codegen-maven

javaコードの生成はjooq-codegen-mavenを使用します。

このプラグインはjOOQが開発しているコードジェネレータ(jooq-codegen)をmavenから使用できるようにしたものです。
対象データベースのスキーマからjOOQで使用するモデルクラスやDAOクラスなどを生成することができます。




## 実行

MySQLサーバーを起動した状態で、下記のmvnコマンドでアプリケーションを実行します。

```bash
> mvn spring-boot:run
```

http://localhost:9000/actor

