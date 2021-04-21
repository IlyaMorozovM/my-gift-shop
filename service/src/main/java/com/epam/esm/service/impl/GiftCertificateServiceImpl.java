package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.request.CertificateRequestBody;
import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.request.SortParameter;
import com.epam.esm.service.request.SortType;
import com.epam.esm.service.util.CertificateValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
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
    public GiftCertificate getGiftCertificate(String name) throws ServiceException {
        certificateValidator.validateName(name);
        try {
            return giftCertificateDAO.getGiftCertificate(name);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getGiftCertificate(String name): " + e.getMessage());
            throw new ServiceException("Failed to get certificate by it name: " + name,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public GiftCertificate getGiftCertificate(int id) throws ServiceException {
        certificateValidator.validateId(id);
        try {
            return giftCertificateDAO.getGiftCertificate(id);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getGiftCertificate(int id): " + e.getMessage());
            throw new ServiceException("Failed to get certificate by it id: " + id,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> geAllCertificates() throws ServiceException {
        try {
            return giftCertificateDAO.getAllGiftCertificates();
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllGiftCertificates(): " + e.getMessage());
            throw new ServiceException("Failed to get all certificates", ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> getGiftCertificatesByContent(String content) throws ServiceException {
        if (content == null || content.isEmpty()) {
            throw new ServiceException("Failed to validate: content is empty", ErrorCodeEnum.INVALID_INPUT);
        }

        try {
            return giftCertificateDAO.getAllGiftCertificates(content);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllGiftCertificates(String content): " + e.getMessage());
            throw new ServiceException("Failed to get certificate by it content: " + content,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> getGiftCertificateByTagName(String tagName) throws ServiceException {
        certificateValidator.validateName(tagName);
        try {
            return giftCertificateDAO.getGiftCertificateByTagName(tagName);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getGiftCertificateByTagName(): " + e.getMessage());
            throw new ServiceException("Failed to get certificate Failed to get certificate by tag name: " + tagName,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificatesSortedByName(boolean isAscending)
            throws ServiceException {
        try {
            return giftCertificateDAO.getAllGiftCertificatesSortedByName(isAscending);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllGiftCertificatesSortedByName(): " + e.getMessage());
            throw new ServiceException("Failed to get certificates", ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificatesSortedByDate(boolean isAscending)
            throws ServiceException {
        try {
            return giftCertificateDAO.getAllGiftCertificatesSortedByDate(isAscending);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllGiftCertificatesSortedByDate(): " + e.getMessage());
            throw new ServiceException("Failed to get certificate", ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> getGiftCertificates(CertificateRequestBody requestBody) throws ServiceException {
        if (requestBody == null) {
            return geAllCertificates();
        } else {
            return getGiftCertificatesByRequestBody(requestBody);
        }
    }

    private List<GiftCertificate> getGiftCertificatesByRequestBody(CertificateRequestBody requestBody)
            throws ServiceException {
        if (requestBody.getContent() != null) {
            return getGiftCertificatesByContent(requestBody.getContent());
        }
        if (requestBody.getSortType() != null && requestBody.getSortBy() != null) {
            return getSortedCertificates(requestBody.getSortType(), requestBody.getSortBy());
        }
        if (requestBody.getTagName() != null) {
            return getGiftCertificateByTagName(requestBody.getTagName());
        }

        throw new ServiceException("Error: request input body is empty", ErrorCodeEnum.INVALID_SORT_INPUT);
    }

    private List<GiftCertificate> getSortedCertificates(SortType sortType, SortParameter sortBy)
            throws ServiceException {
        switch (sortBy) {
            case DATE : return getAllGiftCertificatesSortedByDate(isAscending(sortType));
            case NAME : return getAllGiftCertificatesSortedByName(isAscending(sortType));
            default: throw new NoSuchElementException("Not found sort criteria");
        }
    }

    private boolean isAscending(SortType sort) {
        return sort == SortType.ASC;
    }

    @Override
    @Transactional
    public GiftCertificate addGiftCertificate(GiftCertificate giftCertificate) throws ServiceException {
        certificateValidator.validateCertificate(giftCertificate);
        try {
            giftCertificate.setCreateDate(ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
            giftCertificate.setLastUpdateDate(giftCertificate.getCreateDate());

            int id = giftCertificateDAO.addGiftCertificate(giftCertificate);
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
    public void deleteGiftCertificate(int id) throws ServiceException {
        GiftCertificate giftCertificate = getGiftCertificate(id);
        try {
            giftCertificateDAO.deleteAllCertificateTagRelations(giftCertificate.getId());

            if (!giftCertificateDAO.deleteGiftCertificate(giftCertificate.getId())) {
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
    public GiftCertificate updateGiftCertificate(GiftCertificate giftCertificate, int id) throws ServiceException {
        giftCertificate.setId(id);
        certificateValidator.validateCertificate(giftCertificate);
        try {
            giftCertificate.setLastUpdateDate(ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));

            addNewTagsToCertificate(giftCertificate);

            if (!giftCertificateDAO.updateGiftCertificate(giftCertificate)) {
                LOGGER.error("Failed to update certificate");
                throw new ServiceException("Failed to update certificate", ErrorCodeEnum.FAILED_TO_UPDATE_CERTIFICATE);
            }
            return getGiftCertificate(id);
        } catch (DataAccessException | PersistenceException e) {
            LOGGER.error("Following exception was thrown in updateGiftCertificate(): " + e.getMessage());
            throw new ServiceException("Failed to update certificate", ErrorCodeEnum.FAILED_TO_UPDATE_CERTIFICATE);
        }
    }

    public void addNewTagsToCertificate(GiftCertificate giftCertificate) throws PersistenceException {
        List<Tag> tagsInDataSource = tagDao.getAllTags();
        for (Tag tag : giftCertificate.getTags()) {
            if (!tagsInDataSource.contains(tag)) {
                tag.setId(tagDao.addTag(tag));
            }

            try {
                giftCertificateDAO.createCertificateTagRelation(giftCertificate.getId(), tag.getId());
            } catch (DataAccessException ignored) {
            }
        }
    }
}
