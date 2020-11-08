package com.dp.agro.data.source

import com.dp.agro.data.model.Disease
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher

class AppRepository(
    private val firestore: FirebaseFirestore,
    private val dispatcher: CoroutineDispatcher
) {
    fun findDiseaseBySlug(diseaseSlug: String, callback: (Disease) -> Unit) {
        firestore
            .collection("diseases")
            .document(diseaseSlug)
            .get()
            .addOnSuccessListener {
                if (it.data.isNullOrEmpty()) return@addOnSuccessListener
                else callback.invoke(Disease(it.data!!))
            }
    }

    fun findAllDiseases(callback: (List<Disease>) -> Unit) {
        firestore
            .collection("diseases")
            .get()
            .addOnSuccessListener {
                if (it.isEmpty) return@addOnSuccessListener
                val results = mutableListOf<Disease>()
                it.documents.forEach { ds ->
                    if (!ds.data.isNullOrEmpty()) {
                        results.add(Disease(ds.data!!))
                    }
                }
                callback.invoke(results)
            }
    }
}