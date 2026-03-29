package com.connector.gitcon.dto.response;

import lombok.Data;

@Data
public class PrResponse {
    private Long id;
    private String title;
    private String state;
    private String htmlUrl;
}
