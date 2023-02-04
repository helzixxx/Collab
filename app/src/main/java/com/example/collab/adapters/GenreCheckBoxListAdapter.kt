package com.example.collab.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.R
import com.example.collab.models.Genre
import com.example.collab.models.Instrument

class GenreCheckBoxListAdapter constructor(
    context: Context, entries: ArrayList<Genre?>, onItemClickListener: OnItemClickListener<Genre?>
) : RecyclerView.Adapter<GenreCheckBoxListAdapter.ListViewHolder>() {

    var context: Context? = null
    private var entries: ArrayList<Genre?> = java.util.ArrayList()
    var onItemClickListener: OnItemClickListener<Genre?>

    init {
        this.context = context
        this.entries = entries
        this.onItemClickListener = onItemClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.single_genre_row, parent, false)

        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val genre: Genre? = entries[position]
        holder.genreName.text = genre!!.name
        genre.isChecked = false

        holder.container.setOnClickListener {
            onItemClickListener.onItemClick(genre)
            holder.checkBox.isChecked = genre.isChecked
        }
        holder.checkBox.setOnClickListener {
            onItemClickListener.onItemClick(genre)
            holder.checkBox.isChecked = genre.isChecked
        }
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var genreName: TextView
         var checkBox: CheckBox
         var container: CardView

        init {
              genreName = itemView.findViewById(R.id.genreName)
              checkBox = itemView.findViewById(R.id.checkBox)
              container = itemView.findViewById(R.id.container)
        }
    }
}