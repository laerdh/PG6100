package no.westerdals.pg6100.rest.ejb;

import no.westerdals.pg6100.rest.entity.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class UserEJB {

    @PersistenceContext
    private EntityManager em;


    public UserEJB() {}


    public Long createUser(String name, String surname, String address) {
        if (!validateInput(name, surname, address)) {
            return null;
        }

        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setAddress(address);

        em.persist(user);

        return user.getId();
    }

    public boolean updateUser(Long id, String name, String surname, String address) {
        if (id == null || !validateInput(name, surname, address)) {
            return false;
        }

        User user = em.find(User.class, id);

        if (user == null) {
            return false;
        }

        user.setName(name);
        user.setSurname(surname);
        user.setAddress(address);

        return true;
    }

    public User getUser(Long id) {
        if (id == null) {
            return null;
        }

        return em.find(User.class, id);
    }

    public List<User> getUsers() {
        Query query = em.createQuery("select u from User u");

        return query.getResultList();
    }

    private boolean validateInput(String... obj) {
        for (String s : obj) {
            if (s == null || s.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
