
package com.example.algorythmics.fragments.course

import android.os.Bundle
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
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.algorythmics.R
import com.example.algorythmics.animation.BubbleSort
import com.example.algorythmics.presentation.BinarySearchViewModel
import com.example.algorythmics.presentation.BubbleSortViewModel
import com.example.algorythmics.presentation.HeapSortViewModel
import com.example.algorythmics.presentation.InsertionSortViewModel
import com.example.algorythmics.presentation.ItemDecoration
import com.example.algorythmics.presentation.LinearSearchViewModel
import com.example.algorythmics.presentation.MergeSortViewModel
import com.example.algorythmics.presentation.QuickSortViewModel
import com.example.algorythmics.presentation.SelectionSortViewModel
import com.example.algorythmics.presentation.ShellSortViewModel





class AnimationFragment : Fragment() {

    private val bubbleSortViewModel: BubbleSortViewModel by activityViewModels()
    private val insertionSortViewModel: InsertionSortViewModel by activityViewModels()
    private val selectionSortViewModel: SelectionSortViewModel by activityViewModels()
    private val shellSortViewModel: ShellSortViewModel by activityViewModels()
    private val quickSortViewModel: QuickSortViewModel by activityViewModels()
    private val linearSearchViewModel: LinearSearchViewModel by activityViewModels()
    private val binarySearchViewModel: BinarySearchViewModel by activityViewModels()
    private val mergeSortViewModel: MergeSortViewModel by activityViewModels()
    private val heapSortViewModel: HeapSortViewModel by activityViewModels()
    private lateinit var btnShuffle: Button
    private lateinit var btnStep: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var sortedListAdapter: SortedListAdapter
    private lateinit var selectionSortListAdapter: SelectionSortListAdapter
    private lateinit var mergeSortAdapter: MergeSortAdapter
    private lateinit var binarySearchAdapter: BinarySearchAdapter
    private lateinit var algorithmId: String
    private lateinit var etSearchNumber: EditText
    private lateinit var tvAnimationSteps: TextView
    private lateinit var backBtn3: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = requireContext()

        // Fő layout létrehozása
        val mainLayout = LinearLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            orientation = LinearLayout.VERTICAL
            setPadding(0, 50, 0, 0)
        }

        // Vissza gomb hozzáadása
        backBtn3 = ImageView(context).apply {
            setImageResource(R.drawable.back2)
            layoutParams = FrameLayout.LayoutParams(
                40.dpToPx(),
                40.dpToPx()
            ).apply {
                gravity = Gravity.START or Gravity.TOP
                setMargins(16.dpToPx(), 16.dpToPx(), 0, 0)
            }
            setPadding(8.dpToPx(), 8.dpToPx(), 8.dpToPx(), 8.dpToPx())
            setOnClickListener { requireActivity().onBackPressed() }
        }
        mainLayout.addView(backBtn3)

        // RecyclerView létrehozása és hozzáadása
        recyclerView = RecyclerView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            ).apply {
                setMargins(8, 30, 8, 16)
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(ItemDecoration())
        }
        mainLayout.addView(recyclerView)

        // ScrollView létrehozása a lépésekhez
        val scrollView = ScrollView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        }

        tvAnimationSteps = TextView(context)
        scrollView.addView(tvAnimationSteps)
        mainLayout.addView(scrollView)

        // EditText létrehozása és hozzáadása
        etSearchNumber = EditText(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            hint = "Enter number to search"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
            visibility = View.GONE // Alapértelmezés szerint rejtve
        }
        mainLayout.addView(etSearchNumber)

        // Gombok container létrehozása
        val buttonLayout = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }

        // Shuffle gomb létrehozása és hozzáadása
        btnShuffle = Button(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                setMargins(16, 40, 8, 50) // Felfelé mozgatás
            }
            text = "Shuffle"
            textSize = 18f
            background = ContextCompat.getDrawable(context, R.drawable.button_rounded_corner)
            setOnClickListener {
                handleShuffleButtonClick()
            }
        }
        buttonLayout.addView(btnShuffle)

        // Step gomb létrehozása és hozzáadása
        btnStep = Button(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                setMargins(8, 40, 16, 50) // Felfelé mozgatás
            }
            text = "Step"
            textSize = 18f
            background = ContextCompat.getDrawable(context, R.drawable.button_rounded_corner)
            setOnClickListener {
                handleStepButtonClick()
            }
        }
        buttonLayout.addView(btnStep)

        // Start gomb létrehozása és hozzáadása
        val btnStart = Button(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                setMargins(8, 40, 8, 50) // Felfelé mozgatás
            }
            text = "Start"
            textSize = 18f
            background = ContextCompat.getDrawable(context, R.drawable.button_rounded_corner)
            setOnClickListener {
                handleStartButtonClick()
            }
        }
        buttonLayout.addView(btnStart)

        mainLayout.addView(buttonLayout)

        return mainLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        algorithmId = arguments?.getString("algorithmId") ?: ""

        sortedListAdapter = SortedListAdapter()
        selectionSortListAdapter = SelectionSortListAdapter()
        mergeSortAdapter = MergeSortAdapter()
        binarySearchAdapter = BinarySearchAdapter()
        recyclerView.adapter = when (algorithmId) {
            "653d35f6ce1b18cbd8bd14b3" -> selectionSortListAdapter
            "653d3dfece1b18cbd8bd14b9" -> binarySearchAdapter
            "653d36ecce1b18cbd8bd14b4" -> mergeSortAdapter
            else -> sortedListAdapter
        }

        if (algorithmId == "653d3cf5ce1b18cbd8bd14b8" || algorithmId == "653d3dfece1b18cbd8bd14b9") {
            etSearchNumber.visibility = View.VISIBLE
        } else {
            etSearchNumber.visibility = View.GONE
        }

        // Reset the step button state
        linearSearchViewModel.resetStepButton()

        observeViewModel()
    }

    private fun observeViewModel() {
        bubbleSortViewModel.listToSort.observe(viewLifecycleOwner, Observer { list ->
            sortedListAdapter.submitList(list)
        })

        bubbleSortViewModel.comparisonMessage.observe(viewLifecycleOwner, Observer { message ->
            tvAnimationSteps.text = tvAnimationSteps.text.toString() + "\n" + message
        })

        insertionSortViewModel.listToSort.observe(viewLifecycleOwner, Observer {
            sortedListAdapter.submitList(it)
        })

        selectionSortViewModel.listToSort.observe(viewLifecycleOwner, Observer {
            selectionSortListAdapter.submitList(it)
        })

        shellSortViewModel.listToSort.observe(viewLifecycleOwner, Observer {
            sortedListAdapter.submitList(it)
        })

        quickSortViewModel.listToSort.observe(viewLifecycleOwner, Observer {
            sortedListAdapter.submitList(it)
        })

        mergeSortViewModel.listToSort.observe(viewLifecycleOwner, Observer {
            mergeSortAdapter.submitList(it)
        })

        heapSortViewModel.listToSort.observe(viewLifecycleOwner, Observer {
            sortedListAdapter.submitList(it)
        })

        linearSearchViewModel.listToSearch.observe(viewLifecycleOwner, Observer {
            sortedListAdapter.submitList(it)
        })

        binarySearchViewModel.listToSearch.observe(viewLifecycleOwner, Observer {
            binarySearchAdapter.submitList(it)
        })

        linearSearchViewModel.searchResult.observe(viewLifecycleOwner, Observer { result ->
            result?.let {
                val newList = sortedListAdapter.currentList.mapIndexed { index, item ->
                    item.copy(isCurrentlyCompared = index == result)
                }
                sortedListAdapter.submitList(newList)
            }
        })

        binarySearchViewModel.searchResult.observe(viewLifecycleOwner, Observer { result ->
            result?.let {
                val newList = binarySearchAdapter.currentList.mapIndexed { index, item ->
                    item.copy(isCurrentlyCompared = index == result)
                }
                binarySearchAdapter.submitList(newList)
            }
        })

        // Observe isStepButtonEnabled LiveData to update button state
        linearSearchViewModel.isStepButtonEnabled.observe(viewLifecycleOwner, Observer { isEnabled ->
            btnStep.isEnabled = isEnabled
        })
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    private fun handleStartButtonClick() {
        when (algorithmId) {
            "65b8db1995d5f3a10bccd361" -> bubbleSortViewModel.startSorting()
            "653d3cf5ce1b18cbd8bd14b8" -> {
                val searchNumber = etSearchNumber.text.toString().toIntOrNull() ?: return
                linearSearchViewModel.startLinearSearch(searchNumber)
            }
        }
    }

    private fun handleShuffleButtonClick() {
        when (algorithmId) {

            "65b8db1995d5f3a10bccd361", // Bubble Sort
            "653d3cf5ce1b18cbd8bd14b8" -> { // Linear Search
                linearSearchViewModel.shuffleList()
            }
        }
    }

    private fun handleStepButtonClick() {
        val searchNumber = etSearchNumber.text.toString().toIntOrNull() ?: return
        linearSearchViewModel.stepLinearSearch(searchNumber)
    }
}

