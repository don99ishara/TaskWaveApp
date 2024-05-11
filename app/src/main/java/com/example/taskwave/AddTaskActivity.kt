package com.example.taskwave

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.taskwave.databinding.ActivityAddTaskBinding


class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var db: TasksDatabaseHelper

    private var selectedPriority: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TasksDatabaseHelper(this)

        val priorityOptions = resources.getStringArray(R.array.priority_options)
        val priorityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorityOptions)
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.prioritySpinner.adapter = priorityAdapter

        binding.prioritySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedPriority = priorityOptions[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedPriority = null
            }
        }

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEdittext.text.toString()
            val priority = selectedPriority ?: "Medium"
            val task = Task(0,title,content, priority)
            db.insertTask(task)
            finish()
            Toast.makeText(this,"Task Saved.", Toast.LENGTH_SHORT).show()
        }

    }
}