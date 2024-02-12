package kr.co.lion.android_homework

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.inputmethod.InputMethodManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kr.co.lion.android_homework.databinding.ActivityInputBinding
import kotlin.concurrent.thread

class InputActivity : AppCompatActivity() {

    lateinit var activityInputBinding: ActivityInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityInputBinding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(activityInputBinding.root)

        setData()
        setToolbar()
        setView()
        setEvent()
    }

    // 기본 데이터 설정
    fun setData() {

    }

    // 툴바 설정
    fun setToolbar() {
        activityInputBinding.apply {
            toolbarInput.apply {
                // 타이틀
                title = "메모 작성"
                // Back
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                // 메뉴
                inflateMenu(R.menu.menu_input)
                setOnMenuItemClickListener {
                    // 메뉴 아이템 아이디로 분기한다.
                    when(it.itemId) {
                        R.id.menu_input_done -> {
                            processInputDone()
                        }
                    }
                    true
                }
            }
        }
    }

    // 뷰 설정
    fun setView(){
        activityInputBinding.apply {
            // 뷰에 포커스를 준다.
            textFieldInputTitle.requestFocus()
            // 키보드를 올린다.
            // 이때, View를 지정해줘야 한다.
            showSoftInput(textFieldInputTitle)

            // 메모 내용 입력 칸
            // 엔터키를 누르면 완료 처리를 한다.
            textFieldInputContent.setOnEditorActionListener { v, actionId, event ->
                processInputDone()
                true
            }
        }
    }

    // 입력 완료 처리
    fun processInputDone() {
        activityInputBinding.apply {
            // 사용자가 입력한 내용을 갖고 온다.
            val title = textFieldInputTitle.text.toString()
            val content = textFieldInputContent.text.toString()

            // 입력 검사
            if (title.isEmpty()) {
                showDialog("제목 입력 오류", "제목을 입력해주세요", textFieldInputTitle)
                return
            }
            if (content.isEmpty()) {
                showDialog("내용 입력 오류", "내용을 입력해주세요", textFieldInputContent)
                return
            }

            // 입력받은 정보를 객체에 담아준다.
            val memoData = MemoData(title, content)

            Snackbar.make(activityInputBinding.root, "등록이 완료되었습니다.", Snackbar.LENGTH_SHORT).show()

            // 이전으로 돌아간다.
            val resultIntent = Intent()
            resultIntent.putExtra("memoData", memoData)

            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    // 다이얼로그를 보여주는 메서드
    fun showDialog(title:String, message:String, focusView:TextInputEditText) {
        // 다이얼로그를 보여준다.
        val builder = MaterialAlertDialogBuilder(this@InputActivity).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                focusView.setText("")
                focusView.requestFocus()
                showSoftInput(focusView)
            }
        }
        builder.show()
    }

    // 포커스를 주고 키보드를 올려주는 메서드
    fun showSoftInput(focusView: TextInputEditText) {
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