package br.com.thiagohora.lancamento.repository;

import br.com.thiagohora.lancamento.domain.Lancamento;
import br.com.thiagohora.infrastructure.jpa.repository.projection.JpaSpecificationExecutorWithProjection;
import br.com.thiagohora.lancamento.repository.query.LancamentoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>,
                                                LancamentoRepositoryQuery ,
                                                JpaSpecificationExecutorWithProjection<Lancamento> {
}
