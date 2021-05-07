# アプリの概要を表す画像集
<img src="https://user-images.githubusercontent.com/63241922/97106398-b17d0280-1704-11eb-943a-2709c324fbee.png" width="480px"/>
<table>
    <tr>
        <td><img src="https://user-images.githubusercontent.com/63241922/97106433-eee19000-1704-11eb-85a3-71ca1c79201a.png" width="300px"/></td>
        <td><img src="https://user-images.githubusercontent.com/63241922/97106437-f30dad80-1704-11eb-970b-c526e4cb044f.png" width="300px"/></td>
        <td><img src="https://user-images.githubusercontent.com/63241922/97106438-f30dad80-1704-11eb-9f1b-17bcc3b38b1a.png" width="300px"/></td>
    </tr>
    <tr>
        <td><img src="https://user-images.githubusercontent.com/63241922/97106439-f3a64400-1704-11eb-87b1-8b670ca963d9.png" width="300px"/></td>
        <td><img src="https://user-images.githubusercontent.com/63241922/97106435-f2751700-1704-11eb-991b-03492ce94284.png" width="300px"/></td>
        <td><img src="https://user-images.githubusercontent.com/63241922/97106436-f2751700-1704-11eb-80a9-8e795be92d15.png" width="300px"/></td>
    </tr>
</table>

# アプリの説明
買い物リスト - 買い物の友

本アプリは忙しい買い物の現場で真価を発揮できるように考えられたアプリです！

とにかく入力、チェックのしやすさを追求しました！

専業主婦である私の母の厳しい指導により、以下の機能を搭載しています。

#### ☆機能

- チェックしたものが自動で下に移動します。ついでに色も付きます
- 項目の追加や削除が指一本で簡単にできます
- 最後の項目でエンターを押すと項目を勝手に追加してくれます
- チャットアプリ等ににリストの内容を送ることができます

これらにより何を買っていないかが一目でわかるリストを、即座につくること、また共有することが可能になっています！

ちなみに母の御眼鏡にはかなったようです。愛用していただいております。
かくいう私も、買い物リストとしてだけでなく、様々なことをメモ、確認するために使用しております。

皆様もぜひ利用してみてください！

# URL
Google Play:https://play.google.com/store/apps/details?id=com.wsr.shopping_friend

プライバシポリシー（全部英語）:https://shopping-friend-279e8.firebaseapp.com/

プライバシポリシーの方はGitにShopping-Friend-webの名前で上げてます。

# 使用している技術

- データの表示：ビューバインディング、Recycler View、LiveData
- データの編集：双方向データバインディング、MutableLiveData
- データの保存：Room Database
- その他乱用技術：Coroutine、ItemTouchHelper等


# アプリの構造（概要）
アクティビィティはMainActivityと、設定用のShowPreferenceの二つ

フラグメントはリストのタイトルを表示するためのTitleFragment、リストの内容を表示するためのContentsFragment、設定のためのPreferenceFragmentの３つ

AdapterはTitleFragmentのためのTitleAdapter、ContentsFragmentのためのContentsAdapterの２つ

ViewModelはデータベースの中身を繋ぐためのAppViewModel、ShowContentsFragmentで表示順番などを保持するためのEditViewModelの２つ

基本的にデータの入出力は双方向データバインディングを利用しています。
