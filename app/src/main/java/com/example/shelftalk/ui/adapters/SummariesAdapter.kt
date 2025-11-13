package com.example.shelftalk.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shelftalk.databinding.ItemSummaryBinding
import com.example.shelftalk.models.Summary
import java.text.SimpleDateFormat
import java.util.*

class SummariesAdapter : ListAdapter<Summary, SummariesAdapter.SummaryViewHolder>(SummaryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        val binding = ItemSummaryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SummaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SummaryViewHolder(private val binding: ItemSummaryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(summary: Summary) {
            binding.tvBookTitle.text = summary.bookTitle
            binding.tvSummary.text = summary.summaryText
            binding.tvLovedPart.text = "❤️ ${summary.lovedPart}"
            binding.tvUserEmail.text = summary.userEmail
            binding.tvTimestamp.text = formatTimestamp(summary.timestamp)
        }

        private fun formatTimestamp(timestamp: Long): String {
            val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
    }

    class SummaryDiffCallback : DiffUtil.ItemCallback<Summary>() {
        override fun areItemsTheSame(oldItem: Summary, newItem: Summary): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Summary, newItem: Summary): Boolean {
            return oldItem == newItem
        }
    }
}
