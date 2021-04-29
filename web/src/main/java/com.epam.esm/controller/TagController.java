package com.epam.esm.controller;

import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
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
    public ResponseEntity<Object> addTag(@RequestBody Tag requestedTag, HttpServletRequest request) throws ServiceException {
        Tag createdTag = tagService.create(requestedTag);
        int id = createdTag.getId();
        HttpHeaders headers = new HttpHeaders();
        String requestUrl = request.getRequestURL().toString();
        headers.setLocation(URI.create(requestUrl + "/" + id));
        return new ResponseEntity<>(createdTag, headers, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTag(@PathVariable int id) throws ServiceException {
        tagService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
