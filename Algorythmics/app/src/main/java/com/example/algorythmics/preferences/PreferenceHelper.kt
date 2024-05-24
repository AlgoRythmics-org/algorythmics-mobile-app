package com.example.algorythmics.preferences

import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {
    private const val PREFS_NAME = "algorithm_prefs"
    private const val KEY_LAST_ALGORITHM_ID = "last_algorithm_id"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getLastAlgorithmId(context: Context): String? {
        return getPreferences(context).getString(KEY_LAST_ALGORITHM_ID, null)
    }

    fun setLastAlgorithmId(context: Context, algorithmId: String) {
        getPreferences(context).edit().putString(KEY_LAST_ALGORITHM_ID, algorithmId).apply()
    }

    fun clearLastAlgorithmId(context: Context) {
        getPreferences(context).edit().remove(KEY_LAST_ALGORITHM_ID).apply()
    }
}