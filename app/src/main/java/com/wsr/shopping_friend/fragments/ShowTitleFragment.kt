package com.wsr.shopping_friend.fragments

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
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.wsr.shopping_friend.R
import com.wsr.shopping_friend.adapter.MainAdapter
import com.wsr.shopping_friend.preference.ShowPreference
import com.wsr.shopping_friend.type_file.renameAlert
import com.wsr.shopping_friend.view_model.AppViewModel
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
        mainAdapter = MainAdapter(requireContext())

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
            list?.let { mainAdapter.setInfoList(it) }
        })

        //fabボタンが押された際の処理
        fab.setOnClickListener {
            renameAlert(requireContext(), makeShowContents, mainAdapter.titleList, "")
        }

        //タイトルが押されたときの処理
        mainAdapter.clickTitleOnListener = { title ->
            makeShowContents(title)
        }

        //deleteボタンが押された際の処理
        mainAdapter.clickDeleteOnListener = { title, position ->
            AlertDialog.Builder(context)
                .setTitle(R.string.delete_with_title_title)
                .setMessage(R.string.delete_with_title_message)
                .setPositiveButton(R.string.delete_with_title_positive) { _, _ ->
                    mainAdapter.notifyItemRemoved(position)
                    viewModel.deleteWithTitle(title)
                    //mainAdapter.notifyItemMoved(0,1)
                }
                .setNegativeButton(R.string.delete_with_title_negative, null)
                .setCancelable(true)
                .show()
        }
        /*val mAdView: AdView = requireActivity().findViewById(R.id.adView)
        val adRequest: AdRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)*/
    }

    //設定から戻ったときに結果を反映するための処理
    override fun onResume() {
        super.onResume()
        mainAdapter.notifyDataSetChanged()
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
        toolbar.title = getString(R.string.app_name)
        toolbar.navigationIcon = null
        toolbar.menu.setGroupVisible(R.id.rename_group, false)
        toolbar.menu.setGroupVisible(R.id.help_group, true)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.settings -> showSettings()
                R.id.help -> {
                    val action =
                        ShowTitleFragmentDirections.actionTitleFragmentToContentsFragment("")
                    findNavController().navigate(action)
                }
            }
            true
        }
    }

    //設定、ヘルプ画面に画面遷移するための処理
    private fun showSettings(){
        val intent = Intent(requireActivity(), ShowPreference::class.java)
        intent.putExtra("Purpose", "settings")
        startActivity(intent)
    }
}