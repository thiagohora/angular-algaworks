package br.com.thiagohora.lancamento.service;

import br.com.thiagohora.lancamento.domain.Lancamento;
import br.com.thiagohora.lancamento.repository.query.LancamentoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LancamentoService {
    Lancamento salvar(final Lancamento lancamento);
    Page findAll(final LancamentoFilter lancamentoFilter, final Pageable pageable);
    Lancamento update(final Long codigo, Lancamento lancamento);
}
