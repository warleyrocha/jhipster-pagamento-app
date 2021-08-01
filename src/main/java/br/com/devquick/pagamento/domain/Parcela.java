package br.com.devquick.pagamento.domain;

import br.com.devquick.pagamento.domain.enumeration.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;

/**
 * A Parcela.
 */
@Entity
@Table(name = "parcela")
public class Parcela implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "documento")
    private Long documento;

    @Column(name = "id_transacao")
    private String idTransacao;

    @Column(name = "valor", precision = 21, scale = 2)
    private BigDecimal valor;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "total")
    private Integer total;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parcelas", "curso" }, allowSetters = true)
    private Cliente cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Parcela id(Long id) {
        this.id = id;
        return this;
    }

    public Long getDocumento() {
        return this.documento;
    }

    public Parcela documento(Long documento) {
        this.documento = documento;
        return this;
    }

    public void setDocumento(Long documento) {
        this.documento = documento;
    }

    public String getIdTransacao() {
        return this.idTransacao;
    }

    public Parcela idTransacao(String idTransacao) {
        this.idTransacao = idTransacao;
        return this;
    }

    public void setIdTransacao(String idTransacao) {
        this.idTransacao = idTransacao;
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public Parcela valor(BigDecimal valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getNumero() {
        return this.numero;
    }

    public Parcela numero(Integer numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getTotal() {
        return this.total;
    }

    public Parcela total(Integer total) {
        this.total = total;
        return this;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Status getStatus() {
        return this.status;
    }

    public Parcela status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public Parcela cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parcela)) {
            return false;
        }
        return id != null && id.equals(((Parcela) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Parcela{" +
            "id=" + getId() +
            ", documento=" + getDocumento() +
            ", idTransacao='" + getIdTransacao() + "'" +
            ", valor=" + getValor() +
            ", numero=" + getNumero() +
            ", total=" + getTotal() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
