package br.com.devquick.pagamento.repository;

import br.com.devquick.pagamento.domain.Curso;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Curso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {}
