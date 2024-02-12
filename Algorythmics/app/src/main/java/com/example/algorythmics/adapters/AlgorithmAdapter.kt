package com.example.algorythmics.adapters

import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.algorythmics.R
import com.example.algorythmics.retrofit.models.AlgorithmModel
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class AlgorithmAdapter(
    var algorithms: List<AlgorithmModel>,
    private val onClickListener: (AlgorithmModel) -> Unit
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

        val executor = Executors.newSingleThreadExecutor()

        val handler = Handler(Looper.getMainLooper())

        executor.execute {

            val imageURL = algorithm.imageFile

            try {
                val url = URL(imageURL)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(input)

                handler.post {
                    holder.imageView.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }



        holder.itemView.setOnClickListener{
            onClickListener.invoke(algorithm)
        }
    }

    override fun getItemCount(): Int {
        return filteredAlgorithms.size
    }

    class AlgorithmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.algorithmTitle)
        val textView2: TextView = itemView.findViewById(R.id.algorithmDescription)
        val imageView: ImageView = itemView.findViewById(R.id.logoImageView)
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
        filteredAlgorithms.clear()
        filteredAlgorithms.addAll(algorithms)
        notifyDataSetChanged()
    }
}