package com.example.appmoney.ui.main.feature.categorytype

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmoney.data.model.Category
import com.example.appmoney.data.repository.Repository
import com.example.appmoney.ui.common.helper.TabObject

class CatTypeViewModel(
) : ViewModel() {
    private val repository: Repository = Repository()

    private val _tabSelected = MutableLiveData<Int>()
    val tabSelected: LiveData<Int> get() = _tabSelected

    fun setTabSelected(tab: Int) {
        _tabSelected.value = tab

        TabObject.changeTab(tab)
    }

    // deleter
    fun delCategory(
        typeId: String = "",
        idCat: String = "",
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        repository.deleteCategory(typeId, idCat, onSuccess, onFailure)
    }

    // updater
    fun updateCategory(
        typeId: String,
        idCat: String,
        category: Category,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        repository.updateCategory(typeId, idCat, category, onSuccess, onFailure)
    }
}