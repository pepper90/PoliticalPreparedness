package com.example.android.politicalpreparedness.utils

import android.util.Log
import com.example.android.politicalpreparedness.network.models.Division

fun formatDivision(division: Division): String {
    val strBuilder = StringBuilder()

    strBuilder.append(division.country)
    if (division.state != "") {
        strBuilder.append(",")
        strBuilder.append(division.state)
    }

    Log.i("FormatDivision", strBuilder.toString())
    return strBuilder.toString()
}