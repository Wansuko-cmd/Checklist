package com.wsr.shopping_friend.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.wsr.shopping_friend.R
import com.wsr.shopping_friend.adapter.ListAdapter
import com.wsr.shopping_friend.info_list_database.InfoList
import com.wsr.shopping_friend.preference.getShareAll
import com.wsr.shopping_friend.type_file.SwipeToDeleteCallback
import com.wsr.shopping_friend.type_file.renameAlert
import com.wsr.shopping_friend.type_file.setHelp
import com.wsr.shopping_friend.view_model.AppViewModel
import com.wsr.shopping_friend.view_model.EditViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_show_contents.*
import kotlinx.android.synthetic.main.fragment_show_contents.edit_button
import kotlinx.coroutines.runBlocking
import java.util.*

class ShowContentsFragment : Fragment() {
    //recyclerViewの定義
    private var recyclerView: RecyclerView? = null

    //使う変数の定義
    private val args: ShowContentsFragmentArgs by navArgs()
    private var titleList = mutableListOf<String>()
    private lateinit var title: String
    private lateinit var viewModel: AppViewModel
    private lateinit var editViewModel: EditViewModel
    private lateinit var showContentsAdapter: ListAdapter
    private lateinit var snackBar: Snackbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_show_contents, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //変数の初期化
        title = args.content
        viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        editViewModel = ViewModelProviders.of(this).get(EditViewModel::class.java)
        showContentsAdapter = ListAdapter(requireContext(), editViewModel)

        //toolbarの設定
        setToolbar()

        //snackBarの設定
        snackBar = setSnackBar()

        if (title == "") setHelp(requireContext(), editViewModel)

        //recyclerViewの初期化
        this.recyclerView = show_contents_recycler_view
        this.recyclerView!!.setOnClickListener { it.requestFocus() }
        this.recyclerView?.apply{
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = showContentsAdapter
        }

        //viewModelが更新された際の処理
        viewModel.infoList.observe(viewLifecycleOwner, { list ->
            list?.let {
                setInfoList(it)
            }
        })

        //editボタンが押された際の処理
        edit_button.setOnClickListener {
            addElements()
            showContentsAdapter.notifyDataSetChanged()
        }

        //delete_checkボタンが押された際の処理
        delete_check_button.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle(R.string.check_out_title)
                .setMessage(R.string.check_out_message)
                .setPositiveButton(R.string.check_out_positive) { _, _ ->
                    val list = mutableListOf<InfoList>()
                    for (i in editViewModel.getList()) {
                        if (i.check) list.add(i)
                    }
                    viewModel.deleteList(list)
                    for (i in list) {
                        editViewModel.delete(i.id)
                    }
                    showContentsAdapter.notifyDataSetChanged()
                }
                .setNegativeButton(R.string.check_out_negative, null)
                .setCancelable(false)
                .show()
        }

        //テキストが変更された際の処理
        showContentsAdapter.changeText = { p0, position ->
            //snackBar.dismiss()
            for (i in editViewModel.getList()) {
                if (position == i.number) {
                    //一番下の要素の中でエンターキーを押した際に新しく空欄を作る機能
                    if (p0.endsWith("\n") && (editViewModel.checkEmpty(i.id))) {
                        showContentsAdapter.notifyDataSetChanged()
                        addElements()
                    } else if (p0 != i.item) {
                        editViewModel.changeItem(i.id, p0)
                        viewModel.update(i)
                        snackBar.dismiss()
                    }
                    break
                }
            }
        }

        //チェックの状態が変更されたときの処理
        showContentsAdapter.changeCheck = { check, holder ->
            snackBar.dismiss()
            val position = holder.adapterPosition
            for (i in editViewModel.getList()) {
                if (position == i.number) {
                    if (check) {
                        holder.view.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    } else {
                        holder.view.setBackgroundColor(Color.parseColor("#AFEEEE"))
                    }
                    editViewModel.changeCheck(i.id, check)
                    showContentsAdapter.notifyItemMoved(position, editViewModel.setNumber(i.id))
                    recyclerView!!.scrollToPosition(position)
                    break
                }
            }
        }

        //特定の要素を削除する処理
        showContentsAdapter.deleteElement = { position ->
            for (i in editViewModel.getList()) {
                if (position == i.number) {
                    showContentsAdapter.notifyItemRemoved(position)
                    editViewModel.delete(i.id)
                    viewModel.delete(editViewModel.deleteEditItem!!)
                    break
                }
            }
            snackBar.show()
        }

        //スワイプでアイテムを消す処理
        val swipeHandler = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewHolder.let {
                    showContentsAdapter.deleteElement(it.adapterPosition)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    //設定から戻ったときに結果を反映するための処理
    override fun onResume() {
        super.onResume()
        showContentsAdapter.notifyDataSetChanged()
    }

    //アプリ停止時にデータをデータベースに保存する処理
    override fun onStop() {
        super.onStop()
        snackBar.dismiss()
        val deleteList = mutableListOf<InfoList>()
        if (title != "") {
            val numList = editViewModel.getNumList()
            for (i in editViewModel.getList()) {
                deleteList.add(i)
                if (i.item == "") numList.removeAll { it.id == i.id }
            }
            numList.sortBy { it.number }
            for ((count, _) in numList.withIndex()) {
                numList[count] = numList[count].copy(number = count)
            }
            updateDatabase(deleteList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.recyclerView?.adapter = null
        this.recyclerView = null
    }

    //toolbarの設定
    private fun setToolbar() {
        val toolbar = requireActivity().main_toolbar
        toolbar.title = if (title != "") title else "Help"
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        toolbar.menu.setGroupVisible(R.id.rename_group, true)
        toolbar.menu.setGroupVisible(R.id.help_group, false)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.share -> shareText()
                R.id.rename_title -> {
                    snackBar.dismiss()
                    if (title != "") renameAlert(requireContext(), changeTitle, titleList, title)
                    else{
                        AlertDialog.Builder(requireContext())
                            .setMessage(getString(R.string.no_edit_title_message))
                            .setPositiveButton(getString(R.string.no_edit_title_positive), null)
                            .show()
                    }
                }
            }
            true
        }
        toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.back_to_title_fragment)
        }
        toolbar.setOnClickListener { it.requestFocus() }
    }

    //LiveDataの内容を反映させる関数
    private fun setInfoList(lists: MutableList<InfoList>) {
        lists.sortBy { it.number }
        for (numOfTitle in lists) {
            if (!titleList.contains(numOfTitle.title)) {
                titleList.add(numOfTitle.title)
            }
        }
        if (editViewModel.getList() == emptyList<InfoList>()) {
            for (numOfTitle in lists) {
                if (numOfTitle.title == title) {
                    editViewModel.insert(numOfTitle)
                }
            }
            showContentsAdapter.notifyDataSetChanged()
        }
    }

    //タイトルが変更された際の処理
    private val changeTitle: (String) -> Unit = { newTitle ->
        viewModel.changeTitle(title, newTitle)
        title = newTitle
        requireActivity().main_toolbar.title = newTitle
    }

    //空欄を追加するための処理
    private fun addElements() {
        val id = UUID.randomUUID().toString()
        val number = showContentsAdapter.itemCount
        editViewModel.insert(InfoList(id, number, title, false, ""))
        viewModel.insert(InfoList(id, number, title, false, ""))
        recyclerView!!.scrollToPosition(editViewModel.setNumber(id))
        showContentsAdapter.focus = editViewModel.setNumber(id)
        showContentsAdapter.notifyItemInserted(editViewModel.nonCheckNumber())
    }

    //editViewModelの内容をデータベースに反映させる関数
    private fun updateDatabase(deleteList: MutableList<InfoList>) {
        val list: MutableList<InfoList> = mutableListOf()
        for (i in editViewModel.getNumList()) {
            list.add(
                InfoList(
                    i.id,
                    i.number,
                    title,
                    editViewModel.getCheck(i.id),
                    editViewModel.getItem(i.id)
                )
            )
        }
        //val tag = "SendList"
        //Log.i(tag, list.toString())
        runBlocking {
            val job1 = viewModel.deleteList(deleteList)
            job1.join()
        }
        runBlocking {
            val job2 = viewModel.insertList(list)
            job2.join()
        }
    }

    //Undo機能の設定
    private fun setSnackBar(): Snackbar {
        return Snackbar.make(
            requireActivity().findViewById(R.id.coordinatorLayout),
            getString(R.string.snack_bar_message),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(getString(R.string.snack_bar_action)) {
                val item = editViewModel.deleteEditItem
                if (item != null) {
                    editViewModel.backDeleteItem()
                    viewModel.insert(editViewModel.deleteEditItem!!)
                    showContentsAdapter.notifyItemInserted(item.number)
                    Snackbar.make(
                        requireActivity().findViewById(R.id.coordinatorLayout),
                        getString(R.string.snack_bar_after),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
    }

    //設定、ヘルプ画面に画面遷移するための処理
    private fun shareText() {
        var text = ""
        val listTop: String = requireActivity().getString(R.string.list_top)
        val setting = getShareAll(requireContext())
        for (i in editViewModel.getList()){
            if(!i.check || !setting){
                text += listTop + i.item + "\n"
            }
        }
        if(text.length >= 2){
            text = text.dropLast(1)
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }
            startActivity(intent)
        }
        else{
            Toast.makeText(requireContext(), requireActivity().getString(R.string.no_value), Toast.LENGTH_LONG).show()
        }
    }
}