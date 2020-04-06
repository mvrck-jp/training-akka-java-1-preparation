伝統的な3層アーキテクチャ、つまりWeb層・アプリケーション層・DB層からなるモノリシックなバックエンドでは、DBのトランザクションを利用して非同期処理の排他制御を行うことが典型的です。
今回はその復習をして、次回のトレーニングでakkaアクターによる非同期制御を学ぶ時に差異がわかるようにします。

<img width=640 src="https://user-images.githubusercontent.com/7414320/78578847-0b060880-786b-11ea-900b-c6b01b0a6351.png">

非同期処理の排他制御が正しく実装されていないのは、本番で動くアプリケーションでも起こりうるものです。
初心者のみが犯すミスや、実験用に作ったアプリケーションでのみ起こる問題ではありません。
下記の記事の例にあるように、間違った排他制御が発覚するのはアクセス量が増えて競合が発生したときが多いので、排他制御のミスは最も起きてほしくない時に起きる厄介なものです。

- [嵐のコンサートがあるとダブルブッキングしてしまうホテル予約システムを作ってみた - 徳丸浩の日記](https://blog.tokumaru.org/2015/05/blog-post.html)
> 今年の5月1日に、仙台市内のホテルで多重予約のトラブルが発生したと報道されています。

リレーショナルデータベースを使っている場合、2テーブル間の整合性を保つにはトランザクションを使うのが普通でしょう。
トランザクションとロックの正しい組み合わせで、パフォーマンスへの影響を最小限に抑えつつ、データの整合性を保ちます。

このトレーニングでは題材として、チケット販売システムのバックエンドでの処理を扱います。
もちろん上記例で紹介したホテルの予約システムでも本来は構いませんし、eコマースシステムでも構いません。課題の出題者であるリチャードの趣味と、よりリアルタイム性を強調するためチケット販売システムとしています。
チケット販売システムのすべてを実装すると大変なので、非同期処理とDB排他制御を学べる最小部分を実装します。(下図)

<img width=640 src="https://user-images.githubusercontent.com/7414320/78578938-2cff8b00-786b-11ea-883d-084ee4f7ccb9.png">

パフォーマンスにDBトランザクションの使用がどれくらい影響するか計測するために、wrkを使ったベンチマークを行いましょう。
wrkはHTTPサーバーのマイクロ・ベンチマーキングを行うツールです。wrkはHTTP用のマイクロベンチマーキングツールとしては性能がよく使いやすいので採用しました。

今回はローカルのマシンでベンチマーキングを行うので、結果の数値自体を単体で見て大きな意味を持つわけではありません。
しかしAkkaアクターを用いた実装のベンチマーク結果と比べる、さらにはAWSなどより本番環境に近い状態でベンチマークを走らせるとより有用性の高い情報が得られます。

Akkaの機能はアクターを使った非同期処理にとどまりません。一連のトレーニングではAkkaのアクター、イベントソーシング、CQRS、クラスタリングなどの機能を動かします。