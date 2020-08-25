package com.example.checklist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.add_checklist.view.*

class ListAdapter(context: Context, var List: List<SampleData>):
    ArrayAdapter<SampleData>(context,0, List){

    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val list = List[position]
        var view = convertView
        if (convertView == null){
            view = layoutInflater.inflate(R.layout.add_checklist, parent, false)
        }
        val item = view?.findViewById<TextView>(R.id.Item)
        item?.text = list.item

        //val remark = view?.findViewById<TextView>(R.id.Remark)
        //remark?.text = list.remark

        return view!!
    }
}