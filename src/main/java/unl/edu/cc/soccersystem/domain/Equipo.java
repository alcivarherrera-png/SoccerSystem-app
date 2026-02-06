package unl.edu.cc.soccersystem.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipos")
public class Equipo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del equipo es obligatorio")
    @Column(name = "nombre_equipo", nullable = false, length = 100)
    private String nombreEquipo;

    @Column(name = "color_uniforme", length = 50)
    private String colorUniforme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campeonato_id")
    private Campeonato campeonato;

    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Jugador> jugadores;

    public Equipo() {
        this.jugadores = new ArrayList<>();
    }

    public Equipo(String nombreEquipo, String colorUniforme) {
        this.nombreEquipo = nombreEquipo;
        this.colorUniforme = colorUniforme;
        this.jugadores = new ArrayList<>();
    }

    public void agregarJugador(Jugador jugador) {
        if (jugadores == null) {
            jugadores = new ArrayList<>();
        }
        for (Jugador j : jugadores){
            if (j.getCedula() != null && j.getCedula().equals(jugador.getCedula())){
                throw new IllegalArgumentException("Ya existe un jugador con esa cédula");
            }
            if (j.getNumeroCamisa() == jugador.getNumeroCamisa()){
                throw new IllegalArgumentException("Ya existe un jugador con ese número de camisa");
            }
        }
        jugadores.add(jugador);
        jugador.setEquipo(this);
        System.out.println("Jugador registrado correctamente");
    }

    public void mostrarJugadores(){
        System.out.println("Jugadores del equipo:" +nombreEquipo + ":");
        if (jugadores != null) {
            for (Jugador j : jugadores){
                System.out.println(j);
            }
        }
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getColorUniforme() {
        return colorUniforme;
    }

    public void setColorUniforme(String colorUniforme) {
        this.colorUniforme = colorUniforme;
    }

    public Campeonato getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
    }

    public List<Jugador> getJugadores() {
        if (jugadores == null) {
            jugadores = new ArrayList<>();
        }
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public int getCantidadJugadores() {
        return jugadores != null ? jugadores.size() : 0;
    }

    @Override
    public String toString() {
        return "Equipo{" +
                "id=" + id +
                ", nombreEquipo='" + nombreEquipo + '\'' +
                ", colorUniforme='" + colorUniforme + '\'' +
                '}';
    }
}