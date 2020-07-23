package com.view.anim

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text_add.setOnClickListener {
            illusion_view.numberOfLines++
            text_no_of_dots.text = "Number of Dots : ${illusion_view.numberOfLines}"
        }
        text_remove.setOnClickListener {
            illusion_view.numberOfLines--
            text_no_of_dots.text = "Number of Dots : ${illusion_view.numberOfLines}"
        }
        checkbox_trace_path.setOnCheckedChangeListener { buttonView, isChecked ->
            illusion_view.tracePath = isChecked
        }
        checkbox_inner_circle.setOnCheckedChangeListener { buttonView, isChecked ->
            illusion_view.innerCircleVisible = isChecked
        }
    }
}