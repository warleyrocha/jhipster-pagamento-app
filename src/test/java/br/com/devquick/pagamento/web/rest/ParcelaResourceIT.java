package br.com.devquick.pagamento.web.rest;

import static br.com.devquick.pagamento.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.devquick.pagamento.IntegrationTest;
import br.com.devquick.pagamento.domain.Parcela;
import br.com.devquick.pagamento.domain.enumeration.Status;
import br.com.devquick.pagamento.repository.ParcelaRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ParcelaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParcelaResourceIT {

    private static final Long DEFAULT_DOCUMENTO = 1L;
    private static final Long UPDATED_DOCUMENTO = 2L;

    private static final String DEFAULT_ID_TRANSACAO = "AAAAAAAAAA";
    private static final String UPDATED_ID_TRANSACAO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(2);

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;

    private static final Integer DEFAULT_TOTAL = 1;
    private static final Integer UPDATED_TOTAL = 2;

    private static final Status DEFAULT_STATUS = Status.PENDENTE;
    private static final Status UPDATED_STATUS = Status.PAGO;

    private static final String ENTITY_API_URL = "/api/parcelas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParcelaRepository parcelaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParcelaMockMvc;

    private Parcela parcela;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parcela createEntity(EntityManager em) {
        Parcela parcela = new Parcela()
            .documento(DEFAULT_DOCUMENTO)
            .idTransacao(DEFAULT_ID_TRANSACAO)
            .valor(DEFAULT_VALOR)
            .numero(DEFAULT_NUMERO)
            .total(DEFAULT_TOTAL)
            .status(DEFAULT_STATUS);
        return parcela;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parcela createUpdatedEntity(EntityManager em) {
        Parcela parcela = new Parcela()
            .documento(UPDATED_DOCUMENTO)
            .idTransacao(UPDATED_ID_TRANSACAO)
            .valor(UPDATED_VALOR)
            .numero(UPDATED_NUMERO)
            .total(UPDATED_TOTAL)
            .status(UPDATED_STATUS);
        return parcela;
    }

    @BeforeEach
    public void initTest() {
        parcela = createEntity(em);
    }

    @Test
    @Transactional
    void createParcela() throws Exception {
        int databaseSizeBeforeCreate = parcelaRepository.findAll().size();
        // Create the Parcela
        restParcelaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parcela)))
            .andExpect(status().isCreated());

        // Validate the Parcela in the database
        List<Parcela> parcelaList = parcelaRepository.findAll();
        assertThat(parcelaList).hasSize(databaseSizeBeforeCreate + 1);
        Parcela testParcela = parcelaList.get(parcelaList.size() - 1);
        assertThat(testParcela.getDocumento()).isEqualTo(DEFAULT_DOCUMENTO);
        assertThat(testParcela.getIdTransacao()).isEqualTo(DEFAULT_ID_TRANSACAO);
        assertThat(testParcela.getValor()).isEqualByComparingTo(DEFAULT_VALOR);
        assertThat(testParcela.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testParcela.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testParcela.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createParcelaWithExistingId() throws Exception {
        // Create the Parcela with an existing ID
        parcela.setId(1L);

        int databaseSizeBeforeCreate = parcelaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParcelaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parcela)))
            .andExpect(status().isBadRequest());

        // Validate the Parcela in the database
        List<Parcela> parcelaList = parcelaRepository.findAll();
        assertThat(parcelaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllParcelas() throws Exception {
        // Initialize the database
        parcelaRepository.saveAndFlush(parcela);

        // Get all the parcelaList
        restParcelaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parcela.getId().intValue())))
            .andExpect(jsonPath("$.[*].documento").value(hasItem(DEFAULT_DOCUMENTO.intValue())))
            .andExpect(jsonPath("$.[*].idTransacao").value(hasItem(DEFAULT_ID_TRANSACAO)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getParcela() throws Exception {
        // Initialize the database
        parcelaRepository.saveAndFlush(parcela);

        // Get the parcela
        restParcelaMockMvc
            .perform(get(ENTITY_API_URL_ID, parcela.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parcela.getId().intValue()))
            .andExpect(jsonPath("$.documento").value(DEFAULT_DOCUMENTO.intValue()))
            .andExpect(jsonPath("$.idTransacao").value(DEFAULT_ID_TRANSACAO))
            .andExpect(jsonPath("$.valor").value(sameNumber(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingParcela() throws Exception {
        // Get the parcela
        restParcelaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewParcela() throws Exception {
        // Initialize the database
        parcelaRepository.saveAndFlush(parcela);

        int databaseSizeBeforeUpdate = parcelaRepository.findAll().size();

        // Update the parcela
        Parcela updatedParcela = parcelaRepository.findById(parcela.getId()).get();
        // Disconnect from session so that the updates on updatedParcela are not directly saved in db
        em.detach(updatedParcela);
        updatedParcela
            .documento(UPDATED_DOCUMENTO)
            .idTransacao(UPDATED_ID_TRANSACAO)
            .valor(UPDATED_VALOR)
            .numero(UPDATED_NUMERO)
            .total(UPDATED_TOTAL)
            .status(UPDATED_STATUS);

        restParcelaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedParcela.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedParcela))
            )
            .andExpect(status().isOk());

        // Validate the Parcela in the database
        List<Parcela> parcelaList = parcelaRepository.findAll();
        assertThat(parcelaList).hasSize(databaseSizeBeforeUpdate);
        Parcela testParcela = parcelaList.get(parcelaList.size() - 1);
        assertThat(testParcela.getDocumento()).isEqualTo(UPDATED_DOCUMENTO);
        assertThat(testParcela.getIdTransacao()).isEqualTo(UPDATED_ID_TRANSACAO);
        assertThat(testParcela.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testParcela.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testParcela.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testParcela.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingParcela() throws Exception {
        int databaseSizeBeforeUpdate = parcelaRepository.findAll().size();
        parcela.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParcelaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parcela.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parcela))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parcela in the database
        List<Parcela> parcelaList = parcelaRepository.findAll();
        assertThat(parcelaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParcela() throws Exception {
        int databaseSizeBeforeUpdate = parcelaRepository.findAll().size();
        parcela.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParcelaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parcela))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parcela in the database
        List<Parcela> parcelaList = parcelaRepository.findAll();
        assertThat(parcelaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParcela() throws Exception {
        int databaseSizeBeforeUpdate = parcelaRepository.findAll().size();
        parcela.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParcelaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parcela)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parcela in the database
        List<Parcela> parcelaList = parcelaRepository.findAll();
        assertThat(parcelaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParcelaWithPatch() throws Exception {
        // Initialize the database
        parcelaRepository.saveAndFlush(parcela);

        int databaseSizeBeforeUpdate = parcelaRepository.findAll().size();

        // Update the parcela using partial update
        Parcela partialUpdatedParcela = new Parcela();
        partialUpdatedParcela.setId(parcela.getId());

        partialUpdatedParcela.valor(UPDATED_VALOR).total(UPDATED_TOTAL).status(UPDATED_STATUS);

        restParcelaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParcela.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParcela))
            )
            .andExpect(status().isOk());

        // Validate the Parcela in the database
        List<Parcela> parcelaList = parcelaRepository.findAll();
        assertThat(parcelaList).hasSize(databaseSizeBeforeUpdate);
        Parcela testParcela = parcelaList.get(parcelaList.size() - 1);
        assertThat(testParcela.getDocumento()).isEqualTo(DEFAULT_DOCUMENTO);
        assertThat(testParcela.getIdTransacao()).isEqualTo(DEFAULT_ID_TRANSACAO);
        assertThat(testParcela.getValor()).isEqualByComparingTo(UPDATED_VALOR);
        assertThat(testParcela.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testParcela.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testParcela.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateParcelaWithPatch() throws Exception {
        // Initialize the database
        parcelaRepository.saveAndFlush(parcela);

        int databaseSizeBeforeUpdate = parcelaRepository.findAll().size();

        // Update the parcela using partial update
        Parcela partialUpdatedParcela = new Parcela();
        partialUpdatedParcela.setId(parcela.getId());

        partialUpdatedParcela
            .documento(UPDATED_DOCUMENTO)
            .idTransacao(UPDATED_ID_TRANSACAO)
            .valor(UPDATED_VALOR)
            .numero(UPDATED_NUMERO)
            .total(UPDATED_TOTAL)
            .status(UPDATED_STATUS);

        restParcelaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParcela.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParcela))
            )
            .andExpect(status().isOk());

        // Validate the Parcela in the database
        List<Parcela> parcelaList = parcelaRepository.findAll();
        assertThat(parcelaList).hasSize(databaseSizeBeforeUpdate);
        Parcela testParcela = parcelaList.get(parcelaList.size() - 1);
        assertThat(testParcela.getDocumento()).isEqualTo(UPDATED_DOCUMENTO);
        assertThat(testParcela.getIdTransacao()).isEqualTo(UPDATED_ID_TRANSACAO);
        assertThat(testParcela.getValor()).isEqualByComparingTo(UPDATED_VALOR);
        assertThat(testParcela.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testParcela.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testParcela.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingParcela() throws Exception {
        int databaseSizeBeforeUpdate = parcelaRepository.findAll().size();
        parcela.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParcelaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parcela.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parcela))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parcela in the database
        List<Parcela> parcelaList = parcelaRepository.findAll();
        assertThat(parcelaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParcela() throws Exception {
        int databaseSizeBeforeUpdate = parcelaRepository.findAll().size();
        parcela.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParcelaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parcela))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parcela in the database
        List<Parcela> parcelaList = parcelaRepository.findAll();
        assertThat(parcelaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParcela() throws Exception {
        int databaseSizeBeforeUpdate = parcelaRepository.findAll().size();
        parcela.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParcelaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(parcela)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parcela in the database
        List<Parcela> parcelaList = parcelaRepository.findAll();
        assertThat(parcelaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParcela() throws Exception {
        // Initialize the database
        parcelaRepository.saveAndFlush(parcela);

        int databaseSizeBeforeDelete = parcelaRepository.findAll().size();

        // Delete the parcela
        restParcelaMockMvc
            .perform(delete(ENTITY_API_URL_ID, parcela.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Parcela> parcelaList = parcelaRepository.findAll();
        assertThat(parcelaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
