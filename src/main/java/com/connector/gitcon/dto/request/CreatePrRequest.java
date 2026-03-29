package com.connector.gitcon.dto.request;

import lombok.Data;

@Data
public class CreatePrRequest {
    private String title;
    private String head;
    private String base;
    private String body;
}
