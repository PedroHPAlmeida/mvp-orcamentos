package br.com.budgets.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import br.com.budgets.domain.model.Owner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// Extensão para criar o DataStore
private val Context.dataStore by preferencesDataStore(name = "owner_preferences")

// Chave para armazenar os dados como JSON
private val OWNER_JSON_KEY = stringPreferencesKey("owner_json")

object OwnerDataStore {

    // Função para salvar os dados do dono como JSON
    suspend fun saveOwnerData(context: Context, owner: Owner) {
        try {
            val ownerJson = Json.Default.encodeToString(owner) // Serializa o objeto para JSON
            context.dataStore.edit { preferences ->
                preferences[OWNER_JSON_KEY] = ownerJson
            }
            println("Dados do dono salvos com sucesso: $ownerJson") // Log para depuração
        } catch (e: Exception) {
            e.printStackTrace() // Logar exceções no Logcat
            println("Erro ao salvar os dados do dono: ${e.message}")
        }
    }

    // Função para recuperar os dados do dono a partir do JSON
    fun getOwnerData(context: Context): Flow<Owner?> {
        return context.dataStore.data.map { preferences ->
            val ownerJson = preferences[OWNER_JSON_KEY]
            try {
                ownerJson?.let { Json.Default.decodeFromString<Owner>(it) } // Desserializa o JSON para objeto
            } catch (e: Exception) {
                e.printStackTrace() // Logar exceções no Logcat
                println("Erro ao recuperar os dados do dono: ${e.message}")
                null
            }
        }
    }
}