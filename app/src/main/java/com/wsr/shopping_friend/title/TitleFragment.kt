package com.wsr.shopping_friend.title

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wsr.shopping_friend.R
import com.wsr.shopping_friend.databinding.FragmentShowTitleBinding
import com.wsr.shopping_friend.preference.ShowPreference
import com.wsr.shopping_friend.preference.getToolbarTextTheme
import com.wsr.shopping_friend.share.renameTitle
import com.wsr.shopping_friend.share.view_model.AppViewModel
import kotlinx.coroutines.runBlocking

//タイトル名を並べるためのFragment
class TitleFragment : Fragment() {

    //viewBindingを利用するための宣言
    private var _binding: FragmentShowTitleBinding? = null
    private val binding get() = _binding!!

    //recyclerViewの定義
    private var recyclerView: RecyclerView? = null

    //使う変数の定義
    private lateinit var appViewModel: AppViewModel
    private lateinit var titleAdapter: TitleAdapter

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

        //DBとの接続用のViewModelの初期化
        appViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(AppViewModel::class.java).apply {

            //adapterにLiveDataで流れてきた値を入れる処理
            infoList.observe(viewLifecycleOwner, { list ->
                list?.let { titleAdapter.setInfoList(it) }
            })
        }

        //Adapterの初期化
        titleAdapter = TitleAdapter().apply {

            //タイトルが押されたときの処理
            clickTitleOnListener = { title -> makeShowContents(title) }

            //deleteボタンが押された際の処理
            clickDeleteOnListener = { title, position -> deleteList(title, position) }
        }
        
        //recyclerViewの初期化
        this.recyclerView = binding.showTitleRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = titleAdapter
        }

        //fabボタンが押された際の処理
        binding.fab.setOnClickListener {
            renameTitle(requireContext(), makeShowContents, titleAdapter.titleList)
        }
    }

    //設定から戻ったときに結果を反映するための処理
    override fun onResume() {
        super.onResume()

        //それぞれの再生成
        titleAdapter.notifyDataSetChanged()
        setToolbar()
    }

    //fragmentのインスタンスを破棄するときに行う処理
    override fun onDestroyView() {
        super.onDestroyView()
        this.recyclerView?.adapter = null
        this.recyclerView = null
        _binding = null
    }

    //タイトル名から、チェックリストを表示するための処理
    private val makeShowContents: (String) -> Unit = { title ->
        val action = TitleFragmentDirections.actionTitleFragmentToContentsFragment(title)
        findNavController().navigate(action)
    }

    //タイトル画面で削除ボタンが押されたときの処理
    private val deleteList: (String, Int) -> Unit =  { title, position ->
        AlertDialog.Builder(context)
            .setTitle(R.string.delete_with_title_title)
            .setMessage(R.string.delete_with_title_message)
            .setPositiveButton(R.string.delete_with_title_positive) { _, _ ->

                //adapterにリストが消えたことを通知
                titleAdapter.notifyItemRemoved(position)

                //データベースからリストを削除
                runBlocking {
                    appViewModel.deleteWithTitle(title)
                }
            }
            .setNegativeButton(R.string.delete_with_title_negative, null)
            .setCancelable(true)
            .show()
    }

    //Toolbarの設定
    private fun setToolbar(){
        binding.titleToolbar.also{

            //toolbarのメニューを削除
            it.menu.clear()

            //toolbarのテーマカラーを設定する処理
            when(getToolbarTextTheme(requireContext())){
                "white" -> {
                    it.setTitleTextColor(Color.WHITE)
                    it.inflateMenu(R.menu.title_menu_white)
                }
                else ->{
                    it.setTitleTextColor(Color.BLACK)
                    it.inflateMenu(R.menu.title_menu)
                }
            }

            //アイコンをクリックされたときの処理
            it.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId) {

                    //設定画面
                    R.id.settings -> {
                        val intent = Intent(requireActivity(), ShowPreference::class.java)
                        intent.putExtra("Purpose", "settings")
                        startActivity(intent)
                    }

                    //ヘルプ画面
                    R.id.help -> {
                        val action =
                            TitleFragmentDirections.actionTitleFragmentToContentsFragment("")
                        findNavController().navigate(action)
                    }
                }
                true
            }
        }
    }
}