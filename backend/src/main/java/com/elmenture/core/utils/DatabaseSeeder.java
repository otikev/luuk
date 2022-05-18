package com.elmenture.core.utils;

import com.elmenture.core.model.Item;
import com.elmenture.core.model.ItemProperty;
import com.elmenture.core.model.Tag;
import com.elmenture.core.model.TagProperty;
import com.elmenture.core.repository.ItemPropertyRepository;
import com.elmenture.core.repository.ItemRepository;
import com.elmenture.core.repository.TagPropertyRepository;
import com.elmenture.core.repository.TagRepository;
import com.elmenture.core.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by otikev on 16-Mar-2022
 */
@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    TagRepository tagRepository;

    @Autowired
    TagPropertyRepository tagPropertyRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemPropertyRepository itemPropertyRepository;

    @Autowired
    EmailService emailService;

    String[] CLOTHING_SIZES = new String[]{"XS", "S", "M", "L", "XL", "XXL"};

    @Override
    public void run(String... args) throws Exception {

        if (tagRepository.count() == 0) {
            tagRepository.save(new Tag("color"));
            tagRepository.save(new Tag("cut"));
            tagRepository.save(new Tag("gender"));
            tagRepository.save(new Tag("hood"));
            tagRepository.save(new Tag("material"));
            tagRepository.save(new Tag("neckline"));
            tagRepository.save(new Tag("pattern"));
            tagRepository.save(new Tag("sleeves"));
            tagRepository.save(new Tag("style"));
            tagRepository.save(new Tag("subcategory"));
        }

        System.out.println("Tags = " + tagRepository.count());

        if (tagPropertyRepository.count() == 0) {
            //1- Color
            Tag tag = tagRepository.findByValue("color");
            tagPropertyRepository.save(new TagProperty(tag, "beige"));
            tagPropertyRepository.save(new TagProperty(tag, "black"));
            tagPropertyRepository.save(new TagProperty(tag, "brown"));
            tagPropertyRepository.save(new TagProperty(tag, "dark"));
            tagPropertyRepository.save(new TagProperty(tag, "blue & navy"));
            tagPropertyRepository.save(new TagProperty(tag, "golden"));
            tagPropertyRepository.save(new TagProperty(tag, "green"));
            tagPropertyRepository.save(new TagProperty(tag, "grey"));
            tagPropertyRepository.save(new TagProperty(tag, "light blue"));
            tagPropertyRepository.save(new TagProperty(tag, "multicolor"));
            tagPropertyRepository.save(new TagProperty(tag, "orange"));
            tagPropertyRepository.save(new TagProperty(tag, "pink"));
            tagPropertyRepository.save(new TagProperty(tag, "purple & violet"));
            tagPropertyRepository.save(new TagProperty(tag, "red"));
            tagPropertyRepository.save(new TagProperty(tag, "silver"));
            tagPropertyRepository.save(new TagProperty(tag, "turquoise"));
            tagPropertyRepository.save(new TagProperty(tag, "white"));
            tagPropertyRepository.save(new TagProperty(tag, "yellow"));
            //2- Cut
            tag = tagRepository.findByValue("cut");
            tagPropertyRepository.save(new TagProperty(tag, "buttons"));
            tagPropertyRepository.save(new TagProperty(tag, "fastening"));
            tagPropertyRepository.save(new TagProperty(tag, "shorted"));
            tagPropertyRepository.save(new TagProperty(tag, "shoulder straps"));
            tagPropertyRepository.save(new TagProperty(tag, "spaghetti"));
            tagPropertyRepository.save(new TagProperty(tag, "strapless"));
            tagPropertyRepository.save(new TagProperty(tag, "zipper"));
            //3 - Gender
            //4 - Hood
            //5 - Material
            tag = tagRepository.findByValue("material");
            tagPropertyRepository.save(new TagProperty(tag, "fur"));
            tagPropertyRepository.save(new TagProperty(tag, "Knitwear"));
            tagPropertyRepository.save(new TagProperty(tag, "Leather"));
            tagPropertyRepository.save(new TagProperty(tag, "Lace"));
            tagPropertyRepository.save(new TagProperty(tag, "Denim"));
            tagPropertyRepository.save(new TagProperty(tag, "Cotton"));
            tagPropertyRepository.save(new TagProperty(tag, "Linen"));
            tagPropertyRepository.save(new TagProperty(tag, "silk"));
            tagPropertyRepository.save(new TagProperty(tag, "softshell"));
            tagPropertyRepository.save(new TagProperty(tag, "synthetic"));
            tagPropertyRepository.save(new TagProperty(tag, "textile"));
            tagPropertyRepository.save(new TagProperty(tag, "wool"));
            //6 - Neckline
            tag = tagRepository.findByValue("neckline");
            tagPropertyRepository.save(new TagProperty(tag, "back"));
            tagPropertyRepository.save(new TagProperty(tag, "boatneck"));
            tagPropertyRepository.save(new TagProperty(tag, "bow"));
            tagPropertyRepository.save(new TagProperty(tag, "collar"));
            tagPropertyRepository.save(new TagProperty(tag, "cow"));
            tagPropertyRepository.save(new TagProperty(tag, "crew"));
            tagPropertyRepository.save(new TagProperty(tag, "turtleneck"));
            tagPropertyRepository.save(new TagProperty(tag, "round"));
            tagPropertyRepository.save(new TagProperty(tag, "v-neck"));
            tagPropertyRepository.save(new TagProperty(tag, "low cut"));
            tagPropertyRepository.save(new TagProperty(tag, "square"));
            tagPropertyRepository.save(new TagProperty(tag, "wrap"));
            tagPropertyRepository.save(new TagProperty(tag, "hooded"));
            tagPropertyRepository.save(new TagProperty(tag, "off shoulder"));
            //8 - Pattern
            tag = tagRepository.findByValue("pattern");
            tagPropertyRepository.save(new TagProperty(tag, "text"));
            tagPropertyRepository.save(new TagProperty(tag, "shiny"));
            tagPropertyRepository.save(new TagProperty(tag, "stripe"));
            tagPropertyRepository.save(new TagProperty(tag, "polka dot"));
            tagPropertyRepository.save(new TagProperty(tag, "checked"));
            tagPropertyRepository.save(new TagProperty(tag, "plain"));
            tagPropertyRepository.save(new TagProperty(tag, "patterned"));
            tagPropertyRepository.save(new TagProperty(tag, "picture"));
            tagPropertyRepository.save(new TagProperty(tag, "camouflage"));
            tagPropertyRepository.save(new TagProperty(tag, "chevron"));
            tagPropertyRepository.save(new TagProperty(tag, "floral"));
            tagPropertyRepository.save(new TagProperty(tag, "lace"));
            //10 - Sleeves
            tag = tagRepository.findByValue("sleeves");
            tagPropertyRepository.save(new TagProperty(tag, "short"));
            tagPropertyRepository.save(new TagProperty(tag, "sleeveless"));
            tagPropertyRepository.save(new TagProperty(tag, "3/4"));
            tagPropertyRepository.save(new TagProperty(tag, "long"));
            //11 - Style
            tag = tagRepository.findByValue("style");
            tagPropertyRepository.save(new TagProperty(tag, "casual"));
            tagPropertyRepository.save(new TagProperty(tag, "elegant"));
            tagPropertyRepository.save(new TagProperty(tag, "sport dress"));
            tagPropertyRepository.save(new TagProperty(tag, "wedding dress"));
            //12 - Subcategory
            tag = tagRepository.findByValue("subcategory");
            tagPropertyRepository.save(new TagProperty(tag, "blouse dresses"));
            tagPropertyRepository.save(new TagProperty(tag, "cocktail dresses"));
            tagPropertyRepository.save(new TagProperty(tag, "evening dresses"));
            tagPropertyRepository.save(new TagProperty(tag, "sheath dresses"));
            tagPropertyRepository.save(new TagProperty(tag, "sundresses"));
        }
        System.out.println("Tag properties = " + tagPropertyRepository.count());
        System.out.println("Items = " + itemRepository.count());
        System.out.println("Item properties = " + itemPropertyRepository.count());

        List<Item> items = itemRepository.findBySoldNull();

        for (Item item : items) {
            item.setSold(false);
            itemRepository.save(item);
        }


        cleanUpTags();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                emailService.sendAppStartedEmail();
            }
        });
    }

    private void cleanUpTags() {
        TagProperty tagProperty = tagPropertyRepository.findByValue("black & white");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("color"), "black & white"));
        }

        tagProperty = tagPropertyRepository.findByValue("white, burgundy");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("color"), "white, burgundy"));
        }

        tagProperty = tagPropertyRepository.findByValue("formal");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("style"), "formal"));
        }

        tagProperty = tagPropertyRepository.findByValue("polyester");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("material"), "polyester"));
        }
    }
}