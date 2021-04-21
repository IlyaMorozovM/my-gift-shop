package com.epam.esm.dao;

import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.dao.mapper.TagRowMapper;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class TagDaoImpTest {

    private TagDao tagDao;

    @BeforeEach
    public void init() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScripts("db/ClearTables.sql")
                .addScript("db/Tags.sql")
                .build();

        tagDao = new TagDaoImpl(new JdbcTemplate(dataSource), new TagRowMapper());
    }

    @Test
    void whenAddTag_thenCorrectlyReturnItById() throws PersistenceException {
        Tag given = new Tag("Tag to return by id");

        given.setId(tagDao.addTag(given));

        Tag actual = tagDao.getTag(given.getId());
        assertEquals(given, actual);
    }

    @Test
    void whenAddTag_thenCorrectlyReturnItByName() throws PersistenceException {
        Tag given = new Tag("Tag to return by name");

        given.setId(tagDao.addTag(given));

        Tag actual = tagDao.getTag(given.getName());
       assertEquals(given, actual);
    }

    @Test
    void whenAddTag_thenCorrectlyDeleteIdById() throws PersistenceException {
        Tag given = new Tag("Tag to delete");

        given.setId(tagDao.addTag(given));

        boolean actual = tagDao.deleteTag(given.getId());
        assertTrue(actual);
    }

    @Test
    void whenAddTag_thenReturnNonZeroId() throws PersistenceException {
        Tag tag = new Tag("Tag to add");

        int id = tagDao.addTag(tag);

        assertNotEquals(0, id);
    }

    @Test
    void whenAddTags_thenCorrectlyReturnsIt() throws PersistenceException {
        List<Tag> given = new ArrayList<>();
        given.add(new Tag("First tag"));
        given.add(new Tag("Second tag"));
        given.add(new Tag("Third tag"));

        for (Tag tag: given) {
            tag.setId(tagDao.addTag(tag));
        }

        List<Tag> actual = tagDao.getAllTags();
        assertEquals(given, actual);
    }
}
