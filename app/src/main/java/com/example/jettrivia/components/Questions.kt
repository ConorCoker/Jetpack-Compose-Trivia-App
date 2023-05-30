package com.example.jettrivia.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jettrivia.model.QuestionItem
import com.example.jettrivia.screens.QuestionsViewModel
import com.example.jettrivia.util.AppColors

@Composable
fun Questions(viewModel: QuestionsViewModel) {
    val questions =
        viewModel.data.value.data?.toMutableList() //the type is ArrayList so it needs to become mutable list for ui.

    val questionIndex = remember {
        mutableStateOf(1)
    }

    val wasPreviousCorrect = remember {
        mutableStateOf(true)
    }
    val score = remember{
        mutableStateOf(0)
    }
    if (viewModel.data.value.loading == true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.mLightPurple),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.size(50.dp)) //shows a loading indicator whilst questions are loading.
        }
    } else {
        Log.d("loading", "Questions: is done loading")
        val question = try {
            questions?.get(questionIndex.value)
        } catch (ex: Exception) {
            null
        }
        if (questions != null) {
            QuestionDisplay(
                question = question!!,
                questionIndex = questionIndex,
                viewModel = viewModel,
                score = score.value,
                wasPreviousCorrect = wasPreviousCorrect.value
            ) { currentIndex, wasItCorrect ->
                questionIndex.value = currentIndex + 1
                wasPreviousCorrect.value = wasItCorrect
                if (wasItCorrect){
                    score.value++
                }
            }
        }
    }
}

//@Preview
@Composable
fun QuestionDisplay(
    question: QuestionItem,
    questionIndex: MutableState<Int>,
    viewModel: QuestionsViewModel,
    wasPreviousCorrect: Boolean,
    score: Int,
    onNextClicked: (Int, Boolean) -> Unit
) {

    val choicesState = remember(question) {
        question.choices.toMutableList()
    }

    val answerState = remember(question) {
        mutableStateOf<Int?>(null)
    }

    val correctAnswerState = remember(question) {
        mutableStateOf<Boolean>(false)
    }

    val updateAnswer: (Int) -> Unit = remember(question) {
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == question.answer
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppColors.mDarkPurple
    ) {
        Column(
            Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            ShowProgress(
                score = score,
                minusOrPlus = wasPreviousCorrect
            )
            QuestionTracker(counter = questionIndex.value, outOf = viewModel.getTotalQuestions())
            DottedLine()
            Column() {
                Text(
                    text = question.question,
                    modifier = Modifier
                        .padding(6.dp)
                        .align(Alignment.Start)
                        .fillMaxHeight(0.3f),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp,
                    color = AppColors.mOffWhite
                )
                //choices
                choicesState.forEachIndexed { index, answerText ->
                    Row(
                        Modifier
                            .padding(3.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                            .border(
                                width = 4.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        AppColors.mOffDarkPurple,
                                        AppColors.mOffDarkPurple
                                    )
                                ),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 50,
                                    topEndPercent = 50,
                                    bottomEndPercent = 50,
                                    bottomStartPercent = 50
                                )
                            )
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (answerState.value == index),
                            onClick = {
                                updateAnswer(index)
                            },
                            Modifier.padding(start = 16.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor = AppColors.mOffWhite
                            )
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Light,
                                        color = AppColors.mOffWhite

                                    )
                                ) {
                                    append(answerText)
                                }

                            }
                        )
                    }
                }
                Box(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = { onNextClicked(questionIndex.value, correctAnswerState.value) },
                        modifier = Modifier
                            .padding(3.dp),
                        shape = RoundedCornerShape(34.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = AppColors.mLightBlue
                        )
                    ) {
                        Text(
                            text = "Next",
                            modifier = Modifier.padding(4.dp),
                            color = AppColors.mOffWhite,
                            fontSize = 17.sp
                        )
                    }
                }


            }
        }
    }
}


@Composable
fun QuestionTracker(counter: Int, outOf: Int) {
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
                            fontSize = 14.sp
                        )
                    ) {
                        append("$outOf")
                    }
                }
            }
        }, modifier = Modifier.padding(20.dp)
    )
}

@Composable
fun DottedLine() {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(5.dp), onDraw = {
        drawLine(
            start = Offset.Zero,
            end = Offset(size.width, 0f),
            color = AppColors.mLightGray,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
    })
}

@SuppressLint("RememberReturnType")
@Preview
@Composable
fun ShowProgress(score: Int = 14, minusOrPlus: Boolean = true) {
    val gradient = Brush.linearGradient(listOf(Color(0xFFF95075), Color(0xFFBE6BE5)))

    val progressFactor = remember(score) {
        mutableStateOf(score * 0.005f)
    }

    Column() {
        Row(
            Modifier
                .padding(3.dp)
                .fillMaxWidth()
                .border(
                    width = 4.dp, brush = Brush.linearGradient(
                        listOf(AppColors.mLightPurple, AppColors.mLightPurple)
                    ), shape = RoundedCornerShape(34.dp)
                )
                .clip(
                    RoundedCornerShape(
                        topStartPercent = 50,
                        topEndPercent = 50,
                        bottomStartPercent = 50,
                        bottomEndPercent = 50
                    )
                )
                .background(Color.Transparent), verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {},
                enabled = false,
                elevation = null,
                contentPadding = PaddingValues(start = 1.dp, top = 1.dp, bottom = 1.dp),
                colors = buttonColors(
                    Color.Transparent,
                    disabledBackgroundColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth(progressFactor.value)
                    .background(brush = gradient)
            ) {
            }
        }
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                score.toString(),
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(23.dp))
                    .padding(start = 6.dp, bottom = 6.dp, top = 6.dp, end = 0.dp),
                color = if (minusOrPlus) Color.Green else Color.Red, maxLines = 1
            )
        }


    }
}