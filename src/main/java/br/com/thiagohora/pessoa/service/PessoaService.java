package br.com.thiagohora.pessoa.service;

import br.com.thiagohora.pessoa.domain.Pessoa;

public interface PessoaService {
    Pessoa atualizar(Long codigo, Pessoa pessoa);
    void atualizarPropriedadeAtivo(Long codigo, Boolean ativo);
    Pessoa buscarPessoaPeloCodigo(Long codigo);
}
