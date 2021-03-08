package com.ajax.ajaxtestassignment.ui.start

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ajax.ajaxtestassignment.R
import com.ajax.ajaxtestassignment.data.model.Contact
import com.ajax.ajaxtestassignment.databinding.ItemListContentBinding
import com.bumptech.glide.Glide

class ContactAdapter(
    private val itemCLickListener: ItemCLickListener
) : ListAdapter<Contact, ContactAdapter.ViewHolder>(diffCallback) {

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Contact>() {
            override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface ItemCLickListener {
        fun onItemClick(contact: Contact)
        fun onItemRemove(id: Long)
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(binding: ItemListContentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val avatar: ImageView = binding.avatar
        private val name: TextView = binding.name
        private val email: TextView = binding.email
        private val delete: ImageView = binding.delete

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    itemCLickListener.onItemClick(getItem(adapterPosition))
                }
            }
            delete.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    itemCLickListener.onItemRemove(getItem(adapterPosition).id)
                }
            }
        }

        fun bind(item: Contact) {
            name.text = itemView.resources.getString(
                R.string.fmt_name, item.firstname, item.lastname
            )
            email.text = item.email

            Glide.with(itemView).load(item.pictureThumbnail).into(avatar)
        }
    }
}