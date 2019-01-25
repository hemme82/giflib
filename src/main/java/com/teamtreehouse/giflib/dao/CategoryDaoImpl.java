package com.teamtreehouse.giflib.dao;

import com.teamtreehouse.giflib.model.Category;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;

@Repository
public class CategoryDaoImpl implements CategoryDao{

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @SuppressWarnings("unchecked")
    public List<Category> findAll() {
        //open a session
        Session session = sessionFactory.openSession();

        //get all categories with Hibernate criteria
        List<Category> categories = session.createCriteria(Category.class).list();

        //close the session
        session.close();

        return categories;
    }

    @Override
    public Category findById(Long id) {
        Session session = sessionFactory.openSession();
        Category category = session.get(Category.class,id);
        session.close();
        return category;
    }

    @Override
    public void save(Category category) {
        //open a session
        Session session = sessionFactory.openSession();

        //begin a transaction
        session.beginTransaction();

        //save the category
        session.save(category);

        //commit the transaction
        session.getTransaction().commit();
        //close the session
        session.close();
    }

    @Override
    public void delete(Category category) {

    }
}
