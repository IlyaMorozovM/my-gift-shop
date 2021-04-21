package com.epam.esm.dao;

import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.model.GiftCertificate;

import java.util.List;

/**
 * This interface provides with ability to
 * transfer GiftCertificate in and out
 * of data source.
 *
 * @author Ilya Morozov
 */
public interface GiftCertificateDAO {

    /**
     * Retrieves data of GiftCertificate from
     * data source by name
     * which equals to {@param name}.
     *
     * @param name certificate name.
     * @return GiftCertificate.
     */
    GiftCertificate getGiftCertificate(String name);

    /**
     * Retrieves data of GiftCertificate from
     * data source by id
     * which equals to {@param id}.
     *
     * @param id certificate id.
     * @return GiftCertificate.
     */
    GiftCertificate getGiftCertificate(int id);

    /**
     * Retrieves all GiftCertificate from data source.
     *
     * @return List<GiftCertificate> - all existing certificates in data source.
     */
    List<GiftCertificate> getAllGiftCertificates();

    /**
     * Retrieves GiftCertificate from data source
     * by content which this GiftCertificate contains
     * in it name or description.
     *
     * @param content GiftCertificate name or description.
     * @return List<GiftCertificate> - existing certificates in data source.
     */
    List<GiftCertificate> getAllGiftCertificates(String content);

    /**
     * Retrieves GiftCertificate from data source
     * by name of a Tag which this GiftCertificate has.
     *
     * @param tagName name of a Tag.
     * @return List<GiftCertificate> - existing certificates in data source.
     */
    List<GiftCertificate> getGiftCertificateByTagName(String tagName);

    /**
     * Retrieves all GiftCertificate from data source
     * and sorts it by name according to isAscending.
     *
     * @param isAscending asc or desc sort.
     * @return List<GiftCertificate> - sorted certificates in data source.
     */
    List<GiftCertificate> getAllGiftCertificatesSortedByName(boolean isAscending);

    /**
     * Retrieves all GiftCertificate from data source
     * and sorts it by date according to isAscending.
     *
     * @param isAscending asc or desc sort.
     * @return List<GiftCertificate> - sorted certificates in data source.
     */
    List<GiftCertificate> getAllGiftCertificatesSortedByDate(boolean isAscending);

    /**
     * Adds new GiftCertificate to data source.
     *
     * @param giftCertificate GiftCertificate which to be added to data source.
     * @return id of a GiftCertificate from data source.
     * @throws PersistenceException when failed to add GiftCertificate to data source.
     */
    int addGiftCertificate(GiftCertificate giftCertificate) throws PersistenceException;

    /**
     * Deletes GiftCertificate from data source.
     *
     * @param id id of GiftCertificate which to deleted from data source.
     * @return whether transaction was successful.
     */
    boolean deleteGiftCertificate(int id);

    /**
     * Updates GiftCertificate in data source.
     * Null or default values in GiftCertificate are not updated.
     *
     * @param giftCertificate GiftCertificate which to update in data source.
     * @return whether transaction was successful.
     */
    boolean updateGiftCertificate(GiftCertificate giftCertificate) throws PersistenceException;

    /**
     * Creates many to many relation with GiftCertificate and Tag.
     *
     * @param certificateId GiftCertificate id which to create a many to many relation with.
     * @param tagId Tag id  which to create a many to many relation with.
     * @return whether transaction was successful.
     */
    boolean createCertificateTagRelation(int certificateId, int tagId);

    /**
     * Deletes many to many relation with GiftCertificate and Tag.
     *
     * @param certificateId GiftCertificate id which to delete a many to many relation with.
     */
    void deleteAllCertificateTagRelations(int certificateId);
}
