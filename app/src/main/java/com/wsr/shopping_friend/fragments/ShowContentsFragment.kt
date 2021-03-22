package com.wsr.shopping_friend.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.wsr.shopping_friend.R
import com.wsr.shopping_friend.adapter.ListAdapter
import com.wsr.shopping_friend.databinding.FragmentShowContentsBinding
import com.wsr.shopping_friend.info_list_database.InfoList
import com.wsr.shopping_friend.preference.getShareAll
import com.wsr.shopping_friend.preference.getToolbarTextTheme
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
    private var deleteValue: InfoList? = null
    private var movingChecker: Boolean = false
    private var tempList = mutableListOf<InfoList>()

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

        //タイトルの初期化
        title = args.content

        //Toolbarの設定
        setToolbar()

        //snackBarの設定
        snackBar = setSnackBar()

        //編集用のViewModelの初期化
        editViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(EditViewModel::class.java)

        //ヘルプを選択した時の処理
        if (title == "") setHelp(requireContext(), editViewModel)

        //DBとの接続用のViewModelの初期化
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(AppViewModel::class.java).apply {
            //editViewModelにLiveDataで流れてきた値を入れる処理
            infoList.observe(viewLifecycleOwner, { list ->
                list?.let { setInfoList(it) }
            })
        }

        //Adapterの初期化
        showContentsAdapter = ListAdapter(editViewModel, this).apply {
            scrollToPosition = {
                recyclerView!!.scrollToPosition(it)
            }
        }

        //recyclerViewの初期化
        this.recyclerView = binding.showContentsRecyclerView.apply{
            setOnClickListener { it.requestFocus() }
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = showContentsAdapter
        }

        //下のボタンの設定
        binding.apply {
            editButton.setOnClickListener{ addElements() }

            deleteCheckButton.setOnClickListener { deleteElements() }
        }

        //スワイプでアイテムを消したり動かしたりするための処理
        val itemTouchHelperCallback = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {

                //移動させる処理
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    if (viewHolder is ListViewHolder
                        && target is ListViewHolder
                        && !viewHolder.check.isChecked
                        && !target.check.isChecked
                    ) {
                        //tempList内で変更を保存しておいて、操作終了後にEditViewModelに反映させる

                        val fromPosition = viewHolder.adapterPosition
                        val toPosition = target.adapterPosition

                        val fromValue = tempList[fromPosition]

                        tempList[fromPosition] = tempList[fromPosition].copy(number = tempList[toPosition].number)
                        tempList[toPosition] = tempList[toPosition].copy(number = fromValue.number)

                        showContentsAdapter.notifyItemMoved(toPosition, fromPosition)

                        tempList = tempList.sortedBy { it.number }.sortedBy { it.check }.toMutableList()

                        movingChecker = true
                    }
                    return false
                }

                //スワイプで削除する処理
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    val list = editViewModel.list
                    val index = viewHolder.adapterPosition

                    list.removeAt(index).let{
                        deleteValue = it
                        runBlocking {
                            viewModel.delete(it)
                        }
                    }
                    showContentsAdapter.notifyItemRemoved(index)
                    editViewModel.list = list
                    snackBar.show()
                }

                override fun onSelectedChanged(
                    viewHolder: RecyclerView.ViewHolder?,
                    actionState: Int
                ) {
                    super.onSelectedChanged(viewHolder, actionState)
                    if (actionState == ACTION_STATE_DRAG && viewHolder is ListViewHolder){
                        viewHolder.view.setBackgroundColor(Color.parseColor("#FFD5EC"))
                        tempList = editViewModel.list
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
                    if(movingChecker){
                        editViewModel.list = tempList
                        GlobalScope.launch(Dispatchers.Main) {
                            editViewModel.checkData({ it == tempList }){showContentsAdapter.notifyDataSetChanged()}
                        }
                        movingChecker = false
                    }
                    super.clearView(recyclerView, viewHolder)
                }
            }
        )

        itemTouchHelperCallback.attachToRecyclerView(recyclerView)

        //LiveDataの内容が反映されるのを待つ処理
        GlobalScope.launch(Dispatchers.Main) {
            editViewModel.checkData({ it != null}){showContentsAdapter.notifyDataSetChanged()}
        }

    }

    //設定から戻ったときに結果を反映するための処理
    override fun onResume() {
        super.onResume()
        showContentsAdapter.notifyDataSetChanged()
    }

    override fun onStop() {
        snackBar.dismiss()
        updateDatabase()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.recyclerView?.adapter = null
        this.recyclerView = null
        _binding = null
    }

    //LiveDataの内容を反映させる関数
    private fun setInfoList(infoList: MutableList<InfoList>) {

        val list = infoList.sortedBy { it.number }
        for (numOfTitle in list) {
            if (!titleList.contains(numOfTitle.title)) {
                titleList.add(numOfTitle.title)
            }
        }
        if (editViewModel.list == emptyList<InfoList>()){
            val tempList = mutableListOf<InfoList>()
            for (value in list) {
                if (value.title == title) {
                    tempList.add(value)
                }
            }
            if (tempList.any { it.item == "" }){
                val deleteList: MutableList<InfoList> = list.filter { it.item == "" } as MutableList<InfoList>
                runBlocking {
                    viewModel.deleteList(deleteList)
                }
            }
            editViewModel.initializeList(tempList.filter { it.item != "" } as MutableList<InfoList>)
        }
    }

    //タイトルが変更された際の処理
    private val changeTitle: (String) -> Unit = { newTitle ->
        runBlocking {
            viewModel.changeTitle(title, newTitle)
        }
        title = newTitle

        binding.contentsToolbar.title = newTitle
    }

    //空欄を追加するための処理
    fun addElements() {

        snackBar.dismiss()

        val id = UUID.randomUUID().toString()
        val number = editViewModel.list.maxByOrNull { it.number }?.number?.plus(1) ?: 0
        val newColumn = InfoList(id, number, title, false, "")

        val newList = editViewModel.list
        newList.add(newColumn)
        editViewModel.list  = newList
        runBlocking {
            viewModel.insert(newColumn)
        }

        newList.sortedBy { it.number }.sortedBy { it.check }.indexOfFirst { it.id == id }.run{
            recyclerView!!.scrollToPosition(this)
            showContentsAdapter.focus = this
//            runBlocking {
//                editViewModel.checkData({ list -> list?.any{ it.id == id }}){ showContentsAdapter.notifyItemInserted(this@run) }
//            }
            showContentsAdapter.notifyItemInserted(this)
        }
    }

    //チェックのついている要素を全て消す処理
    private fun deleteElements(){
        AlertDialog.Builder(context)
            .setTitle(R.string.check_out_title)
            .setMessage(R.string.check_out_message)
            .setPositiveButton(R.string.check_out_positive) { _, _ ->
                val list = editViewModel.list.filter { !it.check }
                val deleteList = editViewModel.list.filter { it.check }
                runBlocking {
                    viewModel.deleteList(deleteList as MutableList<InfoList>)
                }

                editViewModel.list = (list as MutableList<InfoList>)

                GlobalScope.launch(Dispatchers.Main) {
                    editViewModel.checkData({ list -> list?.none { it.check } }){ showContentsAdapter.notifyDataSetChanged() }
                }
            }
            .setNegativeButton(R.string.check_out_negative, null)
            .setCancelable(false)
            .show()
    }

    //editViewModelの内容をデータベースに反映させる関数
    private fun updateDatabase(){
        val list: MutableList<InfoList> = editViewModel.list

        val updateList: MutableList<InfoList> = mutableListOf()
        val deleteList: MutableList<InfoList> = list.filter { it.item == "" } as MutableList<InfoList>

        for ( i in list.filter { it.item != "" }){
            updateList.add(InfoList(i.id, i.number, title, i.check, i.item))
        }

        runBlocking {
            viewModel.update(updateList)
            viewModel.deleteList(deleteList)
        }
    }

    //Toolbarの設定
    private fun setToolbar(){
        binding.contentsToolbar.also{
            when(getToolbarTextTheme(requireContext())){
                "white" -> {
                    it.setTitleTextColor(Color.WHITE)
                    it.inflateMenu(R.menu.show_menu_white)
                    it.setNavigationIcon(R.drawable.ic_back_arrow_white)
                }
                else -> {
                    it.inflateMenu(R.menu.show_menu)
                    it.setNavigationIcon(R.drawable.ic_back_arrow)
                }
            }

            it.title = if (title != "") title else getString(R.string.help_title)

            it.setOnClickListener{
                snackBar.dismiss()
                if (title != "") renameAlert(requireContext(), changeTitle, titleList, title)
                else{
                    AlertDialog.Builder(requireContext())
                        .setMessage(getString(R.string.no_edit_title_message))
                        .setPositiveButton(getString(R.string.no_edit_title_positive), null)
                        .show()
                }
            }
            it.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.share -> {
                        shareText()
                    }
                    R.id.reload -> {
                        showContentsAdapter.notifyDataSetChanged()
                    }
                    android.R.id.home ->{
                        findNavController().navigate(R.id.back_to_title_fragment)
                    }
                }
                true
            }
            it.setNavigationOnClickListener {
                findNavController().navigate(R.id.back_to_title_fragment)
            }
        }
    }

    //Undo機能の設定
    private fun setSnackBar(): Snackbar {
        return Snackbar.make(
            binding.coordinatorLayout,
            getString(R.string.snack_bar_message),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(getString(R.string.snack_bar_action)) {

                deleteValue?.let { value ->
                    val list = editViewModel.list
                    list.add(value)
                    editViewModel.list = list
                    showContentsAdapter.notifyItemInserted(list.sortedBy { it.number }
                        .sortedBy { it.check }.indexOf(value))
                    runBlocking {
                        viewModel.insert(value)
                    }
                }
                Snackbar.make(
                    binding.coordinatorLayout,
                    getString(R.string.snack_bar_after),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
    }

    //設定、ヘルプ画面に画面遷移するための処理
    private fun shareText() {
        var text = ""
        val listTop: String = requireActivity().getString(R.string.list_top)
        val setting = getShareAll(requireContext())
        for (i in editViewModel.list){
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