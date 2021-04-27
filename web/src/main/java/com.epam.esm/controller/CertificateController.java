package com.epam.esm.controller;

import com.epam.esm.service.request.CertificateRequestBody;
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
    public List<GiftCertificate> getGiftCertificates(
            @RequestBody(required = false) CertificateRequestBody request) throws ServiceException {
           return giftCertificateService.get(request);
    }

//    @GetMapping
//    public List<GiftCertificate> getGiftCertificatesWithSorting(
//            @RequestParam(name = "sortBy") String sortBy,
//            @RequestParam(name = "sortParam") String sortParam) throws ServiceException {
//        SortType sortType;
//        SortParameter sortParameter;
//        if (sortBy.equals("date")){
//            sortParameter = SortParameter.DATE;
//        } else {
//            sortParameter = SortParameter.NAME;
//        }
//        if (sortParam.equals("asc")){
//            sortType = SortType.ASC;
//        } else {
//            sortType = SortType.DESC;
//        }
//        return giftCertificateService.getSortedCertificates(sortType, sortParameter);
//    }

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
    public HttpStatus deleteGiftCertificate(@PathVariable int id) throws ServiceException {
        giftCertificateService.delete(id);
        return HttpStatus.NO_CONTENT;
    }

    @PutMapping("/{id}")
    public GiftCertificate updateGiftCertificate(
            @RequestBody GiftCertificate giftCertificate, @PathVariable int id) throws ServiceException {
        return giftCertificateService.update(giftCertificate, id);
    }
}
