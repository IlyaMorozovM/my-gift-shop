package com.epam.esm.service.request;

public class CertificateRequestBody {

    private String searchingPart;
    private SortType sortType;
    private SortParameter sortBy;
    private String tagName;

    public CertificateRequestBody() {
    }

    public CertificateRequestBody(String searchingPart, SortType sortType, SortParameter sortBy, String tagName) {
        this.searchingPart = searchingPart;
        this.sortType = sortType;
        this.sortBy = sortBy;
        this.tagName = tagName;
    }

    public String getSearchingPart() {
        return searchingPart;
    }

    public void setSearchingPart(String searchingPart) {
        this.searchingPart = searchingPart;
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public SortParameter getSortBy() {
        return sortBy;
    }

    public void setSortBy(SortParameter sortBy) {
        this.sortBy = sortBy;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
