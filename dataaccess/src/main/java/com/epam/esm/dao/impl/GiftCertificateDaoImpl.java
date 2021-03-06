package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.dao.util.DateTimeUtil;
import com.epam.esm.model.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

public class GiftCertificateDaoImpl implements GiftCertificateDAO {

    private static final ZoneId DEFAULT_ZONE = ZoneOffset.UTC;

    private static final String SQL_GET_CERTIFICATE_BY_ID =
            "SELECT * FROM gift_certificates " +
            "LEFT JOIN certificate_details " +
            "ON gift_certificates.id = certificate_details.certificate_id " +
            "LEFT JOIN tags " +
            "ON tags.id = certificate_details.tag_id " +
            "WHERE gift_certificates.id = ?";

    private static final String SQL_GET_CERTIFICATE_BY_NAME =
            "SELECT * FROM gift_certificates " +
            "LEFT JOIN certificate_details " +
            "ON gift_certificates.id = certificate_details.certificate_id " +
            "LEFT JOIN tags " +
            "ON tags.id = certificate_details.tag_id " +
            "WHERE gift_certificates.name = ?";

    private static final String SQL_GET_ALL_CERTIFICATES =
            "SELECT * FROM gift_certificates " +
            "LEFT JOIN certificate_details " +
            "ON gift_certificates.id = certificate_details.certificate_id " +
            "LEFT JOIN tags " +
            "ON tags.id = certificate_details.tag_id ";

    private static final String SQL_GET_ALL_CERTIFICATES_BY_CONTENT =
            "SELECT * FROM gift_certificates " +
            "LEFT JOIN certificate_details " +
            "ON gift_certificates.id = certificate_details.certificate_id " +
            "LEFT JOIN tags " +
            "ON tags.id = certificate_details.tag_id " +
            "WHERE gift_certificates.name LIKE ? OR description LIKE ? ";

    private static final String SQL_GET_CERTIFICATES_BY_TAG_NAME =
            "SELECT * FROM gift_certificates " +
            "JOIN certificate_details ON gift_certificates.id = certificate_details.certificate_id " +
            "JOIN tags ON tags.id = certificate_details.tag_id " +
            "WHERE certificate_details.certificate_id IN " +
            "( " +
            "SELECT certificate_details.certificate_id FROM certificate_details " +
            "JOIN tags ON Tags.id = certificate_details.tag_id " +
            "WHERE tags.name = ?" +
            ")";

    private static final String SQL_ADD_CERTIFICATE =
            "INSERT INTO gift_certificates " +
            "(name, description, price, create_date, last_update_date, duration) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_DELETE_CERTIFICATE =
            "DELETE FROM gift_certificates WHERE (id = ?)";

    private static final String SQL_UPDATE_CERTIFICATE =
            "UPDATE gift_certificates " +
            "SET name = IFNULL(?, name), description = IFNULL(?, description)," +
            "price = IFNULL(?, price), last_update_date = IFNULL(?, last_update_date), " +
            "duration = IFNULL(?, duration) " +
            "WHERE (id = ?)";

    private static final String SQL_CREATE_JOIN =
            "INSERT INTO certificate_details (certificate_id, tag_id) VALUES (?, ?)";

    private static final String SQL_DELETE_JOIN =
            "DELETE FROM certificate_details WHERE (certificate_id = ?)";

    private final JdbcTemplate jdbcTemplate;
    private final ResultSetExtractor<List<GiftCertificate>> extractor;
    private final RowMapper<GiftCertificate> mapper;

    @Autowired
    public GiftCertificateDaoImpl(
            JdbcTemplate jdbcTemplate, ResultSetExtractor<List<GiftCertificate>> extractor,
            RowMapper<GiftCertificate> mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.extractor = extractor;
        this.mapper = mapper;
    }

    @Override
    public GiftCertificate get(String name) {
        return jdbcTemplate.queryForObject(SQL_GET_CERTIFICATE_BY_NAME, mapper, name);
    }

    @Override
    public GiftCertificate get(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_CERTIFICATE_BY_ID, mapper, id);
    }

    @Override
    public List<GiftCertificate> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL_CERTIFICATES, extractor);
    }

    @Override
    public List<GiftCertificate> getAll(String searchingPart) {
        String param = "%" + searchingPart + "%";
        return jdbcTemplate.query(
                SQL_GET_ALL_CERTIFICATES_BY_CONTENT, extractor, param, param);
    }

    @Override
    public List<GiftCertificate> getByTagName(String tagName) {
        return jdbcTemplate.query(SQL_GET_CERTIFICATES_BY_TAG_NAME, extractor, tagName);
    }

    @Override
    public List<GiftCertificate> getAllSortedByName(boolean isAscending) {
        return getAllSortedByParameter("gift_certificates.name", isAscending);
    }

    @Override
    public List<GiftCertificate> getAllSortedByDate(boolean isAscending) {
        return getAllSortedByParameter("create_date", isAscending);
    }

    private List<GiftCertificate> getAllSortedByParameter(
            String parameter, boolean isAscending) {
        String sql = SQL_GET_ALL_CERTIFICATES + "ORDER BY " + parameter + " ";
        sql += isAscending ? "ASC" : "DESC";

        return jdbcTemplate.query(sql, extractor);
    }

    @Override
    public int create(GiftCertificate giftCertificate) throws PersistenceException {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_ADD_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, giftCertificate.getName());
            ps.setString(2, giftCertificate.getDescription());
            ps.setBigDecimal(3, giftCertificate.getPrice());
            LocalDateTime createdDate = DateTimeUtil
                    .toZone(LocalDateTime.now(), DEFAULT_ZONE, ZoneId.systemDefault());
            ps.setTimestamp(4, Timestamp.valueOf(createdDate));
            ps.setTimestamp(5, Timestamp.valueOf(createdDate));
            ps.setInt(6, giftCertificate.getDuration());
            return ps;
        }, holder);

        if (holder.getKey() == null) {
            throw new PersistenceException("Failed to add GiftCertificate to DB",
                    ErrorCodeEnum.FAILED_TO_ADD_CERTIFICATE);
        }

        return holder.getKey().intValue();
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(SQL_DELETE_CERTIFICATE, id) == 1;
    }

    @Override
    public boolean update(GiftCertificate giftCertificate) {
        return jdbcTemplate.update(SQL_UPDATE_CERTIFICATE, getParams(giftCertificate)) == 1;
    }

    private Object[] getParams(GiftCertificate giftCertificate) {
        Object[] params = new Object[6];

        params[0] = giftCertificate.getName();
        params[1] = giftCertificate.getDescription();

        if (giftCertificate.getPrice().compareTo(BigDecimal.ZERO) != 0) {
            params[2] = giftCertificate.getPrice();
        }

        LocalDateTime updatedDate = DateTimeUtil
                .toZone(LocalDateTime.now(), DEFAULT_ZONE, ZoneId.systemDefault());
        params[3] = Timestamp.valueOf(updatedDate);

        if (giftCertificate.getDuration() != 0) {
            params[4] = giftCertificate.getDuration();
        }

        params[5] = giftCertificate.getId();

        return params;
    }

    @Override
    public boolean createCertificateTagRelation(int certificateId, int tagId) {
        return jdbcTemplate.update(SQL_CREATE_JOIN, certificateId, tagId) == 1;
    }

    @Override
    public void deleteAllCertificateTagRelations(int certificateId) {
        jdbcTemplate.update(SQL_DELETE_JOIN, certificateId);
    }
}
