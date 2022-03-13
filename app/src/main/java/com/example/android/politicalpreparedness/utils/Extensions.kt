package com.example.android.politicalpreparedness.utils

import android.util.Log
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Division

fun divisionFormat(division: Division): String {
    val strBuilder = StringBuilder()

    strBuilder.append(division.country)
    if (division.state != "") {
        strBuilder.append(",")
        strBuilder.append(division.state)
    }

    Log.i("FormatDivision", strBuilder.toString())
    return strBuilder.toString()
}

fun addressFormat(address: Address?): String {
    val strAddress = StringBuilder()
    strAddress.append("geo:0,0?q=")
    if (!address?.line1.isNullOrBlank()) strAddress.append("${address?.line1}+")
    if (!address?.city.isNullOrBlank()) strAddress.append("${address?.city}+")
    if (!address?.state.isNullOrBlank()) strAddress.append("${address?.state}+")
    if (!address?.zip.isNullOrBlank()) strAddress.append("${address?.zip}")
    Log.i("serviceVoterInfo", "--> ADDRESS: $strAddress")
    return strAddress.toString()
}