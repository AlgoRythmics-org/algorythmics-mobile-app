import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.algorythmics.R
import com.example.algorythmics.presentation.ListUiItem

class SortedListAdapter : ListAdapter<ListUiItem, SortedListAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sorted_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.textView.text = item.value.toString()
        holder.itemView.setBackgroundColor(if (item.isCurrentlyCompared) Color.YELLOW else Color.TRANSPARENT)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ListUiItem>() {
            override fun areItemsTheSame(oldItem: ListUiItem, newItem: ListUiItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListUiItem, newItem: ListUiItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
