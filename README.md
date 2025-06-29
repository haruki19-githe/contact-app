## タイトル
### うちらのメモリー🤝


## 概要
本プロジェクトは、恋人との日々の連絡を記録・管理し、現在の「連続連絡日数」を算出・表示するWebアプリケーションのバックエンドAPIです。  
シンプルなAPIを通じて、連絡記録の追加、参照、更新、削除といった基本的なCRUD操作と、日付に基づいた連続日数計算を提供しています。




## なぜこれを作ったのか
きっかけは、遠距離恋愛をしている友人が「毎日LINEするべきかな、でも何の話題を話せばいいかわからないし、あまりLINEしてないんよね」と恋人との連絡の頻度について語っていたことです。

連絡は相手に安心と誠実さを与える大切なツールと私は考えています。特に遠距離カップルは連絡していないと自然消滅してしまいます。
そのため、毎日連絡できているか管理できるアプリが欲しいと考えつくらせて頂きました。
## デモ動画
https://github.com/user-attachments/assets/c09d98f7-01aa-4ced-836e-2c96c4cc9a23


## 環境
| Category    | Technology Stack                   |
|-------------|------------------------------------|
| language    | Java Oracle OpenJDK 21.0.4         |
| framework   | springBoot 3.5.3                   |
| Database    | MySQL                              |
| buildTool   | Gradle                             |
| APITestTool | postman                            |  
| etc.        | Git, GitHub                        |

## 機能一覧
| 機能                 | HTTPメソッド | エンドポイント (/api/contact-logs を基点) | 説明                                                                                                                                                                          | 
| -------------------- | ------------ | ----------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | 
| 連続日数とメッセージ | GET          | /consecutive-days                         | 現在の連続連絡日数と、それに応じたメッセージをJSON形式で取得します。（例: 0日なら「別れますよ」、1日なら「昨日も連絡しましたね、その調子です」、2日以上なら「その調子です」） | 
| 全記録取得           | GET          | /ContactLogList                           | 全ての連絡記録（連絡日、恋人の名前など）を日付順で取得します。                                                                                                                | 
| 特定のID取得         | GET          | /ContactLog/id/{id}                       | 指定したIDの連絡記録を単一オブジェクトとして取得します。                                                                                                                      | 
| 特定の名前           | GET          | ContactLog/lover/{lover}                  |                                                                                                                                                                               | 
| 記録追加             | POST         | insertContactLog                          | 新しい連絡記録を日付と恋人の名前を指定して追加します。（1日1回限定）                                                                                                          | 
| 記録更新             | PUT          | updateContactLog/id/{id}                  | 指定したIDの連絡記録の日付と恋人の名前を更新します。                                                                                                                          | 
| 記録削除             | DELETE       | deleteContactLog/id/{id}                  | 指定したIDの連絡記録を物理削除します。                                                                                                                                        |                                                                                                                                      | 

## 実装予定の機能
* フロントエンドの実装
* 統計機能
* 会話記録（メモ）機能
* ログイン機能
* 話題リストの追加
* モチベーションアップ機能


## おわりに
* Java学習者のアウトプットとして、リポジトリを公開させていただきました。
* ゆくゆく完成させて悩みを持った人を解決させていきたいと考えています。
* 感想・コメント等あればXアカウントまでご連絡くださると幸いです。


