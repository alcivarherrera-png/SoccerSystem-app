package unl.edu.cc.soccersystem.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "campeonatos")
public class Campeonato implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del campeonato es obligatorio")
    @Column(nullable = false, unique = true, length = 200)
    private String nombre;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @OneToMany(mappedBy = "campeonato", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Equipo> equipos;

    @OneToMany(mappedBy = "campeonato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Encuentro> encuentros;

    public Campeonato() {
        this.equipos = new ArrayList<>();
        this.encuentros = new ArrayList<>();
    }

    public Campeonato(String nombre, LocalDate fechaInicio) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.equipos = new ArrayList<>();
        this.encuentros = new ArrayList<>();
    }

    public void agregarEquipo(Equipo equipo) {
        if (equipos == null) {
            equipos = new ArrayList<>();
        }
        // Validar que no exista un equipo con el mismo nombre
        for (Equipo e : equipos) {
            if (e.getNombreEquipo().equalsIgnoreCase(equipo.getNombreEquipo())) {
                throw new IllegalArgumentException("Ya existe un equipo con ese nombre");
            }
        }
        equipos.add(equipo);
        equipo.setCampeonato(this);
    }

    public void eliminarEquipo(Equipo equipo) {
        equipos.remove(equipo);
        equipo.setCampeonato(null);
    }

    public void agregarEncuentro(Encuentro encuentro) {
        if (encuentros == null) {
            encuentros = new ArrayList<>();
        }
        encuentros.add(encuentro);
        encuentro.setCampeonato(this);
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public List<Equipo> getEquipos() {
        if (equipos == null) {
            equipos = new ArrayList<>();
        }
        return equipos;
    }

    public void setEquipos(List<Equipo> equipos) {
        this.equipos = equipos;
    }

    public List<Encuentro> getEncuentros() {
        if (encuentros == null) {
            encuentros = new ArrayList<>();
        }
        return encuentros;
    }

    public void setEncuentros(List<Encuentro> encuentros) {
        this.encuentros = encuentros;
    }

    public int getCantidadEquipos() {
        return equipos != null ? equipos.size() : 0;
    }

    @Override
    public String toString() {
        return "Campeonato{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", equipos=" + (equipos != null ? equipos.size() : 0) +
                '}';
    }
}