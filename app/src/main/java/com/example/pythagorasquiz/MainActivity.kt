package com.example.pythagorasquiz

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private data class Question(
        val statement: String,
        val answers: List<String>,
        val correctIndex: Int,
        val explanation: String
    )

    private val questions = listOf(
        Question(
            statement = "Dans un triangle rectangle, si a=3 et b=4, quelle est la longueur de l'hypoténuse c ?",
            answers = listOf("5", "6", "7", "8"),
            correctIndex = 0,
            explanation = "c = √(3² + 4²) = √25 = 5."
        ),
        Question(
            statement = "Si l'hypoténuse vaut 13 et un côté vaut 5, quelle est la longueur de l'autre côté ?",
            answers = listOf("8", "10", "12", "14"),
            correctIndex = 2,
            explanation = "b = √(13² - 5²) = √(169 - 25) = √144 = 12."
        ),
        Question(
            statement = "Quel énoncé correspond au théorème de Pythagore ?",
            answers = listOf(
                "Dans tout triangle, a + b = c",
                "Dans un triangle rectangle, a² + b² = c²",
                "Dans un triangle isocèle, a = b",
                "Dans un triangle rectangle, a + b = c²"
            ),
            correctIndex = 1,
            explanation = "Le théorème relie les carrés des côtés de l'angle droit à l'hypoténuse."
        )
    )

    private var currentIndex = 0
    private var score = 0

    private lateinit var questionText: TextView
    private lateinit var scoreText: TextView
    private lateinit var feedbackText: TextView
    private lateinit var answersGroup: RadioGroup
    private lateinit var validateButton: Button
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionText = findViewById(R.id.question_text)
        scoreText = findViewById(R.id.score_text)
        feedbackText = findViewById(R.id.feedback_text)
        answersGroup = findViewById(R.id.answers_group)
        validateButton = findViewById(R.id.validate_button)
        nextButton = findViewById(R.id.next_button)

        renderQuestion()

        validateButton.setOnClickListener { checkAnswer() }
        nextButton.setOnClickListener { goToNext() }
    }

    private fun renderQuestion() {
        val question = questions[currentIndex]
        questionText.text = question.statement
        scoreText.text = getString(R.string.score_format, score, questions.size)
        feedbackText.text = ""

        answersGroup.removeAllViews()
        question.answers.forEachIndexed { index, answer ->
            val radioButton = layoutInflater.inflate(R.layout.answer_option, answersGroup, false) as RadioButton
            radioButton.id = index
            radioButton.text = answer
            answersGroup.addView(radioButton)
        }

        validateButton.isEnabled = true
        nextButton.isEnabled = false
    }

    private fun checkAnswer() {
        val selectedId = answersGroup.checkedRadioButtonId
        if (selectedId == -1) {
            feedbackText.text = getString(R.string.select_answer_prompt)
            return
        }

        val question = questions[currentIndex]
        if (selectedId == question.correctIndex) {
            score += 1
            feedbackText.text = getString(R.string.correct_answer, question.explanation)
        } else {
            val correctText = question.answers[question.correctIndex]
            feedbackText.text = getString(R.string.wrong_answer, correctText, question.explanation)
        }

        scoreText.text = getString(R.string.score_format, score, questions.size)
        validateButton.isEnabled = false
        nextButton.isEnabled = true
    }

    private fun goToNext() {
        if (currentIndex < questions.lastIndex) {
            currentIndex += 1
            renderQuestion()
        } else {
            feedbackText.text = getString(R.string.quiz_finished, score, questions.size)
            validateButton.isEnabled = false
            nextButton.isEnabled = false
        }
    }
}
