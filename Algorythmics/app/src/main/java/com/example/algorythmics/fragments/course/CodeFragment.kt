package com.example.algorythmics.fragments.course

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
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
        val linearLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = android.view.Gravity.CENTER_VERTICAL // Vertikális középre igazítás
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }

        lifecycleScope.launch {
            val codeList = getCodeFromRepository()
            if (codeList.isNotEmpty()) {
                val algorithmId = arguments?.getString("algorithmId")
                val code = codeList.find { it.algorithmId == algorithmId }
                code?.let {
                    val textView = TextView(context).apply {
                        text = it.algorithmCode // Állítsd be a TextView szövegét az adatbázisból kapott szövegre
                        textSize = 18f
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            gravity = android.view.Gravity.CENTER // Középre igazítás a szülőben
                            setMargins(0, 50, 0, 0) // Példa a margók hozzáadására a TextView-hoz
                        }
                    }
                    linearLayout.addView(textView)
                } ?: showError("No code found for this algorithm")
            } else {
                showError("No codes found")
            }
        }

        return linearLayout
    }

    private suspend fun getCodeFromRepository(): List<CodeModel> {
        return withContext(Dispatchers.IO) {
            codeRepository.getAllCode()
        }
    }

    private fun showError(message: String) {
        // Hibakezelő metódus
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
