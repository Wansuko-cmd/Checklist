package com.wsr.shopping_friend.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wsr.shopping_friend.R
import com.wsr.shopping_friend.adapter.MainAdapter
import com.wsr.shopping_friend.databinding.FragmentShowTitleBinding
import com.wsr.shopping_friend.preference.ShowPreference
import com.wsr.shopping_friend.type_file.renameAlert
import com.wsr.shopping_friend.view_model.AppViewModel
import kotlinx.coroutines.runBlocking

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.title_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            //設定画面
            R.id.settings -> {
                val intent = Intent(requireActivity(), ShowPreference::class.java)
                intent.putExtra("Purpose", "settings")
                startActivity(intent)
                true
            }
            //ヘルプ画面
            R.id.help -> {
                val action =
                    ShowTitleFragmentDirections.actionTitleFragmentToContentsFragment("")
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ActionBarのタイトルの設定
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)

        //DBとの接続用のViewModelの初期化
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(AppViewModel::class.java).apply {
            infoList.observe(viewLifecycleOwner, { list ->
                list?.let { mainAdapter.setInfoList(it) }
            })
        }

        //Adapterの初期化
        mainAdapter = MainAdapter(requireContext()).apply {

            //タイトルが押されたときの処理
            clickTitleOnListener = { title -> makeShowContents(title) }

            //deleteボタンが押された際の処理
            clickDeleteOnListener = { title, position -> deleteList(title, position) }
        }
        
        //recyclerViewの初期化
        this.recyclerView = binding.showTitleRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = mainAdapter
        }

        //fabボタンが押された際の処理
        binding.fab.setOnClickListener {
            renameAlert(requireContext(), makeShowContents, mainAdapter.titleList, "")
        }
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

    //タイトル画面で削除ボタンが押されたときの処理
    private val deleteList: (String, Int) -> Unit =  { title, position ->
        AlertDialog.Builder(context)
            .setTitle(R.string.delete_with_title_title)
            .setMessage(R.string.delete_with_title_message)
            .setPositiveButton(R.string.delete_with_title_positive) { _, _ ->
                mainAdapter.notifyItemRemoved(position)
                runBlocking {
                    viewModel.deleteWithTitle(title)
                }
            }
            .setNegativeButton(R.string.delete_with_title_negative, null)
            .setCancelable(true)
            .show()
    }
}