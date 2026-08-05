package com.connector.gitcon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScannableContent {
    private String fileName;
    private String content;
    private boolean patchBased;
    private boolean fallbackUsed;
}
