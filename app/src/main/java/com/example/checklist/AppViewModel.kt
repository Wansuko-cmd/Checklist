package com.example.checklist

import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.add_checklist.view.*

class AppViewModel: ViewModel() {
    var infoList = Datas().getAll()

    fun changeCheck(holder: ListViewHolder){
        infoList[holder.adapterPosition] = infoList[holder.adapterPosition].copy(check = holder.check.CheckBox.isChecked)
    }
}