package com.example.appmoney.data.repository

import android.util.Log
import com.example.appmoney.data.model.Category
import com.example.appmoney.data.model.Transaction
import com.example.appmoney.ui.common.helper.ConvertMap
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class Repository() {

    private val db = Firebase.firestore
    private val auth = Firebase.auth
    // Lấy UserId
    private fun getUserId(): String {
        return auth.currentUser?.uid ?: throw Exception("User not logged in")
    }
//------------------------CATEGORY-----------------------------------//
    // ---Thêm 1 category---
    fun addCategory(
        typeId: String,
        category: Category,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userId = getUserId()
        val itemId = typeId + System.currentTimeMillis()
        db.collection("User").document(userId)
            .collection("Category").document(typeId)
            .collection("Item").document(itemId)
            .set(ConvertMap.toMap(category))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message ?: "Unknown error") }
    }
    // ---Kiểm tra xem category tồn tại chưa?---
    fun checkCategoryExists(typeId: String,desCat: String,onResult: (Boolean)->Unit){
        val userId = getUserId()
        db.collection("User").document(userId)
            .collection("Category").document(typeId)
            .collection("Item")
            .whereEqualTo("desCat", desCat.trim())
            .get()
            .addOnSuccessListener { result ->
                val exists = !result.isEmpty
                onResult(exists)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }
    // ---Lấy category theo type (Income/expenditure)---
    fun getCategory(
        typeId: String,
        onSuccess: (List<Category>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userId = getUserId()
        db.collection("User").document(userId)
            .collection("Category").document(typeId)
            .collection("Item")
            .get()
            .addOnSuccessListener { result ->
                val list = result.map { doc ->
                    val category = ConvertMap.toCategory(doc.data)
                    category.copy(idCat = doc.id)
                }
                onSuccess(list)
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Unknown error")
            }
    }
    // ---Update 1 category theo Id---
    fun updateCategory(
        typeId: String,
        itemId: String,
        category: Category,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userId = getUserId()
        db.collection("User").document(userId)
            .collection("Category").document(typeId)
            .collection("Item").document(itemId)
            .set(ConvertMap.toMap(category))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message ?: "Unknown error") }
    }
    // ---Del 1 category theo Id---
    fun deleteCategory(
        typeId: String,
        itemId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userId = getUserId()
        db.collection("User").document(userId)
            .collection("Category").document(typeId)
            .collection("Item").document(itemId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message ?: "Unknown error") }
    }
//------------------------TRANSACTION-----------------------------------//
    // ---Thêm 1 transaction---
    fun addTrans(trans: Transaction, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val userId = getUserId()
        val itemId = "Trans" + System.currentTimeMillis()
        db.collection("User").document(userId)
            .collection("Transaction").document(itemId)
            .set(ConvertMap.toMapTrans(trans))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message ?: "Unknown error") }
    }
    // ---Update 1 transaction---
    fun updateTrans(
        idTrans: String,
        trans: Transaction,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userId = getUserId()
        db.collection("User").document(userId)
            .collection("Transaction").document(idTrans)
            .set(ConvertMap.toMapTrans(trans))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message ?: "Unknown error") }
    }
    // ---Del 1 transaction theo Id---
    fun delTrans(
        idTrans: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userId = getUserId()
        db.collection("User").document(userId)
            .collection("Transaction").document(idTrans)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message ?: "Unknown error") }
    }
    // ---Lấy toàn bộ transaction---
    fun getTrans(
        onSuccess: (List<Transaction>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userId = getUserId()
        db.collection("User").document(userId)
            .collection("Transaction")
            .get()
            .addOnSuccessListener { result ->
                val list = mutableListOf<Transaction>()
                result.forEach { doc ->
                    list.add(ConvertMap.toStringTrans(doc.data).copy(idTrans = doc.id))
                }
                Log.d("Repository", "Fetched ${list.size} TransAndCat")
                onSuccess(list)
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Lỗi khi lấy Transaction")
            }
    }
    // ---Lấy transaction theo khoảng thời gian---
    fun getTransByTimes(
        dateStart: Timestamp,
        dateEnd: Timestamp,
        onSuccess: (List<Transaction>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userId = getUserId()
        db.collection("User").document(userId)
            .collection("Transaction")
            .whereGreaterThanOrEqualTo("date", dateStart)
            .whereLessThanOrEqualTo("date", dateEnd)
            .get()
            .addOnSuccessListener { result ->
                val list = result.map { doc ->
                    val trans = ConvertMap.toStringTrans(doc.data)
                    trans.copy(idTrans = doc.id)
                }
                onSuccess(list)
            }
            .addOnFailureListener{onFailure(it.message ?: "Unknown error")}

    }
}