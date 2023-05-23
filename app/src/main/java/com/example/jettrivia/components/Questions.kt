package com.example.jettrivia.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jettrivia.screens.QuestionsViewModel
import com.example.jettrivia.util.AppColors

@Composable
fun Questions(viewModel: QuestionsViewModel) {
    val questions =
        viewModel.data.value.data?.toMutableList() //the type is ArrayList so it needs to become mutable list for ui.
    if (viewModel.data.value.loading == true) {
        CircularProgressIndicator() //shows a loading indicator whilst questions are loading.
    } else {
        Log.d("loading", "Questions: is done loading")
        questions?.forEach {
            Log.d("list", "Question: ${it.question}")
        }
    }
}

@Preview
@Composable
fun QuestionDisplay() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp), color = AppColors.mDarkPurple
    ) {
        Column(
            Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            QuestionTracker()
            DottedLine()
        }
    }
}

@Composable
fun QuestionTracker(counter: Int = 10, outOf: Int = 4875) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
                withStyle(
                    style = SpanStyle(
                        color = AppColors.mLightGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 27.sp
                    )
                ) {
                    append("Question $counter/") // Add the text you want to display
                    withStyle(
                        style = SpanStyle(
                            color = AppColors.mLightGray,
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp)) {
                            append("$outOf")
                        }
                }
            }
        }
    , modifier = Modifier.padding(20.dp))
}

@Composable
fun DottedLine(){
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(5.dp), onDraw = {
        drawLine(start = Offset.Zero, end = Offset(size.width,0f), color = AppColors.mLightGray, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
    })
}