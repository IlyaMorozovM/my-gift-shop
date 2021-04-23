package com.epam.esm.service.util;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.exception.ServiceException;

public interface CertificateValidator {
    void validateCertificate(GiftCertificate giftCertificate) throws ServiceException;
    void validateId(int id) throws ServiceException;
    void validateName(String name) throws ServiceException;
    void validateDescription(String description) throws ServiceException;
}
