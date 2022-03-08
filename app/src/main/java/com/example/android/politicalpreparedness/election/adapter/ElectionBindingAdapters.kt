package com.example.android.politicalpreparedness.election.adapter

import android.text.format.DateUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.election.CivicApiStatus
import com.example.android.politicalpreparedness.network.models.Election
import java.util.*

@BindingAdapter("textDate")
fun dateFormat(textView: TextView, value: Date?) {
    var localizedDate = ""
    if (value != null) {
        localizedDate = DateUtils.formatDateTime(
            textView.context,
            value.time,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR
        )
    }

    textView.text = localizedDate
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Election>?) {
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)
}

@BindingAdapter("civicApiStatus")
fun bindStatus(statusImageView: ImageView, status: CivicApiStatus?) {
    when (status) {
        CivicApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        CivicApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        else -> {
            statusImageView.visibility = View.GONE
        }
    }
}