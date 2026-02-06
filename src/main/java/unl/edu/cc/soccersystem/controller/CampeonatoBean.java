package unl.edu.cc.soccersystem.controller;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import unl.edu.cc.soccersystem.domain.Campeonato;
import unl.edu.cc.soccersystem.facade.SoccerSystemFacade;

import unl.edu.cc.soccersystem.domain.Encuentro;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Named("campeonatoBean")
@SessionScoped
public class CampeonatoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @jakarta.inject.Inject
    private SoccerSystemFacade facade;

    private Campeonato campeonato;
    private Campeonato campeonatoSeleccionado;
    private List<Campeonato> campeonatos;
    private List<Encuentro> encuentrosDetalle;
    private boolean mostrarFormulario;

    @PostConstruct
    public void init() {
        this.campeonato = new Campeonato();
        this.mostrarFormulario = false;
        this.encuentrosDetalle = new ArrayList<>();
        cargarCampeonatos();
    }

    /**
     * Carga todos los campeonatos desde la base de datos
     */
    public void cargarCampeonatos() {
        try {
            this.campeonatos = facade.obtenerTodosCampeonatos();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Error al cargar campeonatos: " + e.getMessage());
        }
    }

    /**
     * Crea un nuevo campeonato
     */
    public String crearCampeonato() {
        try {
            if (campeonato.getNombre() == null || campeonato.getNombre().trim().isEmpty()) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error",
                        "El nombre del campeonato es obligatorio");
                return null;
            }

            if (campeonato.getFechaInicio() == null) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error",
                        "La fecha de inicio es obligatoria");
                return null;
            }

            // Crear campeonato usando el facade
            facade.crearCampeonato(campeonato.getNombre(), campeonato.getFechaInicio());

            addMessage(FacesMessage.SEVERITY_INFO, "Éxito",
                    "Campeonato creado exitosamente");

            // Recargar lista
            cargarCampeonatos();

            // Resetear formulario
            campeonato = new Campeonato();
            mostrarFormulario = false;

            return null;
        } catch (IllegalArgumentException e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error de validación", e.getMessage());
            return null;
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Error al crear campeonato: " + e.getMessage());
            return null;
        }
    }

    /**
     * Selecciona un campeonato y navega a la gestión de equipos
     */
    /**
     * Selecciona un campeonato y navega a la vista de detalles
     */
    public String verDetalleCampeonato(Campeonato c) {
        try {
            // Cargar campeonato completo con equipos
            this.campeonatoSeleccionado = facade.obtenerCampeonatoConEquipos(c.getId());
            // Cargar encuentros del campeonato
            this.encuentrosDetalle = facade.obtenerEncuentrosPorCampeonato(c.getId());
            return "detalle_campeonato?faces-redirect=true";
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Error al cargar detalle de campeonato: " + e.getMessage());
            return null;
        }
    }

    /**
     * Actualiza el campeonato seleccionado
     */
    public void actualizarCampeonato() {
        try {
            if (campeonatoSeleccionado == null) {
                return;
            }
            facade.actualizarCampeonato(campeonatoSeleccionado);
            campeonatoSeleccionado = facade.obtenerCampeonatoConEquipos(campeonatoSeleccionado.getId());
            cargarCampeonatos();
            addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Campeonato actualizado correctamente");
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al actualizar: " + e.getMessage());
        }
    }

    /**
     * Elimina un campeonato
     */
    public String eliminarCampeonato(Campeonato c) {
        try {
            facade.eliminarCampeonato(c.getId());
            addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Campeonato eliminado");
            cargarCampeonatos(); // Recargar lista
            return "campeonatos?faces-redirect=true"; // Volver a la lista
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al eliminar: " + e.getMessage());
            return null;
        }
    }

    /**
     * Elimina el campeonato seleccionado actualmente desde el detalle
     */
    public String eliminarCampeonatoActual() {
        if (campeonatoSeleccionado != null) {
            return eliminarCampeonato(campeonatoSeleccionado);
        }
        return null;
    }

    /**
     * Cancela la creación de campeonato
     */
    public void cancelar() {
        campeonato = new Campeonato();
        mostrarFormulario = false;
    }

    /**
     * Muestra el formulario para crear un nuevo campeonato
     */
    public void mostrarFormularioNuevo() {
        campeonato = new Campeonato();
        mostrarFormulario = true;
    }

    /**
     * Agrega un mensaje al contexto de JSF
     */
    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(severity, summary, detail));
    }

    // Getters y Setters
    public Campeonato getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
    }

    public List<Campeonato> getCampeonatos() {
        if (campeonatos == null) {
            cargarCampeonatos();
        }
        return campeonatos;
    }

    public void setCampeonatos(List<Campeonato> campeonatos) {
        this.campeonatos = campeonatos;
    }

    public boolean isMostrarFormulario() {
        return mostrarFormulario;
    }

    public void setMostrarFormulario(boolean mostrarFormulario) {
        this.mostrarFormulario = mostrarFormulario;
    }

    public Campeonato getCampeonatoSeleccionado() {
        return campeonatoSeleccionado;
    }

    public void setCampeonatoSeleccionado(Campeonato campeonatoSeleccionado) {
        this.campeonatoSeleccionado = campeonatoSeleccionado;
    }

    public List<Encuentro> getEncuentrosDetalle() {
        return encuentrosDetalle;
    }

    public void setEncuentrosDetalle(List<Encuentro> encuentrosDetalle) {
        this.encuentrosDetalle = encuentrosDetalle;
    }
}
