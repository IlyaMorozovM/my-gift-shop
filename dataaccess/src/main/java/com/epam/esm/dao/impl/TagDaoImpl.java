package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class TagDaoImpl implements TagDao {

    private static final String SQL_GET_ALL_TAGS = "SELECT id, name FROM Tags";
    private static final String SQL_GET_TAG_BY_NAME = "SELECT id, name FROM Tags WHERE name = ?";
    private static final String SQL_GET_TAG_BY_ID = "SELECT id, name FROM Tags WHERE id = ?";
    private static final String SQL_ADD_TAG = "INSERT INTO Tags (name) VALUES (?)";
    private static final String SQL_DELETE_TAG = "DELETE FROM Tags WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Tag> tagRowMapper;

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<Tag> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagRowMapper = rowMapper;
    }

    @Override
    public Tag get(String name) {
        return jdbcTemplate.queryForObject(SQL_GET_TAG_BY_NAME, tagRowMapper,  name );
    }

    @Override
    public Tag get(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_TAG_BY_ID, tagRowMapper, id);
    }

    @Override
    public List<Tag> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL_TAGS, tagRowMapper);
    }

    @Override
    public int create(Tag tag) throws PersistenceException {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_ADD_TAG, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tag.getName());
            return ps;
        }, holder);

        if (holder.getKey() == null) {
            throw new PersistenceException("Failed to add Tag to DB", ErrorCodeEnum.FAILED_TO_ADD_TAG);
        }

        return holder.getKey().intValue();
    }

    @Override
    public boolean delete(int tagId) {
        return jdbcTemplate.update(SQL_DELETE_TAG, tagId) == 1;
    }
}
