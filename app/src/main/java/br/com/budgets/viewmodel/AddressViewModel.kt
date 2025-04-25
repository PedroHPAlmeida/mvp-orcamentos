package br.com.budgets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.budgets.data.AddressResponse
import br.com.budgets.network.RetrofitInstance
import br.com.budgets.network.ViaCepService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddressViewModel : ViewModel() {

    // Estado para armazenar o endereço retornado pela API
    private val _address = MutableStateFlow<AddressResponse?>(null)
    val address: StateFlow<AddressResponse?> = _address

    // Estado para armazenar mensagens de erro (se necessário)
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Instância do serviço Retrofit
    private val viaCepService = RetrofitInstance.createService<ViaCepService>()

    /**
     * Busca o endereço na API ViaCEP com base no CEP fornecido.
     * @param cep O CEP a ser consultado.
     */
    fun fetchAddress(cep: String) {
        viewModelScope.launch {
            try {
                // Validar o CEP antes de fazer a requisição
                if (isValidCep(cep)) {
                    // Fazer a requisição para buscar o endereço
                    val response = viaCepService.getAddressByCep(cep)
                    _address.value = response
                    _errorMessage.value = null // Limpar mensagens de erro caso a busca seja bem-sucedida
                } else {
                    // CEP inválido
                    _errorMessage.value = "Por favor, insira um CEP válido com 8 dígitos numéricos."
                    _address.value = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _address.value = null
                _errorMessage.value = "Erro ao buscar o endereço. Verifique sua conexão ou o formato do CEP."
            }
        }
    }

    /**
     * Valida o CEP para garantir que tenha 8 dígitos numéricos.
     * @param cep O CEP a ser validado.
     * @return `true` se o CEP for válido, caso contrário `false`.
     */
    private fun isValidCep(cep: String): Boolean {
        return cep.length == 8 && cep.all { it.isDigit() }
    }

    /**
     * Limpa o estado atual do endereço e da mensagem de erro.
     */
    fun clearAddress() {
        _address.value = null
        _errorMessage.value = null
    }
}