package com.example.algorythmics.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.algorythmics.R
import com.example.algorythmics.fragments.course.CoursesFragment


class CoursesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courses)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CoursesFragment())
                .commit()
        }
    }
}