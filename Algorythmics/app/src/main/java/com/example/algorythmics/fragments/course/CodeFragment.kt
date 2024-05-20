package com.example.algorythmics.fragments.course

import android.content.ClipData
import android.os.Bundle
import android.os.Looper
import android.view.DragEvent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        codeRepository = CodeRepository()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_code, container, false)

        // Adding ImageView to the top of the screen
        val backBtn = ImageView(context).apply {
            id = View.generateViewId()
            setImageResource(R.drawable.back2)
            layoutParams = FrameLayout.LayoutParams(
                40.dpToPx(),
                40.dpToPx()
            ).apply {
                gravity = Gravity.START or Gravity.TOP // Top-left alignment
                setMargins(16.dpToPx(), 16.dpToPx(), 0, 0) // Margins from top and left
            }
            setPadding(8.dpToPx(), 8.dpToPx(), 8.dpToPx(), 8.dpToPx()) // Padding
            // Add click listener to navigate back
            setOnClickListener {
                findNavController().navigateUp() // Navigate up one level
            }
        }
        (rootView as ViewGroup).addView(backBtn)

        val mainLinearLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            ).apply {
                setMargins(0, 300, 0, 0) // Az egész tartalom lejjebb kezdődik
            }
        }

        lifecycleScope.launch {
            val codeList = getCodeFromRepository()
            if (codeList.isNotEmpty()) {
                val algorithmId = arguments?.getString("algorithmId")
                val code = codeList.find { it.algorithmId == algorithmId }
                code?.let {
                    correctAnswers = it.correctAnswers.split("\n") // Helyes válaszok listája

                    val codeText = it.algorithmCode
                    val lines = codeText.split("\n")

                    val codeContainer = LinearLayout(context).apply {
                        orientation = LinearLayout.VERTICAL
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(16, 16, 16, 16) // Margins beállítása a fehér doboz körül
                            setPadding(16, 16, 16, 16) // Padding beállítása a fehér dobozban
                            setBackgroundColor(ContextCompat.getColor(context, android.R.color.white)) // Fehér háttér
                            setBackgroundResource(R.drawable.bg_word) // Alternatív háttér beállítása, ha van drawable resource
                        }
                        elevation = 8f // Emelkedés beállítása
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
                                    minWidth = 50  // Explicit minWidth beállítása pixelben
                                    layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    )
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

                    // Adding answers below the code
                    answersLayout = LinearLayout(context).apply {
                        orientation = LinearLayout.VERTICAL
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(0, 100, 0, 0) // Növelje a marginTop értékét, hogy több helyet hagyjon
                        }
                    }

                    val answers = it.answers.split("\n")
                    val maxWidth = resources.displayMetrics.widthPixels // A képernyő szélessége
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
                                setMargins(10, 20, 10, 20) // Margins beállítása az egyes válaszok között
                            }
                            setBackgroundResource(R.drawable.bg_word) // Háttér beállítása
                            elevation = 4f // Emelkedés beállítása
                            setPadding(12, 12, 12, 12) // Padding beállítása
                            setTextColor(resources.getColor(android.R.color.black)) // Szöveg színének beállítása
                            gravity = Gravity.CENTER // Szöveg középre igazítása
                            minWidth = 100 // Minimum szélesség beállítása
                            maxLines = 3 // Maximum sorok száma
                            setOnClickListener {
                                handleAnswerClick(this, answer)
                            }
                        }

                        // Mérjük meg a válasz szélességét
                        answerEditText.measure(
                            View.MeasureSpec.UNSPECIFIED,
                            View.MeasureSpec.UNSPECIFIED
                        )
                        val answerWidth = answerEditText.measuredWidth

                        // Ha a válasz hozzáadása után a sor szélessége meghaladná a maxWidth értéket, kezdjünk új sort
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

                    // Hozzáadjuk az utolsó sort az elrendezéshez
                    answersLayout.addView(currentRow)
                    mainLinearLayout.addView(answersLayout)
                } ?: showError("No code found for this algorithm")
            } else {
                showError("No codes found")
            }
        }

        // Görgetőpanel hozzáadása a fő elrendezéshez
        val scrollView = ScrollView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(ContextCompat.getColor(context, R.color.pale_blue)) // Beállítja a háttérszínt
            addView(mainLinearLayout)
        }
        return scrollView
    }



    private fun handleAnswerClick(answerEditText: EditText, answer: String) {
        val firstEmptyEditText = findFirstEmptyEditText(view as ViewGroup)
        firstEmptyEditText?.let { editText ->
            val index = findEditTextIndex(view as ViewGroup, editText)
            editText.setText(answer)
            (answerEditText.parent as? ViewGroup)?.removeView(answerEditText)

            if (index != null && index < correctAnswers.size && correctAnswers[index] == answer) {
                // Helyes válasz
                //Toast.makeText(context, "Correct answer", Toast.LENGTH_SHORT).show()
                answerEditText.textSize = 12f
            } else {
                // Helytelen válasz, késleltetés után visszaállítás
                lifecycleScope.launch {
                    delay(200) // 1 másodperces késleltetés
                    editText.setText("")
                    addAnswerBack(answerEditText)
                }
            }
        }
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

    private fun findEditTextIndex(root: ViewGroup, editText: EditText): Int? {
        var index = 0
        for (i in 0 until root.childCount) {
            val child = root.getChildAt(i)
            if (child is ViewGroup) {
                val foundIndex = findEditTextIndex(child, editText)
                if (foundIndex != null) {
                    return index + foundIndex
                }
                index += countEditTexts(child)
            } else if (child is EditText) {
                if (child == editText) {
                    return index
                }
                index++
            }
        }
        return null
    }

    private fun countEditTexts(root: ViewGroup): Int {
        var count = 0
        for (i in 0 until root.childCount) {
            val child = root.getChildAt(i)
            if (child is ViewGroup) {
                count += countEditTexts(child)
            } else if (child is EditText) {
                count++
            }
        }
        return count
    }

    private suspend fun getCodeFromRepository(): List<CodeModel> {
        return withContext(Dispatchers.IO) {
            codeRepository.getAllCode()
        }
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    // Extension function to convert dp to pixels
    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}
