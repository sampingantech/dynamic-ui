package co.sampingan.android.dynamic_ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.sampingan.android.dynamic_ui.adapter.TaskSubmissionAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_map)

        TaskSubmissionAdapter
    }
}