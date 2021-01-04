package com.example.projetandroidstudio

import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView

class InfoListAdapter(val listener: OnItemClickListener) : ListAdapter<Info, InfoListAdapter.InfoViewHolder>(InfoComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_info, parent, false)
        return InfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        val current = getItem(position)
        holder.AddTitle(current.Title)
        holder.AddDescription(current.Description)
        holder.AddUrl(current.Link)
        holder.Nouveau(current.Nouveau)

    }

    inner class InfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener{
        val Title = itemView.findViewById<TextView>(R.id.Tag)
        val Description= itemView.findViewById<TextView>(R.id.Source)
        val Link= itemView.findViewById<TextView>(R.id.Url)




        fun AddTitle(text: String?) { Title.text = text }
        fun AddDescription(text: String?) { Description.text = text }
        fun AddUrl(text: String?) {
            Link.text = text
        }
        fun Nouveau(n:Boolean)
        {
            if(n)
                itemView.setBackgroundColor(Color.CYAN)
            else
                itemView.setBackgroundColor(Color.WHITE)
        }

        init {
            //Nouveau.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }



        override fun onClick(v: View?) {
            val position:Int = adapterPosition
                listener.onItemClick(position,Link.text.toString())
        }
    }

    class InfoComparator : DiffUtil.ItemCallback<Info>() {
        override fun areItemsTheSame(oldItem: Info, newItem: Info): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Info, newItem: Info): Boolean {
            return oldItem.Title == newItem.Title
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int,Source:String)
    }
}