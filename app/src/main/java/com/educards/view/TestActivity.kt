package com.educards.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.educards.R

class TestActivity : AppCompatActivity() {
    private lateinit var tvCardAnswer: TextView
    private lateinit var tvCardQuestion: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        tvCardAnswer = findViewById(R.id.tvCardAnswer)
        tvCardQuestion = findViewById(R.id.tvCardQuestion)
    }

//    private fun addCard(){
//        tvCardQuestion.setOnClickListener{
//            tvCardQuestion.visibility = View.INVISIBLE
//            tvCardAnswer.visibility = View.VISIBLE
//        }
//        tvCardAnswer.setOnClickListener {
//            tvCardQuestion.visibility = View.VISIBLE
//            tvCardAnswer.visibility = View.INVISIBLE
//        }
//
//        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//                result ->
//            val data: Intent? = result.data
//            if (data != null){
//                val questionString = data.getStringExtra("Question_Key")
//                val answerString = data.getStringExtra("Answer_Key")
//                tvCardQuestion.text = questionString
//                tvCardAnswer.text = answerString
//            }
//        }
//        val addIcon = findViewById<ImageView>(R.id.add_question_button)
//        addIcon.setOnClickListener {
//            val intent = Intent(this, AddCardActivity::class.java)
//            resultLauncher.launch(intent)
//        }
//    }
}