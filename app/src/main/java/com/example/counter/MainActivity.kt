package com.example.counter

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
//    private lateinit var navView: NavigationView
    private lateinit var viewModel: CounterViewModel
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var counterAdapter: CounterAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var counterList: MutableList<Counter>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        drawerLayout = findViewById(R.id.drawer_layout)
//        navView = findViewById(R.id.navigation_view)

        viewModel = ViewModelProvider(this).get(CounterViewModel::class.java)
        SharedPreferencesManager.init(this)
        counterList = SharedPreferencesManager.getList()



        setupNavigationDrawer()
        setupMainContent()

        recyclerView = findViewById(R.id.drawer_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        counterAdapter = CounterAdapter(this, counterList)
        recyclerView.adapter = counterAdapter
    }


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        drawerLayout = findViewById(R.id.drawer_layout)
//        navView = findViewById(R.id.navigation_view)
//
//        viewModel = ViewModelProvider(this).get(CounterViewModel::class.java)
//        sharedPreferencesHelper = SharedPreferencesHelper(this)
//
//        setupNavigationDrawer()
//        setupMainContent()
//
//        // Load counters from SharedPreferences
//        val counters = sharedPreferencesHelper.loadCounters("myListKey")
//        viewModel.setCounters(counters)
//
////        viewModel.counters.observe(this, { updatedCounters ->
////            viewModel.saveCounters(sharedPreferencesHelper, "myListKey")
////        })
//    }

    private fun setupNavigationDrawer() {
//        val recyclerView = navView.findViewById<RecyclerView>(R.id.drawer_layout)

        val addnewcount: Button = findViewById(R.id.newcount)
        addnewcount.setOnClickListener() {
            showAddCounterDialog()
        }

        val retrievedList = SharedPreferencesManager.getList()
        retrievedList.forEach {
            Log.d("testingdulu", "Title: ${it.title}, Count: ${it.value}")
        }

//        recyclerView.layoutManager = LinearLayoutManager(this)
//        val adapter = CounterAdapter(SharedPreferencesManager.loadCounters("myListKey"))
//        recyclerView.adapter = adapter

//        val headerView = navView.getHeaderView(0)
//        val headerTitle = headerView.findViewById<TextView>(R.id.nav_header_title)
//        headerTitle.text = "Counters"
    }

    private fun setupMainContent() {
        val addButton: Button = findViewById(R.id.button_add)
        val minusButton: Button = findViewById(R.id.button_minus)
        val counterTextView: TextView = findViewById(R.id.counter_value)
        val resetButton: ImageButton = findViewById(R.id.button_reset)
        val editButton: ImageButton = findViewById(R.id.button_edit)
        val menuButton: ImageButton = findViewById(R.id.button_menu)

        viewModel.counters.observe(this, { counters ->
            counterTextView.text = "${counters[0].value}" // Update with first counter value
        })

        addButton.setOnClickListener {
            viewModel.incrementCounter(0) // Increment the first counter
        }

        minusButton.setOnClickListener {
            viewModel.decrementCounter(0) // Decrement the first counter
        }

        resetButton.setOnClickListener {
            viewModel.resetCounter(0)
            val toolbar:TextView = findViewById(R.id.toolbar_title)
            toolbar.text = "New Counter"
        }

        editButton.setOnClickListener {
            showEditCounterDialog(0)
        }

        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun showAddCounterDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_counter, null)
        val editTextTitle: EditText = dialogView.findViewById(R.id.edit_text_title)
        val editTextValue: EditText = dialogView.findViewById(R.id.edit_text_value)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<Button>(R.id.button_cancel).setOnClickListener {
            dialog.dismiss()
        }
        dialogView.findViewById<Button>(R.id.button_add).setOnClickListener {

            val title = editTextTitle.text.toString()
            val value = editTextValue.text.toString().toIntOrNull() ?: 0
//            val updatedCounter = counters[index].copy(title = title, value = value)
//            val updatedCounters = counters.toMutableList()
//            updatedCounters[index] = updatedCounter
//            viewModel.updateCounters(updatedCounters)
            val newItem = Counter(title, value)


            // Mendapatkan daftar yang sudah ada dari SharedPreferences
            val existingList = SharedPreferencesManager.getList()
            Log.d("biarin", "Data disimpan ke " + existingList)
            // Menambahkan item baru ke daftar yang sudah ada
            existingList.add(newItem)

            // Menyimpan kembali daftar yang sudah diperbarui ke SharedPreferences
            SharedPreferencesManager.saveList(existingList)

            // Menampilkan data yang sudah disimpan ke log
            Log.d("gokkil", "Data disimpan ke SharedPreferences: $existingList")

            updateRecyclerView(existingList)

//            dialog.dismiss()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateRecyclerView(newList: MutableList<Counter>) {
        // Misalkan mAdapter adalah instance dari RecyclerView.Adapter yang digunakan
        counterAdapter.updateData(newList)
        counterAdapter.notifyDataSetChanged()
    }

    private fun showEditCounterDialog(index: Int) {
        val counters = viewModel.counters.value ?: return
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_counter, null)
        val editTextTitle: EditText = dialogView.findViewById(R.id.edit_text_title)
        val editTextValue: EditText = dialogView.findViewById(R.id.edit_text_value)

        // Set initial values in the dialog fields


        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()



        dialogView.findViewById<Button>(R.id.button_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.editdit).setOnClickListener {
            val title = editTextTitle.text.toString()
            val value = editTextValue.text.toString().toIntOrNull() ?: 0
            val counter : TextView = findViewById(R.id.counter_value)
        val titlebar: TextView = findViewById(R.id.toolbar_title)
        counter.text = value.toString()
        titlebar.text = title
            val newItem = Counter(title, value)


            // Mendapatkan daftar yang sudah ada dari SharedPreferences
            val existingList = SharedPreferencesManager.getList()
            Log.d("biarin", "Data disimpan ke " + existingList)
            // Menambahkan item baru ke daftar yang sudah ada
            existingList.add(newItem)

            // Menyimpan kembali daftar yang sudah diperbarui ke SharedPreferences
            SharedPreferencesManager.saveList(existingList)

            // Menampilkan data yang sudah disimpan ke log
            Log.d("gokkil", "Data disimpan ke SharedPreferences: $existingList")

            dialog.dismiss()

            // Update toolbar title with the new counter title
//            textviewcounter.text = title

            // Save updated counters to SharedPreferences
//            sharedPreferencesHelper.saveCounters("myListKey", updatedCounters)
        }

        dialog.show()
    }

}
