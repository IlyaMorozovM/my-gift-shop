package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.request.CertificateRequestBody;
import com.epam.esm.service.request.SortParameter;
import com.epam.esm.service.request.SortType;
import com.epam.esm.service.util.CertificateValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class GiftCertificateServiceImpl implements GiftCertificateService {

    private static final Logger LOGGER = LogManager.getLogger(GiftCertificateServiceImpl.class);

    private final GiftCertificateDAO giftCertificateDAO;
    private final TagDao tagDao;
    private final CertificateValidator certificateValidator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDAO giftCertificateDAO, TagDao tagDao,
                                      CertificateValidator certificateValidator) {
        this.giftCertificateDAO = giftCertificateDAO;
        this.tagDao = tagDao;
        this.certificateValidator = certificateValidator;
    }

    @Override
    public GiftCertificate get(String name) throws ServiceException {
        certificateValidator.validateName(name);
        try {
            return giftCertificateDAO.get(name);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getGiftCertificate(String name): " + e.getMessage());
            throw new ServiceException("Failed to get certificate by it name: " + name,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public GiftCertificate get(int id) throws ServiceException {
        certificateValidator.validateId(id);
        try {
            return giftCertificateDAO.get(id);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getGiftCertificate(int id): " + e.getMessage());
            throw new ServiceException("Failed to get certificate by id: " + id,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> getAll() throws ServiceException {
        try {
            return giftCertificateDAO.getAll();
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllGiftCertificates(): " + e.getMessage());
            throw new ServiceException("Failed to get all certificates", ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> getBySearchingPart(String searchingPart) throws ServiceException {
        if (searchingPart == null || searchingPart.isEmpty()) {
            throw new ServiceException("Failed to validate: content is empty", ErrorCodeEnum.INVALID_INPUT);
        }

        try {
            return giftCertificateDAO.getAll(searchingPart);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllGiftCertificates(String content): " + e.getMessage());
            throw new ServiceException("Failed to get certificate by it content: " + searchingPart,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> getByTagName(String tagName) throws ServiceException {
        certificateValidator.validateName(tagName);
        try {
            return giftCertificateDAO.getByTagName(tagName);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getGiftCertificateByTagName(): " + e.getMessage());
            throw new ServiceException("Failed to get certificate Failed to get certificate by tag name: " + tagName,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> getAllSortedByName(boolean isAscending)
            throws ServiceException {
        try {
            return giftCertificateDAO.getAllSortedByName(isAscending);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllGiftCertificatesSortedByName(): " + e.getMessage());
            throw new ServiceException("Failed to get certificates", ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> getAllSortedByDate(boolean isAscending)
            throws ServiceException {
        try {
            return giftCertificateDAO.getAllSortedByDate(isAscending);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllGiftCertificatesSortedByDate(): " + e.getMessage());
            throw new ServiceException("Failed to get certificate", ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> get(CertificateRequestBody requestBody) throws ServiceException {
        if (requestBody == null) {
            return getAll();
        } else {
            return getGiftCertificatesByRequestBody(requestBody);
        }
    }

    private List<GiftCertificate> getGiftCertificatesByRequestBody(CertificateRequestBody requestBody)
            throws ServiceException {
        if (requestBody.getSearchByPart() != null) {
            return getBySearchingPart(requestBody.getSearchByPart());
        }
        if (requestBody.getSortType() != null && requestBody.getSortBy() != null) {
            return getSortedCertificates(requestBody.getSortType(), requestBody.getSortBy());
        }
        if (requestBody.getTagName() != null) {
            return getByTagName(requestBody.getTagName());
        }

        throw new ServiceException("Error: request input body is empty", ErrorCodeEnum.INVALID_SORT_INPUT);
    }

    public List<GiftCertificate> getSortedCertificates(SortType sortType, SortParameter sortBy)
            throws ServiceException {
        switch (sortBy) {
            case DATE : return getAllSortedByDate(isAscending(sortType));
            case NAME : return getAllSortedByName(isAscending(sortType));
            default: throw new NoSuchElementException("Not found sort criteria");
        }
    }

    private boolean isAscending(SortType sort) {
        return sort == SortType.ASC;
    }

    @Override
    @Transactional
    public GiftCertificate create(GiftCertificate giftCertificate) throws ServiceException {
        certificateValidator.validateCertificate(giftCertificate);
        try {
            giftCertificate.setCreateDate(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
            giftCertificate.setLastUpdateDate(giftCertificate.getCreateDate());

            int id = giftCertificateDAO.create(giftCertificate);
            giftCertificate.setId(id);

            addNewTagsToCertificate(giftCertificate);

            return giftCertificate;
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in addGiftCertificate(): " + e.getMessage());
            throw new ServiceException("Failed to add certificate: certificate already exist",
                    ErrorCodeEnum.FAILED_TO_ADD_CERTIFICATE);
        } catch (PersistenceException e) {
            LOGGER.error("Following exception was thrown: " + e.getMessage());
            throw new ServiceException(e.getMessage(), ErrorCodeEnum.FAILED_TO_ADD_CERTIFICATE);
        }
    }

    @Override
    @Transactional
    public void delete(int id) throws ServiceException {
        GiftCertificate giftCertificate = get(id);
        try {
            giftCertificateDAO.deleteAllCertificateTagRelations(giftCertificate.getId());

            if (!giftCertificateDAO.delete(giftCertificate.getId())) {
                LOGGER.error("Failed to delete certificate");
                throw new ServiceException("Failed to delete certificate", ErrorCodeEnum.FAILED_TO_DELETE_CERTIFICATE);
            }
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in deleteGiftCertificate(): " + e.getMessage());
            throw new ServiceException("Failed to delete certificate", ErrorCodeEnum.FAILED_TO_DELETE_CERTIFICATE);
        }
    }

    @Override
    @Transactional
    public GiftCertificate update(GiftCertificate giftCertificate, int id) throws ServiceException {
        giftCertificate.setId(id);
        certificateValidator.validateCertificate(giftCertificate);
        try {
            giftCertificate.setLastUpdateDate(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));

            addNewTagsToCertificate(giftCertificate);

            if (!giftCertificateDAO.update(giftCertificate)) {
                LOGGER.error("Failed to update certificate");
                throw new ServiceException("Failed to update certificate", ErrorCodeEnum.FAILED_TO_UPDATE_CERTIFICATE);
            }

            giftCertificateDAO.deleteAllCertificateTagRelations(id);
            addNewTagsToCertificate(giftCertificate);
            return get(id);
        } catch (DataAccessException | PersistenceException e) {
            LOGGER.error("Following exception was thrown in updateGiftCertificate(): " + e.getMessage());
            throw new ServiceException("Failed to update certificate", ErrorCodeEnum.FAILED_TO_UPDATE_CERTIFICATE);
        }
    }

    public void addNewTagsToCertificate(GiftCertificate giftCertificate) throws PersistenceException {
        List<Tag> tagsInDataSource = tagDao.getAll();
        List<String> tagNamesInDataSource = new ArrayList<>();
        for (Tag tag: tagsInDataSource){
            tagNamesInDataSource.add(tag.getName());
        }
        for (Tag tag : giftCertificate.getTags()) {
            if (!tagNamesInDataSource.contains(tag.getName())) {
                tag.setId(tagDao.create(tag));
            } else {
                tag.setId(tagDao.get(tag.getName()).getId());
            }

            try {
                giftCertificateDAO.createCertificateTagRelation(giftCertificate.getId(), tag.getId());
            } catch (DataAccessException ignored) {
            }
        }
    }
}
