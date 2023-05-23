package com.example.jettrivia.repo

import android.util.Log
import com.example.jettrivia.data.DataOrException
import com.example.jettrivia.model.QuestionItem
import com.example.jettrivia.network.QuestionApi
import javax.inject.Inject

class QuestionRepo @Inject constructor(private val api: QuestionApi) {

    private val dataOrException = DataOrException<ArrayList<QuestionItem>, Boolean, Exception>()

    suspend fun getAllQuestions(): DataOrException<ArrayList<QuestionItem>, Boolean, Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllQuestions() //getting arraylist of QuestionItem
            if (dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false //if its not empty we received some data so its no longer loading

        } catch (exception: Exception){
            dataOrException.e = exception //appending the exception to our wrapper class
            Log.d("Exc","getAllQuestions: ${dataOrException.e!!.localizedMessage}")
        }
        return dataOrException
    }

}