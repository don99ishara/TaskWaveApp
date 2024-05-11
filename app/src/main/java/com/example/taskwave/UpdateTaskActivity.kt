package com.example.taskwave

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.taskwave.databinding.ActivityUpdateTaskBinding

class UpdateTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateTaskBinding
    private lateinit var db: TasksDatabaseHelper
    private var taskId: Int = -1
    private var selectedPriority: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TasksDatabaseHelper(this)

        taskId = intent.getIntExtra("task_id", -1)
        if(taskId == -1){
            finish()
            return
        }

        val task = db.getTaskByID(taskId)
        binding.updateTitleEditText.setText(task.title)
        binding.updateContentEdittext.setText(task.content)
        selectedPriority = task.priority

        val priorityOptions = resources.getStringArray(R.array.priority_options)
        val priorityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorityOptions)
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.updatePrioritySpinner.adapter = priorityAdapter

        // Set the selected priority in the spinner
        val priorityPosition = priorityOptions.indexOfFirst { it == task.priority }
        binding.updatePrioritySpinner.setSelection(priorityPosition)

        binding.updatePrioritySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedPriority = priorityOptions[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedPriority = null
            }
        }

        binding.updateSaveButton.setOnClickListener {
            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.updateContentEdittext.text.toString()
            val priority = selectedPriority ?: task.priority
            val updatedTask = Task(taskId, newTitle, newContent, priority)
            db.updateTask(updatedTask)
            finish()
            Toast.makeText(this,"Changes saved.", Toast.LENGTH_SHORT).show()
        }

    }
}