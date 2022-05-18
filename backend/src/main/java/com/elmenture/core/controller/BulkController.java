package com.elmenture.core.controller;

import com.elmenture.core.service.BulkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.elmenture.core.utils.LuukProperties.LUUK_REST_TOKEN;

/**
 * Created by otikev on 18-May-2022
 */

@RestController
@RequestMapping("/bulk")
public class BulkController extends BaseController {

    @Autowired
    private BulkService bulkService;

    @PostMapping("/csv/catalog")
    public ResponseEntity<String> uploadFile(@RequestHeader(name = "LuukToken", defaultValue = "none", required = false) String token,
                                             @RequestHeader(name = "DryRun") boolean dryRun,
                                             @RequestParam("file") MultipartFile file) {
        try {
            if (!token.equals(LUUK_REST_TOKEN)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            List<String> issues = bulkService.parseCatalog(file, dryRun);
            if (issues.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body("csv file processed successfully, dryRun = " + dryRun);
            } else {
                String errors = issues.toString();//Arrays.toString(new List[]{issues});
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("csv file could not be processed.\n" + issues.size() + " Errors\n" + errors);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("csv file could not be processed: " + e.getMessage());
        }
    }
}