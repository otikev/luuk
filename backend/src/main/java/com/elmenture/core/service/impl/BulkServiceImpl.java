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

import static com.elmenture.core.utils.LuukProperties.LUUK_AWS_S3_BUCKET;

/**
 * Created by otikev on 18-May-2022
 */
@Service
public class BulkServiceImpl implements BulkService {
    public static String TYPE_CSV = "text/csv";

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
                    ItemDto item = new ItemDto();
                    item.setTarget("f");
                    item.setExternalId(Integer.parseInt(csvRecord.get("id")));
                    System.out.println("Parsing item : " + csvRecord.get("id"));
                    item.setBrand(csvRecord.get("brand"));
                    item.setDescription(csvRecord.get("description"));
                    float price = Float.parseFloat(csvRecord.get("price"));//price in KES
                    item.setPrice((long) (price * 100));//convert price to cents
                    if (csvRecord.get("size_type").equalsIgnoreCase("international")) {
                        item.setSizeType("INT");
                        item.setSizeInternational(csvRecord.get("size"));
                    } else {
                        item.setSizeType(csvRecord.get("size_type"));
                        item.setSizeNumber(Long.valueOf(csvRecord.get("size")));
                    }
                    item.setImageUrl("https://" + LUUK_AWS_S3_BUCKET + ".s3.eu-central-1.amazonaws.com/" + csvRecord.get("id") + ".jpg");

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
        if (!TYPE_CSV.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    private List<Long> getTagProperties(CSVRecord csvRecord) {
        List<Long> tagProperties = new ArrayList<>();
        List<Long> ids = getTagProperty(csvRecord, "color");
        if (ids != null) {
            tagProperties.addAll(ids);
        }
        ids = getTagProperty(csvRecord, "skirt length");
        if (!ids.isEmpty()) {
            tagProperties.addAll(ids);
        }
        ids = getTagProperty(csvRecord, "cut");
        if (!ids.isEmpty()) {
            tagProperties.addAll(ids);
        }
        ids = getTagProperty(csvRecord, "neckline");
        if (!ids.isEmpty()) {
            tagProperties.addAll(ids);
        }
        ids = getTagProperty(csvRecord, "pattern");
        if (!ids.isEmpty()) {
            tagProperties.addAll(ids);
        }
        ids = getTagProperty(csvRecord, "material");
        if (!ids.isEmpty()) {
            tagProperties.addAll(ids);
        }
        ids = getTagProperty(csvRecord, "sleeves");
        if (!ids.isEmpty()) {
            tagProperties.addAll(ids);
        }
        ids = getTagProperty(csvRecord, "style");
        if (!ids.isEmpty()) {
            tagProperties.addAll(ids);
        }
        ids = getTagProperty(csvRecord, "subcategory");
        if (!ids.isEmpty()) {
            tagProperties.addAll(ids);
        }
        return tagProperties;
    }

    private List<Long> getTagProperty(CSVRecord csvRecord, String tagValue) {
        List<Long> ids = new ArrayList<>();
        System.out.println("Processing Tag : " + tagValue);
        String tagPropertyValue = csvRecord.get(tagValue);
        if (tagPropertyValue != null && !tagPropertyValue.isEmpty()) {
            Tag tag = tagRepository.findByValue(tagValue);

            if (tagPropertyValue.contains(",")) {
                String[] values = tagPropertyValue.split(",");
                for (String val : values) {
                    if (val.equalsIgnoreCase("other")) {
                        System.out.println("Ignoring TagProperty : " + val.toLowerCase());
                        continue;//Ignore 'Other' tags
                    }
                    System.out.println("Getting TagProperty : " + val.toLowerCase());
                    Long id = getTagPropertyIds(val, tag);
                    if (id == null) {
                        issues.add(csvRecord.get("id") + ": Could not find Tag Property : " + val);
                    } else {
                        ids.add(id);
                    }
                }
            } else {
                if (tagPropertyValue.equalsIgnoreCase("other")) {
                    System.out.println("Ignoring TagProperty : " + tagPropertyValue.toLowerCase());
                } else {
                    System.out.println("Getting TagProperty : " + tagPropertyValue.toLowerCase());
                    Long id = getTagPropertyIds(tagPropertyValue, tag);
                    if (id == null) {
                        issues.add(csvRecord.get("id") + ": Could not find Tag Property : " + tagPropertyValue);
                    } else {
                        ids.add(id);
                    }
                }
            }
        }

        return ids;
    }

    private Long getTagPropertyIds(String tagPropertyValue, Tag tag) {
        String value = tagPropertyValue.toLowerCase().trim();
        System.out.println("Getting TagProperty : " + value);
        List<TagProperty> tagProperty = tagPropertyRepository.findByValueAndTagId(value, tag.getId());
        if (tagProperty != null && !tagProperty.isEmpty()) {
            return tagProperty.get(0).getId();
        }
        return null;
    }
}