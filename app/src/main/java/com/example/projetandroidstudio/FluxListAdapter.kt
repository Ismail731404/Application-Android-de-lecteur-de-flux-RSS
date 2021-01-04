package com.example.projetandroidstudio

import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class FluxListAdapter(val listener: OnItemClickListener) : ListAdapter<Flux, FluxListAdapter.FluxViewHolder>(FluxComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FluxViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        return FluxViewHolder(view)
    }

    override fun onBindViewHolder(holder: FluxViewHolder, position: Int) {
        val current = getItem(position)
        holder.AddSource(current.Source)
        holder.AddTag(current.TAG)
        holder.AddUrl(current.URL)
        holder.Addcoche(current.coche)
    }

    inner class FluxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener{
        val source = itemView.findViewById<TextView>(R.id.Source)
        val Tag= itemView.findViewById<TextView>(R.id.Tag)
        val Url= itemView.findViewById<TextView>(R.id.Url)
        val casse=itemView.findViewById<CheckBox>(R.id.checkrecy)

        fun AddSource(text: String?) { source.text = text }
        fun AddTag(text: String?) { Tag.text = text }
        fun AddUrl(text: String?) { Url.text = text }
        fun Addcoche(boolean: Boolean){

                casse.setChecked(boolean)

        }
        init {
            casse.setOnClickListener(this)
        }



        override fun onClick(v: View?) {
            val position:Int = adapterPosition

            if(position != RecyclerView.NO_POSITION)
            {
                if(casse.isChecked())
                    listener.onItemClick(position,source.text.toString(),true)
                else
                listener.onItemClick(position,source.text.toString(),false)
            }

        }
    }

    class FluxComparator : DiffUtil.ItemCallback<Flux>() {
        override fun areItemsTheSame(oldItem: Flux, newItem: Flux): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Flux, newItem: Flux): Boolean {
            return oldItem.Source == newItem.Source
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int,Source:String,coche:Boolean)
    }
}