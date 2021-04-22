package com.epam.esm.dao.util;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class GiftCertificateExtractorUtil {

    private static final String CERTIFICATE_ID_COLUMN = "ID";
    private static final String CERTIFICATE_NAME_COLUMN = "Name";
    private static final String CERTIFICATE_DESCRIPTION_COLUMN = "Description";
    private static final String CERTIFICATE_PRICE_COLUMN = "Price";
    private static final String CERTIFICATE_CREATE_DATE_COLUMN = "CreateDate";
    private static final String CERTIFICATE_LAST_UPDATE_DATE_COLUMN = "LastUpdateDate";
    private static final String CERTIFICATE_DURATION_COLUMN = "Duration";

    private static final String TAG_ID_COLUMN = "TAGS.ID";
    private static final String TAG_NAME_COLUMN = "TAGS.Name";

    private GiftCertificateExtractorUtil(){
    }

    public static List<GiftCertificate> extractGiftCertificates(ResultSet rs) throws SQLException {
        Map<Integer, GiftCertificate> map = new LinkedHashMap<>();
        while (rs.next()) {
            setCertificateToMap(rs, map);
        }

        return new ArrayList<>(map.values());
    }

    public static GiftCertificate extractGiftCertificate(ResultSet rs) throws SQLException {
        Map<Integer, GiftCertificate> map = new LinkedHashMap<>();
        do {
            setCertificateToMap(rs, map);
        } while (rs.next());

        return new ArrayList<>(map.values()).get(0);
    }

    private static void setCertificateToMap(ResultSet rs, Map<Integer, GiftCertificate> map)
            throws SQLException {
        GiftCertificate giftCertificate = map.get(rs.getInt(CERTIFICATE_ID_COLUMN));
        if (giftCertificate == null) {
            giftCertificate = getGiftCertificateFromResultSet(rs);
            map.put(giftCertificate.getId(), giftCertificate);
        }

        setTagsFromResultSet(rs, giftCertificate);
    }

    private static GiftCertificate getGiftCertificateFromResultSet(ResultSet rs) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();

        giftCertificate.setId(rs.getInt(CERTIFICATE_ID_COLUMN));
        giftCertificate.setName(rs.getString(CERTIFICATE_NAME_COLUMN));
        giftCertificate.setDescription(rs.getString(CERTIFICATE_DESCRIPTION_COLUMN));
        giftCertificate.setPrice(rs.getDouble(CERTIFICATE_PRICE_COLUMN));
        giftCertificate.setCreateDate(
                ZonedDateTime.ofInstant(
                        ((Timestamp) rs.getObject(CERTIFICATE_CREATE_DATE_COLUMN)).toInstant(),
                        ZoneOffset.UTC
                )
        );
        giftCertificate.setLastUpdateDate(
                ZonedDateTime.ofInstant(
                        ((Timestamp) rs.getObject(CERTIFICATE_LAST_UPDATE_DATE_COLUMN)).toInstant(),
                        ZoneOffset.UTC
                )
        );
        giftCertificate.setDuration(rs.getInt(CERTIFICATE_DURATION_COLUMN));

        return giftCertificate;
    }

    private static void setTagsFromResultSet(ResultSet rs, GiftCertificate certificate)
            throws SQLException {
        Tag tag = new Tag(rs.getInt(TAG_ID_COLUMN), rs.getString(TAG_NAME_COLUMN));
        if (tag.getId() != 0 && tag.getName() != null) {
            certificate.getTags().add(tag);
        }
    }
}
