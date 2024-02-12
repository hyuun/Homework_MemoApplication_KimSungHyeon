package kr.co.lion.android_homework

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.android_homework.databinding.ActivityMainBinding
import kr.co.lion.android_homework.databinding.RowMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    // InputActitvity 런처
    lateinit var inputActivityLauncher : ActivityResultLauncher<Intent>
    // ShowActivity 런처
    lateinit var showActivityLauncher : ActivityResultLauncher<Intent>

    // 메모들의 정보를 담을 리스트
    val memoList = mutableListOf<MemoData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        setData()
        setToolbar()
        setView()
        setEvent()
    }

    // 기본 데이터 및 객체 셋팅
    fun setData() { 
        // InputActivity 런처
        val contract1 = ActivityResultContracts.StartActivityForResult()
        inputActivityLauncher = registerForActivityResult(contract1) {
            // 작업 결과가 OK라면
            if (it.resultCode == RESULT_OK) {
                // 전달된 Intent 객체가 있다면
                if (it.data != null) {
                    // 메모 객체를 추출한다.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val memoData = it.data?.getParcelableExtra("memoData", MemoData::class.java)
                        memoList.add(memoData!!)
                        activityMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
                    } else {
                        val memoData = it.data?.getParcelableExtra<MemoData>("memoData")
                        memoList.add(memoData!!)
                        activityMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }

        // ShowActivity 런처
        val contract2 = ActivityResultContracts.StartActivityForResult()
        showActivityLauncher = registerForActivityResult(contract2) {
            // 작업 결과가 OK라면
            if (it.resultCode == RESULT_OK) {
                // 전달된 Intent 객체가 있다면
                if (it.data != null) {
                    // 메모 객체를 추출한다.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val memoData = it.data?.getParcelableExtra("memoData", MemoData::class.java)
                        memoList.add(memoData!!)
                        activityMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
                    } else {
                        val memoData = it.data?.getParcelableExtra<MemoData>("memoData")
                        memoList.add(memoData!!)
                        activityMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    // 툴바 설정
    fun setToolbar() {
        activityMainBinding.apply {
            toolbarMain.apply {
                // 타이틀
                title = "메모 관리"
                // 메뉴
                inflateMenu(R.menu.menu_main)
                // 메뉴의 리스너
                setOnMenuItemClickListener {
                    // 메뉴의 id로 분기한다.
                    when(it.itemId) {
                        R.id.menu_main_add -> {
                            // InputActivity를 실행시킨다.
                            val inputIntent = Intent(this@MainActivity, InputActivity::class.java)
                            inputActivityLauncher.launch(inputIntent)
                        }
                    }
                    true
                }
            }
        }
    }

    // 뷰 설정
    fun setView() {
        activityMainBinding.apply {
            // RecyclerView 어댑처
            recyclerViewMain.apply {
                // 어댑터
                adapter = RecyclerViewAdapter()
                // 레이아웃 매니저
                layoutManager = LinearLayoutManager(this@MainActivity)
                // 데코레이션
                val deco = MaterialDividerItemDecoration(this@MainActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    // 이벤트 설정
    fun setEvent() {

    }

    // RecyclerView 어댑터
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderMain>() {

        // ViewHolder
        inner class ViewHolderMain(rowMainBinding: RowMainBinding) : RecyclerView.ViewHolder(rowMainBinding.root) {
            val rowMainBinding:RowMainBinding

            init {
                this.rowMainBinding = rowMainBinding

                // 항목 클릭 시 전체가 클릭되도록 가로 세로 길이를 정해준다.
                this.rowMainBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                // 항목을 눌렀을 때의 리스너
                this.rowMainBinding.root.setOnClickListener {

                    // ShowActivity를 실행한다.
                    val showIntent = Intent(this@MainActivity, ShowActivity::class.java)

                    // 선택한 항목 번째의 메뉴 객체를 Intent에 넣어준다.
                    showIntent.putExtra("memoData", memoList[adapterPosition])

                    showActivityLauncher.launch(showIntent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMain {
            val rowMainBinding = RowMainBinding.inflate(layoutInflater)
            val viewHolderMain = ViewHolderMain(rowMainBinding)

            return viewHolderMain
        }

        override fun getItemCount(): Int {
            // return 20
            return memoList.size
        }

        override fun onBindViewHolder(holder: ViewHolderMain, position: Int) {
//            holder.rowMainBinding.textViewRowMainTitle.text = "제목 $position"
//            holder.rowMainBinding.textViewRowMainContent.text = "$position 내용"
            holder.rowMainBinding.textViewRowMainTitle.text = memoList[position].title
            holder.rowMainBinding.textViewRowMainContent.text = memoList[position].content
        }
    }
}