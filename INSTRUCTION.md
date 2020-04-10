MacBook前提

### 事前準備:

- MySQL8.0.19をローカル開発環境にインストールしてください
  - `brew update`
  - `brew install mysql@8.0.19`
  - `mysql.Sever stop` //もし自分の環境で別のバージョンのMySQLが走っていたら
  - `/usr/local/opt/mysql@8.0/bin/mysql.Sever start`

まずはMySQLを準備します。自分の環境に古いバージョンのMySQLがインストールされているかもしれないので
`brew install mysql@8.0.19`とバージョンを指定してインストールします。

なぜか公式ガイドにはbrewのインストールコマンドが載っていないのですが、上記のコマンドを使えばいいようです。

- [6.1 General Notes on Installing MySQL on macOS](https://dev.mysql.com/doc/mysql-installation-excerpt/8.0/en/osx-installation-notes.html)

ちなみに、上記公式ガイドに`brew services stop mysql`と書いてありますが、私の環境では`brew services`コマンドは動きませんでした…

- Mavenをインストールしてください
  - `brew install maven`

この一連のトレーニングではJavaのアプリケーションのビルドにMavenを使用します。
Gradleが好みの場合、Gradle対応のPull Requestを投げてもらえると助かります。

### 作業開始:

- このレポジトリをgit cloneしてください
  - `git clone git@github.com:mvrck-inc/training-akka-java-1-perparation.git`
- データベースのセットアップをしてください
  - `CREATE TABLE`を走らせてください(リンク)
  - `INSERT INTO`を走らせてください(リンク)
- アプリケーション層と一体になったWeb APIサーバを走らせてください
  - `mvn compile`
  - `mvn exec:java -Dexec.mainClass=com.mycompany.app.Main`
- wrkでベンチマークを走らせてください
  - `wrk -t2 -c4 -d5s -s wrk-scripts/order.lua http://localhost:8080/orders`
    - t2: 2 threads, c4: 4 http connections, d5: test duration is 5 seconds
    - クライアント側とサーバー側の実行結果を確認してください
- DBをSELECTして整合性が保たれていることを確認してください
  - SELECT *
- SELECT … FOR UPDATEのUPDATEを外して整合性が壊れることを確認してください
  - `ctrl+c` // mvn execで走らせたWeb APIサーバの停止
  - `DROP TABLE`を走らせてください(リンク)
  - `CREATE TABLE`を走らせてください(リンク)
  - `SELECT .. FOR UPDATE`から`FOR UPDATE`を消去してください(リンク)
  - `mvn compile`
  - `mvn exec:java -Dexec.mainClass=com.mycompany.app.Main`
  - `wrk -t2 -c4 -d5s -s wrk-scripts/order.lua http://localhost:8080/orders`
  - `SELECT *`

### 発展的内容:

- jmhを利用して、akka-httpに影響されない、JavaコードとDB部分のみのパフォーマンス測定をしてください
- イベント、ユーザ、席種を考えるたERダイアグラムを考えてください、その際注文処理で整合性を保たなければならないテーブルを確認してください
- カテゴリ検索、地域検索、ワード検索を実現するテーブルを考えてください
- コンサート以外に、スポーツや映画、入場券のみイベントを実現するテーブルを考えてください
