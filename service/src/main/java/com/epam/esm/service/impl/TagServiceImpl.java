package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.util.TagValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;

public class TagServiceImpl implements TagService {

    private static final Logger LOGGER = LogManager.getLogger(TagServiceImpl.class);

    private final TagDao tagDao;
    private final TagValidator tagValidator;

    @Autowired
    public TagServiceImpl(TagDao tagDao, TagValidator tagValidator) {
        this.tagDao = tagDao;
        this.tagValidator = tagValidator;
    }

    @Override
    public Tag get(String name) throws ServiceException {
        tagValidator.validateName(name);
        try {
            return tagDao.get(name);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getTag(String name): " + e.getMessage());
            throw new ServiceException("Failed to get tag by it name: " + name,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public Tag get(int id) throws ServiceException {
        tagValidator.validateId(id);
        try {
            return tagDao.get(id);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getTag(int id): " + e.getMessage());
            throw new ServiceException("Failed to get tag by id: " + id,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public List<Tag> get() throws ServiceException {
        try {
            return tagDao.getAll();
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllTags(): " + e.getMessage());
            throw new ServiceException("Failed to get all tags", ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public Tag create(Tag tag) throws ServiceException {
        tagValidator.validateTag(tag);
        try {
            int id = tagDao.create(tag);
            tag.setId(id);
            return tag;
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in addTag(): " + e.getMessage());
            throw new ServiceException("Failed to add tag: tag already exists", ErrorCodeEnum.FAILED_TO_ADD_TAG);
        } catch (PersistenceException e) {
            LOGGER.error("Following exception was thrown in addTag(): " + e.getMessage());
            throw new ServiceException(e.getMessage(), ErrorCodeEnum.FAILED_TO_ADD_TAG);
        }
    }

    @Override
    public void delete(int tagId) throws ServiceException {
        tagValidator.validateId(tagId);
        try {
            if (!tagDao.delete(tagId)) {
                LOGGER.error("Failed to delete tag");
                throw new ServiceException("Failed to delete tag because tag with id = " + tagId + " is not found",
                        ErrorCodeEnum.FAILED_TO_DELETE_TAG);
            }
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in deleteTag(): " + e.getMessage());
            throw new ServiceException("Failed to delete tag by id: " + tagId,
                    ErrorCodeEnum.FAILED_TO_DELETE_TAG);
        }
    }
}
