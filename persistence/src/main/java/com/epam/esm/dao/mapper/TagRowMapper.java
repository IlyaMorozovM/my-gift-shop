package com.epam.esm.dao.mapper;

import com.epam.esm.model.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class TagRowMapper implements RowMapper<Tag> {

    private static final String TAG_ID_COLUMN = "ID";
    private static final String TAG_ID_NAME = "Name";

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setId(rs.getInt(TAG_ID_COLUMN));
        tag.setName(rs.getString(TAG_ID_NAME));

        return tag;
    }
}
