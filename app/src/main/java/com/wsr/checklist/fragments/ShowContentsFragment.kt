package com.wsr.checklist.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wsr.checklist.R
import com.wsr.checklist.adapter.ListAdapter
import com.wsr.checklist.info_list_database.InfoList
import com.wsr.checklist.type_file.SwipeToDeleteCallback
import com.wsr.checklist.type_file.renameAlert
import com.wsr.checklist.view.ShowPreference
import com.wsr.checklist.view_model.AppViewModel
import com.wsr.checklist.view_model.EditViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_show_contents.*
import kotlinx.android.synthetic.main.fragment_show_contents.edit_button
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class ShowContentsFragment : Fragment(){
    //recyclerViewの定義
    private var recyclerView: RecyclerView? = null

    private val args: ShowContentsFragmentArgs by navArgs()

    //使う変数の定義
    private var titleList = mutableListOf<String>()
    private lateinit var title: String
    private lateinit var viewModel: AppViewModel
    private lateinit var editViewModel: EditViewModel
    private lateinit var showContentsAdapter: ListAdapter

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
        showContentsAdapter = ListAdapter(editViewModel)

        //toolbarの設定
        setToolbar()

        //recyclerViewの初期化
        this.recyclerView = show_contents_recycler_view
        this.recyclerView?.apply {
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
        }

        //renameボタンが押された際の処理
        rename_button.setOnClickListener {
            //renameAlert(requireContext(), changeTitle, titleList, title)
            showContentsAdapter.notifyDataSetChanged()
        }

        //delete_checkボタンが押された際の処理
        delete_check_button.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle(R.string.check_out_title)
                .setMessage(R.string.check_out_message)
                .setPositiveButton(R.string.check_out_positive) { _, _ ->
                    val idList = mutableListOf<String>()
                    for(i in editViewModel.getList()){
                        if(i.check) idList.add(i.id)
                    }
                    for(i in idList){
                        editViewModel.delete(i)
                    }
                    showContentsAdapter.notifyDataSetChanged()
                }
                .setNegativeButton(R.string.check_out_negative, null)
                .setCancelable(false)
                .show()
        }

        requireActivity().main_toolbar.title = title
        requireActivity().main_toolbar.setNavigationIcon(R.drawable.ic_back_arrow)

        //Backボタンを押した際の処理
        requireActivity().main_toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.back_to_title_fragment)
        }

        //テキストが変更された際の処理
        showContentsAdapter.changeText = { p0, position ->
            for (i in editViewModel.getList()) {
                if (position == i.number) {
                    //一番下の要素の中でエンターキーを押した際に新しく空欄を作る機能
                    if (p0.endsWith("\n") && (editViewModel.checkEmpty(i.id))) {
                        showContentsAdapter.notifyDataSetChanged()
                        addElements()
                    } else if (p0 != i.item){
                        editViewModel.changeItem(i.id, p0)
                    }
                    break
                }
            }
        }

        //チェックの状態が変更されたときの処理
        showContentsAdapter.changeCheck = { check, position ->
            for (i in editViewModel.getList()) {
                if (position == i.number) {
                    editViewModel.changeCheck(i.id, check)
                    //showContentsAdapter.notifyDataSetChanged()
                    showContentsAdapter.notifyItemMoved(0, 4)
                    break
                }
            }
            //showContentsAdapter.notifyItemMoved(position, editViewModel.setPosition(id))
            showContentsAdapter.notifyDataSetChanged()
        }

        //特定の要素を削除する処理
        showContentsAdapter.deleteElement = { position ->
            for (i in editViewModel.getList()) {
                if (position == i.number) {
                    showContentsAdapter.notifyItemRemoved(position)
                    editViewModel.delete(i.id)
                    break
                }
            }
        }

        val swipeHandler = object : SwipeToDeleteCallback(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewHolder.let{
                    showContentsAdapter.deleteElement(it.adapterPosition)
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    //アプリ停止時にデータをデータベースに保存する処理
    override fun onStop() {
        super.onStop()
        val numList = editViewModel.getNumList()
        for(i in editViewModel.getList()){
            if(i.item == "") numList.removeAll {it.id == i.id}
        }
        numList.sortBy { it.number }
        for ((count, _) in numList.withIndex()){
            numList[count] = numList[count].copy(number = count)
        }
        updateDatabase(title, title)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.recyclerView?.adapter = null
        this.recyclerView = null
    }

    //toolbarの設定
    private fun setToolbar(){
        val toolbar = requireActivity().main_toolbar
        toolbar.title = title
        toolbar.navigationIcon = null
        toolbar.menu.setGroupVisible(R.id.rename_group, true)
        toolbar.menu.setGroupVisible(R.id.help_group, false)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.settings -> showSettings()
                R.id.rename_title -> renameAlert(requireContext(), changeTitle, titleList, title)
            }
            true
        }
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
        }
        showContentsAdapter.notifyDataSetChanged()
    }

    //タイトルが変更された際の処理
    private val changeTitle: (String) -> Unit = { newTitle ->
        updateDatabase(title, newTitle)
        title =  newTitle
        requireActivity().main_toolbar.title = newTitle
    }

    //空欄を追加するための処理
    private fun addElements() {
        val id = UUID.randomUUID().toString()
        val number = showContentsAdapter.itemCount
        editViewModel.insert(InfoList(id, number, title, false, ""))
        recyclerView!!.scrollToPosition(editViewModel.setNumber(id))
        showContentsAdapter.focus = editViewModel.setNumber(id)
        showContentsAdapter.notifyItemInserted(editViewModel.nonCheckNumber())
    }

    private fun updateDatabase(oldTitle: String, newTitle: String){
        runBlocking {
            val job = GlobalScope.launch {
                viewModel.deleteWithTitle(oldTitle)
            }
            job.join()
        }
        for (i in editViewModel.getNumList()) {
            runBlocking {
                val job = GlobalScope.launch {
                    viewModel.insert(InfoList(i.id, i.number, newTitle, editViewModel.getCheck(i.id), editViewModel.getItem(i.id)))
                }
                job.join()
            }
        }
    }

    //設定画面に画面遷移するための処理
    private fun showSettings(){
        val intent = Intent(requireActivity(), ShowPreference::class.java)
        startActivity(intent)
    }
}