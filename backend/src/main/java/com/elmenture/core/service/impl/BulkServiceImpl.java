package com.elmenture.core.service.impl;

import com.elmenture.core.model.Tag;
import com.elmenture.core.model.TagProperty;
import com.elmenture.core.payload.ItemDto;
import com.elmenture.core.repository.TagPropertyRepository;
import com.elmenture.core.repository.TagRepository;
import com.elmenture.core.service.BulkService;
import com.elmenture.core.service.ItemService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.elmenture.core.utils.LuukProperties.LUUK_AWS_S3_BUCKET;

/**
 * Created by otikev on 18-May-2022
 */
@Service
public class BulkServiceImpl implements BulkService {
    static String[] CATALOG_HEADERS = {"id", "brand", "size_type", "size", "color", "length", "neckline", "pattern", "material", "sleeves", "style", "subcategory"};
    public static String TYPE = "text/csv";
    @Autowired
    TagPropertyRepository tagPropertyRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    ItemService itemService;

    private List<String> issues;

    @Override
    public List<String> parseCatalog(MultipartFile file, boolean dryRun) throws IOException {
        issues = new ArrayList<>();
        if (hasCSVFormat(file)) {
            InputStream is = file.getInputStream();
            try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                 CSVParser csvParser = new CSVParser(fileReader,
                         CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

                Iterable<CSVRecord> csvRecords = csvParser.getRecords();
                for (CSVRecord csvRecord : csvRecords) {
                    if(csvRecord.get("brand")== null ||csvRecord.get("brand").isEmpty() ){
                        issues.add("Brand is missing for id "+csvRecord.get("id"));
                    }
                    ItemDto item = new ItemDto();
                    item.setTarget("f");
                    item.setExternalId(Integer.parseInt(csvRecord.get("id")));
                    System.out.println("Parsing item : "+csvRecord.get("id"));
                    item.setBrand(csvRecord.get("brand"));
                    item.setDescription(csvRecord.get("brand"));
                    if (csvRecord.get("size_type").equalsIgnoreCase("international")) {
                        item.setSizeType("INT");
                        item.setSizeInternational(csvRecord.get("size"));
                    } else {
                        item.setSizeType(csvRecord.get("size_type"));
                        item.setSizeNumber(Long.valueOf(csvRecord.get("size")));
                    }
                    item.setImageUrl("https://"+LUUK_AWS_S3_BUCKET+".s3.eu-central-1.amazonaws.com/"+csvRecord.get("id")+".jpg");

                    List<Long> tagProperties = getTagProperties(csvRecord);
                    item.setTagProperties(tagProperties);

                    if (!dryRun) {
                        itemService.createItem(item);
                    }
                }
                return issues;
            } catch (IOException e) {
                throw new RuntimeException("failed to parse CSV file: " + e.getMessage());
            }
        } else {
            issues.add("Uploaded file is not a CSV format");
        }
        return issues;
    }

    private boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    private List<Long> getTagProperties(CSVRecord csvRecord) {
        List<Long> tagProperties = new ArrayList<>();
        Long id = getTagProperty(csvRecord,"color");
        if (id != null) {
            tagProperties.add(id);
        }
        id = getTagProperty(csvRecord,"skirt length");
        if (id != null) {
            tagProperties.add(id);
        }
        id = getTagProperty(csvRecord,"cut");
        if (id != null) {
            tagProperties.add(id);
        }
        id = getTagProperty(csvRecord,"neckline");
        if (id != null) {
            tagProperties.add(id);
        }
        id = getTagProperty(csvRecord,"pattern");
        if (id != null) {
            tagProperties.add(id);
        }
        id = getTagProperty(csvRecord,"material");
        if (id != null) {
            tagProperties.add(id);
        }
        id = getTagProperty(csvRecord,"sleeves");
        if (id != null) {
            tagProperties.add(id);
        }
        id = getTagProperty(csvRecord,"style");
        if (id != null) {
            tagProperties.add(id);
        }
        id = getTagProperty(csvRecord,"subcategory");
        if (id != null) {
            tagProperties.add(id);
        }
        return tagProperties;
    }

    private Long getTagProperty(CSVRecord csvRecord, String tagValue) {
        System.out.println("Processing Tag : "+tagValue);
        String tagPropertyValue = csvRecord.get(tagValue);
        if (tagPropertyValue != null && !tagPropertyValue.isEmpty()) {
            Tag tag = tagRepository.findByValue(tagValue);
            System.out.println("Getting TagProperty : "+tagPropertyValue.toLowerCase());
            List<TagProperty> tagProperty = tagPropertyRepository.findByValueAndTagId(tagPropertyValue.toLowerCase(),tag.getId());
            if(tagProperty != null && !tagProperty.isEmpty()){
                return tagProperty.get(0).getId();
            }
        }else{
            return null;
        }
        issues.add("Could not find Tag Property : " + tagPropertyValue);
        return null;
    }
}
