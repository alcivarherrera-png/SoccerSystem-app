package unl.edu.cc.soccersystem.repository;

import jakarta.persistence.TypedQuery;
import unl.edu.cc.soccersystem.domain.Jugador;

import java.util.List;

/**
 * DAO específico para la entidad Jugador
 */
@jakarta.enterprise.context.ApplicationScoped
public class JugadorRepository extends GenericRepositoryIpm<Jugador, Long> {

    public JugadorRepository() {
        super(Jugador.class);
    }

    /**
     * Busca un jugador por su cédula
     * 
     * @param cedula Cédula del jugador
     * @return Jugador encontrado o null
     */
    public Jugador findByCedula(String cedula) {
        TypedQuery<Jugador> query = getEntityManager().createQuery(
                "SELECT j FROM Jugador j WHERE j.cedula = :cedula", Jugador.class);
        query.setParameter("cedula", cedula);
        List<Jugador> resultados = query.getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    /**
     * Busca jugadores por equipo
     * 
     * @param equipoId ID del equipo
     * @return Lista de jugadores del equipo
     */
    public List<Jugador> findByEquipo(Long equipoId) {
        TypedQuery<Jugador> query = getEntityManager().createQuery(
                "SELECT j FROM Jugador j WHERE j.equipo.id = :equipoId ORDER BY j.numeroCamisa",
                Jugador.class);
        query.setParameter("equipoId", equipoId);
        return query.getResultList();
    }

    /**
     * Busca jugadores por nombre (búsqueda parcial)
     * 
     * @param nombre Nombre o apellido del jugador
     * @return Lista de jugadores encontrados
     */
    public List<Jugador> findByNombre(String nombre) {
        TypedQuery<Jugador> query = getEntityManager().createQuery(
                "SELECT j FROM Jugador j WHERE LOWER(j.nombre) LIKE LOWER(:nombre) " +
                        "OR LOWER(j.apellido) LIKE LOWER(:nombre)",
                Jugador.class);
        query.setParameter("nombre", "%" + nombre + "%");
        return query.getResultList();
    }

    /**
     * Verifica si existe un jugador con un número de camisa en un equipo
     * 
     * @param equipoId     ID del equipo
     * @param numeroCamisa Número de camisa a verificar
     * @param jugadorId    ID del jugador actual (para excluirlo en actualizaciones)
     * @return true si existe, false si no
     */
    public boolean existeNumeroCamisaEnEquipo(Long equipoId, int numeroCamisa, Long jugadorId) {
        String jpql = "SELECT COUNT(j) FROM Jugador j WHERE j.equipo.id = :equipoId " +
                "AND j.numeroCamisa = :numeroCamisa";
        if (jugadorId != null) {
            jpql += " AND j.id != :jugadorId";
        }

        TypedQuery<Long> query = getEntityManager().createQuery(jpql, Long.class);
        query.setParameter("equipoId", equipoId);
        query.setParameter("numeroCamisa", numeroCamisa);
        if (jugadorId != null) {
            query.setParameter("jugadorId", jugadorId);
        }

        return query.getSingleResult() > 0;
    }
}
