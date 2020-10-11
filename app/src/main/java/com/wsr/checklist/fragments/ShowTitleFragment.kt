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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wsr.checklist.R
import com.wsr.checklist.adapter.MainAdapter
import com.wsr.checklist.type_file.renameAlert
import com.wsr.checklist.view.ShowPreference
import com.wsr.checklist.view_model.AppViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_show_title.*

class ShowTitleFragment : Fragment(){
    //recyclerViewの定義
    private var recyclerView: RecyclerView? = null

    //使う変数の定義
    private lateinit var viewModel: AppViewModel
    private lateinit var mainAdapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_show_title, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //変数の初期化
        viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        mainAdapter = MainAdapter()

        //toolbarの設定
        setToolbar()

        //recyclerViewの初期化
        this.recyclerView = show_title_recycler_view
        this.recyclerView?.apply{
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = mainAdapter
        }

        //viewModelが更新された際の処理
        viewModel.infoList.observe(viewLifecycleOwner, { list ->
            list?.let{mainAdapter.setInfoList(it)}
        })

        //fabボタンが押された際の処理
        fab.setOnClickListener {
            renameAlert(requireContext(), makeShowContents, mainAdapter.titleList, "")
        }

        //タイトルが押されたときの処理
        mainAdapter.clickTitleOnListener = {title ->
            makeShowContents(title)
        }

        //deleteボタンが押された際の処理
        mainAdapter.clickDeleteOnListener = {title, position ->
            AlertDialog.Builder(context)
                .setTitle(R.string.delete_with_title_title)
                .setMessage(R.string.delete_with_title_message)
                .setPositiveButton(R.string.delete_with_title_positive) { _, _ ->
                    mainAdapter.notifyItemRemoved(position)
                    viewModel.deleteWithTitle(title)
                }
                .setNegativeButton(R.string.delete_with_title_negative, null)
                .setCancelable(true)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.recyclerView?.adapter = null
        this.recyclerView = null
    }


    //タイトル名から、チェックリストを表示するための処理
    private val makeShowContents: (String) -> Unit = { title ->
        val action = ShowTitleFragmentDirections.actionTitleFragmentToContentsFragment(title)
        findNavController().navigate(action)
    }

    //toolbarの設定
    private fun setToolbar(){
        val toolbar = requireActivity().main_toolbar
        toolbar.title = "CheckList"
        toolbar.navigationIcon = null
        toolbar.menu.setGroupVisible(R.id.rename_group, false)
        toolbar.menu.setGroupVisible(R.id.help_group, true)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.settings -> showSettingsOrHelp("settings")
                R.id.help -> showSettingsOrHelp("help")
            }
            true
        }
    }

    //設定、ヘルプ画面に画面遷移するための処理
    private fun showSettingsOrHelp(purpose: String){
        val intent = Intent(requireActivity(), ShowPreference::class.java)
        intent.putExtra("Purpose", purpose)
        startActivity(intent)
    }
}