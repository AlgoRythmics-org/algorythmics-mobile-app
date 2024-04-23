package com.example.algorythmics.retrofit.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class QuizModel(
    val id: Int,
    val question: String?,
    val answer1: String?,
    val answer2: String?,
    val answer3: String?,
    val answer4: String?,
    val correctAnswer: String?,
    val score: Double,
    var clickedAnswer: String?,
    @SerializedName("algorithm_id")
    val algorithmId: String?
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(question)
        parcel.writeString(answer1)
        parcel.writeString(answer2)
        parcel.writeString(answer3)
        parcel.writeString(answer4)
        parcel.writeString(correctAnswer)
        parcel.writeInt(score.toInt())
        parcel.writeString(clickedAnswer)
        parcel.writeString(algorithmId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QuizModel> {
        override fun createFromParcel(parcel: Parcel): QuizModel {
            return QuizModel(parcel)
        }

        override fun newArray(size: Int): Array<QuizModel?> {
            return arrayOfNulls(size)
        }
    }

}
