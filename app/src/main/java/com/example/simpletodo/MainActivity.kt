package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.apache.commons.io.FileUtils
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    // holds list of tasks
    var listOfTasks = mutableListOf<String>()
    // creates variable reference here that will be initialized later
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // variable for onClickListener
        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                // remove item from list
                listOfTasks.removeAt(position)
                // notify adapter that data set has changed
                adapter.notifyItemRemoved(position)
                // check for action in Logcat
                // Log.i("Caren", "Trying to remove")
                // save new list of items
                saveItems()
            }

        }

//        // detect when user clicks on add button
//        findViewById<Button>(R.id.button).setOnClickListener {
//            // Code to be executed when user clicks on a button
//            // can use this code to check in Logcat if things are connected
//            Log.i("Caren", "User clicked on button")
//        }

        // Fake list of tasks
        // listOfTasks.add("Do laundry")
        // listOfTasks.add("Go for a walk")

        // load items from file
        loadItems()

        // Lookup the recyclerview in activity layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        // set up the button and input field so that the user can enter a task and add it to the list
        val inputTestField = findViewById<EditText>(R.id.addTaskField)

        // get a reference to the button, and set an onClickListener
        findViewById<Button>(R.id.button).setOnClickListener {
            // grab test that user inputted into @id/addTaskField
             val userInputtedTask = inputTestField.text.toString()
            // add string to list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)
            // notify adapter that data has been updated
            adapter.notifyItemInserted(listOfTasks.size -1)
            // reset text field
            inputTestField.setText("")
            // save items to list
            saveItems()
        }
    }

    // Save the data that the user has inputted by writing and reading from file

    // get datafile
    fun getDataFile() : File {
        // every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt")
    }

    // load tasks from file
    fun loadItems() {
        // try-catch to read file, catches incorrect/invalid files so that app doesn't crash
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // save items by writing to a file
    fun saveItems() {
        // try-catch to write to file, catches incorrect/invalid files so that app doesn't crash
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}