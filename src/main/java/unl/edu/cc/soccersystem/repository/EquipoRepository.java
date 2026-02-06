package unl.edu.cc.soccersystem.repository;

import jakarta.persistence.TypedQuery;
import unl.edu.cc.soccersystem.domain.Equipo;

import java.util.List;

@jakarta.enterprise.context.ApplicationScoped
public class EquipoRepository extends GenericRepositoryIpm<Equipo, Long> {

    public EquipoRepository() {
        super(Equipo.class);
    }

    /**
     * Busca equipos por nombre (búsqueda parcial)
     * @param nombre Nombre o parte del nombre
     * @return Lista de equipos encontrados
     */
    public List<Equipo> findByNombre(String nombre) {
        TypedQuery<Equipo> query = getEntityManager().createQuery(
                "SELECT e FROM Equipo e WHERE LOWER(e.nombreEquipo) LIKE LOWER(:nombre)",
                Equipo.class);
        query.setParameter("nombre", "%" + nombre + "%");
        return query.getResultList();
    }

    /**
     * Busca equipos de un campeonato específico
     * @param campeonatoId ID del campeonato
     * @return Lista de equipos del campeonato
     */
    public List<Equipo> findByCampeonato(Long campeonatoId) {
        TypedQuery<Equipo> query = getEntityManager().createQuery(
                "SELECT e FROM Equipo e WHERE e.campeonato.id = :campeonatoId",
                Equipo.class);
        query.setParameter("campeonatoId", campeonatoId);
        return query.getResultList();
    }

    /**
     * Obtiene un equipo con todos sus jugadores cargados
     * @param id ID del equipo
     * @return Equipo con jugadores cargados
     */
    public Equipo findByIdWithJugadores(Long id) {
        TypedQuery<Equipo> query = getEntityManager().createQuery(
                "SELECT e FROM Equipo e LEFT JOIN FETCH e.jugadores WHERE e.id = :id",
                Equipo.class);
        query.setParameter("id", id);
        List<Equipo> resultados = query.getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }
}
