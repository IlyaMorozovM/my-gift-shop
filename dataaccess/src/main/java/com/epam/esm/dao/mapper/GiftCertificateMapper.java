package com.epam.esm.dao.mapper;

import com.epam.esm.dao.util.GiftCertificateExtractorUtil;
import com.epam.esm.model.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class GiftCertificateMapper implements RowMapper<GiftCertificate> {

    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        return GiftCertificateExtractorUtil.extractGiftCertificate(rs);
    }
}
