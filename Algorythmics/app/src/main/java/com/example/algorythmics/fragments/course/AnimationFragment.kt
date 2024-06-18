
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.algorythmics.R
import com.example.algorythmics.adapters.BinarySearchAdapter
import com.example.algorythmics.adapters.MergeSortAdapter
import com.example.algorythmics.adapters.SelectionSortListAdapter
import com.example.algorythmics.adapters.SortedListAdapter
import com.example.algorythmics.presentation.BinarySearchViewModel
import com.example.algorythmics.presentation.BubbleSortViewModel
import com.example.algorythmics.presentation.HeapSortViewModel
import com.example.algorythmics.presentation.InsertionSortViewModel
import com.example.algorythmics.presentation.ItemDecorator
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
            setBackgroundColor(ContextCompat.getColor(context, R.color.pale_blue))
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
            setOnClickListener {
                requireActivity().onBackPressed()
                bubbleSortViewModel.cancelSorting()
            }
        }
        mainLayout.addView(backBtn3)

        recyclerView = RecyclerView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            ).apply {
                setMargins(8, 30, 8, 16) // Eredeti margók
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(ItemDecorator(20, 8)) // Itt állíthatjuk az eltolást
            setBackgroundColor(ContextCompat.getColor(context, R.color.white))
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
                setMargins(16, 40, 8, 120) // Felfelé mozgatás
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
                setMargins(8, 40, 16, 120) // Felfelé mozgatás
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
                setMargins(8, 40, 8, 120) // Felfelé mozgatás
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
        // Clear all previous observers
        bubbleSortViewModel.listToSort.removeObservers(viewLifecycleOwner)
        bubbleSortViewModel.comparisonMessage.removeObservers(viewLifecycleOwner)
        insertionSortViewModel.sortedList.removeObservers(viewLifecycleOwner)
        selectionSortViewModel.listToSort.removeObservers(viewLifecycleOwner)
        shellSortViewModel.listToSort.removeObservers(viewLifecycleOwner)
        quickSortViewModel.listToSort.removeObservers(viewLifecycleOwner)
        mergeSortViewModel.listToSort.removeObservers(viewLifecycleOwner)
        heapSortViewModel.listToSort.removeObservers(viewLifecycleOwner)
        linearSearchViewModel.listToSearch.removeObservers(viewLifecycleOwner)
        binarySearchViewModel.listToSearch.removeObservers(viewLifecycleOwner)
        linearSearchViewModel.searchResult.removeObservers(viewLifecycleOwner)
        binarySearchViewModel.searchResult.removeObservers(viewLifecycleOwner)
        linearSearchViewModel.isStepButtonEnabled.removeObservers(viewLifecycleOwner)

        when (algorithmId) {
            "653d32ffce1b18cbd8bd14b2" -> {
                insertionSortViewModel.sortedList.observe(viewLifecycleOwner, Observer {
                    sortedListAdapter.submitList(it)
                })
                insertionSortViewModel.comparisonMessage.observe(
                    viewLifecycleOwner,
                    Observer { message ->
                        tvAnimationSteps.text = tvAnimationSteps.text.toString() + "\n" + message
                    })
            }

            "65b8db1995d5f3a10bccd361" -> {
                bubbleSortViewModel.listToSort.observe(viewLifecycleOwner, Observer { list ->
                    sortedListAdapter.submitList(list)
                })
                bubbleSortViewModel.comparisonMessage.observe(
                    viewLifecycleOwner,
                    Observer { message ->
                        tvAnimationSteps.text = tvAnimationSteps.text.toString() + "\n" + message
                    })
            }

            "653d35f6ce1b18cbd8bd14b3" -> {
                selectionSortViewModel.listToSort.observe(viewLifecycleOwner, Observer {
                    selectionSortListAdapter.submitList(it)
                })
                selectionSortViewModel.comparisonMessage.observe(
                    viewLifecycleOwner,
                    Observer { message ->
                        tvAnimationSteps.text = tvAnimationSteps.text.toString() + "\n" + message
                    })
            }

            "653d3c49ce1b18cbd8bd14b7" -> {
                shellSortViewModel.listToSort.observe(viewLifecycleOwner, Observer {
                    sortedListAdapter.submitList(it)
                })
            }

            "653d3aa2ce1b18cbd8bd14b5" -> {
                quickSortViewModel.listToSort.observe(viewLifecycleOwner, Observer {
                    sortedListAdapter.submitList(it)
                })

                quickSortViewModel.comparisonMessage.observe(
                    viewLifecycleOwner,
                    Observer { message ->
                        tvAnimationSteps.text = tvAnimationSteps.text.toString() + "\n" + message
                    })
            }

            "653d36ecce1b18cbd8bd14b4" -> {
                mergeSortViewModel.listToSort.observe(viewLifecycleOwner, Observer {
                    mergeSortAdapter.submitList(it)
                })
            }

            "653d3cf5ce1b18cbd8bd14b8" -> {
                linearSearchViewModel.listToSearch.observe(viewLifecycleOwner, Observer {
                    sortedListAdapter.submitList(it)
                })
                linearSearchViewModel.searchResult.observe(viewLifecycleOwner, Observer { result ->
                    result?.let {
                        val newList = sortedListAdapter.currentList.mapIndexed { index, item ->
                            item.copy(isCurrentlyCompared = index == result)
                        }
                        sortedListAdapter.submitList(newList)
                    }
                })
                linearSearchViewModel.isStepButtonEnabled.observe(
                    viewLifecycleOwner,
                    Observer { isEnabled ->
                        btnStep.isEnabled = isEnabled
                    })
                linearSearchViewModel.comparisonMessage.observe(
                    viewLifecycleOwner,
                    Observer { message ->
                        tvAnimationSteps.text = tvAnimationSteps.text.toString() + "\n" + message
                    })
            }

            "653d3dfece1b18cbd8bd14b9" -> {
                binarySearchViewModel.listToSearch.observe(viewLifecycleOwner, Observer {
                    binarySearchAdapter.submitList(it)
                })
                binarySearchViewModel.searchResult.observe(viewLifecycleOwner, Observer { result ->
                    result?.let {
                        val newList = binarySearchAdapter.currentList.mapIndexed { index, item ->
                            item.copy(isCurrentlyCompared = index == result)
                        }
                        binarySearchAdapter.submitList(newList)
                    }
                })
                binarySearchViewModel.comparisonMessage.observe(
                    viewLifecycleOwner,
                    Observer { message ->
                        tvAnimationSteps.text = tvAnimationSteps.text.toString() + "\n" + message
                    })
            }

            else -> {
                heapSortViewModel.listToSort.observe(viewLifecycleOwner, Observer {
                    sortedListAdapter.submitList(it)
                })
            }
        }
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    private fun handleStartButtonClick() {
        when (algorithmId) {
            "653d32ffce1b18cbd8bd14b2" -> insertionSortViewModel.startInsertionSorting()
            "65b8db1995d5f3a10bccd361" -> bubbleSortViewModel.startSorting()
            "653d35f6ce1b18cbd8bd14b3" -> selectionSortViewModel.startSelectionSorting()
            "653d3aa2ce1b18cbd8bd14b5" -> quickSortViewModel.startQuickSorting()
            "653d3c49ce1b18cbd8bd14b7" -> shellSortViewModel.startShellSorting()
            "653d3cf5ce1b18cbd8bd14b8" -> {
                val searchNumber = etSearchNumber.text.toString().toIntOrNull() ?: return
                linearSearchViewModel.startLinearSearch(searchNumber)
            }

            "653d3dfece1b18cbd8bd14b9" -> {
                val searchNumber = etSearchNumber.text.toString().toIntOrNull() ?: return
                binarySearchViewModel.startBinarySearch(searchNumber)
            }
        }
    }

    private fun handleShuffleButtonClick() {
        when (algorithmId) {
            "653d32ffce1b18cbd8bd14b2" -> insertionSortViewModel.shuffleList() //Insertion sort
            "653d3cf5ce1b18cbd8bd14b8" -> linearSearchViewModel.shuffleList() //Linear search
            "653d35f6ce1b18cbd8bd14b3" -> selectionSortViewModel.shuffleList() //Selection sort
            "65b8db1995d5f3a10bccd361" -> bubbleSortViewModel.shuffleList() //Bubble sort
            "653d3aa2ce1b18cbd8bd14b5" -> quickSortViewModel.shuffleList() //Quick sort
        }
    }

    private fun handleStepButtonClick() {
        //  val searchNumber = etSearchNumber.text.toString().toIntOrNull() ?: return
        when (algorithmId) {
            "653d32ffce1b18cbd8bd14b2" -> insertionSortViewModel.stepInsertionSort() //Insertion sort
            "653d35f6ce1b18cbd8bd14b3" -> selectionSortViewModel.stepSelectionSorting() //Selection sort
            //  "653d3cf5ce1b18cbd8bd14b8" -> linearSearchViewModel.stepLinearSearch(searchNumber) //Linear search
            "65b8db1995d5f3a10bccd361" -> bubbleSortViewModel.stepSorting() //Bubble sort
            "653d3aa2ce1b18cbd8bd14b5" -> quickSortViewModel.stepQuickSorting() //Quick sort
            //   "653d3dfece1b18cbd8bd14b9" -> binarySearchViewModel.stepBinarySearch(searchNumber)
        }
    }

}




