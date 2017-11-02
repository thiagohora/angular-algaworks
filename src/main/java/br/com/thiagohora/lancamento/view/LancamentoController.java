package br.com.thiagohora.lancamento.view;

import br.com.thiagohora.infrastructure.event.CreateEvent;
import br.com.thiagohora.lancamento.domain.projection.LancamentoProjection;
import br.com.thiagohora.lancamento.domain.Lancamento;
import br.com.thiagohora.lancamento.repository.LancamentoRepository;
import br.com.thiagohora.lancamento.repository.query.LancamentoFilter;
import br.com.thiagohora.lancamento.service.LancamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {

    private final LancamentoRepository lancamentoRepository;
    private final LancamentoService lancamentoService;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public LancamentoController(final LancamentoRepository lancamentoRepository,
                              final LancamentoService lancamentoService,
                              final ApplicationEventPublisher publisher) {
        this.lancamentoRepository = lancamentoRepository;
        this.lancamentoService = lancamentoService;
        this.publisher = publisher;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {
        return lancamentoRepository.filtrar(lancamentoFilter, pageable);
    }

    @GetMapping(params = "resumo")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public Page<LancamentoProjection> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
        return lancamentoService.findAll(lancamentoFilter, pageable);
    }

    @GetMapping("/{codigo}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {
        Lancamento lancamento = lancamentoRepository.findOne(codigo);
        return lancamento != null ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
    public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
        Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
        publisher.publishEvent(new CreateEvent<>(this, lancamentoSalvo.getCodigo(), response));
        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
    }

    /* @ExceptionHandler({ PessoaInexistenteOuInativaException.class })
    public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {
        String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
        String mensagemDesenvolvedor = ex.toString();
        List<DefaultExceptionHandler.Erro> erros = Arrays.asList(new DefaultExceptionHandler.Erro(mensagemUsuario, mensagemDesenvolvedor));
        return ResponseEntity.badRequest().body(erros);
    }*/

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('write')")
    public void remover(@PathVariable Long codigo) {
        lancamentoRepository.delete(codigo);
    }

    @PutMapping("/{codigo}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
    public ResponseEntity<Lancamento> atualizar(@PathVariable Long codigo, @Valid @RequestBody Lancamento lancamento) {
        try {
            Lancamento lancamentoSalvo = lancamentoService.update(codigo, lancamento);
            return ResponseEntity.ok(lancamentoSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

}