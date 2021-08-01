package br.com.devquick.pagamento.web.rest;

import br.com.devquick.pagamento.domain.Curso;
import br.com.devquick.pagamento.repository.CursoRepository;
import br.com.devquick.pagamento.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.devquick.pagamento.domain.Curso}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CursoResource {

    private final Logger log = LoggerFactory.getLogger(CursoResource.class);

    private static final String ENTITY_NAME = "pagamentoApplicationCurso";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CursoRepository cursoRepository;

    public CursoResource(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    /**
     * {@code POST  /cursos} : Create a new curso.
     *
     * @param curso the curso to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new curso, or with status {@code 400 (Bad Request)} if the curso has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cursos")
    public ResponseEntity<Curso> createCurso(@RequestBody Curso curso) throws URISyntaxException {
        log.debug("REST request to save Curso : {}", curso);
        if (curso.getId() != null) {
            throw new BadRequestAlertException("A new curso cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Curso result = cursoRepository.save(curso);
        return ResponseEntity
            .created(new URI("/api/cursos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cursos/:id} : Updates an existing curso.
     *
     * @param id the id of the curso to save.
     * @param curso the curso to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated curso,
     * or with status {@code 400 (Bad Request)} if the curso is not valid,
     * or with status {@code 500 (Internal Server Error)} if the curso couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cursos/{id}")
    public ResponseEntity<Curso> updateCurso(@PathVariable(value = "id", required = false) final Long id, @RequestBody Curso curso)
        throws URISyntaxException {
        log.debug("REST request to update Curso : {}, {}", id, curso);
        if (curso.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, curso.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cursoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Curso result = cursoRepository.save(curso);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, curso.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cursos/:id} : Partial updates given fields of an existing curso, field will ignore if it is null
     *
     * @param id the id of the curso to save.
     * @param curso the curso to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated curso,
     * or with status {@code 400 (Bad Request)} if the curso is not valid,
     * or with status {@code 404 (Not Found)} if the curso is not found,
     * or with status {@code 500 (Internal Server Error)} if the curso couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cursos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Curso> partialUpdateCurso(@PathVariable(value = "id", required = false) final Long id, @RequestBody Curso curso)
        throws URISyntaxException {
        log.debug("REST request to partial update Curso partially : {}, {}", id, curso);
        if (curso.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, curso.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cursoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Curso> result = cursoRepository
            .findById(curso.getId())
            .map(
                existingCurso -> {
                    if (curso.getNome() != null) {
                        existingCurso.setNome(curso.getNome());
                    }

                    return existingCurso;
                }
            )
            .map(cursoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, curso.getId().toString())
        );
    }

    /**
     * {@code GET  /cursos} : get all the cursos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cursos in body.
     */
    @GetMapping("/cursos")
    public List<Curso> getAllCursos() {
        log.debug("REST request to get all Cursos");
        return cursoRepository.findAll();
    }

    /**
     * {@code GET  /cursos/:id} : get the "id" curso.
     *
     * @param id the id of the curso to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the curso, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cursos/{id}")
    public ResponseEntity<Curso> getCurso(@PathVariable Long id) {
        log.debug("REST request to get Curso : {}", id);
        Optional<Curso> curso = cursoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(curso);
    }

    /**
     * {@code DELETE  /cursos/:id} : delete the "id" curso.
     *
     * @param id the id of the curso to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cursos/{id}")
    public ResponseEntity<Void> deleteCurso(@PathVariable Long id) {
        log.debug("REST request to delete Curso : {}", id);
        cursoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
