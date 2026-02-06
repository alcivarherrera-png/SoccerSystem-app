package unl.edu.cc.soccersystem.controller;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import unl.edu.cc.soccersystem.domain.Equipo;
import unl.edu.cc.soccersystem.facade.SoccerSystemFacade;

import java.io.Serializable;

@Named("equipoBean")
@SessionScoped
public class EquipoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private CampeonatoBean campeonatoBean;

    @Inject
    private SoccerSystemFacade facade;

    private Equipo equipo;
    private Equipo equipoSeleccionado;
    private boolean mostrarFormulario;

    @PostConstruct
    public void init() {
        this.equipo = new Equipo();
        this.mostrarFormulario = false;
    }

    /**
     * Crea un nuevo equipo
     */
    public String crearEquipo() {
        try {
            if (campeonatoBean.getCampeonatoSeleccionado() == null) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error",
                        "No hay campeonato seleccionado");
                return null;
            }

            if (equipo.getNombreEquipo() == null || equipo.getNombreEquipo().trim().isEmpty()) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error",
                        "El nombre del equipo es obligatorio");
                return null;
            }

            // Crear equipo usando el facade
            facade.crearEquipo(
                    equipo.getNombreEquipo(),
                    equipo.getColorUniforme(),
                    campeonatoBean.getCampeonatoSeleccionado().getId());

            addMessage(FacesMessage.SEVERITY_INFO, "Éxito",
                    "Equipo creado exitosamente");

            // Recargar campeonato con equipos
            campeonatoBean.setCampeonatoSeleccionado(
                    facade.obtenerCampeonatoConEquipos(
                            campeonatoBean.getCampeonatoSeleccionado().getId()));

            // Resetear formulario
            equipo = new Equipo();
            mostrarFormulario = false;

            return null;
        } catch (IllegalArgumentException e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error de validación", e.getMessage());
            return null;
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Error al crear equipo: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void prepararEditar(Equipo e) {
        this.equipoSeleccionado = e;
    }

    public void actualizarEquipo() {
        try {
            if (equipoSeleccionado == null) {
                return;
            }
            if (equipoSeleccionado.getNombreEquipo() == null
                    || equipoSeleccionado.getNombreEquipo().trim().isEmpty()) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error",
                        "El nombre del equipo es obligatorio");
                return;
            }

            facade.actualizarEquipo(equipoSeleccionado);
            campeonatoBean.setCampeonatoSeleccionado(
                    facade.obtenerCampeonatoConEquipos(
                            campeonatoBean.getCampeonatoSeleccionado().getId()));
            addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Equipo actualizado correctamente");
        } catch (Exception ex) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Error al actualizar equipo: " + ex.getMessage());
        }
    }

    /**
     * Selecciona un equipo y navega a la gestión de jugadores
     */
    public String seleccionarEquipo(Equipo e) {
        try {
            // Cargar equipo completo con jugadores
            this.equipoSeleccionado = facade.obtenerEquipoConJugadores(e.getId());
            return "jugadores?faces-redirect=true";
        } catch (Exception ex) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Error al cargar equipo: " + ex.getMessage());
            return null;
        }
    }

    /**
     * Elimina un equipo
     */
    public void eliminarEquipo(Equipo e) {
        try {
            facade.eliminarEquipo(e.getId());

            // Recargar campeonato
            campeonatoBean.setCampeonatoSeleccionado(
                    facade.obtenerCampeonatoConEquipos(
                            campeonatoBean.getCampeonatoSeleccionado().getId()));

            addMessage(FacesMessage.SEVERITY_INFO, "Éxito",
                    "Equipo eliminado exitosamente");
        } catch (Exception ex) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Error al eliminar equipo: " + ex.getMessage());
        }
    }

    /**
     * Cancela la creación de equipo
     */
    public void cancelar() {
        equipo = new Equipo();
        mostrarFormulario = false;
    }

    /**
     * Muestra el formulario para crear un nuevo equipo
     */
    public void mostrarFormularioNuevo() {
        equipo = new Equipo();
        mostrarFormulario = true;
    }

    /**
     * Vuelve a la pantalla de campeonatos
     */
    public String volverACampeonatos() {
        campeonatoBean.setCampeonatoSeleccionado(null);
        return "campeonatos?faces-redirect=true";
    }

    /**
     * Navega a la pantalla de creación de encuentros
     */
    public String irACrearEncuentro() {
        return "encuentro?faces-redirect=true";
    }

    /**
     * Agrega un mensaje al contexto de JSF
     */
    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(severity, summary, detail));
    }

    // Getters y Setters
    public Equipo getEquipo() {
        if (equipo == null) {
            equipo = new Equipo();
        }
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public boolean isMostrarFormulario() {
        return mostrarFormulario;
    }

    public void setMostrarFormulario(boolean mostrarFormulario) {
        this.mostrarFormulario = mostrarFormulario;
    }

    public Equipo getEquipoSeleccionado() {
        if (equipoSeleccionado == null) {
            equipoSeleccionado = new Equipo();
        }
        return equipoSeleccionado;
    }

    public void setEquipoSeleccionado(Equipo equipoSeleccionado) {
        this.equipoSeleccionado = equipoSeleccionado;
    }
}
