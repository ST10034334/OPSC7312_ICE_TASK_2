package za.co.varsitycollege.st10034334.currenyconversionapp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query



interface CurrencyAPI {
    @GET("/v2/currency/convert")
    fun convertCurrency(
        @Query("api_key") apiKey: String?,
        @Query("from") fromCurrency: String?,
        @Query("to") toCurrency: String?,
        @Query("amount") amount: Double
    ): Call<CurrencyResponse?>?
}