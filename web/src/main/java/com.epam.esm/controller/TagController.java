package com.epam.esm.controller;

import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<Tag> getTags() throws ServiceException {
        return tagService.get();
    }

    @GetMapping("/{id}")
    public Tag getTag(@PathVariable int id) throws ServiceException {
        return tagService.get(id);
    }

    @PostMapping
    public Tag addTag(@RequestBody Tag tag) throws ServiceException {
        return tagService.create(tag);
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteTag(@PathVariable int id) throws ServiceException {
        tagService.delete(id);
        return HttpStatus.OK;
    }
}
