package br.com.budgets.data.remote

import br.com.budgets.data.remote.dto.AddressResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepService {
    /**
     * Endpoint para buscar endereço pelo CEP.
     * @param cep O CEP a ser consultado (somente números).
     * @return Um objeto AddressResponse contendo os dados do endereço.
     */
    @GET("{cep}/json/")
    suspend fun getAddressByCep(@Path("cep") cep: String): AddressResponse
}