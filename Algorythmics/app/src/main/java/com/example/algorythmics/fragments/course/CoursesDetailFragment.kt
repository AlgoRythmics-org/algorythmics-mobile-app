package com.example.algorythmics.fragments.course
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.example.algorythmics.R
import com.example.algorythmics.retrofit.repositories.AlgorithmRepository
import kotlinx.coroutines.launch

class CoursesDetailFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_courses_detail, container, false)

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Hello!")
        alertDialogBuilder.setMessage("Welcome to the marvelous world of algorithms! Would you like a detailed description about the algorithm?")
        alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->
            dialog.dismiss()

            val algorithmId = arguments?.getString("algorithmId")
            if (algorithmId != null) {
                val algorithmRepository = AlgorithmRepository()
                lifecycleScope.launch {
                    val algorithm = algorithmRepository.getOneAlgorithmById(algorithmId)
                    if (algorithm != null && algorithm.detailDescription != null) {
                        val detailDescriptionDialogBuilder = AlertDialog.Builder(requireContext())
                        detailDescriptionDialogBuilder.setTitle("Detailed description")
                        detailDescriptionDialogBuilder.setMessage(algorithm.detailDescription)
                        detailDescriptionDialogBuilder.setPositiveButton("Finish") { dialog, which ->
                            dialog.dismiss()
                        }
                        val detailDescriptionDialog = detailDescriptionDialogBuilder.create()
                        detailDescriptionDialog.show()
                    } else {
                        Log.d("Detail Description", "Nincs részletes leírás.")
                    }
                }
            }
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()



        val videoCardView = view.findViewById<CardView>(R.id.video)
        val animationCardView = view.findViewById<CardView>(R.id.animation)
        val codeCardView = view.findViewById<CardView>(R.id.code)
        val quizCardView = view.findViewById<CardView>(R.id.quiz)

        videoCardView.setOnClickListener {
            //navigateToFragment(VideoFragment())
            val algorithmId = arguments?.getString("algorithmId")
            if (algorithmId != null) {
                val videoFragment = VideoFragment().apply {
                    arguments = Bundle().apply {
                        putString("algorithmId", algorithmId)
                    }
                }
                navigateToFragment(videoFragment)
            }
        }

        animationCardView.setOnClickListener {
            val algorithmId = arguments?.getString("algorithmId")
            if (algorithmId != null) {
                val animationFragment = AnimationFragment().apply {
                    arguments = Bundle().apply {
                        putString("algorithmId", algorithmId)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, animationFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }



        codeCardView.setOnClickListener {
            //navigateToFragment(CodeFragment())
            val algorithmId = arguments?.getString("algorithmId")
            if (algorithmId != null) {
                val codeFragment = CodeFragment().apply {
                    arguments = Bundle().apply {
                        putString("algorithmId", algorithmId)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, codeFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        quizCardView.setOnClickListener {
            val algorithmId = arguments?.getString("algorithmId")
            if (algorithmId != null) {
                val quizFragment = QuizFragment().apply {
                    arguments = Bundle().apply {
                        putString("algorithmId", algorithmId)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, quizFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        return view
    }

    private fun navigateToFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentContainer, fragment, fragment.javaClass.simpleName)
        transaction.hide(this@CoursesDetailFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }



}