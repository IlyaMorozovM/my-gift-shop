package com.epam.esm.service.util;

import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ServiceException;

public interface TagValidator {
    void validateTag(Tag tag) throws ServiceException;
    void validateId(int id) throws ServiceException;
    void validateName(String name) throws ServiceException;
}
