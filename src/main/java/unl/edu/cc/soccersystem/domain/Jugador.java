package unl.edu.cc.soccersystem.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

@Entity
@Table(name = "jugadores")
public class Jugador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del jugador es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El apellido del jugador es obligatorio")
    @Column(nullable = false, length = 100)
    private String apellido;

    @NotBlank(message = "La cédula es obligatoria")
    @Size(min = 10, max = 10, message = "La cédula debe tener exactamente 10 caracteres")
    @Column(nullable = false, unique = true, length = 10)
    private String cedula;

    @Min(value = 0, message = "El número de camisa debe ser mayor o igual a 0")
    @Max(value = 99, message = "El número de camisa debe ser menor o igual a 99")
    @Column(name = "numero_camisa", nullable = false)
    private int numeroCamisa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    public Jugador() {
    }

    public Jugador(int numeroCamisa) {
        this.numeroCamisa = numeroCamisa;
    }

    public Jugador(String nombre, String apellido, String cedula, int numeroCamisa) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.numeroCamisa = numeroCamisa;
    }

    public static void validarNumeroCamisa(int numeroCamisa) {
        if(numeroCamisa < 0 || numeroCamisa > 99){
            throw new IllegalArgumentException("El numero de camisa debe estar entre 0 y 99");
        }
    }

    public static void validarCedula(String cedula){
        if(cedula == null || cedula.length() != 10){
            throw new IllegalArgumentException("La cedula debe tener 10 caracteres");
        }
    }

    public void registrarSancion(Sancion sancion) {
        System.out.println("Sancion registrada para el jugador: " +getNombre() + "\n");
        System.out.println("Tipo: " +sancion.getTipoDeSancion() + "\n");
        System.out.println("Motivo: " +sancion.getMotivo().toUpperCase() + "\n");
        System.out.println("Minuto en el que se cometió la sancion: " +sancion.getMinuto() + "\n");
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public int getNumeroCamisa() {
        return numeroCamisa;
    }

    public void setNumeroCamisa(int numeroCamisa) {
        this.numeroCamisa = numeroCamisa;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    @Override
    public String toString() {
        return "Jugador: " + getNombre() + " " + getApellido() +
                " | Cédula: " + getCedula() +
                " | Nº Camisa: " + numeroCamisa;
    }
}