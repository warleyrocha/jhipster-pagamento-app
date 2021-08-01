package br.com.devquick.pagamento.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.devquick.pagamento.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParcelaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parcela.class);
        Parcela parcela1 = new Parcela();
        parcela1.setId(1L);
        Parcela parcela2 = new Parcela();
        parcela2.setId(parcela1.getId());
        assertThat(parcela1).isEqualTo(parcela2);
        parcela2.setId(2L);
        assertThat(parcela1).isNotEqualTo(parcela2);
        parcela1.setId(null);
        assertThat(parcela1).isNotEqualTo(parcela2);
    }
}
