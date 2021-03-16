package com.wsr.shopping_friend.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.wsr.shopping_friend.R
import com.wsr.shopping_friend.adapter.ListAdapter
import com.wsr.shopping_friend.databinding.FragmentShowContentsBinding
import com.wsr.shopping_friend.info_list_database.InfoList
import com.wsr.shopping_friend.preference.getShareAll
import com.wsr.shopping_friend.type_file.ItemTouchHelperCallback
import com.wsr.shopping_friend.type_file.renameAlert
import com.wsr.shopping_friend.type_file.setHelp
import com.wsr.shopping_friend.view_holder.ListViewHolder
import com.wsr.shopping_friend.view_model.AppViewModel
import com.wsr.shopping_friend.view_model.EditViewModel
import kotlinx.coroutines.*
import java.util.*

//リストの中身を見せるためのFragment
class ShowContentsFragment : Fragment() {

    //viewBindingを利用するための宣言
    private var _binding: FragmentShowContentsBinding? = null
    private val binding get() = _binding!!

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
    private lateinit var checkSetData: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.show_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.share -> {
                showContentsAdapter.notifyDataSetChanged()
                //shareText()
                true
            }
            R.id.rename_title -> {
                snackBar.dismiss()
                if (title != "") renameAlert(requireContext(), changeTitle, titleList, title)
                else{
                    AlertDialog.Builder(requireContext())
                        .setMessage(getString(R.string.no_edit_title_message))
                        .setPositiveButton(getString(R.string.no_edit_title_positive), null)
                        .show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

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

        //変数の初期化
        title = args.content
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(AppViewModel::class.java)
        editViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(EditViewModel::class.java)
        showContentsAdapter = ListAdapter(requireContext(), editViewModel)

        //actionbarの設定
        (activity as AppCompatActivity).supportActionBar?.title = title

        //snackBarの設定
        snackBar = setSnackBar()

        //ヘルプを選択した時の処理
        if (title == "") setHelp(requireContext(), editViewModel)

        //recyclerViewの初期化
        this.recyclerView = binding.showContentsRecyclerView
        this.recyclerView!!.setOnClickListener { it.requestFocus() }
        this.recyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = showContentsAdapter
        }

        //viewModelが更新された際の処理
        viewModel.infoList.observe(viewLifecycleOwner, { list ->
            list?.let {
                setInfoList(it)
                //editViewModel.list.postValue(list)
            }
        })

        //editボタンが押された際の処理
        binding.editButton.setOnClickListener {
            addElements()
            showContentsAdapter.notifyDataSetChanged()
        }

        //delete_checkボタンが押された際の処理
        binding.deleteCheckButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle(R.string.check_out_title)
                .setMessage(R.string.check_out_message)
                .setPositiveButton(R.string.check_out_positive) { _, _ ->
                    val list = mutableListOf<InfoList>()
                    for (i in editViewModel.getList()) {
                        if (i.check) list.add(i)
                    }
                    runBlocking {
                        viewModel.deleteList(list)
                    }
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
                        //viewModel.update(i)
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
                    //showContentsAdapter.notifyDataSetChanged()
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
                    runBlocking {
                        viewModel.delete(editViewModel.deleteEditItem!!)
                    }
                    break
                }
            }
            snackBar.show()
        }

        //スワイプでアイテムを消したり動かしたりするための処理

        val itemTouchHelperCallback = ItemTouchHelper(
            object : ItemTouchHelperCallback() {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    if (viewHolder is ListViewHolder
                        && !viewHolder.check.isChecked) {
                        val fromPosition = viewHolder.adapterPosition
                        val toPosition = target.adapterPosition

                        if(editViewModel.changePlace(fromPosition, toPosition)){
                            showContentsAdapter.notifyItemMoved(fromPosition, toPosition)
                            return false
                        }
                    }
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    viewHolder.let {
                        showContentsAdapter.deleteElement(it.adapterPosition)
                    }
                }

                override fun clearView(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ) {
                    if (viewHolder is ListViewHolder){
                        if (viewHolder.check.isChecked) {
                            viewHolder.view.setBackgroundColor(Color.parseColor("#FFFFFF"))
                        } else {
                            viewHolder.view.setBackgroundColor(Color.parseColor("#AFEEEE"))
                        }
                    }
                    super.clearView(recyclerView, viewHolder)
                }
            }
        )

        itemTouchHelperCallback.attachToRecyclerView(recyclerView)


        checkSetData = GlobalScope.launch(Dispatchers.Main) {
            if (editViewModel.checkSetData()) showContentsAdapter.notifyDataSetChanged()
        }

    }

    //設定から戻ったときに結果を反映するための処理
    override fun onResume() {
        super.onResume()
        showContentsAdapter.notifyDataSetChanged()
    }

    override fun onPause() {
        snackBar.dismiss()
        val deleteList = editViewModel.getDeleteList()
        if (title != "") {
            val numList = editViewModel.getNumList()
            for (i in editViewModel.getList()) {
                if (i.item == "") {
                    deleteList.add(i)
                    numList.removeAll { it.id == i.id }
                }
            }
            numList.sortBy { it.number }
            for ((count, _) in numList.withIndex()) {
                numList[count] = numList[count].copy(number = count)
            }
            if(updateDatabase(editViewModel.getDeleteList())){
                Log.i("save", "Success")
            }
        }
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.recyclerView?.adapter = null
        this.recyclerView = null
        _binding = null
    }

    //LiveDataの内容を反映させる関数
    private fun setInfoList(infoList: MutableList<InfoList>) {
        if (editViewModel.getList() == emptyList<InfoList>()) {
            for (numOfTitle in infoList) {
                if (numOfTitle.title == title) {
                    editViewModel.insert(numOfTitle)
                }
            }
        }

        val list = infoList.sortedBy { it.number }
        for (numOfTitle in list) {
            if (!titleList.contains(numOfTitle.title)) {
                titleList.add(numOfTitle.title)
            }
        }
        val tempList = mutableListOf<InfoList>()
        for (value in list) {
            if (value.title == title) {
                tempList.add(value)
            }
        }
        editViewModel.list.postValue(tempList)
        //showContentsAdapter.notifyDataSetChanged()
//        if (editViewModel.list.value == emptyList<InfoList>()) {
//
//        }
    }

    //タイトルが変更された際の処理
    private val changeTitle: (String) -> Unit = { newTitle ->
        runBlocking {
            viewModel.changeTitle(title, newTitle)
        }
        title = newTitle

        (activity as AppCompatActivity).supportActionBar?.title = newTitle
    }

    //空欄を追加するための処理
    private fun addElements() {
        val id = UUID.randomUUID().toString()
        val number = showContentsAdapter.itemCount
        editViewModel.insert(InfoList(id, number, title, false, ""))
        runBlocking {
            viewModel.insert(InfoList(id, number, title, false, ""))
        }
        recyclerView!!.scrollToPosition(editViewModel.setNumber(id))
        showContentsAdapter.focus = editViewModel.setNumber(id)
        showContentsAdapter.notifyItemInserted(editViewModel.nonCheckNumber())
    }

    //editViewModelの内容をデータベースに反映させる関数
    private fun updateDatabase(deleteList: MutableList<InfoList>) : Boolean {
        val list: MutableList<InfoList> = mutableListOf()
        editViewModel.list.value?.let{
            for (i in it){
                list.add(i)
            }
        }
        /*for (i in editViewModel.getNumList()) {
            list.add(
                InfoList(
                    i.id,
                    i.number,
                    title,
                    editViewModel.getCheck(i.id),
                    editViewModel.getItem(i.id)
                )
            )
        }*/
        //val tag = "SendList"
        //Log.i(tag, list.toString())


        runBlocking {
            viewModel.deleteList(deleteList)
            viewModel.update(list)
            viewModel.insertList(list)
        }
        return true
    }

    //Undo機能の設定
    private fun setSnackBar(): Snackbar {
        return Snackbar.make(
            binding.coordinatorLayout,
            getString(R.string.snack_bar_message),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(getString(R.string.snack_bar_action)) {
                val item = editViewModel.deleteEditItem
                if (item != null) {
                    editViewModel.backDeleteItem()
                    runBlocking {
                        viewModel.insert(editViewModel.deleteEditItem!!)
                    }
                    showContentsAdapter.notifyItemInserted(item.number)
                    Snackbar.make(
                        binding.coordinatorLayout,
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
                text += "${listTop}${i.item}\n"
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