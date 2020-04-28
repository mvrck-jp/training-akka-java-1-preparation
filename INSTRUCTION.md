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

`ticket_stocks`テーブルは以下のようになります。このトレーニングでは`ticket_id = 1`の方のみ使います。

| ticket_id | quantity |
| --------- | -------- |
| 1         | 5000     |
| 2         | 2000     |

`orders`テーブルはこの時点では空のテーブルです。このテーブルにレコードが追加される度、`ticket_stocks`テーブルの`quantity`と一貫性を保たなくてはなりません。

| id   | ticket_id | user_id | quantity |
| ---- | --------- | ------- | -------- |


---  
- アプリケーションを走らせてください
  - `mvn compile`
  - `mvn exec:java -Dexec.mainClass=com.mycompany.app.Main`
  - もしMySQLのログイン・ユーザとパスワードを変更する場合、以下のJavaのプロパティを`-D`を使って渡してください。TypeSafe Configの仕組みによってconfigの値を上書きできます。([リンク](./src/main/resources/application.conf#L13L19))
  - `mvn exec:java -Dexec.mainClass=com.mycompany.app.Main -Ddatabase.user=xxx -Ddatabase.password=yyy`

以下のように表示されるはずです。

```
[INFO] Scanning for projects...
[INFO]
[INFO] -------------< org.mvrck.training:akka-java-1-preparation >-------------
[INFO] Building app 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- exec-maven-plugin:1.6.0:java (default-cli) @ akka-java-1-preparation ---
Server online at http://localhost:8080/
```

---
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

<p align="center">
  <img width=640 src="https://user-images.githubusercontent.com/7414320/80276175-cc54c700-8721-11ea-8ec5-d9a804324b00.png">
</p>

---
- DBをSELECTして不可分性と一貫性が保たれていることを確認してください([リンク](./dbsetup/select.sql))

`ticket_stocks`テーブルの`tikcet_id = 1`のレコードに関して、`quantity = 100`から`quantity = 99`に減っていることがわかります。

| ticket_id | quantity |
| --------- | -------- |
| 1         | 4999     |
| 2         | 2000     |

`orders`テーブルにはレコードが一件追加されています。`quantity`に関して一貫性は保たれています。

| id   | ticket_id | user_id | quantity |
| ---- | --------- | ------- | -------- |
| 1    | 1         | 2       | 1        |

---  
- ORMであるDOMAのログを無効にしてください([リンク](./src/java/main/config/AppConfig#L34L37))
  - 上記リンク先のコメント化した部分を、コメントではなく有効にしてください
  - `mvn compile`
  - `mvn exec:java -Dexec.mainClass=org.mvrck.training.app.Main`
  - ログのコンソール出力を続けると、次のステップで計測するベンチマークのパフォーマンスに影響します

本来なら、[`JdbcNoLogging`](./src/main/java/org/mvrck/training/config/JdbcNoLogging.java)でログを一切やめるという乱暴な方法でなく、
DOMAのロガーをSlf4J/logbackに接続して、非同期でロギングすることによりパフォーマンスの悪化を避けたいところです。
[application.conf](./src/main/resources/application.conf)と[logback.xml](./src/main/resources/logback.xml)を見ればわかるように、akka-http部分ではすでにSlf4J/logbackを使っています。
しかし、DOMAが提供している[UtilLoggingJdbcLogger ](https://github.com/domaframework/doma/blob/2.27.1/src/main/java/org/seasar/doma/jdbc/UtilLoggingJdbcLogger.java#L5L11)は`java.util.logging.Logger`を使っているため
Slf4Jに差し替えることが出来ません。どうしてもSlf4Jを使いたければ自分でDOMAのJdbcLoggerを拡張してSlf4Jを使うように実装せねばならず、かなり面倒です。
そこで、今回は`JdbcNoLogging`という一切ロギングを行わない方法にしました。
    
---  
- wrkでベンチマークを走らせてください
  - `wrk -t2 -c4 -d5s -s wrk-scripts/order.lua http://localhost:8080/orders`
    - `-t2`: 2 threads
    - `-c4`: 4 http connections
    - `-d5`: 5 seconds of test duration
    - `wrk-scripts/order.lua` ([リンク](./wrk-scrips/order.lua))
    - クライアント側とサーバー側の実行結果を確認してください

<p align="center">
  <img width=640 src="https://user-images.githubusercontent.com/7414320/80275884-56e7f700-871f-11ea-9aa5-caa84abe7ec7.png">
</p>

```
Running 5s test @ http://localhost:8080/orders
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    38.04ms   15.46ms 115.64ms   88.39%
    Req/Sec    53.21     15.05    80.00     70.00%
  534 requests in 5.02s, 100.12KB read
Requests/sec:    106.28
Transfer/sec:     19.93KB
```

---  
- DBをSELECTして不可分性と一貫性が保たれていることを確認してください([リンク](./dbsetup/select.sql))

`ticket_stocks`テーブルの`tikcet_id = 1`のレコードの`quantity`が減少しました。

| ticket_id | quantity |
| --------- | -------- |
| 1         | 4466     |
| 2         | 2000     |

その`quantity`の減少分だけ、`orders`テーブルには`ticket_id = 1, quantity=1`となるレコードが入っており、`ticket_stocks`テーブルと一貫性が保たれています。

| id   | ticket_id | user_id | quantity |
| ---- | --------- | ------- | -------- |
| 1    | 1         | 2       | 1        |
| 2    | 1         | 2       | 1        |
| ...  | ...       | ...     | ...      |
| ...  | ...       | ...     | ...      |
| 534  | 1         | 2       | 1        |

---  
- SELECT … FOR UPDATEのUPDATEを外して一貫性が壊れることを確認してください
  - `ctrl+c` // mvn execで走らせたWeb APIサーバの停止
  - `CREATE DATABASE`を走らせてください([リンク](./dbsetup/create_database.sql))
  - `CREATE TABLE`を走らせてください([リンク](./dbsetup/create_tables.sql))
  - `INSERT INTO`を走らせてください([リンク](./dbsetup/insert_into.sql))

`ticket_stocks`テーブル、`orders`テーブルはそれぞれ初期の状態に戻ります。

| ticket_id | quantity |
| --------- | -------- |
| 1         | 5000     |
| 2         | 2000     |

| id   | ticket_id | user_id | quantity |
| ---- | --------- | ------- | -------- |

  - DOMAの.sqlファイルに記述した`SELECT .. FOR UPDATE`から`FOR UPDATE`を消去してください([リンク](./src/main/resources/META-INF/com/mycompany/dao/TicketStockDao/selectById.sql))

  - `mvn clean`
  - `mvn compile`
  - `mvn exec:java -Dexec.mainClass=com.mycompany.app.Main`
  - `wrk -t2 -c4 -d5s -s wrk-scripts/order.lua http://localhost:8080/orders`
  - DBをSELECTして一貫性が壊れていることを確認してください([リンク](./dbsetup/select.sql))

`ticket_stocks`テーブル

| ticket_id | quantity |
| --------- | -------- |
| 1         | 4776     |
| 2         | 2000     |

`orders`テーブル

| id   | ticket_id | user_id | quantity |
| ---- | --------- | ------- | -------- |
| 1    | 1         | 2       | 1        |
| 2    | 1         | 2       | 1        |
| ...  | ...       | ...     | ...      |
| ...  | ...       | ...     | ...      |
| 548  | 1         | 2       | 1        |

`orders`テーブルに対して`SELECT count(*) FROM orders`を走らせると、以下のように`ticket_stocks`テーブルの`quantity`減少分`5000 - 4776 = 224`と`orders`テーブルのカウント`548`が一致しません。
一貫性が壊れています。

| count(*) |
| -------- |
| 548      |

### 発展的内容:

- jmhを利用して、akka-httpに影響されない、JavaコードとDB部分のみのパフォーマンス測定をしてください
- イベント、ユーザ、席種を考えるたERダイアグラムを考えてください、その際注文処理で不可分性と一貫性を保たなければならないテーブルを確認してください
- カテゴリ検索、地域検索、ワード検索を実現するテーブルを考えてください
- コンサート以外に、スポーツや映画、入場券のみイベントを実現するテーブルを考えてください
- 履歴管理のためにトリガとAuditテーブルを設定してください、その上でパフォーマンス測定してください
  - その前に、トランザクション内でトリガがテーブルをアップデートする場合、トランザクション内の更新として扱われるのか確認
  - [発火元クエリのトランザクションの一部として処理されるようです。](https://heartbeats.jp/hbblog/2013/01/mysql.html)

## 参考文献

- [HTTP POST - Everything curl](https://ec.haxx.se/http/http-post)
- [wg/wrk - Modern HTTP benchmarking tool](https://github.com/wg/wrk)
- [InnoDB Transaction Model - MySQL](https://dev.mysql.com/doc/refman/8.0/en/innodb-transaction-model.html)
- [Database Transactions, part 2: Examples - Barry Brown, YouTube](https://www.youtube.com/watch?v=PguCDI_fi3U)
- [Code Tools : jmh - OpenJDK](https://openjdk.java.net/projects/code-tools/jmh/)
