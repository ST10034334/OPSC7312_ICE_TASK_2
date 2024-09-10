package za.co.varsitycollege.st10034334.currenyconversionapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var amountInput: EditText
    private lateinit var fromCurrencyInput:EditText
    private lateinit var toCurrencyInput:EditText
    private lateinit var convertButton: Button
    private lateinit var resultText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //View Binding.
        amountInput = findViewById(R.id.edtAmount);
        fromCurrencyInput = findViewById(R.id.edtFromCurrency);
        toCurrencyInput = findViewById(R.id.edtToCurrency);
        convertButton = findViewById(R.id.btnConvert);
        resultText = findViewById(R.id.txtResult);


        val retrofit = Retrofit.Builder()
            .baseUrl("https://currency.getgeoapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val currencyApi: CurrencyAPI = retrofit.create(CurrencyAPI::class.java)

        convertButton.setOnClickListener {
            val fromCurrency =
                fromCurrencyInput.text.toString().uppercase(Locale.getDefault())
            val toCurrency =
                toCurrencyInput.text.toString().uppercase(Locale.getDefault())
            val amount = amountInput.text.toString().toDouble()

            val call: Call<CurrencyResponse?>? = currencyApi.convertCurrency(
                "6b36eb0e28e37e260d13cdfae3ed3528276c5435",
                fromCurrency,
                toCurrency,
                amount
            )
            call?.enqueue(object : Callback<CurrencyResponse?> {
                override fun onResponse(
                    call: Call<CurrencyResponse?>?,
                    response: Response<CurrencyResponse?>
                ) {
                    if (response.isSuccessful()) {
                        val currencyResponse: CurrencyResponse = response.body()!!
                        if (currencyResponse != null) {
                            val convertedAmount = currencyResponse.result
                            resultText.text = "Converted Amount: $convertedAmount"
                        }
                    } else {
                        resultText.text = "Failed to convert"
                    }
                }

                override fun onFailure(call: Call<CurrencyResponse?>?, t: Throwable) {
                    resultText.text = "Error: " + t.message
                }
            })
        }
    }
}