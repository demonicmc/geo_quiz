package com.example.android.geoquiz

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class QuizActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "QuizActivity"
        private const val KEY_INDEX = "index"
        private const val KEY_ANSWERS = "answers"
        private const val KEY_COUNT = "count"
    }

    private var mTrueButton: Button? = null
    private var mFalseButton: Button? = null
    private var mNextButton: ImageButton? = null
    private var mPreviousButton: ImageButton? = null
    private var mQuestionTextView: TextView? = null
    private var mListAnswersTrue: MutableList<Int> = mutableListOf()
    private var mListAnswersFalse: MutableList<Int> = mutableListOf()
    private var mCheatButton: Button? = null

    private val mQuestionBank = arrayOf(
        Question(
            R.string.question_australia, true
        ),
        Question(
            R.string.question_oceans, true
        ),
        Question(
            R.string.question_mideast, false
        ),
        Question(
            R.string.question_africa, false
        ),
        Question(
            R.string.question_americas, true
        ),
        Question(
            R.string.question_asia, true
        )
    )

    private var mQuestionsAnswered = BooleanArray(mQuestionBank.size)

    private var mCurrentIndex: Int = 0

    private var mCountAnswers = mQuestionBank.size


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle) called")
        setContentView(R.layout.activity_quiz)

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0)
            mQuestionsAnswered = savedInstanceState.getBooleanArray(KEY_ANSWERS)
        }

        mQuestionTextView = findViewById(R.id.question_text_view) as? TextView

        mTrueButton = findViewById(R.id.true_button) as? Button
        mTrueButton?.setOnClickListener {
            checkAnswer(true)
        }

        mFalseButton = findViewById(R.id.false_button) as? Button
        mFalseButton?.setOnClickListener {
            checkAnswer(false)
        }

        mCheatButton = findViewById(R.id.cheat_button) as? Button
        mCheatButton?.setOnClickListener {
            val answerIsTrue = mQuestionBank[mCurrentIndex].mAnswerTrue
            val intent = CheatActivity.newIntent(this, answerIsTrue)
            startActivity(intent)
            updateQuestion()
        }

        mNextButton = findViewById(R.id.next_button) as? ImageButton
        mNextButton?.setOnClickListener {
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.size
            updateQuestion()
        }

        mPreviousButton = findViewById(R.id.previous_button) as? ImageButton
        mPreviousButton?.setOnClickListener {
            mCurrentIndex = if (mCurrentIndex == 0) mQuestionBank.size - 1 else mCurrentIndex - 1
            updateQuestion()
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle?) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState?.putInt(KEY_INDEX, mCurrentIndex)
        savedInstanceState?.putBooleanArray(KEY_ANSWERS, mQuestionsAnswered)
        savedInstanceState?.putInt(KEY_COUNT, mCountAnswers)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val question = mQuestionBank[mCurrentIndex].mTextResId
        mQuestionTextView?.setText(question)
        mTrueButton!!.isEnabled = !mQuestionsAnswered[mCurrentIndex]
        mFalseButton!!.isEnabled = !mQuestionsAnswered[mCurrentIndex]
    }

    private fun checkAnswer(userPressedTrue: Boolean) {
        val answerIsTrue = mQuestionBank[mCurrentIndex].mAnswerTrue
        mQuestionsAnswered[mCurrentIndex] = true
        mTrueButton!!.isEnabled = false
        mFalseButton!!.isEnabled = false
        val messageResId: Int = when (userPressedTrue == answerIsTrue) {
            true -> {
                mListAnswersTrue.add(1)
                R.string.correct_toast
            }
            false -> {
                mListAnswersFalse.add(0)
                R.string.incorrect_toast
            }

        }

        mCountAnswers--

        if (mCountAnswers == 0) {
            val score = applicationContext.resources.getString(R.string.quiz_score, percentageOfCorrectAnswers())
            Toast.makeText(this, score, Toast.LENGTH_LONG).show()
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    private fun percentageOfCorrectAnswers(): Int = mListAnswersTrue.size * 100 / mQuestionBank.size

}
