package com.wsr.shopping_friend.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wsr.shopping_friend.R
import com.wsr.shopping_friend.adapter.MainAdapter
import com.wsr.shopping_friend.databinding.FragmentShowTitleBinding
import com.wsr.shopping_friend.type_file.renameAlert
import com.wsr.shopping_friend.view_model.AppViewModel
//タイトル名を並べるためのFragment
class ShowTitleFragment : Fragment() {

    //viewBindingを利用するための宣言
    private var _binding: FragmentShowTitleBinding? = null
    private val binding get() = _binding!!

    //recyclerViewの定義
    private var recyclerView: RecyclerView? = null

    //使う変数の定義
    private lateinit var viewModel: AppViewModel
    private lateinit var mainAdapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowTitleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //変数の初期化
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(AppViewModel::class.java)
        mainAdapter = MainAdapter(requireContext())

        //recyclerViewの初期化
        this.recyclerView = binding.showTitleRecyclerView
        this.recyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = mainAdapter
        }

        //toolbarの設定
        //setToolbar()

        //fabボタンが押された際の処理
        binding.fab.setOnClickListener {
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
                }
                .setNegativeButton(R.string.delete_with_title_negative, null)
                .setCancelable(true)
                .show()
        }

        //viewModelが更新された際の処理
        viewModel.infoList.observe(viewLifecycleOwner, { list ->
            list?.let { mainAdapter.setInfoList(it) }
        })
    }

    //設定から戻ったときに結果を反映するための処理
    override fun onResume() {
        super.onResume()
        mainAdapter.notifyDataSetChanged()
        requireActivity().reportFullyDrawn()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.recyclerView?.adapter = null
        this.recyclerView = null
        _binding = null
    }


    //タイトル名から、チェックリストを表示するための処理
    private val makeShowContents: (String) -> Unit = { title ->
        val action = ShowTitleFragmentDirections.actionTitleFragmentToContentsFragment(title)
        findNavController().navigate(action)
    }

    //toolbarの設定
    /*private fun setToolbar() {
        val toolbar = requireActivity().main_toolbar
        toolbar.title = getString(R.string.app_name)
        toolbar.navigationIcon = null
        toolbar.menu.setGroupVisible(R.id.rename_group, false)
        toolbar.menu.setGroupVisible(R.id.help_group, true)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
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
    private fun showSettings() {
        val intent = Intent(requireActivity(), ShowPreference::class.java)
        intent.putExtra("Purpose", "settings")
        startActivity(intent)
    }*/
}