package unl.edu.cc.soccersystem.controller;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import unl.edu.cc.soccersystem.domain.Encuentro;
import unl.edu.cc.soccersystem.domain.Equipo;
import unl.edu.cc.soccersystem.domain.Jugador;
import unl.edu.cc.soccersystem.facade.SoccerSystemFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

@Named("encuentroBean")
@SessionScoped
public class EncuentroBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private CampeonatoBean campeonatoBean;

    @Inject
    private SoccerSystemFacade facade;

    private Encuentro encuentro;
    private Equipo equipoA;
    private Equipo equipoB;
    private List<Jugador> jugadoresSeleccionadosA;
    private List<Jugador> jugadoresSeleccionadosB;
    private int minJugadores = 12;

    @PostConstruct
    public void init() {
        this.encuentro = new Encuentro();
        this.jugadoresSeleccionadosA = new ArrayList<>();
        this.jugadoresSeleccionadosB = new ArrayList<>();
    }

    public String iniciarEncuentro() {
        try {
            if (equipoA == null || equipoB == null) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error",
                        "Debe seleccionar ambos equipos");
                return null;
            }

            if (equipoA.getId().equals(equipoB.getId())) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error",
                        "Debe seleccionar equipos diferentes");
                return null;
            }

            if (jugadoresSeleccionadosA.size() < minJugadores) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error",
                        "El Equipo A debe tener al menos " + minJugadores + " jugadores seleccionados");
                return null;
            }

            if (jugadoresSeleccionadosB.size() < minJugadores) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error",
                        "El Equipo B debe tener al menos " + minJugadores + " jugadores seleccionados");
                return null;
            }

            // Crear encuentro usando el facade
            facade.crearEncuentro(
                    equipoA.getId(),
                    equipoB.getId(),
                    LocalDate.now(),
                    campeonatoBean.getCampeonatoSeleccionado().getId());

            addMessage(FacesMessage.SEVERITY_INFO, "Éxito",
                    "Encuentro creado exitosamente");

            // Limpiar selecciones
            equipoA = null;
            equipoB = null;
            jugadoresSeleccionadosA.clear();
            jugadoresSeleccionadosB.clear();

            return "detalle_campeonato?faces-redirect=true";
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Error al crear encuentro: " + e.getMessage());
            return null;
        }
    }

    public String volver() {
        equipoA = null;
        equipoB = null;
        jugadoresSeleccionadosA.clear();
        jugadoresSeleccionadosB.clear();
        return "detalle_campeonato?faces-redirect=true";
    }

    public List<Equipo> getEquiposDisponibles() {
        if (campeonatoBean.getCampeonatoSeleccionado() != null) {
            return campeonatoBean.getCampeonatoSeleccionado().getEquipos();
        }
        return new ArrayList<>();
    }

    public List<Jugador> getJugadoresEquipoA() {
        if (equipoA != null) {
            return equipoA.getJugadores();
        }
        return new ArrayList<>();
    }

    public List<Jugador> getJugadoresEquipoB() {
        if (equipoB != null) {
            return equipoB.getJugadores();
        }
        return new ArrayList<>();
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(severity, summary, detail));
    }

    // Getters y Setters
    public Encuentro getEncuentro() {
        return encuentro;
    }

    public void setEncuentro(Encuentro encuentro) {
        this.encuentro = encuentro;
    }

    public Equipo getEquipoA() {
        return equipoA;
    }

    public void setEquipoA(Equipo equipoA) {
        this.equipoA = equipoA;
        this.jugadoresSeleccionadosA.clear();
    }

    public Equipo getEquipoB() {
        return equipoB;
    }

    public void setEquipoB(Equipo equipoB) {
        this.equipoB = equipoB;
        this.jugadoresSeleccionadosB.clear();
    }

    public List<Jugador> getJugadoresSeleccionadosA() {
        return jugadoresSeleccionadosA;
    }

    public void setJugadoresSeleccionadosA(List<Jugador> jugadoresSeleccionadosA) {
        this.jugadoresSeleccionadosA = jugadoresSeleccionadosA;
    }

    public List<Jugador> getJugadoresSeleccionadosB() {
        return jugadoresSeleccionadosB;
    }

    public void setJugadoresSeleccionadosB(List<Jugador> jugadoresSeleccionadosB) {
        this.jugadoresSeleccionadosB = jugadoresSeleccionadosB;
    }

    public int getMinJugadores() {
        return minJugadores;
    }
}