package com.example.algorythmics.preferences

import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {
    private const val PREFERENCES_NAME = "my_preferences"
    private const val KEY_PREFIX_SHOW_WELCOME_DIALOG = "show_welcome_dialog_"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun shouldShowWelcomeDialog(context: Context, algorithmId: String): Boolean {
        return getPreferences(context).getBoolean(KEY_PREFIX_SHOW_WELCOME_DIALOG + algorithmId, true)
    }

    fun setWelcomeDialogShown(context: Context, algorithmId: String) {
        getPreferences(context).edit().putBoolean(KEY_PREFIX_SHOW_WELCOME_DIALOG + algorithmId, false).apply()
    }
}