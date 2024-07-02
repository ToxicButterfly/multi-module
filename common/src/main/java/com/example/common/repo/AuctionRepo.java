package com.example.common.repo;

import com.example.common.entity.Auction;
import com.example.common.entity.Status;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuctionRepo {

    private final SessionFactory sessionFactory ;

    @Autowired
    public AuctionRepo(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<Auction> getAllAuctions() {
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Auction> cq = cb.createQuery(Auction.class);
        Root<Auction> root = cq.from(Auction.class);
        cq.select(root);
        return getCurrentSession().createQuery(cq).getResultList();

    }

    public Auction getAuction(Integer id) {
        String queryString = "from Auction where id = :id";
        return (Auction) getCurrentSession()
                .createQuery(queryString)
                .setParameter("id", id)
                .setCacheable(true)
                .uniqueResult();
    }

    public void save(Auction auction) {
        getCurrentSession().saveOrUpdate(auction);
    }

    public List<Auction> findByStatus(Status status) {
        return getCurrentSession()
                .createQuery("from Auction where status = :status", Auction.class)
                .setParameter("status", status)
                .getResultList();
    }

    public Auction delete(Integer id) {
        Auction auction = getAuction(id);
        if (auction != null) {
            sessionFactory.getCurrentSession().delete(auction);
        }
        return auction;
    }

    public boolean existsByName(String name) {
        String queryString = "select count(*) from Auction where name = :name";
        Long count = (Long) getCurrentSession()
                .createQuery(queryString)
                .setParameter("name", name)
                .uniqueResult();
        return count > 0;
    }
}
