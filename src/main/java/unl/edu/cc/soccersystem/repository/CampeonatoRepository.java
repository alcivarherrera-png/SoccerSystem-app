package unl.edu.cc.soccersystem.repository;

import jakarta.persistence.TypedQuery;
import unl.edu.cc.soccersystem.domain.Campeonato;

import java.util.List;

@jakarta.enterprise.context.ApplicationScoped
public class CampeonatoRepository extends GenericRepositoryIpm<Campeonato, Long> {

    public CampeonatoRepository() {
        super(Campeonato.class);
    }

    /**
     * Busca un campeonato por su nombre
     * @param nombre Nombre del campeonato
     * @return Campeonato encontrado o null
     */
    public Campeonato findByNombre(String nombre) {
        TypedQuery<Campeonato> query = getEntityManager().createQuery(
                "SELECT c FROM Campeonato c WHERE c.nombre = :nombre", Campeonato.class);
        query.setParameter("nombre", nombre);
        List<Campeonato> resultados = query.getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    /**
     * Busca campeonatos activos (posteriores a una fecha)
     * @return Lista de campeonatos activos
     */
    public List<Campeonato> findCampeonatosActivos() {
        TypedQuery<Campeonato> query = getEntityManager().createQuery(
                "SELECT c FROM Campeonato c ORDER BY c.fechaInicio DESC", Campeonato.class);
        return query.getResultList();
    }

    /**
     * Obtiene un campeonato con todos sus equipos cargados
     * @param id ID del campeonato
     * @return Campeonato con equipos cargados
     */
    public Campeonato findByIdWithEquipos(Long id) {
        TypedQuery<Campeonato> query = getEntityManager().createQuery(
                "SELECT c FROM Campeonato c LEFT JOIN FETCH c.equipos WHERE c.id = :id",
                Campeonato.class);
        query.setParameter("id", id);
        List<Campeonato> resultados = query.getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }
}
