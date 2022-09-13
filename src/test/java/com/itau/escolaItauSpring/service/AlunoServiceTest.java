package com.itau.escolaItauSpring.service;

import com.itau.escolaItauSpring.config.mapper.AlunoMapper;
import com.itau.escolaItauSpring.dto.request.AlunoRequest;
import com.itau.escolaItauSpring.dto.response.AlunoResponse;
import com.itau.escolaItauSpring.exception.ItemNaoExistenteException;
import com.itau.escolaItauSpring.model.Aluno;
import com.itau.escolaItauSpring.repository.AlunoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class AlunoServiceTest {

    @InjectMocks
    AlunoService alunoService;

    @Mock
    AlunoMapper alunoMapper;

    @Mock
    AlunoRepository alunoRepository;

    @Test
    void adicionarTeste() {
        Mockito.when(alunoMapper.toModel(ArgumentMatchers.any(AlunoRequest.class))).thenReturn(new Aluno());
        Mockito.when(alunoMapper.toResponse(ArgumentMatchers.any(Aluno.class))).thenReturn(new AlunoResponse());
        Mockito.when(alunoRepository.adicionar(ArgumentMatchers.any(Aluno.class))).thenReturn(new Aluno());
        Assertions.assertNotNull(alunoService.adicionar(buildAlunoRequest()));
    }

    @Test
    void ativarTeste() throws ItemNaoExistenteException {
        UUID uuid = UUID.randomUUID();
        Aluno novoAluno = new Aluno();
        novoAluno.setId(uuid);
        novoAluno.setAtivado(false);

        when(alunoRepository.localizar(uuid)).thenReturn(novoAluno);
        when(alunoRepository.alterar(uuid, novoAluno)).thenReturn(novoAluno);

        alunoService.ativar(uuid);
        verify(alunoRepository, times(1)).alterar(any(), any());
        assertTrue(novoAluno.getAtivado());
    }

    @Test
    void desativarTeste() throws ItemNaoExistenteException {
        UUID uuid = UUID.randomUUID();
        Aluno novoAluno = new Aluno();
        novoAluno.setId(uuid);
        novoAluno.setAtivado(true);

        when(alunoRepository.localizar(uuid)).thenReturn(novoAluno);
        when(alunoRepository.alterar(uuid, novoAluno)).thenReturn(novoAluno);
        alunoService.desativar(uuid);
        assertFalse(novoAluno.getAtivado());
        verify(alunoRepository, times(1)).alterar(uuid, novoAluno);
    }

    @Test
    void listarTeste() {

        List<Aluno> listaAlunos = List.of(new Aluno(), new Aluno());
        when(alunoRepository.listar()).thenReturn(listaAlunos);
        when(alunoMapper.mapAluno(listaAlunos)).thenReturn(List.of(new AlunoResponse(), new AlunoResponse()));

        assertEquals(2, alunoService.listar().size());
    }

    @Test
    void localizarTeste() throws ItemNaoExistenteException {
        UUID uuid = UUID.randomUUID();
        Aluno alunoEsperado = new Aluno();
        alunoEsperado.setId(uuid);

        when(alunoRepository.localizar(uuid)).thenReturn(alunoEsperado);
        when(alunoMapper.toResponse(alunoEsperado)).thenReturn(new AlunoResponse());
        assertNotNull(alunoService.localizar(uuid));
    }


    private AlunoRequest buildAlunoRequest() {
        return AlunoRequest.builder()
                .nome("genovaro")
                .cpf(987654321L)
                .idade(21)
                .build();
    }
}
