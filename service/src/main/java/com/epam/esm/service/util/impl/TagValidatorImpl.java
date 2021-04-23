package com.epam.esm.service.util.impl;

import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.util.TagValidator;
import org.springframework.stereotype.Component;

@Component
public class TagValidatorImpl implements TagValidator {

    @Override
    public void validateTag(Tag tag) throws ServiceException {
        if (tag == null) {
            throw new ServiceException("Failed to validate: tag is empty", ErrorCodeEnum.TAG_VALIDATION_ERROR);
        }
        validateId(tag.getId());
        validateName(tag.getName());
    }

    public void validateId(int id) throws ServiceException {
        if (id < 0) {
            //TODO: without error code
            throw new ServiceException("Failed to validate: tag id is negative",
                    ErrorCodeEnum.TAG_VALIDATION_ERROR);
        }
    }

    public void validateName(String name) throws ServiceException {
        if (name == null || name.isEmpty()) {
            throw new ServiceException("Failed to validate: tag name is empty",
                    ErrorCodeEnum.TAG_VALIDATION_ERROR);
        }
    }
}
