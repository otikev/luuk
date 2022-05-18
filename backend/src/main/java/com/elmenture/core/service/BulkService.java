package com.elmenture.core.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BulkService {
    List<String> parseCatalog(MultipartFile file, boolean dryRun) throws IOException;
}
