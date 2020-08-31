package com.example.checklist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_checklist.*
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val test1 = SampleData(true, "HomeWork")
        val test2 = SampleData(true, "Watch")
        val test3 = SampleData(false, "Laptop")
        val test4 = SampleData(false,"Money")
        val testList = arrayListOf(test1, test2, test3, test4)

        val adapter = ListAdapter(testList)
        val layoutManager = LinearLayoutManager(this)
        RecyclerView.adapter = adapter
        RecyclerView.layoutManager = layoutManager
        RecyclerView.setHasFixedSize(true)


    }
}