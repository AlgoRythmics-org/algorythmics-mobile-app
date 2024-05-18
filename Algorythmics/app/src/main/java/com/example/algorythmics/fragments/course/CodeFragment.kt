package com.example.algorythmics.fragments.course

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.algorythmics.R
import com.example.algorythmics.retrofit.models.CodeModel
import com.example.algorythmics.retrofit.repositories.CodeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CodeFragment : Fragment() {

    private lateinit var codeRepository: CodeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        codeRepository = CodeRepository()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                    val codeText = it.algorithmCode
                    val lines = codeText.split("\n")
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

                        mainLinearLayout.addView(lineLayout)
                    }

                    // Adding answers below the code
                    val answersLayout = LinearLayout(context).apply {
                        orientation = LinearLayout.VERTICAL
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
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
                            isFocusable = false
                            isFocusableInTouchMode = false
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(0, 10, 10, 10) // Margins beállítása az egyes válaszok között
                            }
                            setBackgroundResource(R.drawable.bg_word) // Háttér beállítása
                            elevation = 4f // Emelkedés beállítása
                            setPadding(12, 12, 12, 12) // Padding beállítása
                            setTextColor(resources.getColor(android.R.color.black)) // Szöveg színének beállítása
                            gravity = Gravity.CENTER // Szöveg középre igazítása
                            minWidth = 100 // Minimum szélesség beállítása
                            maxLines = 3 // Maximum sorok száma
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
            addView(mainLinearLayout)
        }
        return scrollView
    }

    private suspend fun getCodeFromRepository(): List<CodeModel> {
        return withContext(Dispatchers.IO) {
            codeRepository.getAllCode()
        }
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
