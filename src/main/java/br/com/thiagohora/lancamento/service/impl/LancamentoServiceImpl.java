package br.com.thiagohora.lancamento.service.impl;

import br.com.thiagohora.lancamento.domain.Lancamento;
import br.com.thiagohora.lancamento.domain.Lancamento_;
import br.com.thiagohora.lancamento.domain.projection.LancamentoProjection;
import br.com.thiagohora.lancamento.repository.LancamentoRepository;
import br.com.thiagohora.lancamento.repository.query.LancamentoFilter;
import br.com.thiagohora.lancamento.service.LancamentoService;
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

    @Autowired
    public LancamentoServiceImpl(LancamentoRepository repository) {
        this.repository = repository;
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


}
