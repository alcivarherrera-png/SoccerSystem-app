package unl.edu.cc.soccersystem.repository;

import jakarta.persistence.TypedQuery;
import unl.edu.cc.soccersystem.domain.Encuentro;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio específico para la entidad Encuentro
 */
@jakarta.enterprise.context.ApplicationScoped
public class EncuentroRepository extends GenericRepositoryIpm<Encuentro, Long> {

        public EncuentroRepository() {
                super(Encuentro.class);
        }

        /**
         * Busca encuentros de un campeonato específico
         * 
         * @param campeonatoId ID del campeonato
         * @return Lista de encuentros del campeonato
         */
        public List<Encuentro> findByCampeonato(Long campeonatoId) {
                TypedQuery<Encuentro> query = getEntityManager().createQuery(
                                "SELECT e FROM Encuentro e WHERE e.campeonato.id = :campeonatoId " +
                                                "ORDER BY e.fecha DESC",
                                Encuentro.class);
                query.setParameter("campeonatoId", campeonatoId);
                return query.getResultList();
        }

        /**
         * Busca encuentros de un equipo específico (como local o visitante)
         * 
         * @param equipoId ID del equipo
         * @return Lista de encuentros del equipo
         */
        public List<Encuentro> findByEquipo(Long equipoId) {
                TypedQuery<Encuentro> query = getEntityManager().createQuery(
                                "SELECT e FROM Encuentro e WHERE e.local.id = :equipoId " +
                                                "OR e.visitante.id = :equipoId ORDER BY e.fecha DESC",
                                Encuentro.class);
                query.setParameter("equipoId", equipoId);
                return query.getResultList();
        }

        /**
         * Busca encuentros por rango de fechas
         * 
         * @param fechaInicio Fecha inicial
         * @param fechaFin    Fecha final
         * @return Lista de encuentros en el rango
         */
        public List<Encuentro> findByFechaRango(LocalDate fechaInicio, LocalDate fechaFin) {
                TypedQuery<Encuentro> query = getEntityManager().createQuery(
                                "SELECT e FROM Encuentro e WHERE e.fecha BETWEEN :fechaInicio AND :fechaFin " +
                                                "ORDER BY e.fecha",
                                Encuentro.class);
                query.setParameter("fechaInicio", fechaInicio);
                query.setParameter("fechaFin", fechaFin);
                return query.getResultList();
        }
}
