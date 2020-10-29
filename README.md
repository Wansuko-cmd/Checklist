<h1>アプリの概要を表す画像集</h1>
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

<h1>アプリの説明</h1>
買い物リスト - 買い物の友

本アプリは忙しい買い物の現場で真価を発揮できるように考えられたアプリです！
とにかく入力、チェックのしやすさを追求しました！
専業主婦である私の母の厳しい指導により、以下の機能を搭載しています。

<p>☆機能</p>
<ul>
    <li>チェックしたものが自動で下に移動します。ついでに色も付きます</li>
    <li>項目の追加や削除が指一本で簡単にできます</li>
    <li>最後の項目でエンターを押すと項目を勝手に追加してくれます</li>
    <li>チャットアプリ等ににリストの内容を送ることができます</li>
</ul>
これらにより何を買っていないかが一目でわかるリストを、即座につくること、また共有することが可能になっています！

ちなみに母の御眼鏡にはかなったようです。愛用していただいております。
かくいう私も、買い物リストとしてだけでなく、様々なことをメモ、確認するために使用しております。

皆様もぜひ利用してみてください！

<h1>URL系統</h1>
<p>Google Play:https://play.google.com/store/apps/details?id=com.wsr.shopping_friend</p>
<p>プライバシポリシー（全部英語）:https://shopping-friend-279e8.firebaseapp.com/</p>

プライバシポリシーの方はGitにShopping-Friend-webの名前で上げてます。

<h1>アプリの構造（概要）</h1>
<p>アクティビィティはMainActivityと、設定用のShowPreferenceの二つ</p>
<p>フラグメントはリストのタイトルを表示するためのShowTitleFragment、リストの内容を表示するためのShowContentsFragment、設定のためのPreferenceFragmentの３つ</p>
<p>AdapterはShowTitleFragmentのためのMainAdapter、ShowContentsFragmentのためのListAdapterの２つ</p>
<p>ViewModelはデータベースの中身を繋ぐためのAppViewModel、ShowContentsFragmentで表示順番などを保持するためのEditViewModelの２つ</p>
<p>あとはところどころソースコードにコメントを書いてますので頑張れ</p>

<h1>ただ今の課題</h1>
<ul>
    <li style="color:red;">データベースの修理</li>
    <li>アプリの起動速度の向上</li>
    <li>ASOを考える</li>
</ul>
