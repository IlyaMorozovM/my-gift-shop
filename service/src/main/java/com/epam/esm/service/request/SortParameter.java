package com.epam.esm.service.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum SortParameter {
    @JsonProperty("date")
    @JsonAlias("DATE")
    DATE,
    @JsonProperty("name")
    @JsonAlias("NAME")
    NAME
}
