package br.com.thiagohora.lancamento.repository.query;


import br.com.thiagohora.lancamento.domain.Lancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface LancamentoRepositoryQuery {
    Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
}
