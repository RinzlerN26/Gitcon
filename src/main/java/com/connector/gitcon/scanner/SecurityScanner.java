package com.connector.gitcon.scanner;

import com.connector.gitcon.dto.response.ScanSummary;
import com.connector.gitcon.dto.response.ScannableContent;
import com.connector.gitcon.enums.ScannerType;

public interface SecurityScanner {

    ScanSummary scan(ScannableContent content);

    ScannerType getScannerType();
}