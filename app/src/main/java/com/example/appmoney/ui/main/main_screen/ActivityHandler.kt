package com.example.appmoney.ui.main.main_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.appmoney.R
import com.example.appmoney.ui.main.feature.categorytype.CatTypeFragment
import com.example.appmoney.ui.main.feature.different.DifferentFragment
import com.example.appmoney.ui.main.feature.edit.EditCategoryFragment
import com.example.appmoney.ui.main.feature.fixcategory.FixFragment
import com.example.appmoney.ui.main.feature.input.InputFragment
import com.example.appmoney.ui.main.feature.input.income.IncomeFragment
import com.example.appmoney.ui.main.feature.report.ReportFragment
import com.example.appmoney.ui.main.feature.transactionhistory.HistoryTransFragment

sealed interface AppScreen {
    data object Income: AppScreen
    data object Input: AppScreen
    data object Edit: AppScreen
    data object HistoryTrans: AppScreen
    data object Report: AppScreen
    data object More: AppScreen
    data object Category: AppScreen
    data object Fix: AppScreen

    fun getScreenInstance(): Fragment {
        return when(this) {
            HistoryTrans -> HistoryTransFragment()
            Edit -> EditCategoryFragment()
            Income -> IncomeFragment()
            Input -> InputFragment()
            More -> DifferentFragment()
            Report -> ReportFragment()
            Category -> CatTypeFragment()
            Fix -> FixFragment()
        }
    }
}

fun FragmentActivity.navigateFragment(screen: AppScreen, bundle: Bundle? = null) {
    val fm = this.supportFragmentManager
    val transaction = fm.beginTransaction()
    val tagName = screen.javaClass.name

    fm.fragments.forEach {
        transaction.hide(it)
    }

    fm.findFragmentByTag(tagName)?.let { previous ->
        previous.arguments = bundle
        transaction.show(previous)
        transaction.commit()
        return
    } ?: run {
        screen.getScreenInstance().let { newFragment ->
            newFragment.arguments = bundle
            transaction.add(R.id.frameHome, newFragment, tagName)
            transaction.addToBackStack(tagName)
        }
        transaction.commit()
    }
}