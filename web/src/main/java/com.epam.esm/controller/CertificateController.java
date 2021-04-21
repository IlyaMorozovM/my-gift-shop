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
public class CertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public CertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping("/certificates")
    public List<GiftCertificate> getGiftCertificates(
            @RequestBody(required = false) CertificateRequestBody request) throws ServiceException {
           return giftCertificateService.getGiftCertificates(request);
    }

    @GetMapping("/certificates/{id}")
    public GiftCertificate getGiftCertificate(@PathVariable int id) throws ServiceException {
        return giftCertificateService.getGiftCertificate(id);
    }

    @PostMapping("/certificates")
    public GiftCertificate addGiftCertificate(@RequestBody GiftCertificate giftCertificate) throws ServiceException {
        return giftCertificateService.addGiftCertificate(giftCertificate);
    }

    @DeleteMapping("/certificates/{id}")
    public HttpStatus deleteGiftCertificate( @PathVariable int id) throws ServiceException {
        giftCertificateService.deleteGiftCertificate(id);
        return HttpStatus.OK;
    }

    @PutMapping("/certificates/{id}")
    public GiftCertificate updateGiftCertificate(
            @RequestBody GiftCertificate giftCertificate, @PathVariable int id) throws ServiceException {
        return giftCertificateService.updateGiftCertificate(giftCertificate, id);
    }
}
