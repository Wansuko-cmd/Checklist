package com.wsr.shopping_friend.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.icu.text.IDNA
import android.os.Bundle
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
    private var deleteValue: InfoList? = null

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

        //タイトルの初期化
        title = args.content

        //ヘルプを選択した時の処理
        if (title == "") setHelp(requireContext(), editViewModel)

        //actionbarの設定
        (activity as AppCompatActivity).supportActionBar?.title = title

        //snackBarの設定
        snackBar = setSnackBar()

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

        //編集用のViewModelの初期化
        editViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(EditViewModel::class.java)

        //Adapterの初期化
        showContentsAdapter = ListAdapter(requireContext(), editViewModel, this)

        //recyclerViewの初期化
        this.recyclerView = binding.showContentsRecyclerView.apply{
            setOnClickListener { it.requestFocus() }
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = showContentsAdapter
        }

        binding.apply {

            editButton.setOnClickListener{ addElements() }

            deleteCheckButton.setOnClickListener { deleteElements() }
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
                        && target is ListViewHolder
                        && !viewHolder.check.isChecked
                        && !target.check.isChecked
                    ) {
                        val list = editViewModel.list
                        val fromPosition = viewHolder.adapterPosition
                        val toPosition = target.adapterPosition

                        val fromValue = list[fromPosition]
                        list[fromPosition] = list[fromPosition].copy(number = list[toPosition].number)
                        list[toPosition] = list[toPosition].copy(number = fromValue.number)

                        editViewModel.list = list
                        showContentsAdapter.notifyItemMoved(fromPosition, toPosition)
                    }
                    return false
                }

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
        updateDatabase()
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
            editViewModel.initializeList(tempList)
        }
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
    fun addElements() {

        snackBar.dismiss()

        val id = UUID.randomUUID().toString()
        val number = editViewModel.list.maxByOrNull { it.number }?.number?.plus(1) ?: 0
        val newColumn = InfoList(id, number, title, false, "")

        val newList = editViewModel.list
        newList.add(newColumn)
        editViewModel.list  = newList
        showContentsAdapter.notifyItemInserted(editViewModel.list.filter { !it.check }.size)
        runBlocking {
            viewModel.insert(newColumn)
        }

        newList.indexOfFirst { it.id == id }.run{
            recyclerView!!.scrollToPosition(this)
            showContentsAdapter.focus = this
            showContentsAdapter.notifyItemInserted(this)
        }
    }

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

                showContentsAdapter.notifyDataSetChanged()
            }
            .setNegativeButton(R.string.check_out_negative, null)
            .setCancelable(false)
            .show()
    }

    //editViewModelの内容をデータベースに反映させる関数
    private fun updateDatabase(){
        val list: MutableList<InfoList> = editViewModel.list
        val updateList: MutableList<InfoList> = list.filter { it.item != "" } as MutableList<InfoList>
        val deleteList: MutableList<InfoList> = list.filter { it.item == "" } as MutableList<InfoList>

        runBlocking {
            viewModel.update(updateList)
            viewModel.deleteList(deleteList)
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