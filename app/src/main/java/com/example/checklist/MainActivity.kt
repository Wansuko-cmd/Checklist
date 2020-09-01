package com.example.checklist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //各種のインスタンス化
        viewModel= ViewModelProvider(this).get(AppViewModel::class.java)
        /*諸悪の根源（#＾ω＾）　原因としてはAppViewModelにおいて継承するものをViewModelからAndroidViewModelに
        変更したからと思われる。
        ViewModelに引数があるとFactoryを作らないといけない？が、引数はapplicationだけであり、作る必要があるかといわれれば
        謎である。また、Android開発の公式がだしてるコードだと、ViewModelにapplicationの引数があるがFactoryを作成
        していないことが分かる。
        https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#16
        ここからダウンロード可能*/

        val adapter = ListAdapter(viewModel)
        val layoutManager = LinearLayoutManager(this)

        //RecycleViewの詳細設定
        RecyclerView.adapter = adapter
        RecyclerView.layoutManager = layoutManager
        RecyclerView.setHasFixedSize(true)

        //監視するLiveDataを決め、そこから流れてきた情報をListAdapterのインスタンスにあるリストに代入
        viewModel.infoList.observe(this, Observer { lists ->
            lists?.let { adapter.setList(it) }
        })
    }
}