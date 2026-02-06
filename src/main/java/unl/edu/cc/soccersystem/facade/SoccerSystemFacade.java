package unl.edu.cc.soccersystem.facade;

import unl.edu.cc.soccersystem.repository.*;
import unl.edu.cc.soccersystem.domain.*;
import unl.edu.cc.soccersystem.repository.CampeonatoRepository;
import unl.edu.cc.soccersystem.repository.EncuentroRepository;
import unl.edu.cc.soccersystem.repository.EquipoRepository;
import unl.edu.cc.soccersystem.repository.JugadorRepository;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@jakarta.enterprise.context.ApplicationScoped
public class SoccerSystemFacade implements Serializable {

    private static final long serialVersionUID = 1L;

    @jakarta.inject.Inject
    private CampeonatoRepository campeonatoDAO;

    @jakarta.inject.Inject
    private EquipoRepository equipoDAO;

    @jakarta.inject.Inject
    private JugadorRepository jugadorDAO;

    @jakarta.inject.Inject
    private EncuentroRepository encuentroDAO;

    public SoccerSystemFacade() {
    }

    /**
     * Crea un nuevo campeonato
     */
    public Campeonato crearCampeonato(String nombre, LocalDate fechaInicio) {
        // Validar datos
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del campeonato es obligatorio");
        }
        if (fechaInicio == null) {
            throw new IllegalArgumentException("La fecha de inicio es obligatoria");
        }

        Campeonato existente = campeonatoDAO.findByNombre(nombre);
        if (existente != null) {
            throw new IllegalArgumentException("Ya existe un campeonato con ese nombre");
        }

        Campeonato campeonato = new Campeonato(nombre, fechaInicio);
        return campeonatoDAO.save(campeonato);
    }

    /**
     * Obtiene todos los campeonatos
     */
    public List<Campeonato> obtenerTodosCampeonatos() {
        return campeonatoDAO.findAll();
    }

    /**
     * Obtiene un campeonato por ID
     */
    public Campeonato obtenerCampeonatoPorId(Long id) {
        return campeonatoDAO.findById(id);
    }

    /**
     * Obtiene un campeonato con todos sus equipos
     */
    public Campeonato obtenerCampeonatoConEquipos(Long id) {
        return campeonatoDAO.findByIdWithEquipos(id);
    }

    /**
     * Actualiza un campeonato
     */
    public Campeonato actualizarCampeonato(Campeonato campeonato) {
        return campeonatoDAO.update(campeonato);
    }

    /**
     * Elimina un campeonato
     */
    public void eliminarCampeonato(Long id) {
        campeonatoDAO.delete(id);
    }

    // ========== OPERACIONES DE EQUIPO ==========

    /**
     * Crea un nuevo equipo y lo agrega al campeonato
     */
    public Equipo crearEquipo(String nombreEquipo, String colorUniforme, Long campeonatoId) {
        // Validar datos
        if (nombreEquipo == null || nombreEquipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del equipo es obligatorio");
        }
        Campeonato campeonato = campeonatoDAO.findById(campeonatoId);
        if (campeonato == null) {
            throw new IllegalArgumentException("Campeonato no encontrado");
        }
        Equipo equipo = new Equipo(nombreEquipo, colorUniforme);
        equipo.setCampeonato(campeonato);

        Equipo equipoGuardado = equipoDAO.save(equipo);

        campeonato.agregarEquipo(equipoGuardado);
        campeonatoDAO.update(campeonato);

        return equipoGuardado;
    }

    /**
     * Obtiene todos los equipos
     */
    public List<Equipo> obtenerTodosEquipos() {
        return equipoDAO.findAll();
    }

    /**
     * Obtiene equipos de un campeonato
     */
    public List<Equipo> obtenerEquiposPorCampeonato(Long campeonatoId) {
        return equipoDAO.findByCampeonato(campeonatoId);
    }

    /**
     * Obtiene un equipo por ID
     */
    public Equipo obtenerEquipoPorId(Long id) {
        return equipoDAO.findById(id);
    }

    /**
     * Obtiene un equipo con todos sus jugadores
     */
    public Equipo obtenerEquipoConJugadores(Long id) {
        return equipoDAO.findByIdWithJugadores(id);
    }

    /**
     * Actualiza un equipo
     */
    public Equipo actualizarEquipo(Equipo equipo) {
        return equipoDAO.update(equipo);
    }

    /**
     * Elimina un equipo
     */
    public void eliminarEquipo(Long id) {
        equipoDAO.delete(id);
    }

    // ========== OPERACIONES DE JUGADOR ==========

    /**
     * Crea un nuevo jugador y lo agrega al equipo
     */
    public Jugador crearJugador(String nombre, String apellido, String cedula,
            int numeroCamisa, Long equipoId) {
        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador es obligatorio");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido del jugador es obligatorio");
        }

        // Validar cédula
        Jugador.validarCedula(cedula);

        // Verificar que no exista un jugador con la misma cédula
        Jugador existente = jugadorDAO.findByCedula(cedula);
        if (existente != null) {
            throw new IllegalArgumentException("Ya existe un jugador con esa cédula");
        }

        // Validar número de camisa
        Jugador.validarNumeroCamisa(numeroCamisa);

        // Obtener el equipo
        Equipo equipo = equipoDAO.findById(equipoId);
        if (equipo == null) {
            throw new IllegalArgumentException("Equipo no encontrado");
        }

        // Verificar que el número de camisa no esté en uso en el equipo
        if (jugadorDAO.existeNumeroCamisaEnEquipo(equipoId, numeroCamisa, null)) {
            throw new IllegalArgumentException("Ya existe un jugador con ese número de camisa en el equipo");
        }

        // Crear jugador
        Jugador jugador = new Jugador(nombre, apellido, cedula, numeroCamisa);
        jugador.setEquipo(equipo);

        // Guardar
        Jugador jugadorGuardado = jugadorDAO.save(jugador);

        // Actualizar equipo
        equipo.agregarJugador(jugadorGuardado);
        equipoDAO.update(equipo);

        return jugadorGuardado;
    }

    /**
     * Obtiene todos los jugadores
     */
    public List<Jugador> obtenerTodosJugadores() {
        return jugadorDAO.findAll();
    }

    /**
     * Obtiene jugadores por equipo
     */
    public List<Jugador> obtenerJugadoresPorEquipo(Long equipoId) {
        return jugadorDAO.findByEquipo(equipoId);
    }

    /**
     * Obtiene un jugador por ID
     */
    public Jugador obtenerJugadorPorId(Long id) {
        return jugadorDAO.findById(id);
    }

    /**
     * Obtiene un jugador por cédula
     */
    public Jugador obtenerJugadorPorCedula(String cedula) {
        return jugadorDAO.findByCedula(cedula);
    }

    /**
     * Actualiza un jugador
     */
    public Jugador actualizarJugador(Jugador jugador) {
        return jugadorDAO.update(jugador);
    }

    /**
     * Elimina un jugador
     */
    public void eliminarJugador(Long id) {
        jugadorDAO.delete(id);
    }

    // ========== OPERACIONES DE ENCUENTRO ==========

    /**
     * Crea un nuevo encuentro entre dos equipos
     */
    public Encuentro crearEncuentro(Long equipoLocalId, Long equipoVisitanteId,
            LocalDate fecha, Long campeonatoId) {
        // Validaciones
        if (equipoLocalId == null || equipoVisitanteId == null) {
            throw new IllegalArgumentException("Debe seleccionar ambos equipos");
        }

        if (equipoLocalId.equals(equipoVisitanteId)) {
            throw new IllegalArgumentException("Debe seleccionar equipos diferentes");
        }

        // Obtener equipos
        Equipo local = equipoDAO.findById(equipoLocalId);
        Equipo visitante = equipoDAO.findById(equipoVisitanteId);

        if (local == null || visitante == null) {
            throw new IllegalArgumentException("Equipos no encontrados");
        }

        // Obtener campeonato
        Campeonato campeonato = campeonatoDAO.findById(campeonatoId);
        if (campeonato == null) {
            throw new IllegalArgumentException("Campeonato no encontrado");
        }

        // Crear encuentro
        Encuentro encuentro = new Encuentro(fecha);
        encuentro.setLocal(local);
        encuentro.setVisitante(visitante);
        encuentro.setCampeonato(campeonato);

        // Guardar
        Encuentro encuentroGuardado = encuentroDAO.save(encuentro);

        // Actualizar campeonato
        campeonato.agregarEncuentro(encuentroGuardado);
        campeonatoDAO.update(campeonato);

        return encuentroGuardado;
    }

    /**
     * Obtiene todos los encuentros
     */
    public List<Encuentro> obtenerTodosEncuentros() {
        return encuentroDAO.findAll();
    }

    /**
     * Obtiene encuentros de un campeonato
     */
    public List<Encuentro> obtenerEncuentrosPorCampeonato(Long campeonatoId) {
        return encuentroDAO.findByCampeonato(campeonatoId);
    }

    /**
     * Obtiene encuentros de un equipo
     */
    public List<Encuentro> obtenerEncuentrosPorEquipo(Long equipoId) {
        return encuentroDAO.findByEquipo(equipoId);
    }

    /**
     * Obtiene un encuentro por ID
     */
    public Encuentro obtenerEncuentroPorId(Long id) {
        return encuentroDAO.findById(id);
    }

    /**
     * Actualiza un encuentro
     */
    public Encuentro actualizarEncuentro(Encuentro encuentro) {
        return encuentroDAO.update(encuentro);
    }

    /**
     * Elimina un encuentro
     */
    public void eliminarEncuentro(Long id) {
        encuentroDAO.delete(id);
    }
}