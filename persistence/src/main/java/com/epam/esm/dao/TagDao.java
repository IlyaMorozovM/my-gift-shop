package com.epam.esm.dao;


import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.model.Tag;
import java.util.List;

/**
 * This interface provides with ability to
 * transfer Tag in and out
 * of data source.
 *
 * @author Ilya Morozov
 */
public interface TagDao {

    /**
     * Retrieves data of Tag from
     * data source by name
     * which equals to {@param name}.
     *
     * @param name tag name.
     * @return Tag.
     */
    Tag getTag(String name);

    /**
     * Retrieves data of Tag from
     * data source by id
     * which equals to {@param id}.
     *
     * @param id tag id.
     * @return Tag.
     */
    Tag getTag(int id);

    /**
     * Retrieves all Tag from data source.
     *
     * @return List<Tag> - all existing tags in data source.
     */
    List<Tag> getAllTags();

    /**
     * Adds new Tag to data source.
     *
     * @param tag Tag which to be added to data source.
     * @return id of a Tag from data source.
     * @throws PersistenceException when failed to add Tag to data source.
     */
    int addTag(Tag tag) throws PersistenceException;

    /**
     * Deletes Tag from data source.
     *
     * @param tagId id of a Tag which to delete from data source.
     * @return whether deleting was successful.
     */
    boolean deleteTag(int tagId);
}
