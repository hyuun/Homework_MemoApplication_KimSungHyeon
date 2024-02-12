package kr.co.lion.android_homework

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.inputmethod.InputMethodManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kr.co.lion.android_homework.databinding.ActivityModifyBinding
import kotlin.concurrent.thread

class ModifyActivity : AppCompatActivity() {

    lateinit var activityModifyBinding: ActivityModifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityModifyBinding = ActivityModifyBinding.inflate(layoutInflater)
        setContentView(activityModifyBinding.root)

        setData()
        setToolbar()
        //setView()
        initView()
        setEvent()
    }

    // 데이터 설정
    fun setData() {

    }

    // 툴바 설정
    fun setToolbar() {
        activityModifyBinding.apply {
            toolbarModify.apply {
                // 타이틀
                title = "메모 수정"
                // Back
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                // 메뉴
                inflateMenu(R.menu.menu_modify)
                // 메뉴 리스너
                setOnMenuItemClickListener {
                    // 메뉴 아이템 아이디로 분기한다.
                    when(it.itemId) {
                        R.id.menu_modify_done -> {
                            processModifyDone()
                        }
                    }
                    true
                }
            }
        }
    }

    // 초기 뷰 설정
    // 왜...수정 화면이 한 번 더 나오는거지...?
    fun initView() {
        activityModifyBinding.apply {
            // Intent로부터 메모 객체를 추출한다.
            val memoData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("memoData", MemoData::class.java)
            } else {
                intent.getParcelableExtra<MemoData>("memoData")
            }

            textFieldModifyTitle.setText(memoData?.title)
            textFieldModifyContent.setText(memoData?.content)

            // 뷰에 포커스를 준다.
            textFieldModifyTitle.requestFocus()
            // 키보드를 올린다.
            // 이때, View를 지정해야 한다.
            showSoftModify(textFieldModifyTitle)

            // 메모 내용 앱력 칸
            // 엔터키를 누르면 입력 완료 처리를 한다.
            textFieldModifyContent.setOnEditorActionListener { v, actionId, event ->
                processModifyDone()
                true
            }
        }
    }

//    // 뷰 설정
//    fun setView() {
//        activityModifyBinding.apply {
//            // 뷰에 포커스를 준다.
//            textFieldModifyTitle.requestFocus()
//            // 키보드를 올린다.
//            // 이때, View를 지정해야 한다.
//            showSoftModify(textFieldModifyTitle)
//
//            // 메모 내용 앱력 칸
//            // 엔터키를 누르면 입력 완료 처리를 한다.
//            textFieldModifyContent.setOnEditorActionListener { v, actionId, event ->
//                processModifyDone()
//                true
//            }
//        }
//    }

    // 입력 완료 처리
    fun processModifyDone() {
        activityModifyBinding.apply {
            // 사용자가 입력한 내용을 가져온다.
            val title = textFieldModifyTitle.text.toString()
            val content = textFieldModifyContent.text.toString()

            // 입력 검사
            if (title.isEmpty()) {
                showDialog("제목 입력 오류", "제목을 입력해주세요", textFieldModifyTitle)
                return
            }
            if (content.isEmpty()) {
                showDialog("내용 입력 오류", "내용을 입력해주세요", textFieldModifyContent)
                return
            }

            // 입력받은 정보를 객체에 담아준다.
            val memoData = MemoData(title, content)

            Snackbar.make(activityModifyBinding.root, "수정이 완료되었습니다", Snackbar.LENGTH_SHORT).show()

            // 이전으로 돌아간다.
            val resultIntent = Intent()
            resultIntent.putExtra("memoData", memoData)

            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    // 다이얼로그를 보여주는 메서드
    fun showDialog(title:String, message:String, focusView: TextInputEditText) {
        // 다이얼로그를 보여준다.
        val builder = MaterialAlertDialogBuilder(this@ModifyActivity).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                focusView.setText("")
                focusView.requestFocus()
                showSoftModify(focusView)
            }
        }
    }

    // 포커스르 주고 키보드를 올려주는 메서드
    fun showSoftModify(focusView:TextInputEditText) {
        thread {
            SystemClock.sleep(1000)
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(focusView, 0)
        }
    }

    // 이벤트 설정
    fun setEvent() {

    }
}