package br.com.devquick.pagamento.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Cliente.
 */
@Entity
@Table(name = "cliente")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "email")
    private String email;

    @Column(name = "telefone")
    private String telefone;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnoreProperties(value = { "cliente" }, allowSetters = true)
    private Set<Curso> cursos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "clientes" }, allowSetters = true)
    private Parcela parcela;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente id(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Cliente nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return this.cpf;
    }

    public Cliente cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return this.email;
    }

    public Cliente email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public Cliente telefone(String telefone) {
        this.telefone = telefone;
        return this;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Set<Curso> getCursos() {
        return this.cursos;
    }

    public Cliente cursos(Set<Curso> cursos) {
        this.setCursos(cursos);
        return this;
    }

    public Cliente addCurso(Curso curso) {
        this.cursos.add(curso);
        curso.setCliente(this);
        return this;
    }

    public Cliente removeCurso(Curso curso) {
        this.cursos.remove(curso);
        curso.setCliente(null);
        return this;
    }

    public void setCursos(Set<Curso> cursos) {
        if (this.cursos != null) {
            this.cursos.forEach(i -> i.setCliente(null));
        }
        if (cursos != null) {
            cursos.forEach(i -> i.setCliente(this));
        }
        this.cursos = cursos;
    }

    public Parcela getParcela() {
        return this.parcela;
    }

    public Cliente parcela(Parcela parcela) {
        this.setParcela(parcela);
        return this;
    }

    public void setParcela(Parcela parcela) {
        this.parcela = parcela;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cliente)) {
            return false;
        }
        return id != null && id.equals(((Cliente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", cpf='" + getCpf() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefone='" + getTelefone() + "'" +
            "}";
    }
}
