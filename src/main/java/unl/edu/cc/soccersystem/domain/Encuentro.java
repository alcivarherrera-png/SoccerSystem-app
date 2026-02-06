package unl.edu.cc.soccersystem.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "encuentros")
public class Encuentro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(length = 50)
    private String arbitro;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipo_visitante_id")
    private Equipo visitante;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipo_local_id")
    private Equipo local;

    @Column(length = 10)
    private String hora;

    @Column(name = "duracion")
    private Integer duracion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campeonato_id")
    private Campeonato campeonato;

    public Encuentro() {
        this.fecha = LocalDate.now();
    }

    public Encuentro(String idEncuentro, LocalDate date) {
    }

    public Encuentro(LocalDate fecha, String arbitro, Equipo visitante, Equipo local,
            String hora, Integer duracion) {
        this.fecha = fecha;
        this.arbitro = arbitro;
        this.visitante = visitante;
        this.local = local;
        this.hora = hora;
        this.duracion = duracion;
    }

    public Encuentro(LocalDate fecha) {
        this.fecha = fecha;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Equipo getLocal() {
        return local;
    }

    public void setLocal(Equipo local) {
        this.local = local;
    }

    public Equipo getVisitante() {
        return visitante;
    }

    public void setVisitante(Equipo visitante) {
        this.visitante = visitante;
    }

    public String getArbitro() {
        return arbitro;
    }

    public void setArbitro(String arbitro) {
        this.arbitro = arbitro;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Campeonato getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
    }

    @Override
    public String toString() {
        return "Encuentro{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", arbitro='" + arbitro + '\'' +
                ", visitante=" + (visitante != null ? visitante.getNombreEquipo() : "null") +
                ", local=" + (local != null ? local.getNombreEquipo() : "null") +
                '}';
    }
}