package br.com.devquick.pagamento.repository;

import br.com.devquick.pagamento.domain.Parcela;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Parcela entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParcelaRepository extends JpaRepository<Parcela, Long> {}
