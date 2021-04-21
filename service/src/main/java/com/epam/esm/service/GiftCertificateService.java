package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.request.CertificateRequestBody;

import java.util.List;

/**
 * This interface provides with ability to create
 * transactions with GiftCertificate in and out
 * of persistence layer.
 *
 * @author Ilya Morozov
 */
public interface GiftCertificateService {

    /**
     * Retrieves data of GiftCertificate from
     * persistence layer by name
     * which equals to {@param name}.
     *
     * @param name certificate name.
     * @throws ServiceException when failed to get GiftCertificate from persistence layer.
     * @return GiftCertificate.
     */
    GiftCertificate getGiftCertificate(String name) throws ServiceException;

    /**
     * Retrieves data of GiftCertificate from
     * persistence layer by id
     * which equals to {@param id}.
     *
     * @param id certificate id.
     * @throws ServiceException when failed to get GiftCertificate from persistence layer.
     * @return GiftCertificate.
     */
    GiftCertificate getGiftCertificate(int id) throws ServiceException;

    /**
     * Retrieves all GiftCertificate from persistence layer.
     * @throws ServiceException when failed to get GiftCertificate from persistence layer.
     * @return List<GiftCertificate> - all existing certificates in persistence layer.
     */
    List<GiftCertificate> geAllCertificates() throws ServiceException;

    /**
     * Retrieves GiftCertificate from persistence layer
     * by content which this GiftCertificate contains
     * in it name or description.
     *
     * @param content GiftCertificate name or description.
     * @throws ServiceException when failed to get GiftCertificate from persistence layer.
     * @return List<GiftCertificate> - existing certificates in persistence layer.
     */
    List<GiftCertificate> getGiftCertificatesByContent(String content) throws ServiceException;

    /**
     * Retrieves GiftCertificate from persistence layer
     * by name of a Tag which this GiftCertificate has.
     *
     * @param tagName name of a Tag.
     * @throws ServiceException when failed to get GiftCertificate from persistence layer.
     * @return List<GiftCertificate> - existing certificates in persistence layer.
     */
    List<GiftCertificate> getGiftCertificateByTagName(String tagName) throws ServiceException;

    /**
     * Retrieves all GiftCertificate from persistence layer
     * and sorts it by name according to isAscending.
     *
     * @param isAscending asc or desc sort.
     * @throws ServiceException when failed to get GiftCertificate from persistence layer.
     * @return List<GiftCertificate> - sorted certificates in persistence layer.
     */
    List<GiftCertificate> getAllGiftCertificatesSortedByName(boolean isAscending) throws ServiceException;

    /**
     * Retrieves all GiftCertificate from persistence layer
     * and sorts it by date according to isAscending.
     *
     * @param isAscending asc or desc sort.
     * @throws ServiceException when failed to get GiftCertificate from persistence layer.
     * @return List<GiftCertificate> - sorted certificates in persistence layer.
     */
    List<GiftCertificate> getAllGiftCertificatesSortedByDate(boolean isAscending) throws ServiceException;

    /**
     * Retrieves GiftCertificate from persistence layer
     * using one of non null fields of CertificateRequestBody.
     *
     * @param requestBody representation of http request body.
     * @throws ServiceException when failed to get GiftCertificate from persistence layer.
     * @return List<GiftCertificate> - certificates from persistence layer.
     */
    List<GiftCertificate> getGiftCertificates(CertificateRequestBody requestBody) throws ServiceException;

    /**
     * Adds new GiftCertificate to persistence layer.
     *
     * @param giftCertificate GiftCertificate which to add to persistence layer.
     * @throws ServiceException when failed to add GiftCertificate to persistence layer.
     * @return GiftCertificate from persistence layer.
     */
    GiftCertificate addGiftCertificate(GiftCertificate giftCertificate) throws ServiceException;

    /**
     * Deletes GiftCertificate from persistence layer.
     *
     * @param id id of GiftCertificate which to delete from persistence layer.
     * @throws ServiceException when failed to delete GiftCertificate from persistence layer.
     */
    void deleteGiftCertificate(int id) throws ServiceException;

    /**
     * Updates GiftCertificate in persistence layer.
     * Null or default values in GiftCertificate are not updated.
     *
     * @param giftCertificate GiftCertificate which to update in persistence layer.
     * @param id id of GiftCertificate which to update in persistence layer.
     * @throws ServiceException when failed to update GiftCertificate in persistence layer.
     * @return updated GiftCertificate.
     */
    GiftCertificate updateGiftCertificate(GiftCertificate giftCertificate, int id) throws ServiceException;
}
