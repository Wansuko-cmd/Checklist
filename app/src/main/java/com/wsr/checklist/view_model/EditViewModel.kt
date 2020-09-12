package com.wsr.checklist.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.wsr.checklist.type_file.CheckList
import com.wsr.checklist.type_file.EditList

class EditViewModel(application: Application) : AndroidViewModel(application) {
    var editList: MutableList<EditList> = mutableListOf()
    var checkList: MutableList<CheckList> = mutableListOf()
}