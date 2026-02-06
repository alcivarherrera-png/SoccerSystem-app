package unl.edu.cc.soccersystem.repository;

import java.io.Serializable;
import java.util.List;

public interface GenericRepository <T, ID extends Serializable> {

    /**
     * Guarda una nueva entidad en la base de datos
     * @param entity Entidad a guardar
     * @return Entidad guardada con ID generado
     */
    T save(T entity);

    /**
     * Actualiza una entidad existente
     * @param entity Entidad a actualizar
     * @return Entidad actualizada
     */
    T update(T entity);

    /**
     * Elimina una entidad por su ID
     * @param id ID de la entidad a eliminar
     */
    void delete(ID id);

    /**
     * Busca una entidad por su ID
     * @param id ID de la entidad
     * @return Entidad encontrada o null
     */
    T findById(ID id);

    /**
     * Obtiene todas las entidades
     * @return Lista de todas las entidades
     */
    List<T> findAll();

    /**
     * Cuenta el número total de entidades
     * @return Número de entidades
     */
    long count();
}