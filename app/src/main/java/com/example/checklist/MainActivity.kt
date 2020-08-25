package com.example.checklist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_checklist.*
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    lateinit var listAdapter: ListAdapter
    lateinit var testList: ArrayList<SampleData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val test1 = SampleData("HomeWork")
        val test2 = SampleData("Watch")
        val test3 = SampleData("Laptop")
        val test4 = SampleData("Money")

        val testList = arrayListOf(test1, test2, test3, test4)
        val listView = findViewById<ListView>(R.id.listView)
        val listAdapter = ListAdapter(this, testList)
        listView.adapter = listAdapter


    }
}