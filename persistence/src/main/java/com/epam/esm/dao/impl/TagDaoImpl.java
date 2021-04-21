package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
public class TagDaoImpl implements TagDao {

    private static final String SQL_GET_ALL_TAGS = "select Id, Name from Tags";
    private static final String SQL_GET_TAG_BY_NAME = "select Id, Name from Tags where Name = ?";
    private static final String SQL_GET_TAG_BY_ID = "select Id, Name from Tags where Id = ?";
    private static final String SQL_ADD_TAG = "insert into Tags (Name) values (?)";
    private static final String SQL_DELETE_TAG = "delete from Tags where ID = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Tag> tagRowMapper;

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<Tag> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagRowMapper = rowMapper;
    }

    @Override
    public Tag getTag(String name) {
        return jdbcTemplate.queryForObject(SQL_GET_TAG_BY_NAME, tagRowMapper,  name );
    }

    @Override
    public Tag getTag(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_TAG_BY_ID, tagRowMapper, id);
    }

    @Override
    public List<Tag> getAllTags() {
        return jdbcTemplate.query(SQL_GET_ALL_TAGS, tagRowMapper);
    }

    @Override
    public int addTag(Tag tag) throws PersistenceException {
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
    public boolean deleteTag(int tagId) {
        return jdbcTemplate.update(SQL_DELETE_TAG, tagId) == 1;
    }
}
