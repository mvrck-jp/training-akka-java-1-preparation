### 事前準備:

MacBook前提。

- MySQL8.0.19をローカル開発環境にインストールしてください
  - `brew update`
  - `brew install mysql@8.0.19`
  - `mysql.Sever stop` //もし自分の環境で別のバージョンのMySQLが走っていたら
  - `/usr/local/opt/mysql@8.0/bin/mysql.Sever start`
  - 解答サンプルがMySQL　8.0.19を前提としているため([pom.xmls参照](./pom.xml))、異なるバージョンをすでにインストールしている場合、追加でインストールし直して貰う必要があります。

- Mavenをインストールしてください
  - `brew install maven`
  - Mavenに関してはMave 3.x系であれば動くのではないかと思います。

### 作業開始:

MacBook前提。

<p align="center">
  <img width=640 src="https://user-images.githubusercontent.com/7414320/78578847-0b060880-786b-11ea-900b-c6b01b0a6351.png">
</p>

- このリポジトリをgit cloneしてください
  - `git clone git@github.com:mvrck-inc/training-akka-java-1-perparation.git`
  
この課題はgit clone下ソースコードをそのまま使うことで、自分で新たにソースコードを書くことなく実行できるようになっています。
もちろん、自分で書き方を覚えたい方や、最後の発展的内容に取り組みたい方はご自分でぜひソースコードを書いてみてください。

---  
- データベースのセットアップをしてください
  - `CREATE DATABASE`を走らせてください([リンク](./dbsetup/create_database.sql))
  - `CREATE TABLE`を走らせてください([リンク](./dbsetup/create_tables.sql))
  - `INSERT INTO`を走らせてください([リンク](./dbsetup/insert_into.sql))

`ticket_stocks`テーブルは以下のようになります。

| ticket_id | quantity |
| --------- | -------- |
| 1         | 100      |
| 2         | 200      |

`orders`テーブルはこの時点では空のテーブルです。

| id   | ticket_id | user_id | quantity |
| ---- | --------- | ------- | -------- |

`http://localhost:8080/orders` にPOSTリクエストを送るたびorderが追加されていきます。

| id   | ticket_id | user_id | quantity |
| ---- | --------- | ------- | -------- |
| 1    | 1         | 2       | 1        |
| 2    | 1         | 2       | 1        |
| ...  | ...       | ...     | ...      |
| 100  | 1         | 2       | 1        |

---  
- `.env.default`ファイル([リンク](./.env.default))を`.env`にファイル名変更し、適切な`DATABASE.USER`と`DATABASE.PASSWORD`を設定してください
---
- アプリケーションを走らせてください
  - `mvn compile`
  - `mvn exec:java -Dexec.mainClass=com.mycompany.app.Main`

以下のように表示されるはずです。

```
[INFO] Scanning for projects...
[INFO]
[INFO] -------------< org.mvrck.training:akka-java-1-preparation >-------------
[INFO] Building app 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- exec-maven-plugin:1.6.0:java (default-cli) @ akka-java-1-preparation ---
AppConfig: Tried to load JDBC properties from .env file. (with environment variables as fallback)
Server online at http://localhost:8080/
```

- curlでデータを挿入してください
  - `curl -X POST -H "Content-Type: application/json" -d "{\"ticket_id\": 1, \"user_id\": 2, \"quantity\": 1}"  http://localhost:8080/orders`
  - レスポンスを確認してください
  - アプリケーション側のログを確認してください

クライアント側ログはこちら

```
>curl -X POST -H "Content-Type: application/json" -d "{\"ticket_id\": 1, \"user_id\": 2, \"quantity\": 1}"  http://localhost:8080/orders
Put request received: ticket_id = 1, user_id = 2
```

サーバー側ログはこちらです。
```
Apr 18, 2020 3:48:42 PM org.seasar.doma.jdbc.tx.LocalTransaction begin
INFO: [DOMA2063] The local transaction "794948527" is begun.
Apr 18, 2020 3:48:42 PM com.mycompany.dao.TicketStockDaoImpl selectById
INFO: [DOMA2220] ENTER  : CLASS=com.mycompany.dao.TicketStockDaoImpl, METHOD=selectById
Apr 18, 2020 3:48:42 PM com.mycompany.dao.TicketStockDaoImpl selectById
INFO: [DOMA2076] SQL LOG : PATH=[META-INF/com/mycompany/dao/TicketStockDao/selectById.sql],
select * from ticket_stocks where ticket_id = 1 FOR UPDATE
Apr 18, 2020 3:48:42 PM com.mycompany.dao.TicketStockDaoImpl selectById
INFO: [DOMA2221] EXIT   : CLASS=com.mycompany.dao.TicketStockDaoImpl, METHOD=selectById
Apr 18, 2020 3:48:42 PM com.mycompany.dao.TicketStockDaoImpl update
INFO: [DOMA2220] ENTER  : CLASS=com.mycompany.dao.TicketStockDaoImpl, METHOD=update
Apr 18, 2020 3:48:42 PM com.mycompany.dao.TicketStockDaoImpl update
INFO: [DOMA2076] SQL LOG : PATH=[null],
update ticket_stocks set quantity = 99 where ticket_id = 1
Apr 18, 2020 3:48:42 PM com.mycompany.dao.TicketStockDaoImpl update
INFO: [DOMA2221] EXIT   : CLASS=com.mycompany.dao.TicketStockDaoImpl, METHOD=update
Apr 18, 2020 3:48:42 PM com.mycompany.dao.OrderDaoImpl insert
INFO: [DOMA2220] ENTER  : CLASS=com.mycompany.dao.OrderDaoImpl, METHOD=insert
Apr 18, 2020 3:48:42 PM com.mycompany.dao.OrderDaoImpl insert
INFO: [DOMA2076] SQL LOG : PATH=[null],
insert into orders (ticket_id, user_id, quantity) values (1, 2, 1)
Apr 18, 2020 3:48:42 PM com.mycompany.dao.OrderDaoImpl insert
INFO: [DOMA2221] EXIT   : CLASS=com.mycompany.dao.OrderDaoImpl, METHOD=insert
Apr 18, 2020 3:48:42 PM org.seasar.doma.jdbc.tx.LocalTransaction commit
INFO: [DOMA2067] The local transaction "794948527" is committed.
Apr 18, 2020 3:48:42 PM org.seasar.doma.jdbc.tx.LocalTransaction commit
INFO: [DOMA2064] The local transaction "794948527" is ended.
```

| ticket_id | quantity |
| --------- | -------- |
| 1         | 99       |
| 2         | 200      |

`orders`テーブルはこの時点では空のテーブルです。

| id   | ticket_id | user_id | quantity |
| ---- | --------- | ------- | -------- |
| 1    | 1         | 2       | 1        |

---  
- wrkでベンチマークを走らせてください
  - `wrk -t2 -c4 -d5s -s wrk-scripts/order.lua http://localhost:8080/orders`
    - `-t2`: 2 threads
    - `-c4`: 4 http connections
    - `-d5`: 5 seconds of test duration
    - クライアント側とサーバー側の実行結果を確認してください

---  
- DBをSELECTして不可分性と一貫性が保たれていることを確認してください([リンク](./dbsetup/select.sql))

| ticket_id | quantity |
| --------- | -------- |
| 1         | 0        |
| 2         | 200      |

| id   | ticket_id | user_id | quantity |
| ---- | --------- | ------- | -------- |
| 1    | 1         | 2       | 1        |
| 1    | 1         | 2       | 1        |
| ...  | ...       | ...     | ...      |
| ...  | ...       | ...     | ...      |
| 100  | 1         | 2       | 1        |

---  
- SELECT … FOR UPDATEのUPDATEを外して一貫性が壊れることを確認してください
  - `ctrl+c` // mvn execで走らせたWeb APIサーバの停止
  - `CREATE DATABASE`を走らせてください([リンク](./dbsetup/create_database.sql))
  - `CREATE TABLE`を走らせてください([リンク](./dbsetup/create_tables.sql))
  - `INSERT INTO`を走らせてください([リンク](./dbsetup/insert_into.sql))
  - DOMAの.sqlファイルに記述した`SELECT .. FOR UPDATE`から`FOR UPDATE`を消去してください([リンク](./src/main/resources/META-INF/com/mycompany/dao/TicketStockDao/selectById.sql))
  - `mvn clean`
  - `mvn compile`
  - `mvn exec:java -Dexec.mainClass=com.mycompany.app.Main`
  - `wrk -t2 -c4 -d5s -s wrk-scripts/order.lua http://localhost:8080/orders`
  - DBをSELECTして一貫性が壊れていることを確認してください([リンク](./dbsetup/select.sql))

### 発展的内容:

- jmhを利用して、akka-httpに影響されない、JavaコードとDB部分のみのパフォーマンス測定をしてください
- イベント、ユーザ、席種を考えるたERダイアグラムを考えてください、その際注文処理で不可分性と一貫性を保たなければならないテーブルを確認してください
- カテゴリ検索、地域検索、ワード検索を実現するテーブルを考えてください
- コンサート以外に、スポーツや映画、入場券のみイベントを実現するテーブルを考えてください
- 履歴管理のためにトリガとAuditテーブルを設定してください、その上でパフォーマンス測定してください
  - その前に、トランザクション内でトリガがテーブルをアップデートする場合、トランザクション内の更新として扱われるのか確認
  - [発火元クエリのトランザクションの一部として処理されるようです。](https://heartbeats.jp/hbblog/2013/01/mysql.html)