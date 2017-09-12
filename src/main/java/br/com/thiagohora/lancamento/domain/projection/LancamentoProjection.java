package br.com.thiagohora.lancamento.domain.projection;

import br.com.thiagohora.lancamento.domain.TipoLancamento;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface LancamentoProjection {

    Long getCodigo();
    String getDescricao();
    LocalDate getDataVencimento();
    LocalDate getDataPagamento();
    BigDecimal getValor();
    TipoLancamento getTipo();

    @Value("#{target.categoria.nome}")
    String getCategoria();

    @Value("#{target.pessoa.nome}")
    String getPessoa();

}
