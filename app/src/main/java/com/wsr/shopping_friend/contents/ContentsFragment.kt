package com.wsr.shopping_friend.contents

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.wsr.shopping_friend.R
import com.wsr.shopping_friend.databinding.FragmentShowContentsBinding
import com.wsr.shopping_friend.database.InfoList
import com.wsr.shopping_friend.preference.getShareAll
import com.wsr.shopping_friend.preference.getToolbarTextTheme
import com.wsr.shopping_friend.share.renameTitle
import com.wsr.shopping_friend.share.setHelp
import com.wsr.shopping_friend.share.view_model.AppViewModel
import com.wsr.shopping_friend.share.view_model.EditViewModel
import kotlinx.coroutines.*
import java.util.*

//リストの中身を見せるためのFragment
class ContentsFragment : Fragment() {

    //viewBindingを利用するための宣言
    private var _binding: FragmentShowContentsBinding? = null
    private val binding get() = _binding!!

    //recyclerViewの定義
    private var recyclerView: RecyclerView? = null

    //ShowTitleFragmentから、表示するタイトル名を受け取るための変数
    private val args: ContentsFragmentArgs by navArgs()
    private lateinit var title: String

    //全部のリストのタイトルを集めたリスト
    private var titleList = mutableListOf<String>()

    //ViewModel
    private lateinit var appViewModel: AppViewModel
    private lateinit var editViewModel: EditViewModel

    //Adapter
    private lateinit var contentsAdapter: ContentsAdapter

    //UNDOを表示するためのsnackBar
    private lateinit var snackBar: Snackbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowContentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ShowTitleFragmentから受け取った、表示するタイトル名を代入
        title = args.content

        //UNDO用のsnackBarの設定
        snackBar = setSnackBar()

        //DBとの接続用のViewModelの初期化
        appViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(AppViewModel::class.java).apply {

            //editViewModelにLiveDataで流れてきた値を入れる処理
            infoList.observe(viewLifecycleOwner, { list ->
                list?.let {
                    lifecycleScope.launch{
                        setInfoList(it)
                        contentsAdapter.notifyDataSetChanged()
                    }
                }
            })
        }

        //編集用のViewModelの初期化
        editViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(EditViewModel::class.java)

        //ヘルプを選択した時の処理
        if (title == "") setHelp(requireContext(), editViewModel)

        //Adapterの初期化
        contentsAdapter = ContentsAdapter(editViewModel, this).apply {

            //指定した位置までスクロールする関数をAdapterのインスタンスに設定
            scrollToPosition = {
                recyclerView!!.scrollToPosition(it)
            }
        }

        //recyclerViewの初期化
        this.recyclerView = binding.showContentsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = contentsAdapter
        }

        //下のボタンの設定
        binding.apply {

            //要素を追加するボタンの設定
            addButton.setOnClickListener { addElement() }

            //チェックのついた要素をすべて削除するボタンの設定
            deleteCheckButton.setOnClickListener { deleteElements() }
        }

        //スワイプでアイテムを消したり動かしたりするための処理
        val itemTouchHelperCallback = ItemTouchHelper(
            ContentsItemTouchHelper(
                appViewModel,
                editViewModel,
                contentsAdapter,
                snackBar
            )
        )

        //上記のコールバックをrecyclerViewに設定
        itemTouchHelperCallback.attachToRecyclerView(recyclerView)

//        //LiveDataの内容が反映されるのを待つ処理
//        GlobalScope.launch(Dispatchers.Main) {
//            editViewModel.checkData({ it != null }) { contentsAdapter.notifyDataSetChanged() }
//        }

    }

    //設定から戻ったときに結果を反映するための処理
    override fun onResume() {
        super.onResume()

        //それぞれの再生成
        contentsAdapter.notifyDataSetChanged()
        setToolbar()
    }

    //fragmentが止まったときに行う処理
    override fun onStop() {
        snackBar.dismiss()
        updateDatabase()
        super.onStop()
    }

    //fragmentのインスタンスを破棄するときに行う処理
    override fun onDestroyView() {
        super.onDestroyView()
        this.recyclerView?.adapter = null
        this.recyclerView = null
        _binding = null
    }

    //LiveDataの内容を反映させる関数
    private suspend fun setInfoList(lists: MutableList<InfoList>) {

        /*登録されている要素の中で以下の要素のみを抽出してタイトル名を保存するリストに代入
        *
        * ・重複していない
        * ・タイトル名が空ではない
        *
        * */
        titleList = lists
            .asSequence()
            .map { it.title }
            .distinct()
            .filter { it != "" }
            .sorted()
            .toMutableList()

        if (editViewModel.list == emptyList<InfoList>()) {

            //アイテムが空欄の要素が含まれていた時に削除する処理
            val deleteList: MutableList<InfoList> =
                lists.filter { it.item == "" } as MutableList<InfoList>

            appViewModel.deleteList(deleteList)

            //editViewModelのlistを、指定のタイトル名を保有している要素のリストで初期化する処理
            editViewModel.initializeList(
                lists.filter { it.item != "" && it.title == title } as MutableList<InfoList>
            )
        }
    }

    //タイトルが変更された際の処理
    private val changeTitle: (String) -> Unit = { newTitle ->

        //データベースに反映
        runBlocking {
            appViewModel.changeTitle(title, newTitle)
        }

        //このFragmentのインスタンスの保有するタイトル名を最新のものに変更
        title = newTitle

        //toolbarに最新のタイトルを反映
        binding.contentsToolbar.title = newTitle

        //editViewModelのlistの要素が保有するtitleを変更する処理
        editViewModel.changeTitle(newTitle)
    }

    //空欄を追加するための処理
    fun addElement() {

        //UNDOを表示するsnackBarを非表示にする
        snackBar.dismiss()

        //アイテムが空の新しい要素を生成
        val id = UUID.randomUUID().toString()
        val number = editViewModel.list.maxByOrNull { it.number }?.number?.plus(1) ?: 0
        val newColumn = InfoList(id, number, title, false, "")

        //editViewModelから現在のリストを入手
        val newList = editViewModel.list

        //新しいカラムを追加
        newList.add(newColumn)

        //新しく追加したカラムのインデックス
        val index = newList
            .sortedWith(editViewModel.infoListComparator)
            .indexOfFirst { it.id == id }

        //editViewModelのlistとデータベースに、新しい要素を追加
        editViewModel.list = newList
        runBlocking {
            appViewModel.insert(newColumn)
        }

        //LiveDataの内容が反映されるのを待つ処理
        GlobalScope.launch(Dispatchers.Main) {
            editViewModel.checkData({ infoList -> infoList?.any{ it.id == id } }){

                //アイテムの追加を通知
                contentsAdapter.notifyItemInserted(index)

                //新しい要素までスクロール
                recyclerView!!.scrollToPosition(index)

                //新しい要素にfocusを当てる
                contentsAdapter.focus = index
            }
        }
    }

    //チェックのついている要素を全て消す処理
    private fun deleteElements(){
        AlertDialog.Builder(context)
            .setTitle(R.string.check_out_title)
            .setMessage(R.string.check_out_message)
            .setPositiveButton(R.string.check_out_positive) { _, _ ->

                //削除後にセットするリスト群
                val (list, deleteList) = editViewModel.list.partition { !it.check }

                //データベース上にある要素の削除
                runBlocking {
                    appViewModel.deleteList(deleteList as MutableList<InfoList>)
                }

                //editViewModelのlistに、削除後のリストを反映
                editViewModel.list = (list as MutableList<InfoList>)
                GlobalScope.launch(Dispatchers.Main) {
                    editViewModel.checkData({ list -> list?.none { it.check } }){ contentsAdapter.notifyDataSetChanged() }
                }
            }
            .setNegativeButton(R.string.check_out_negative, null)
            .setCancelable(false)
            .show()
    }

    //editViewModelの内容をデータベースに反映させる関数
    private fun updateDatabase(){
        val (list, deleteList) = editViewModel.list.partition { it.item != "" }

        //アイテムが空ではない要素をデータベースに更新する処理
        runBlocking {
            appViewModel.update(list as MutableList<InfoList>)
            appViewModel.deleteList(deleteList as MutableList<InfoList>)
        }
    }

    //Toolbarの設定
    private fun setToolbar(){
        binding.contentsToolbar.also{

            //toolbarのメニューを削除
            it.menu.clear()

            //設定された値の読み取り
            when(getToolbarTextTheme(requireContext())){
                "white" -> {
                    it.setTitleTextColor(Color.WHITE)
                    it.inflateMenu(R.menu.show_menu_white)
                    it.setNavigationIcon(R.drawable.ic_back_arrow_white)
                }
                else -> {
                    it.setTitleTextColor(Color.BLACK)
                    it.inflateMenu(R.menu.show_menu)
                    it.setNavigationIcon(R.drawable.ic_back_arrow)
                }
            }

            //タイトルをtoolbarに反映させる処理（ヘルプ画面のときは設定した値を出す）
            it.title = if (title != "") title else getString(R.string.help_title)

            //toolbarに表示されるタイトルをクリックした際の処理
            it.setOnClickListener{

                //UNDOをするためのsnackBarの非表示
                snackBar.dismiss()

                //タイトルを変更する処理
                if (title != "") renameTitle(requireContext(), changeTitle, titleList, title)

                //ヘルプ画面だった時
                else{
                    AlertDialog.Builder(requireContext())
                        .setMessage(getString(R.string.no_edit_title_message))
                        .setPositiveButton(getString(R.string.no_edit_title_positive), null)
                        .show()
                }
            }

            //アイコンをクリックされたときの処理
            it.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){

                    //共有処理
                    R.id.share -> {
                        shareText()
                    }

                    //設定画面
                    R.id.settings -> {
                        contentsAdapter.notifyDataSetChanged()
//                        val intent = Intent(requireActivity(), ShowPreference::class.java)
//                        intent.putExtra("Purpose", "settings")
//                        startActivity(intent)
                    }

                    //タイトル表示画面に戻る処理
                    android.R.id.home ->{
                        findNavController().navigate(R.id.back_to_title_fragment)
                    }
                }
                true
            }

            //戻るボタンを押された際の処理
            it.setNavigationOnClickListener {
                findNavController().navigate(R.id.back_to_title_fragment)
            }
        }
    }

    //Undo機能の設定
    private fun setSnackBar(): Snackbar {

        //snackBarの設定
        return Snackbar.make(
            binding.showSnackBarLayout,
            getString(R.string.snack_bar_message),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(getString(R.string.snack_bar_action)) {

                editViewModel.deleteValue?.let { value ->

                    //一時的に保存していた要素をeditViewModelのlistに入れなおす
                    val list = editViewModel.list
                    list.add(value)
                    editViewModel.list = list

                    //adapterに要素を入れたことを通知する
                    contentsAdapter.notifyItemInserted(
                        list.sortedWith(editViewModel.infoListComparator)
                            .indexOf(value)
                    )

                    //データベースに要素を入れる
                    runBlocking {
                        appViewModel.insert(value)
                    }
                }

                //無事に要素を戻したことを伝えるsnackBarの設定
                Snackbar.make(
                    binding.showSnackBarLayout,
                    getString(R.string.snack_bar_after),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
    }

    //設定、ヘルプ画面に画面遷移するための処理
    private fun shareText() {

        //共有するテキストを代入する変数
        var text = ""

        //要素の先頭につける文字の設定
        val listTop: String = requireActivity().getString(R.string.list_top)

        //共有方法の設定を読み取る
        val setting = getShareAll(requireContext())

        //textに共有するテキストを代入
        for (i in editViewModel.list){
            if(!i.check || !setting){
                text += "${listTop}${i.item}\n"
            }
        }

        //共有できる要素が存在する場合の処理
        if(text.length >= 2){

            //最後の改行文字の削除
            text = text.dropLast(1)

            //ユーザの指定したアプリへのintentの処理
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }
            startActivity(intent)
        }

        //共有できる要素が存在しない場合の処理
        else{
            Toast.makeText(requireContext(), requireActivity().getString(R.string.no_value), Toast.LENGTH_LONG).show()
        }
    }
}