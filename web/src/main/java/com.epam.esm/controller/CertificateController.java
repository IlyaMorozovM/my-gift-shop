package com.epam.esm.controller;

import com.epam.esm.service.request.CertificateRequestBody;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public GiftCertificate getGiftCertificate(@PathVariable int id) throws ServiceException {
        return giftCertificateService.get(id);
    }

    @PostMapping
    public GiftCertificate addGiftCertificate(@RequestBody GiftCertificate giftCertificate) throws ServiceException {
        return giftCertificateService.create(giftCertificate);
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteGiftCertificate( @PathVariable int id) throws ServiceException {
        giftCertificateService.delete(id);
        return HttpStatus.OK;
    }

    @PutMapping("/{id}")
    public GiftCertificate updateGiftCertificate(
            @RequestBody GiftCertificate giftCertificate, @PathVariable int id) throws ServiceException {
        return giftCertificateService.update(giftCertificate, id);
    }
}
