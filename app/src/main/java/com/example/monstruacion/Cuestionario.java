package com.example.monstruacion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Cuestionario extends AppCompatActivity {

    private TextView optionA, optionB, optionC, optionD;
    private TextView questionnumber, question;
    private TextView chechkout1, checkout2;
    int currentIndex;
    int mscore = 0;
    int qn = 0;
    String nombre;
    private UserDataJavaWrapper userDataJavaWrapper;
    ProgressBar progressBar;
    ArrayList<String> userResponses = new ArrayList<>();

    int CurrentQuestion, CurrentOptionA, CurrentOptionB, CurrentOptionC, CurrentOptionD;

    private answerclass[] questionBank = new answerclass[]
            {
                    new answerclass(R.string.question_1, R.string.question1_A, R.string.question1_B, R.string.question1_C, R.string.question1_D, R.string.answer_1),
                    new answerclass(R.string.question_2, R.string.question_2A, R.string.question_2B, R.string.question_2C, R.string.question_2D, R.string.answer_2),
                    new answerclass(R.string.question_3, R.string.question_3A, R.string.question_3B, R.string.question_3C, R.string.question_3D, R.string.answer_3),
                    new answerclass(R.string.question_4, R.string.question_4A, R.string.question_4B, R.string.question_4C, R.string.question_4D, R.string.answer_4),
                    new answerclass(R.string.question_5, R.string.question_5A, R.string.question_5B, R.string.question_5C, R.string.question_5D, R.string.answer_5),
                    new answerclass(R.string.question_6, R.string.question_6A, R.string.question_6B, R.string.question_6C, R.string.question_6D, R.string.answer_6),
                    new answerclass(R.string.question_7, R.string.question_7A, R.string.question_7B, R.string.question_7C, R.string.question_7D, R.string.answer_7),
                    new answerclass(R.string.question_8, R.string.question_8A, R.string.question_8B, R.string.question_8C, R.string.question_8D, R.string.answer_8),
                    new answerclass(R.string.question_9, R.string.question_9A, R.string.question_9B, R.string.question_9C, R.string.question_9D, R.string.answer_9),
                    new answerclass(R.string.question_10, R.string.question_10A, R.string.question_10B, R.string.question_10C, R.string.question_10D, R.string.answer_10)
            };

    final int PROGRESS_BAR = (int) Math.ceil(100 / questionBank.length);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cuestionario_activity);

        nombre = getIntent().getStringExtra("nombre");

        userDataJavaWrapper = new UserDataJavaWrapper(new UserData());


        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);

        question = findViewById(R.id.question);
        questionnumber = findViewById(R.id.QuestionNumber);

        chechkout1 = findViewById(R.id.selectoption);
        checkout2 = findViewById(R.id.CorrectAnswer);
        progressBar = findViewById(R.id.progress_bar);

        CurrentQuestion = questionBank[currentIndex].getQuestionid();
        question.setText(CurrentQuestion);
        CurrentOptionA = questionBank[currentIndex].getOptionA();
        optionA.setText(CurrentOptionA);
        CurrentOptionB = questionBank[currentIndex].getOptionB();
        optionB.setText(CurrentOptionB);
        CurrentOptionC = questionBank[currentIndex].getOptionC();
        optionC.setText(CurrentOptionC);
        CurrentOptionD = questionBank[currentIndex].getOptionD();
        optionD.setText(CurrentOptionD);

        optionA.setOnClickListener(v -> {
            checkAnswer(CurrentOptionA);
            updateQuestion();
        });

        optionB.setOnClickListener(v -> {
            checkAnswer(CurrentOptionB);
            updateQuestion();
        });

        optionC.setOnClickListener(v -> {
            checkAnswer(CurrentOptionC);
            updateQuestion();
        });

        optionD.setOnClickListener(v -> {
            checkAnswer(CurrentOptionD);
            updateQuestion();
        });
    }

    private void checkAnswer(int userSelection) {
        int correctanswer = questionBank[currentIndex].getAnswerid();

        chechkout1.setText(userSelection);
        checkout2.setText(correctanswer);

        String m = chechkout1.getText().toString().trim();
        String n = checkout2.getText().toString().trim();


        userResponses.add(m);

        if (m.equals(n)) {
            mscore = mscore + 1;
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateQuestion() {
        currentIndex = (currentIndex + 1) % questionBank.length;

        if (currentIndex == 0) {
            String URL = "http://192.168.43.236/information.php";
            RequestQueue queue = Volley.newRequestQueue(this);

            //Se almacenan las respuestas del usuario en un objeto StringBuilder
            StringBuilder userResponsesParam = new StringBuilder();
            for (int i = 0; i < userResponses.size(); i++) {
                userResponsesParam.append("respuesta").append(i + 1).append("=").append(userResponses.get(i)).append("&");
            }

            StringRequest postRequest = new StringRequest(Request.Method.POST, URL, response -> {
                // Manejar la respuesta del servidor
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();


                String data = "duracion_p";
                Log.d("Debugging", userResponses.get(4));
                userDataJavaWrapper.callChangeMyPeriodInformation("duracion_p", userResponses.get(4));



                startActivity(new Intent(this, MainActivity.class));
                finish();
            }, error -> Toast.makeText(getApplicationContext(), "Ha habido un error  " + error, Toast.LENGTH_LONG).show()) {
                @Override
                protected Map<String, String> getParams() {
                    // Se envían las respuestas y el nombre del usuario para identificar qué respuestas dió quien
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("respuestas", userResponsesParam.toString());
                    parameters.put("nombreUsuario", nombre);
                    return parameters;
                }
            };
            queue.add(postRequest);
        }

        CurrentQuestion = questionBank[currentIndex].getQuestionid();
        question.setText(CurrentQuestion);
        CurrentOptionA = questionBank[currentIndex].getOptionA();
        optionA.setText(CurrentOptionA);
        CurrentOptionB = questionBank[currentIndex].getOptionB();
        optionB.setText(CurrentOptionB);
        CurrentOptionC = questionBank[currentIndex].getOptionC();
        optionC.setText(CurrentOptionC);
        CurrentOptionD = questionBank[currentIndex].getOptionD();
        optionD.setText(CurrentOptionD);

        qn = qn + 1;

        if (qn <= questionBank.length) {
            questionnumber.setText(qn + "/" + questionBank.length + "Preguntas");
        }

        progressBar.incrementProgressBy(PROGRESS_BAR);
    }
}