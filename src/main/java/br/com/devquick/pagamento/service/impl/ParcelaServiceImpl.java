package br.com.devquick.pagamento.service.impl;

import br.com.devquick.pagamento.domain.Parcela;
import br.com.devquick.pagamento.repository.ParcelaRepository;
import br.com.devquick.pagamento.service.ParcelaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Parcela}.
 */
@Service
@Transactional
public class ParcelaServiceImpl implements ParcelaService {

    private final Logger log = LoggerFactory.getLogger(ParcelaServiceImpl.class);

    private final ParcelaRepository parcelaRepository;

    public ParcelaServiceImpl(ParcelaRepository parcelaRepository) {
        this.parcelaRepository = parcelaRepository;
    }

    @Override
    public Parcela save(Parcela parcela) {
        log.debug("Request to save Parcela : {}", parcela);
        return parcelaRepository.save(parcela);
    }

    @Override
    public Optional<Parcela> partialUpdate(Parcela parcela) {
        log.debug("Request to partially update Parcela : {}", parcela);

        return parcelaRepository
            .findById(parcela.getId())
            .map(
                existingParcela -> {
                    if (parcela.getDocumento() != null) {
                        existingParcela.setDocumento(parcela.getDocumento());
                    }
                    if (parcela.getIdTransacao() != null) {
                        existingParcela.setIdTransacao(parcela.getIdTransacao());
                    }
                    if (parcela.getValor() != null) {
                        existingParcela.setValor(parcela.getValor());
                    }
                    if (parcela.getNumero() != null) {
                        existingParcela.setNumero(parcela.getNumero());
                    }
                    if (parcela.getTotal() != null) {
                        existingParcela.setTotal(parcela.getTotal());
                    }
                    if (parcela.getStatus() != null) {
                        existingParcela.setStatus(parcela.getStatus());
                    }

                    return existingParcela;
                }
            )
            .map(parcelaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Parcela> findAll(Pageable pageable) {
        log.debug("Request to get all Parcelas");
        return parcelaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Parcela> findOne(Long id) {
        log.debug("Request to get Parcela : {}", id);
        return parcelaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Parcela : {}", id);
        parcelaRepository.deleteById(id);
    }
}
