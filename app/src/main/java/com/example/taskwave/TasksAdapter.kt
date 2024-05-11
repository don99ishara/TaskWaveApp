package com.example.taskwave

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TasksAdapter(private var tasks: List<Task>, context: Context) :
    RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    private val db : TasksDatabaseHelper = TasksDatabaseHelper(context)

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val priorityTextView: TextView = itemView.findViewById(R.id.priorityTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.titleTextView.text = task.title
        holder.contentTextView.text = task.content
        holder.priorityTextView.text = task.priority

        when (task.priority) {
            "High" -> holder.itemView.setBackgroundResource(R.drawable.red_border)
            "Medium" -> holder.itemView.setBackgroundResource(R.drawable.blue_border)
            "Low" -> holder.itemView.setBackgroundResource(R.drawable.green_border)
        }

        when (task.priority) {
            "High" -> {
                holder.itemView.setBackgroundResource(R.drawable.red_border)
                holder.priorityTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.highpriority))
            }
            "Medium" -> {
                holder.itemView.setBackgroundResource(R.drawable.blue_border)
                holder.priorityTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.mediumpriority))
            }
            "Low" -> {
                holder.itemView.setBackgroundResource(R.drawable.green_border)
                holder.priorityTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.lowpriority))
            }
        }




        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateTaskActivity::class.java).apply {
                putExtra("task_id", task.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            db.deleteTask(task.id)
            refreshData(db.getAllTasks())
            Toast.makeText(holder.itemView.context, "Task deleted.", Toast.LENGTH_SHORT).show()
        }

    }


    //refresh data
    fun refreshData(newTasks: List<Task>){
        tasks = newTasks
        notifyDataSetChanged()
    }


}