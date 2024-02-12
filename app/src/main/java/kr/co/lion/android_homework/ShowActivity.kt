package kr.co.lion.android_homework

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kr.co.lion.android_homework.databinding.ActivityShowBinding

class ShowActivity : AppCompatActivity() {

    lateinit var activityShowBinding: ActivityShowBinding
    var modifyMemoData: MemoData? = null

    // ModifyActivity 런처
    lateinit var modifyActivityLauncher : ActivityResultLauncher<Intent>

    // 수정된 메모의 정보를 담을 리스트
    val modifyMemoList = mutableListOf<MemoData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityShowBinding = ActivityShowBinding.inflate(layoutInflater)
        setContentView(activityShowBinding.root)

        setData()
        setToolbar()
        setView()
        setEvent()
    }

    // 데이터 설정
    fun setData() {
        // ModifyActivity 런처
        val contract1 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // 작업 결과가 OK라면
            if (result.resultCode == RESULT_OK) {
                // 전달된 Intent 객체가 있다면
                if (result.data != null) {
                    // 수정된 메모 객체를 추출한다.
                    modifyMemoData = result.data?.getParcelableExtra("memoData")
                    modifyMemoList.add(modifyMemoData!!)
                    // 수정된 메모 데이터를 표시하는 함수 호출
                    showModifiedMemo(modifyMemoData)
                }
            }
        }
        modifyActivityLauncher = contract1
    }

    // 수정된 메모 데이터를 화면에 표시하는 함수
    fun showModifiedMemo(modifyMemoData: MemoData?) {
        modifyMemoData?.let {
            activityShowBinding.apply {
                // 수정된 메모 데이터를 화면에 표시
                textViewShowTitle.text = it.title
                textViewShowContent.text = it.content
                // 여기에 수정된 날짜를 표시할 코드 추가 가능
            }
        }
    }

    // 툴바 설정
    fun setToolbar() {
        activityShowBinding.apply {
            toolbarShow.apply {
                // 타이틀
                title = "메모 보기"
                // Back
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    // 수정된 내용을 MainActivity로 전달하고 ShowActivity 종료
                    val resultIntent = Intent()
                    resultIntent.putExtra("modifyMemoData", modifyMemoData)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
                // 메뉴
                inflateMenu(R.menu.menu_show)
                // 메뉴의 리스너
                setOnMenuItemClickListener {
                    // 메뉴 아이템 아이디로 분기한다.
                    when(it.itemId) {
                        // 수정
                        R.id.menu_show_modify -> {
                            // ModifyActivity를 실행해준다.
                            val modifyIntent = Intent(this@ShowActivity, ModifyActivity::class.java)
                            modifyIntent.putExtra("memoData", modifyMemoData)
                            modifyActivityLauncher.launch(modifyIntent)
                        }
                        // 삭제
                        R.id.menu_show_delete -> {
                            // 삭제하는 기능
                            finish()
                        }
                    }
                    true
                }
            }
        }
    }


    // 뷰 설정
    fun setView() {
        activityShowBinding.apply {

            // 제목
            textViewShowTitle.apply {
                // Intent로부터 메모 정보 객체를 추출한다.
                val memoData = intent.getParcelableExtra<MemoData>("memoData")
                text = memoData?.title
            }

            // 작성 날짜
            textViewShowDate.apply {
                // Intent로부터 메모 정보 객체를 추출한다.
                val memoData = intent.getParcelableExtra<MemoData>("memoData")
                text = "01/31"
            }

            // 내용
            textViewShowContent.apply {
                // Intent로부터 메모 정보 객체를 추출한다.
                val memoData = intent.getParcelableExtra<MemoData>("memoData")
                text = memoData?.content
            }
        }
    }

    // 이벤트 설정
    fun setEvent() {

    }
}
