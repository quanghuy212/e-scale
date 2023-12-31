package com.example.escalecaculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.math.BigDecimal
import java.text.DecimalFormat
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class MainActivity : AppCompatActivity() {

    private var weight: Double = 0.0
    private var height: Double = 0.0
    private var bmiIndex: Double = 0.0
    private lateinit var statusTextView: TextView
    private lateinit var heightEditText: EditText
    private lateinit var resultTextView: TextView
    private lateinit var calculatorBtn: Button
    private lateinit var getBtn: Button
    private lateinit var weightTextView: TextView

    // Init realtime database of Firebase
    private val db = Firebase.database
    val myRef = db.getReference("get")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Init components
        getBtn = findViewById(R.id.getWeightButton)
        weightTextView = findViewById(R.id.weightTextView)
        calculatorBtn = findViewById(R.id.calculateButton)
        resultTextView = findViewById(R.id.resultTextView)
        heightEditText = findViewById(R.id.heightEditText)
        statusTextView = findViewById(R.id.statusTextView)



        // Get weight from Firebase
        getBtn.setOnClickListener {
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue(Double::class.java)
                    if (value != null) {
                        weight = value
                        weightTextView.text = "${value.toString()} kg"
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    println("error head dickk!!")
                }
            })
        }

        calculatorBtn.setOnClickListener {
            if (weightTextView.text.isEmpty()) {
                Toast.makeText(this,"You must click Button Get Weight first.",Toast.LENGTH_SHORT).show()
            }
            if (heightEditText.text.isEmpty()) {
                Toast.makeText(this,"You must enter your height first",Toast.LENGTH_SHORT).show()
            } else {
                height = heightEditText.text.toString().toDouble()
            }

            bmiIndex = calculateBMI(weight, height)
            println(bmiIndex)
            resultTextView.text = bmiIndex.toString()
            statusTextView.text = checkingStatus(bmiIndex)
        }
    }
}

fun calculateBMI(weight: Double, height: Double) : Double {

    // BMI = Cân nặng (kg) / Chiều cao (m) ^ 2
    val bmiIdx: Double = weight / (height / 100).pow(2.0)

    val bmiIndex = String.format("%.2f", bmiIdx).toDouble()
    return bmiIndex
}

fun checkingStatus(bmiIndex: Double) : String {

    when(bmiIndex) {
        in 0.0..18.5 -> return "Gầy"
        in 18.5..24.9 -> return "Bình thường"
        in 25.0..29.9 -> return "Thừa cân"
        in 30.0..34.9 -> return "Béo phì độ I"
        in 35.0..39.9 -> return "Béo phì độ II"
        else -> return "Béo phì độ III"
    }
    return "Lỗi!"
}