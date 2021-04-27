package com.epam.esm.service.request;

public class CertificateRequestBody {

    private String searchByPart;
    private SortType sortType;
    private SortParameter sortBy;
    private String tagName;

    public CertificateRequestBody() {
    }

    public CertificateRequestBody(String searchByPart, SortType sortType, SortParameter sortBy, String tagName) {
        this.searchByPart = searchByPart;
        this.sortType = sortType;
        this.sortBy = sortBy;
        this.tagName = tagName;
    }

    public String getSearchByPart() {
        return searchByPart;
    }

    public void setSearchByPart(String searchByPart) {
        this.searchByPart = searchByPart;
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
