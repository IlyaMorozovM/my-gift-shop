package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.model.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class GiftCertificateDaoImpl implements GiftCertificateDAO {

    private static final String SQL_GET_CERTIFICATE_BY_ID =
            "select * from GiftCertificates " +
            "left join CertificateDetails " +
            "on GiftCertificates.ID = CertificateDetails.CertificateID " +
            "left join Tags " +
            "on Tags.ID = CertificateDetails.TagID " +
            "where GiftCertificates.ID = ?";

    private static final String SQL_GET_CERTIFICATE_BY_NAME =
            "select * from GiftCertificates " +
            "left join CertificateDetails " +
            "on GiftCertificates.ID = CertificateDetails.CertificateID " +
            "left join Tags " +
            "on Tags.ID = CertificateDetails.TagID " +
            "where GiftCertificates.Name = ?";

    private static final String SQL_GET_ALL_CERTIFICATES =
            "select * from GiftCertificates " +
            "left join CertificateDetails " +
            "on GiftCertificates.ID = CertificateDetails.CertificateID " +
            "left join Tags " +
            "on Tags.ID = CertificateDetails.TagID ";

    private static final String SQL_GET_ALL_CERTIFICATES_BY_CONTENT =
            "select * from GiftCertificates " +
            "left join CertificateDetails " +
            "on GiftCertificates.ID = CertificateDetails.CertificateID " +
            "left join Tags " +
            "on Tags.ID = CertificateDetails.TagID " +
            "where GiftCertificates.Name like ? or Description like ? ";

    private static final String SQL_GET_CERTIFICATES_BY_TAG_NAME =
            "select * from GiftCertificates " +
            "join CertificateDetails on GiftCertificates.ID = CertificateDetails.CertificateID " +
            "join Tags on Tags.ID = CertificateDetails.TagID " +
            "where CertificateDetails.CertificateID in " +
            "( " +
            "select CertificateDetails.CertificateID from CertificateDetails " +
            "join Tags on Tags.ID = CertificateDetails.TagID " +
            "where Tags.name = ?" +
            ")";

    private static final String SQL_ADD_CERTIFICATE =
            "insert into GiftCertificates " +
            "(Name, Description, Price, CreateDate, LastUpdateDate, Duration) " +
            "values (?, ?, ?, ?, ?, ?)";

    private static final String SQL_DELETE_CERTIFICATE =
            "delete from GiftCertificates where (ID = ?)";

    private static final String SQL_UPDATE_CERTIFICATE =
            "update GiftCertificates " +
            "set Name = IFNULL(?, Name), Description = IFNULL(?, Description)," +
            "Price = IFNULL(?, Price), LastUpdateDate = IFNULL(?, LastUpdateDate), " +
            "Duration = IFNULL(?, Duration) " +
            "where (ID = ?)";

    private static final String SQL_CREATE_JOIN =
            "insert into CertificateDetails (CertificateID, TagID) values (?, ?)";

    private static final String SQL_DELETE_JOIN =
            "delete from CertificateDetails where (CertificateID = ?)";

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
    public GiftCertificate getGiftCertificate(String name) {
        return jdbcTemplate.queryForObject(SQL_GET_CERTIFICATE_BY_NAME, mapper, name);
    }

    @Override
    public GiftCertificate getGiftCertificate(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_CERTIFICATE_BY_ID, mapper, id);
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificates() {
        return jdbcTemplate.query(SQL_GET_ALL_CERTIFICATES, extractor);
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificates(String content) {
        String param = "%" + content + "%";
        return jdbcTemplate.query(
                SQL_GET_ALL_CERTIFICATES_BY_CONTENT, extractor, param, param);
    }

    @Override
    public List<GiftCertificate> getGiftCertificateByTagName(String tagName) {
        return jdbcTemplate.query(SQL_GET_CERTIFICATES_BY_TAG_NAME, extractor, tagName);
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificatesSortedByName(boolean isAscending) {
        return getAllGiftCertificatesSortedByParameter("GiftCertificates.Name", isAscending);
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificatesSortedByDate(boolean isAscending) {
        return getAllGiftCertificatesSortedByParameter("CreateDate", isAscending);
    }

    private List<GiftCertificate> getAllGiftCertificatesSortedByParameter(
            String parameter, boolean isAscending) {
        String sql = SQL_GET_ALL_CERTIFICATES + "order by " + parameter + " ";
        sql += isAscending ? "ASC" : "DESC";

        return jdbcTemplate.query(sql, extractor);
    }

    @Override
    public int addGiftCertificate(GiftCertificate giftCertificate) throws PersistenceException {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_ADD_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, giftCertificate.getName());
            ps.setString(2, giftCertificate.getDescription());
            ps.setDouble(3, giftCertificate.getPrice());
            ps.setTimestamp(4,
                    Timestamp.valueOf(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")
                            .format(ZonedDateTime.ofInstant(
                                    giftCertificate.getLastUpdateDate().toInstant(), ZoneOffset.of("-03:00")))));
            ps.setTimestamp(5,
                    Timestamp.valueOf(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")
                            .format(ZonedDateTime.ofInstant(
                                    giftCertificate.getLastUpdateDate().toInstant(), ZoneOffset.of("-03:00")))));
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
    public boolean deleteGiftCertificate(int id) {
        return jdbcTemplate.update(SQL_DELETE_CERTIFICATE, id) == 1;
    }

    @Override
    public boolean updateGiftCertificate(GiftCertificate giftCertificate) {
        return jdbcTemplate.update(SQL_UPDATE_CERTIFICATE, getParams(giftCertificate)) == 1;
    }

    private Object[] getParams(GiftCertificate giftCertificate) {
        Object[] params = new Object[6];

        params[0] = giftCertificate.getName();
        params[1] = giftCertificate.getDescription();

        if (giftCertificate.getPrice() != 0) {
            params[2] = giftCertificate.getPrice();
        }

        params[3] = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")
                .format(ZonedDateTime.ofInstant(
                        giftCertificate.getLastUpdateDate().toInstant(), ZoneOffset.of("-03:00")));

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
