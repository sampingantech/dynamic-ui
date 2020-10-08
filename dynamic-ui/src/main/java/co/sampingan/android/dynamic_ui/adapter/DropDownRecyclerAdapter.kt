package co.sampingan.android.dynamic_ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import co.sampingan.android.dynamic_ui.R
import co.sampingan.android.dynamic_ui.extension.visible
import co.sampingan.android.dynamic_ui.model.DropDownValue
import kotlinx.android.synthetic.main.item_filter.view.*

/**
 * Created by ilgaputra15
 * on Wednesday, 13/05/2020 23.38
 * Mobile Engineer - https://github.com/ilgaputra15
 **/
class DropDownRecyclerAdapter(
    private val data: List<DropDownValue>,
    private val clickListener: (DropDownValue?) -> Unit,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataShow = data

    fun showDataFilter(value: List<DropDownValue>) {
        dataShow = value
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return dataShow[position].value.hashCode().toLong()
    }

    override fun getItemCount(): Int {
        return dataShow.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = PartViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_filter,
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val selected = dataShow[position]
            if (selected.isSelected) selected.isSelected = false
            else {
                data.forEach { it.isSelected = false }
                data.find { it == selected }?.isSelected = true
            }
            notifyDataSetChanged()
            clickListener(selected)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PartViewHolder).bind(dataShow[position])
    }

    class PartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(part: DropDownValue) {
            itemView.textName.text = part.label
            setView(part.isSelected)
        }

        private fun setView(isSelected: Boolean) {
            val linearColor = if (isSelected) R.color.very_light_pink else R.color.white
            val textColor = if (isSelected) R.color.colorLightGold else R.color.gun_metal
            itemView.imageCheck.visible = isSelected
            itemView.linearItem.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    linearColor
                )
            )
            itemView.textName.setTextColor(ContextCompat.getColor(itemView.context, textColor))
        }
    }
}