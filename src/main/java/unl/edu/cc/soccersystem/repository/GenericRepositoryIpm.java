package unl.edu.cc.soccersystem.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.criteria.CriteriaQuery;

import java.io.Serializable;
import java.util.List;

public abstract class GenericRepositoryIpm<T, ID extends Serializable> implements GenericRepository<T, ID> {

    @PersistenceContext
    private EntityManager em;

    private Class<T> entityClass;

    public GenericRepositoryIpm() {
    }

    public GenericRepositoryIpm(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    @Transactional
    public T save(T entity) {
        getEntityManager().persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public T update(T entity) {
        return getEntityManager().merge(entity);
    }

    @Override
    @Transactional
    public void delete(ID id) {
        T entity = getEntityManager().find(entityClass, id);
        if (entity != null) {
            getEntityManager().remove(entity);
        }
    }

    @Override
    public T findById(ID id) {
        return getEntityManager().find(entityClass, id);
    }

    @Override
    public List<T> findAll() {
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    @Override
    public long count() {
        CriteriaQuery<Object> cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(getEntityManager().getCriteriaBuilder().count(cq.from(entityClass)));
        return (long) getEntityManager().createQuery(cq).getSingleResult();
    }
}
