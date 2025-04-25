package br.com.budgets.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create

object RetrofitInstance {

    // Base URL da API ViaCEP
    private const val BASE_URL = "https://viacep.com.br/ws/"

    // Configuração do Retrofit
    val retrofit: Retrofit by lazy {
        // Configurar o Json para serialização
        val json = Json {
            ignoreUnknownKeys = true // Ignorar campos desconhecidos na resposta
            isLenient = true // Permitir respostas parcialmente válidas
        }

        // Configurar o cliente HTTP com interceptor de logging (opcional)
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY // Log detalhado das requisições
            })
            .build()

        // Criar o Retrofit com o conversor JSON e cliente HTTP
        Retrofit.Builder()
            .baseUrl(BASE_URL) // URL base da API
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType())) // Conversor JSON
            .client(client) // Cliente HTTP configurado
            .build()
    }

    // Função para criar o serviço Retrofit
    inline fun <reified T> createService(): T = retrofit.create(T::class.java)
}