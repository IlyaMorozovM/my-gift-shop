package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.dao.impl.GiftCertificateDaoImpl;
import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.service.request.CertificateRequestBody;
import com.epam.esm.service.request.SortParameter;
import com.epam.esm.service.request.SortType;
import com.epam.esm.service.util.impl.CertificateValidatorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GiftCertificateServiceImplTest {

    private TagDao tagDao;
    private GiftCertificateDAO giftCertificateDAO;
    private GiftCertificateService giftCertificateService;

    @BeforeEach
    public void init() {
        tagDao = Mockito.mock(TagDaoImpl.class);
        giftCertificateDAO = Mockito.mock(GiftCertificateDaoImpl.class);

        giftCertificateService = new GiftCertificateServiceImpl(
                giftCertificateDAO, tagDao, new CertificateValidatorImpl());
    }

    private List<GiftCertificate> initCertificates() {
        List<GiftCertificate> certificates = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GiftCertificate certificate = initCertificate();
            certificate.setName("name" + i);

            certificates.add(certificate);
        }

        return certificates;
    }

    private GiftCertificate initCertificate() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1);
        giftCertificate.setName("Tourism");
        giftCertificate.setDescription("Description");
        giftCertificate.setPrice(new BigDecimal("10"));
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        giftCertificate.setDuration(10);
        Tag tag = new Tag(1, "spa");
        giftCertificate.getTags().add(tag);

        return giftCertificate;
    }

    @Test
    void whenGetCertificate_thenCorrectlyReturnsItById() throws ServiceException {
        GiftCertificate given = new GiftCertificate();
        given.setId(1);
        given.setName("Tourism");

        Mockito.when(giftCertificateDAO.get(given.getId())).thenReturn(given);

        GiftCertificate actual = giftCertificateService.get(given.getId());
        assertEquals(given, actual);
        Mockito.verify(giftCertificateDAO).get(given.getId());
    }

    @Test
    void whenGetCertificate_thenCorrectlyReturnsItByName() throws ServiceException {
        GiftCertificate given = new GiftCertificate();
        given.setId(1);
        given.setName("Tourism");

        Mockito.when(giftCertificateDAO.get(given.getName())).thenReturn(given);

        GiftCertificate actual = giftCertificateService.get(given.getName());
        assertEquals(given, actual);
        Mockito.verify(giftCertificateDAO).get(given.getName());
    }

    @Test
    void whenAddGiftCertificates_thenCorrectlyReturnThem() throws ServiceException {
        List<GiftCertificate> given = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            GiftCertificate certificate = new GiftCertificate();
            certificate.setId(i);
            certificate.setName("Tag " + i);
            given.add(certificate);
        }

        Mockito.when(giftCertificateService.getAll()).thenReturn(given);

        List<GiftCertificate> actual = giftCertificateService.getAll();
        assertEquals(given, actual);
        Mockito.verify(giftCertificateDAO).getAll();
    }

    @Test
    void whenAddCertificate_thenReturnItId() throws PersistenceException, ServiceException {
        GiftCertificate givenCertificate = initCertificate();
        Tag givenTag = new Tag(1,"spa");
        int expectedId = 1;

        Mockito.when(tagDao.create(givenTag)).thenReturn(1);
        Mockito.when(giftCertificateDAO.createCertificateTagRelation(
                givenCertificate.getId(), givenTag.getId())).thenReturn(true);
        Mockito.when(giftCertificateDAO.create(givenCertificate)).thenReturn(expectedId);

        givenCertificate = giftCertificateService.create(givenCertificate);
        assertEquals(expectedId, givenCertificate.getId());
        Mockito.verify(tagDao).create(givenTag);
        Mockito.verify(giftCertificateDAO)
                .createCertificateTagRelation(givenCertificate.getId(), givenCertificate.getId());
        Mockito.verify(giftCertificateDAO).create(givenCertificate);
    }

    @Test
    void whenTryAddVoidCertificate_thenThrowException() {
        GiftCertificate giftCertificate = new GiftCertificate();

        try {
            giftCertificateService.create(giftCertificate);
        } catch (ServiceException e) {
            assertEquals("Failed to validate: certificate name is empty", e.getMessage());
        }
    }

//    @Test
//    public void whenTryDeleteCertificate_thenThrowException() {
//        GiftCertificate givenCertificate = initCertificate();
//
//        try {
//            giftCertificateService.deleteGiftCertificate(givenCertificate.getId());
//        } catch (ServiceException e) {
//            assertEquals("Failed to validate: certificate is empty", e.getMessage());
//        }
//    }

    @Test
    void whenAddCertificate_thenReturnThemSortedByDateAsc() throws ServiceException {
        CertificateRequestBody givenRequestBody = new CertificateRequestBody();
        givenRequestBody.setSortType(SortType.ASC);
        givenRequestBody.setSortBy(SortParameter.DATE);
        List<GiftCertificate> givenCertificates = initCertificates();

        Mockito.when(giftCertificateDAO.getAllSortedByDate(true))
                .thenReturn(givenCertificates);

        List<GiftCertificate> actual = giftCertificateService.get(givenRequestBody);
        assertEquals(givenCertificates, actual);
        Mockito.verify(giftCertificateDAO).getAllSortedByDate(true);
    }

    @Test
    void whenAddCertificate_thenReturnThemSortedByDateDesc() throws ServiceException {
        CertificateRequestBody givenRequestBody = new CertificateRequestBody();
        givenRequestBody.setSortType(SortType.DESC);
        givenRequestBody.setSortBy(SortParameter.DATE);
        List<GiftCertificate> givenCertificates = initCertificates();
        Collections.reverse(givenCertificates);

        Mockito.when(giftCertificateDAO.getAllSortedByDate(false))
                .thenReturn(givenCertificates);

        List<GiftCertificate> actual = giftCertificateService.get(givenRequestBody);
        assertEquals(givenCertificates, actual);
        Mockito.verify(giftCertificateDAO).getAllSortedByDate(false);
    }
}
