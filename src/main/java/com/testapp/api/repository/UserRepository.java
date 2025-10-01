package com.testapp.api.repository;

import com.testapp.api.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UserRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    // VULNERABILITY: SQL Injection - concatenating user input directly into SQL query
    public List<User> findByUsername(String username) {
        String queryString = "SELECT u FROM User u WHERE u.username = '" + username + "'";
        Query query = entityManager.createQuery(queryString);
        return query.getResultList();
    }
    
    // VULNERABILITY: SQL Injection - using native query with string concatenation
    public List<User> searchUsers(String searchTerm) {
        String sql = "SELECT * FROM users WHERE username LIKE '%" + searchTerm + "%' OR email LIKE '%" + searchTerm + "%'";
        Query query = entityManager.createNativeQuery(sql, User.class);
        return query.getResultList();
    }
    
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }
    
    public void save(User user) {
        entityManager.persist(user);
    }
    
    public void update(User user) {
        entityManager.merge(user);
    }
    
    public void delete(Long id) {
        User user = findById(id);
        if (user != null) {
            entityManager.remove(user);
        }
    }
}
