package jpa.learn.repos.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import jpa.learn.beans.Address;
import jpa.learn.beans.User;
import jpa.learn.repos.user.UserCriteriaRepository;

@Repository
public class UserCriteriaRepositoryImpl implements UserCriteriaRepository {

	@PersistenceContext
    private EntityManager entityManager;
	
	
	@Override
	public List<User> searchUsers(String firstName, String city, Boolean isOldUser, 
			Date startDate, Date endDate) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        Join<User, Address> addressJoin = root.join("address", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (firstName!=null && !firstName.isBlank()) {
            predicates.add(cb.equal(root.get("name"), firstName));
        }

        if (city != null && !city.isBlank()) {
            predicates.add(cb.equal(addressJoin.get("city"), city));
        }

        if (isOldUser != null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -1);
            Date oneYearAgo = cal.getTime();

            if (isOldUser) {
                predicates.add(cb.lessThanOrEqualTo(root.get("joinDate"), oneYearAgo));
            } else {
                predicates.add(cb.greaterThan(root.get("joinDate"), oneYearAgo));
            }
        }

        if (startDate != null && endDate != null) {
            predicates.add(cb.between(root.get("joinDate"), startDate, endDate));
        } else if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("joinDate"), startDate));
        } else if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("joinDate"), endDate));
        }

        cq.where(predicates.toArray(new Predicate[0]))
        	.groupBy(addressJoin.get("city"))
        	.orderBy(cb.asc(addressJoin.get("city"))); //Hard coded Asc order

        TypedQuery<User> query = entityManager.createQuery(cq);
        
        return query.getResultList();
	}
}
