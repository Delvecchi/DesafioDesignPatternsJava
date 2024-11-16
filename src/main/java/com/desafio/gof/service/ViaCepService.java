package com.desafio.gof.service;

import com.desafio.gof.model.EnderecoModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viacep", url = "https://viacep.com.br/ws")
public interface ViaCepService {

    @GetMapping("/{cep}/json/")
    EnderecoModel consultarCep(@PathVariable("cep") String cep);
}
