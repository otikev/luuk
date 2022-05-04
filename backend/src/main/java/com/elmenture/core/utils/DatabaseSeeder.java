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
            tagRepository.save(new Tag("pants length"));
            tagRepository.save(new Tag("pattern"));
            tagRepository.save(new Tag("skirt length"));
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
            //7 - pants length
            tag = tagRepository.findByValue("pants length");
            tagPropertyRepository.save(new TagProperty(tag, "shorts"));
            tagPropertyRepository.save(new TagProperty(tag, "long pants"));
            tagPropertyRepository.save(new TagProperty(tag, "3/4 pants"));
            tagPropertyRepository.save(new TagProperty(tag, "1/8 pants"));
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
            //9 - skirt length
            tag = tagRepository.findByValue("skirt length");
            tagPropertyRepository.save(new TagProperty(tag, "maxi skirt"));
            tagPropertyRepository.save(new TagProperty(tag, "midi skirt"));
            tagPropertyRepository.save(new TagProperty(tag, "mini skirt"));
            //10 - Sleeves
            tag = tagRepository.findByValue("sleeves");
            tagPropertyRepository.save(new TagProperty(tag, "short sleeves"));
            tagPropertyRepository.save(new TagProperty(tag, "sleeveless"));
            tagPropertyRepository.save(new TagProperty(tag, "3/4 sleeves"));
            tagPropertyRepository.save(new TagProperty(tag, "long sleeves"));
            //11 - Style
            tag = tagRepository.findByValue("style");
            tagPropertyRepository.save(new TagProperty(tag, "casual"));
            tagPropertyRepository.save(new TagProperty(tag, "elegant"));
            tagPropertyRepository.save(new TagProperty(tag, "sport dresses"));
            tagPropertyRepository.save(new TagProperty(tag, "wedding dresses"));
            //12 - Subcategory
            tag = tagRepository.findByValue("subcategory");
            tagPropertyRepository.save(new TagProperty(tag, "blouse dresses"));
            tagPropertyRepository.save(new TagProperty(tag, "cocktail dresses"));
            tagPropertyRepository.save(new TagProperty(tag, "evening dresses"));
            tagPropertyRepository.save(new TagProperty(tag, "sheath dresses"));
            tagPropertyRepository.save(new TagProperty(tag, "sundresses"));
        }
        System.out.println("Tag properties = " + tagPropertyRepository.count());

        if (itemRepository.count() == 0) {
            Random rn = new Random();
            String placeholderImageUrl = "https://cdn2.iconfinder.com/data/icons/pick-a-dress/900/dress-dresses-fashion-clothes-clothing-silhouette-shadow-15-512.png";
            //Generate 50 dummy items
            for (int i = 1; i <= 50; i++) {
                int size_max = CLOTHING_SIZES.length - 1;
                int size_min = 0;
                String clothingSize = CLOTHING_SIZES[rn.nextInt(size_max - size_min + 1) + size_min];
                int price_max = 600000;
                int price_min = 20000;
                long price = rn.nextInt(price_max - price_min + 1) + price_min;
                Item item = new Item("Clothing " + i, clothingSize, "US", 18L, price, placeholderImageUrl);
                item.setTarget("f");
                itemRepository.save(item);

                //Generate 5 dummy tags
                for (int j = 0; j < 5; j++) {
                    //Generate tags
                    int maxTagPropertyId = (int) tagPropertyRepository.count();
                    int minTagPropertyId = 1;
                    long tagPropertyId = rn.nextInt(maxTagPropertyId - minTagPropertyId + 1) + minTagPropertyId;
                    TagProperty tagProperty = tagPropertyRepository.getById(tagPropertyId);
                    ItemProperty itemProperty = new ItemProperty(item, tagProperty);
                    itemPropertyRepository.save(itemProperty);
                }
            }
        }
        System.out.println("Items = " + itemRepository.count());
        System.out.println("Item properties = " + itemPropertyRepository.count());

        List<Item> items = itemRepository.findBySoldNull();

        for (Item item : items) {
            item.setSold(false);
            itemRepository.save(item);
        }

        emailService.sendAppStartedEmail();
    }
}