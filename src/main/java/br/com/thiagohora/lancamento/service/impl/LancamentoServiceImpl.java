package br.com.thiagohora.lancamento.service.impl;

import br.com.thiagohora.lancamento.domain.Lancamento;
import br.com.thiagohora.lancamento.domain.Lancamento_;
import br.com.thiagohora.lancamento.domain.projection.LancamentoProjection;
import br.com.thiagohora.lancamento.repository.LancamentoRepository;
import br.com.thiagohora.lancamento.repository.query.LancamentoFilter;
import br.com.thiagohora.lancamento.service.LancamentoService;
import br.com.thiagohora.pessoa.domain.Pessoa;
import br.com.thiagohora.pessoa.service.PessoaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Service
public class LancamentoServiceImpl implements LancamentoService {

    private final LancamentoRepository repository;
    private final PessoaService pessoaService;

    @Autowired
    public LancamentoServiceImpl(LancamentoRepository repository, PessoaService pessoaService) {
        this.repository = repository;
        this.pessoaService = pessoaService;
    }

    @Override
    public Lancamento salvar(Lancamento lancamento) {
        return repository.save(lancamento);
    }

    public Page findAll(LancamentoFilter lancamentoFilter, Pageable pageable) {

        final Queue<Specification<Lancamento>> predicates = new LinkedList<>();

        if(lancamentoFilter.getDataVencimentoAte()!=null)
            predicates.add((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoAte()));

        if(lancamentoFilter.getDataVencimentoDe()!=null)
            predicates.add((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoDe()));

        if(lancamentoFilter.getDescricao()!=null)
            predicates.add((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(Lancamento_.descricao), "%"+lancamentoFilter.getDescricao()+"%"));

        final Specifications<Lancamento> specification = predicates.stream()
                                                            .map(Specifications::where)
                                                            .reduce((where, clause) -> where.and(clause))
                                                            .orElse(null);

        return repository.findAll(specification, LancamentoProjection.class, pageable, Lancamento.GRAPH, FETCH);
    }

    private void validarPessoa(Lancamento lancamento) {
        Pessoa pessoa = null;
        if (lancamento.getPessoa().getCodigo() != null) {
            pessoa = pessoaService.buscarPessoaPeloCodigo(lancamento.getPessoa().getCodigo());
        }

        if (pessoa == null || pessoa.isInativo()) {
            throw new IllegalStateException();
        }
    }

    @Override
    public Lancamento update(Long codigo, Lancamento lancamento) {
        Lancamento lancamentoSalvo = repository.findOne(codigo);
        if (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
            validarPessoa(lancamento);
        }

        BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");

        return repository.save(lancamentoSalvo);
    }


}
