JavaによるAkkaトレーニング第1回 

## リレーショナルDBトランザクションによる排他制御

Akkaは状態を持つ非同期処理を実装するのに有用なツールキットです。
しかし、まずトレーニングの第1回ではその準備のためAkkaによる非同期処理自体は紹介せず、リレーショナルDBのトランザクションによる非同期処理の排他制御を復習します。
これは伝統的な3層アーキテクチャ、つまりWeb層・アプリケーション層・DB層をもつモノリシックなバックエンドでは典型的な手法です。

以下に課題を用意したので、それをこなすことがこのトレーニングのゴールです。
課題の提出方法は後ほど紹介しますが、課題を通じて手を動かすとともに、トレーナーと対話することで学びを促進することが狙いです。

- [課題提出トレーニングのポリシー](./POLICIES.md)
- [次回のトレーニング: Akkaアクターを用いた非同期処理](https://github.com/mvrck-inc/training-akka-java-2-actor)

## この課題で身につく能力

- RDBのトランザクションを使って２テーブル間の整合性を保つ
- ２テーブルを同時に更新するメソッドのパフォーマンスを測定できる

## 課題

MacBook前提。

### 事前準備:

- MySQL8.0.19をローカル開発環境にインストールしてください
  - `brew update`
  - `brew install mysql@8.0.19`
  - `mysql.Sever stop` //もし自分の環境で別のバージョンのMySQLが走っていたら
  - `/usr/local/opt/mysql@8.0/bin/mysql.Sever start`
- Mavenをインストールしてください
  - `brew install maven`

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

## 説明

- [課題背景](./BACKGROUND.md)
- [課題提出方法](./SUBMIT.md)
- [課題手順の詳細](./DETAILES.md)

## 参考文献・資料