package br.com.avaliacao.backend.controller;

import br.com.avaliacao.backend.service.BusinessException;
import br.com.avaliacao.backend.service.CustomerService;
import br.com.avaliacao.backend.service.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerHttpTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    void deveRetornar404QuandoClienteNaoExistir() throws Exception {
        when(customerService.findById(99L))
                .thenThrow(new NotFoundException("Cliente nao encontrado."));

        mockMvc.perform(get("/api/customers/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cliente nao encontrado."));
    }

    @Test
    void deveRetornar400QuandoOcorrerErroDeNegocio() throws Exception {
        when(customerService.withdraw(1L, BigDecimal.valueOf(500)))
                .thenThrow(new BusinessException("Saldo insuficiente."));

        mockMvc.perform(post("/api/customers/1/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": 500
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Saldo insuficiente."));
    }

    @Test
    void deveRetornar400QuandoPayloadForInvalido() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Eduardo",
                                  "email": 
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}