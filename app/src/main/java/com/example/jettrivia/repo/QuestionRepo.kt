package com.example.jettrivia.repo

import com.example.jettrivia.data.DataOrException
import com.example.jettrivia.model.QuestionItem
import com.example.jettrivia.network.QuestionApi
import javax.inject.Inject

class QuestionRepo @Inject constructor(private val api: QuestionApi) {

    private val listOfQuestions = DataOrException<ArrayList<QuestionItem>, Boolean, Exception>()

}