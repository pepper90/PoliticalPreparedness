package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.*
import com.squareup.moshi.*
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "election_table")
data class Election(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "name")val name: String,
        @ColumnInfo(name = "electionDay")val electionDay: Date,
        @Embedded(prefix = "division_") @Json(name="ocdDivisionId") val division: Division,
        @ColumnInfo(name = "isFollowed") var isFollowed: Boolean?
)     : Parcelable,
        BaseObservable() {
        @Ignore
        @Bindable
        fun getStatusFollowed(): Boolean? {
                return isFollowed
        }

        @Ignore
        fun setStatusFollowed(value: Boolean) {
                if (isFollowed != value)
                        isFollowed = value
                notifyPropertyChanged(BR.statusFollowed)
        }
}