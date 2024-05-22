package com.example.algorythmics.fragments.course

import android.app.AlertDialog
import android.content.ClipData
import android.os.Bundle
import android.os.Looper
import android.view.DragEvent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.algorythmics.R
import com.example.algorythmics.retrofit.models.CodeModel
import com.example.algorythmics.retrofit.repositories.CodeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CodeFragment : Fragment() {
    private lateinit var codeRepository: CodeRepository
    private lateinit var correctAnswers: List<String>
    private lateinit var answersLayout: LinearLayout
    private var submitButton: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        codeRepository = CodeRepository()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_code, container, false)

        val backBtn = ImageView(context).apply {
            id = View.generateViewId()
            setImageResource(R.drawable.back2)
            layoutParams = FrameLayout.LayoutParams(
                40.dpToPx(),
                40.dpToPx()
            ).apply {
                gravity = Gravity.START or Gravity.TOP
                setMargins(16.dpToPx(), 16.dpToPx(), 0, 0)
            }
            setPadding(8.dpToPx(), 8.dpToPx(), 8.dpToPx(), 8.dpToPx())
            setOnClickListener {
                findNavController().navigateUp()
            }
        }
        (rootView as ViewGroup).addView(backBtn)

        val mainLinearLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            ).apply {
                setMargins(0, 300, 0, 0)
            }
        }

        lifecycleScope.launch {
            val codeList = getCodeFromRepository()
            if (codeList.isNotEmpty()) {
                val algorithmId = arguments?.getString("algorithmId")
                val code = codeList.find { it.algorithmId == algorithmId }
                code?.let {
                    correctAnswers = it.correctAnswers.split("\n")

                    val codeText = it.algorithmCode
                    val lines = codeText.split("\n")

                    val codeContainer = LinearLayout(context).apply {
                        orientation = LinearLayout.VERTICAL
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(16, 16, 16, 16)
                            setPadding(16, 16, 16, 16)
                            setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
                            setBackgroundResource(R.drawable.bg_word)
                        }
                        elevation = 8f
                    }

                    for (line in lines) {
                        val lineLayout = LinearLayout(context).apply {
                            orientation = LinearLayout.HORIZONTAL
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                        }

                        val parts = line.split("?")
                        for (i in parts.indices) {
                            if (i > 0) {
                                val editText = EditText(context).apply {
                                    setText("")
                                    minWidth = 50
                                    layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    )
                                    tag = "codeEditText"
                                }
                                lineLayout.addView(editText)
                            }

                            val textView = TextView(context).apply {
                                text = parts[i]
                                layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                            }
                            lineLayout.addView(textView)
                        }

                        codeContainer.addView(lineLayout)
                    }

                    mainLinearLayout.addView(codeContainer)

                    answersLayout = LinearLayout(context).apply {
                        orientation = LinearLayout.VERTICAL
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(0, 100, 0, 0)
                        }
                    }

                    val answers = it.answers.split("\n")
                    val maxWidth = resources.displayMetrics.widthPixels
                    var currentRow = LinearLayout(context).apply {
                        orientation = LinearLayout.HORIZONTAL
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    }

                    answers.forEach { answer ->
                        val answerEditText = EditText(context).apply {
                            setText(answer)
                            textSize = 14f
                            isFocusable = false
                            isFocusableInTouchMode = false
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(10, 20, 10, 20)
                            }
                            setBackgroundResource(R.drawable.bg_word)
                            elevation = 4f
                            setPadding(12, 12, 12, 12)
                            setTextColor(resources.getColor(android.R.color.black))
                            gravity = Gravity.CENTER
                            minWidth = 100
                            maxLines = 3
                            setOnClickListener {
                                handleAnswerClick(this, answer)
                            }
                        }

                        answerEditText.measure(
                            View.MeasureSpec.UNSPECIFIED,
                            View.MeasureSpec.UNSPECIFIED
                        )
                        val answerWidth = answerEditText.measuredWidth

                        currentRow.measure(
                            View.MeasureSpec.UNSPECIFIED,
                            View.MeasureSpec.UNSPECIFIED
                        )
                        val currentRowWidth = currentRow.measuredWidth

                        if (currentRowWidth + answerWidth > maxWidth) {
                            answersLayout.addView(currentRow)
                            currentRow = LinearLayout(context).apply {
                                orientation = LinearLayout.HORIZONTAL
                                layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                            }
                        }

                        currentRow.addView(answerEditText)
                    }

                    answersLayout.addView(currentRow)
                    mainLinearLayout.addView(answersLayout)
                } ?: showError("No code found for this algorithm")
            } else {
                showError("No codes found")
            }
        }

        val scrollView = ScrollView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(ContextCompat.getColor(context, R.color.pale_blue))
            addView(mainLinearLayout)
        }
        return scrollView
    }

    private fun handleAnswerClick(answerEditText: EditText, answer: String) {
        val firstEmptyEditText = findFirstEmptyEditText(view as ViewGroup)
        firstEmptyEditText?.let { editText ->
            editText.setText(answer)
            (answerEditText.parent as? ViewGroup)?.removeView(answerEditText)
            answerEditText.textSize = 12f

            if (areAllEditTextsFilled(view as ViewGroup)) {
                showSubmitButton()
            }
        }
    }

    private fun showSubmitButton() {
        val builder = AlertDialog.Builder(context)
        val dialogView = layoutInflater.inflate(R.layout.dialog_submit, null)
        builder.setView(dialogView)

        val dialog = builder.create()

        val submitButton = dialogView.findViewById<Button>(R.id.submit_button)
        submitButton.setOnClickListener {
            checkAnswers()
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun checkAnswers() {
        val root = view as ViewGroup
        var editTextIndex = 0

        for (i in 0 until root.childCount) {
            val child = root.getChildAt(i)
            if (child is ViewGroup) {
                editTextIndex = checkAnswersInViewGroup(child, editTextIndex)
            }
        }
    }

    private fun checkAnswersInViewGroup(root: ViewGroup, index: Int): Int {
        var currentIndex = index
        for (i in 0 until root.childCount) {
            val child = root.getChildAt(i)
            if (child is ViewGroup) {
                currentIndex = checkAnswersInViewGroup(child, currentIndex)
            } else if (child is EditText && child.tag == "codeEditText") {
                val userAnswer = child.text.toString()
                val correctAnswer = correctAnswers.getOrNull(currentIndex)

                if (correctAnswer != null && userAnswer == correctAnswer) {
                    child.setBackgroundResource(R.drawable.green_background)
                } else {
                    child.setBackgroundResource(R.drawable.red_background)
                }
                currentIndex++
            }
        }
        return currentIndex
    }

    private fun addAnswerBack(answerEditText: EditText) {
        val currentRow = answersLayout.getChildAt(answersLayout.childCount - 1) as LinearLayout
        currentRow.addView(answerEditText)
    }

    private fun findFirstEmptyEditText(root: ViewGroup): EditText? {
        for (i in 0 until root.childCount) {
            val child = root.getChildAt(i)
            if (child is ViewGroup) {
                val found = findFirstEmptyEditText(child)
                if (found != null) {
                    return found
                }
            } else if (child is EditText && child.text.isEmpty()) {
                return child
            }
        }
        return null
    }

    private fun areAllEditTextsFilled(root: ViewGroup): Boolean {
        for (i in 0 until root.childCount) {
            val child = root.getChildAt(i)
            if (child is ViewGroup) {
                if (!areAllEditTextsFilled(child)) {
                    return false
                }
            } else if (child is EditText && child.text.isEmpty()) {
                return false
            }
        }
        return true
    }

    private suspend fun getCodeFromRepository(): List<CodeModel> {
        return withContext(Dispatchers.IO) {
            codeRepository.getAllCode()
        }
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}
