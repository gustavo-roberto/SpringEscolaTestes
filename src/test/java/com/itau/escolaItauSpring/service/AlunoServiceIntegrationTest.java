package com.itau.escolaItauSpring.service;

import com.itau.escolaItauSpring.config.mapper.AlunoMapperImpl;
import com.itau.escolaItauSpring.dto.request.AlunoRequest;
import com.itau.escolaItauSpring.exception.ItemNaoExistenteException;
import com.itau.escolaItauSpring.repository.AlunoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AlunoService.class, AlunoRepository.class, AlunoMapperImpl.class})
class AlunoServiceIntegrationTest {

    @Autowired
    AlunoService alunoService;

    @Test
    void testListar_WhenOK() {
        alunoService.adicionar(buildAlunoRequest());
        alunoService.adicionar(buildAlunoRequest());
        var actual = alunoService.listar();
        Assertions.assertEquals(2, actual.size());
    }


    @Test
    void testAtivar_WhenOK() throws ItemNaoExistenteException {
        var expected = alunoService.adicionar(buildAlunoRequest());
        alunoService.desativar(expected.getId());
        alunoService.ativar(expected.getId());
        var actual = alunoService.localizar(expected.getId());
        Assertions.assertTrue(actual.getAtivado());

    }

    @Test
    void testDesativar_WhenOK() throws ItemNaoExistenteException {
        var expected = alunoService.adicionar(buildAlunoRequest());
        alunoService.ativar(expected.getId());
        alunoService.desativar(expected.getId());
        var actual = alunoService.localizar(expected.getId());
        Assertions.assertFalse(actual.getAtivado());

    }

    @Test
    void testAdicionarLocalizar_WhenOK() throws ItemNaoExistenteException {
        var expected = alunoService.adicionar(buildAlunoRequest());
        var actual = alunoService.localizar(expected.getId());
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getNome(), actual.getNome());
        Assertions.assertEquals(expected.getCpf(), actual.getCpf());
        Assertions.assertEquals(expected.getIdade(), actual.getIdade());

    }

    @Test
    void testLocalizar_WhenNotFound() {
        Assertions.assertThrows(ItemNaoExistenteException.class, () -> alunoService.localizar(UUID.randomUUID()));
    }



    private AlunoRequest buildAlunoRequest() {
        return AlunoRequest.builder()
                .nome("Aluno Teste")
                .cpf(5077788869L)
                .idade(20)
                .build();
    }


}