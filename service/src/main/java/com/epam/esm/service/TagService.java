package com.epam.esm.service;

import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;

/**
 * This interface provides with ability to create
 * transactions with Tag in and out
 * of persistence layer.
 *
 * @author Ilya Morozov
 */
public interface TagService {

    /**
     * Retrieves data of Tag from
     * persistence layer by name
     * which equals to {@param name}.
     *
     * @param name tag name.
     * @throws ServiceException when failed to get Tag from persistence layer.
     * @return Tag.
     */
    Tag getTag(String name) throws ServiceException;

    /**
     * Retrieves data of Tag from
     * persistence layer by id
     * which equals to {@param id}.
     *
     * @param id tag id.
     * @throws ServiceException when failed to get Tag from persistence layer.
     * @return Tag.
     */
    Tag getTag(int id) throws ServiceException;

    /**
     * Retrieves all Tag from persistence layer.
     *
     * @throws ServiceException when failed to get Tag from persistence layer.
     * @return List<Tag> - all existing tags in persistence layer.
     */
    List<Tag> getAllTags() throws ServiceException;

    /**
     * Adds new Tag to persistence layer.
     *
     * @param tag Tag which to be added to persistence layer.
     * @throws ServiceException when failed to add Tag to persistence layer.
     * @return id of a Tag from persistence layer.
     */
    Tag addTag(Tag tag) throws ServiceException;

    /**
     * Deletes Tag from persistence layer.
     *
     * @param tagId id of a Tag which to delete from persistence layer.
     * @throws ServiceException when failed to delete Tag from persistence layer.
     */
    void deleteTag(int tagId) throws ServiceException;
}
