
package com.example.algorythmics.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.algorythmics.R
import com.example.algorythmics.retrofit.models.AlgorithmModel

class AlgorithmAdapter(
    var algorithms: List<AlgorithmModel>
) : RecyclerView.Adapter<AlgorithmAdapter.AlgorithmViewHolder>() {

    private var filteredAlgorithms: MutableList<AlgorithmModel> = algorithms.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlgorithmViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.algorithm_item, parent, false)
        return AlgorithmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlgorithmViewHolder, position: Int) {
        val algorithm = filteredAlgorithms[position]
        holder.textView.text = algorithm.algorithmName
        holder.textView2.text = algorithm.description
    }

    override fun getItemCount(): Int {
        return filteredAlgorithms.size
    }

    class AlgorithmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.algorithmTitle)
        val textView2: TextView = itemView.findViewById(R.id.algorithmDescription)
    }

    fun filter(text: String) {
        filteredAlgorithms.clear()
        if (text.isBlank()) {
            filteredAlgorithms.addAll(algorithms)
        } else {
            filteredAlgorithms.addAll(algorithms.filter {
                it.algorithmName.contains(text, ignoreCase = true)
            })
        }
        notifyDataSetChanged()
    }



    fun updateData(newAlgorithms: List<AlgorithmModel>) {
        algorithms = newAlgorithms
        filter("")
    }
}
