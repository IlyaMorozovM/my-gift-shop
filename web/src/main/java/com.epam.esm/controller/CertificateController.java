package com.epam.esm.controller;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.request.SortParameter;
import com.epam.esm.service.request.SortType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public CertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping
    public List<GiftCertificate> getGiftCertificatesWithSorting(
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "sortType", required = false) String sortType,
            @RequestParam(name = "tagName", required = false) String tagName,
            @RequestParam(name = "searchByPart", required = false) String searchByPart) throws ServiceException {
        if (isSortParamsNotNull(sortBy, sortType)){
            SortParameter sortParameter = SortParameter.valueOf(sortBy.toUpperCase());
            SortType requestedSortType = SortType.valueOf(sortType.toUpperCase());
            return giftCertificateService.getSortedCertificates(requestedSortType, sortParameter);
        } else if (tagName != null){
            return giftCertificateService.getByTagName(tagName);
        } else if (searchByPart != null){
            return giftCertificateService.getBySearchingPart(searchByPart);
        }
        return giftCertificateService.getAll();
    }

    private boolean isSortParamsNotNull(String sortBy, String sortParam){
        return sortBy != null && sortParam != null;
    }

    @GetMapping("/{id}")
    public GiftCertificate getGiftCertificate(@PathVariable int id) throws ServiceException {
        return giftCertificateService.get(id);
    }

    @PostMapping
    public ResponseEntity<Object> addGiftCertificate(@RequestBody GiftCertificate giftCertificate, HttpServletRequest request) throws ServiceException {
        GiftCertificate certificate = giftCertificateService.create(giftCertificate);
        int id = certificate.getId();
        HttpHeaders headers = new HttpHeaders();
        String requestUrl = request.getRequestURL().toString();
        headers.setLocation(URI.create(requestUrl + "/" + id));
        return new ResponseEntity<>(certificate, headers, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGiftCertificate(@PathVariable int id) throws ServiceException {
        giftCertificateService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public GiftCertificate updateGiftCertificate(
            @RequestBody GiftCertificate giftCertificate, @PathVariable int id) throws ServiceException {
        return giftCertificateService.update(giftCertificate, id);
    }
}
