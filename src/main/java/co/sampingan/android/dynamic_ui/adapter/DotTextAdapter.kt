package co.sampingan.android.dynamic_ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sampingan.agentapp.dynamic_ui.R
import kotlinx.android.synthetic.main.item_dot_text.view.*

/**
 * Created by ilgaputra15
 * on Friday, 20/03/2020 23.24
 * Mobile Engineer - https://github.com/ilgaputra15
 **/
class DotTextAdapter(private val data: List<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PartViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_dot_text,
                parent,
                false
            ))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PartViewHolder).bind(data[position])
    }

    class PartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(part: String) {
            itemView.textValue.text = part
        }
    }
}