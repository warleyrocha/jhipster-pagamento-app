package br.com.devquick.pagamento.web.rest;

import br.com.devquick.pagamento.domain.Parcela;
import br.com.devquick.pagamento.repository.ParcelaRepository;
import br.com.devquick.pagamento.service.ParcelaService;
import br.com.devquick.pagamento.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.devquick.pagamento.domain.Parcela}.
 */
@RestController
@RequestMapping("/api")
public class ParcelaResource {

    private final Logger log = LoggerFactory.getLogger(ParcelaResource.class);

    private static final String ENTITY_NAME = "pagamentoApplicationParcela";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParcelaService parcelaService;

    private final ParcelaRepository parcelaRepository;

    public ParcelaResource(ParcelaService parcelaService, ParcelaRepository parcelaRepository) {
        this.parcelaService = parcelaService;
        this.parcelaRepository = parcelaRepository;
    }

    /**
     * {@code POST  /parcelas} : Create a new parcela.
     *
     * @param parcela the parcela to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parcela, or with status {@code 400 (Bad Request)} if the parcela has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parcelas")
    public ResponseEntity<Parcela> createParcela(@RequestBody Parcela parcela) throws URISyntaxException {
        log.debug("REST request to save Parcela : {}", parcela);
        if (parcela.getId() != null) {
            throw new BadRequestAlertException("A new parcela cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Parcela result = parcelaService.save(parcela);
        return ResponseEntity
            .created(new URI("/api/parcelas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parcelas/:id} : Updates an existing parcela.
     *
     * @param id the id of the parcela to save.
     * @param parcela the parcela to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parcela,
     * or with status {@code 400 (Bad Request)} if the parcela is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parcela couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parcelas/{id}")
    public ResponseEntity<Parcela> updateParcela(@PathVariable(value = "id", required = false) final Long id, @RequestBody Parcela parcela)
        throws URISyntaxException {
        log.debug("REST request to update Parcela : {}, {}", id, parcela);
        if (parcela.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parcela.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parcelaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Parcela result = parcelaService.save(parcela);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parcela.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /parcelas/:id} : Partial updates given fields of an existing parcela, field will ignore if it is null
     *
     * @param id the id of the parcela to save.
     * @param parcela the parcela to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parcela,
     * or with status {@code 400 (Bad Request)} if the parcela is not valid,
     * or with status {@code 404 (Not Found)} if the parcela is not found,
     * or with status {@code 500 (Internal Server Error)} if the parcela couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parcelas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Parcela> partialUpdateParcela(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Parcela parcela
    ) throws URISyntaxException {
        log.debug("REST request to partial update Parcela partially : {}, {}", id, parcela);
        if (parcela.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parcela.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parcelaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Parcela> result = parcelaService.partialUpdate(parcela);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parcela.getId().toString())
        );
    }

    /**
     * {@code GET  /parcelas} : get all the parcelas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parcelas in body.
     */
    @GetMapping("/parcelas")
    public ResponseEntity<List<Parcela>> getAllParcelas(Pageable pageable) {
        log.debug("REST request to get a page of Parcelas");
        Page<Parcela> page = parcelaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /parcelas/:id} : get the "id" parcela.
     *
     * @param id the id of the parcela to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parcela, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parcelas/{id}")
    public ResponseEntity<Parcela> getParcela(@PathVariable Long id) {
        log.debug("REST request to get Parcela : {}", id);
        Optional<Parcela> parcela = parcelaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parcela);
    }

    /**
     * {@code DELETE  /parcelas/:id} : delete the "id" parcela.
     *
     * @param id the id of the parcela to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parcelas/{id}")
    public ResponseEntity<Void> deleteParcela(@PathVariable Long id) {
        log.debug("REST request to delete Parcela : {}", id);
        parcelaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
