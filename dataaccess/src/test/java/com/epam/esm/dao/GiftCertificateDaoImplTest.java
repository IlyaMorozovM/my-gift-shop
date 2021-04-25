package com.epam.esm.dao;

import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.dao.extractor.GiftCertificateExtractor;
import com.epam.esm.dao.impl.GiftCertificateDaoImpl;
import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.dao.mapper.TagRowMapper;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Collections;


class GiftCertificateDaoImplTest {

    private GiftCertificateDAO giftCertificateDAO;
    private TagDao tagDao;

    @BeforeEach
    public void init() throws PersistenceException {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("db/ClearTables.sql")
                .addScript("db/Tags.sql")
                .addScript("db/GiftCertificates.sql")
                .addScript("db/CertificateDetails.sql")
                .build();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        tagDao = new TagDaoImpl(jdbcTemplate, new TagRowMapper());
        giftCertificateDAO = new GiftCertificateDaoImpl(
                jdbcTemplate, new GiftCertificateExtractor(), new GiftCertificateMapper());

        tagDao.create(new Tag("spa"));
        tagDao.create(new Tag("relax"));
        tagDao.create(new Tag("tourism"));
    }

    private List<GiftCertificate> initCertificates(int size) {
        List<GiftCertificate> given = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            GiftCertificate certificate = initCertificate();
            certificate.setName("name" + i);

            given.add(certificate);
        }

        return given;
    }

    private GiftCertificate initCertificate() {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Tour to Greece");
        certificate.setDescription("Certificate description");
        certificate.setPrice(new BigDecimal("99.99"));

        certificate.setCreateDate(ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        certificate.setLastUpdateDate(certificate.getCreateDate());
        certificate.setDuration(10);

        Set<Tag> tags = new HashSet<>();
        tags.add(tagDao.get("tourism"));
        tags.add(tagDao.get("relax"));
        certificate.setTags(tags);

        return certificate;
    }

    private void assertEqualsWithoutUpdateDate(GiftCertificate actual, GiftCertificate expected) {
        assertEquals(actual.getId(), expected.getId());
        assertEquals(actual.getName(), expected.getName());
        assertEquals(actual.getPrice(), expected.getPrice());
        assertEquals(actual.getDuration(), expected.getDuration());
        assertEquals(actual.getTags(), expected.getTags());
        assertTrue(equalDates(actual.getCreateDate(), expected.getCreateDate()));
    }

    private void assertEqualsCertificateLists(List<GiftCertificate> expected, List<GiftCertificate> actual) {
        for (int i = 0; i < expected.size(); i++) {
            assertEqualsCertificates(expected.get(i), actual.get(i));
        }
    }

    private void assertEqualsCertificates(GiftCertificate actual, GiftCertificate expected) {
        assertEquals(actual.getId(), expected.getId());
        assertEquals(actual.getName(), expected.getName());
        assertEquals(actual.getPrice(), expected.getPrice());
        assertEquals(actual.getDuration(), expected.getDuration());
        assertEquals(actual.getTags(), expected.getTags());

        assertTrue(equalDates(actual.getCreateDate(), expected.getCreateDate()));
        assertTrue(equalDates(actual.getLastUpdateDate(), expected.getLastUpdateDate()));
    }

    private boolean equalDates(ZonedDateTime first, ZonedDateTime second) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        String firstDate = ZonedDateTime.ofInstant(first.toInstant(), ZoneOffset.UTC).format(formatter);
        String secondDate = ZonedDateTime.ofInstant(second.toInstant(), ZoneOffset.of("+03:00")).format(formatter);

        return firstDate.equals(secondDate);
    }

    @Test
    void whenAddGiftCertificate_thenCorrectlyReturnItById() throws PersistenceException {
        GiftCertificate given = initCertificate();

        given.setId(giftCertificateDAO.create(given));
        for (Tag tag: given.getTags()) {
            giftCertificateDAO.createCertificateTagRelation(given.getId(), tag.getId());
        }

        GiftCertificate actual = giftCertificateDAO.get(given.getId());
        assertEqualsCertificates(given, actual);
    }

    @Test
    void whenAddGiftCertificate_thenCorrectlyReturnItByName() throws PersistenceException {
        GiftCertificate given = initCertificate();

        given.setId(giftCertificateDAO.create(given));
        for (Tag tag: given.getTags()) {
            giftCertificateDAO.createCertificateTagRelation(given.getId(), tag.getId());
        }

        GiftCertificate actual = giftCertificateDAO.get(given.getName());
        assertEqualsCertificates(given, actual);
    }

    @Test
    void whenAddGiftCertificates_thenCorrectlyReturnThemByTagName() throws PersistenceException {
        List<GiftCertificate> given = initCertificates(10);

        for (GiftCertificate certificate: given) {
            certificate.setId(giftCertificateDAO.create(certificate));

            for (Tag tag: certificate.getTags()) {
                giftCertificateDAO.createCertificateTagRelation(certificate.getId(), tag.getId());
            }
        }

        List<GiftCertificate> actual = giftCertificateDAO.getByTagName("relax");
        assertEqualsCertificateLists(given, actual);
    }

    @Test
    void whenAddGiftCertificate_thenCorrectlyDeleteIt() throws PersistenceException {
        GiftCertificate given = initCertificate();

        given.setId(giftCertificateDAO.create(given));
        for (Tag tag: given.getTags()) {
            giftCertificateDAO.createCertificateTagRelation(given.getId(), tag.getId());
        }

        giftCertificateDAO.deleteAllCertificateTagRelations(given.getId());
        boolean actual = giftCertificateDAO.delete(given.getId());
        assertTrue(actual);
    }

    @Test
    void whenAddGiftCertificates_thenCorrectlyReturnThem() throws PersistenceException {
        List<GiftCertificate> given = initCertificates(20);

        for (GiftCertificate certificate: given) {
            certificate.setId(giftCertificateDAO.create(certificate));

            for (Tag tag: certificate.getTags()) {
                giftCertificateDAO.createCertificateTagRelation(certificate.getId(), tag.getId());
            }
        }

        List<GiftCertificate> actual = giftCertificateDAO.getAll();
        assertEqualsCertificateLists(given, actual);
    }

    @Test
    void whenAddGiftCertificate_thenCorrectlyUpdateIt() throws PersistenceException {
        GiftCertificate given = initCertificate();

        given.setId(giftCertificateDAO.create(given));
        given.setName("new name");
        given.setDescription("new description");
        given.setPrice(new BigDecimal("199.99"));
        given.setDuration(99);
        given.getTags().add(tagDao.get("spa"));
        for (Tag tag: given.getTags()) {
            giftCertificateDAO.createCertificateTagRelation(given.getId(), tag.getId());
        }

        giftCertificateDAO.update(given);
        GiftCertificate actual = giftCertificateDAO.get(given.getId());
        assertEqualsWithoutUpdateDate(given, actual);
    }

    @Test
    void whenAddGiftCertificates_thenCorrectlyReturnThemSortedByDateAsc() throws PersistenceException {
        List<GiftCertificate> given = initCertificates(10);

        for (GiftCertificate certificate: given) {
            certificate.setId(giftCertificateDAO.create(certificate));

            for (Tag tag: certificate.getTags()) {
                giftCertificateDAO.createCertificateTagRelation(certificate.getId(), tag.getId());
            }
        }

        List<GiftCertificate> actual = giftCertificateDAO.getAllSortedByDate(true);
        assertEqualsCertificateLists(given, actual);
    }

    @Test
    void whenAddGiftCertificates_thenCorrectlyReturnThemSortedByDateDesc() throws PersistenceException {
        List<GiftCertificate> given = initCertificates(10);

        for (GiftCertificate certificate: given) {
            certificate.setId(giftCertificateDAO.create(certificate));

            for (Tag tag: certificate.getTags()) {
                giftCertificateDAO.createCertificateTagRelation(certificate.getId(), tag.getId());
            }
        }

        List<GiftCertificate> actual = giftCertificateDAO.getAllSortedByDate(false);
        assertEqualsCertificateLists(given, actual);
    }

    @Test
    void whenAddGiftCertificates_thenCorrectlyReturnThemSortedByNameAsc() throws PersistenceException {
        List<GiftCertificate> given = initCertificates(10);

        for (int i = 0; i < given.size(); i++) {
            given.get(i).setName(String.valueOf(i));
            given.get(i).setId(giftCertificateDAO.create(given.get(i)));

            for (Tag tag:  given.get(i).getTags()) {
                giftCertificateDAO.createCertificateTagRelation(given.get(i).getId(), tag.getId());
            }
        }

        List<GiftCertificate> actual = giftCertificateDAO.getAllSortedByName(true);
        assertEqualsCertificateLists(given, actual);
    }

    @Test
    void whenAddGiftCertificates_thenCorrectlyReturnThemSortedByNameDesc() throws PersistenceException {
        List<GiftCertificate> given = initCertificates(10);

        for (int i = 0; i < given.size(); i++) {
            given.get(i).setName(String.valueOf(i));
            given.get(i).setId(giftCertificateDAO.create(given.get(i)));

            for (Tag tag:  given.get(i).getTags()) {
                giftCertificateDAO.createCertificateTagRelation(given.get(i).getId(), tag.getId());
            }
        }

        Collections.reverse(given);
        List<GiftCertificate> actual = giftCertificateDAO.getAllSortedByName(false);
        assertEqualsCertificateLists(given, actual);
    }

    @Test
    void whenAddGiftCertificates_thenCorrectlyReturnThemByContent() throws PersistenceException {
        List<GiftCertificate> given = initCertificates(10);

        for (GiftCertificate certificate: given) {
            certificate.setId(giftCertificateDAO.create(certificate));

            for (Tag tag: certificate.getTags()) {
                giftCertificateDAO.createCertificateTagRelation(certificate.getId(), tag.getId());
            }
        }

        List<GiftCertificate> actual = giftCertificateDAO.getAll("Certificate description");
        assertEqualsCertificateLists(given, actual);
    }
}
