package com.example.android.politicalpreparedness.election.adapter

//import com.example.android.politicalpreparedness.election.CivicApiStatus
import android.text.format.DateUtils
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Election
import java.util.*




// Refresh adapters when fragment loads
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Election>?) {
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)
}

@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.GONE else View.VISIBLE
}

// Hide views without provided data.
@BindingAdapter("hideIfNull")
fun hideIfNull(view: TextView, it: Any?) {
    view.visibility = if (it !=null) View.VISIBLE else View.GONE
}

@BindingAdapter("addressFormat")
fun addressFormat(view: TextView, address: Address?) {
    hideIfNull(view, address)

    val strAddress = StringBuilder()
    if (!address?.line1.isNullOrBlank()) strAddress.append("${address?.line1}\n")
    if (!address?.line2.isNullOrBlank()) strAddress.append("${address?.line2}\n")
    if (!address?.city.isNullOrBlank()) strAddress.append("${address?.city}\n")
    if (!address?.state.isNullOrBlank()) strAddress.append("${address?.state}\n")
    if (!address?.zip.isNullOrBlank()) strAddress.append("${address?.zip}")
    view.text = strAddress
}

@BindingAdapter("dateFormat")
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

//@BindingAdapter("civicApiStatus")
//fun bindStatus(statusImageView: ImageView, status: CivicApiStatus?) {
//    when (status) {
//        CivicApiStatus.LOADING -> {
//            statusImageView.visibility = View.VISIBLE
//            statusImageView.setImageResource(R.drawable.loading_animation)
//        }
//        CivicApiStatus.ERROR -> {
//            statusImageView.visibility = View.VISIBLE
//            statusImageView.setImageResource(R.drawable.ic_connection_error)
//        }
//        else -> {
//            statusImageView.visibility = View.GONE
//        }
//    }
//}