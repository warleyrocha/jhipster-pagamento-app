package br.com.devquick.pagamento.service;

import br.com.devquick.pagamento.domain.Parcela;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Parcela}.
 */
public interface ParcelaService {
    /**
     * Save a parcela.
     *
     * @param parcela the entity to save.
     * @return the persisted entity.
     */
    Parcela save(Parcela parcela);

    /**
     * Partially updates a parcela.
     *
     * @param parcela the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Parcela> partialUpdate(Parcela parcela);

    /**
     * Get all the parcelas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Parcela> findAll(Pageable pageable);

    /**
     * Get the "id" parcela.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Parcela> findOne(Long id);

    /**
     * Delete the "id" parcela.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
