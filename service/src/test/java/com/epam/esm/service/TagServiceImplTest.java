package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.service.util.impl.TagValidatorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TagServiceImplTest {

    private TagDao tagDao;
    private TagService tagService;

    @BeforeEach
    public void init() {
        tagDao = Mockito.mock(TagDaoImpl.class);
        tagService = new TagServiceImpl(tagDao, new TagValidatorImpl());
    }

    @Test
    void whenGetTag_thenCorrectlyReturnItById() throws ServiceException {
        Tag given = new Tag(1, "spa");

        Mockito.when(tagDao.get(given.getId())).thenReturn(given);

        Tag actual = tagService.get(given.getId());
        assertEquals(given, actual);
        Mockito.verify(tagDao).get(given.getId());
    }

    @Test
    void whenGetTag_thenCorrectlyReturnItByName() throws ServiceException {
        Tag given = new Tag(1, "spa");

        Mockito.when(tagDao.get(given.getName())).thenReturn(given);

        Tag actual = tagService.get(given.getName());
        assertEquals(given, actual);
        Mockito.verify(tagDao).get(given.getName());
    }


    @Test
    void whenAddTags_thenCorrectlyReturnThem() throws ServiceException {
        List<Tag> expected = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            expected.add(new Tag(i, "Tag " + i));
        }

        Mockito.when(tagDao.getAll()).thenReturn(expected);

        List<Tag> actual = tagService.get();
        assertEquals(expected, actual);
        Mockito.verify(tagDao).getAll();
    }

    @Test
    void whenTryAddVoidTag_thenThrowException() {
        Tag tag = new Tag();

        try {
            tagService.create(tag);
        } catch (ServiceException e) {
            assertEquals("Failed to validate: tag name is empty", e.getMessage());
        }
    }

    @Test
    void whenTryDeleteNonExistingTag_thenThrowException() {
        Tag tag = new Tag(1, "spa");

        try {
            tagService.delete(tag.getId());
        } catch (ServiceException e) {
            assertEquals("Failed to delete tag because it id ("
                    + tag.getId() +") is not found", e.getMessage());
        }
        Mockito.verify(tagDao).delete(tag.getId());
    }
}
