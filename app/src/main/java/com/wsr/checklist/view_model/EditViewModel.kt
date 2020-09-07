package com.wsr.checklist.view_model

import androidx.lifecycle.ViewModel
import com.wsr.checklist.type_file.EditList

class EditViewModel: ViewModel() {
    var editList: MutableList<EditList> = mutableListOf<EditList>()
}