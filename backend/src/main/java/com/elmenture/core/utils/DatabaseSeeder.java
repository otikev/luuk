package com.elmenture.core.utils;

import com.elmenture.core.model.Brand;
import com.elmenture.core.model.Item;
import com.elmenture.core.model.Tag;
import com.elmenture.core.model.TagProperty;
import com.elmenture.core.repository.*;
import com.elmenture.core.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
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

    @Autowired
    BrandRepository brandRepository;

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

        if (brandRepository.count() == 0) {
            brandRepository.save(new Brand("Amisu", "Medium", "Retail"));
            brandRepository.save(new Brand("An- Nur Gz", "Low", "Retail"));
            brandRepository.save(new Brand("Archivo", "None", "Retail"));
            brandRepository.save(new Brand("Arden", "None", "Retail"));
            brandRepository.save(new Brand("Ardene", "Medium", "Retail"));
            brandRepository.save(new Brand("Atmospere", "None", "Retail"));
            brandRepository.save(new Brand("Bailey Blue", "Medium", "Retail"));
            brandRepository.save(new Brand("Banana Republic", "High", "Luxury"));
            brandRepository.save(new Brand("Band Of Gypsies", "Low", "Retail"));
            brandRepository.save(new Brand("Bar Iii", "Low", "Retail"));
            brandRepository.save(new Brand("Barami", "Low", "Retail"));
            brandRepository.save(new Brand("Bcuggbg", "Low", "Retail"));
            brandRepository.save(new Brand("Belle De Jeur", "Medium", "Retail"));
            brandRepository.save(new Brand("Biselena", "Low", "Retail"));
            brandRepository.save(new Brand("Body Central", "Low", "Retail"));
            brandRepository.save(new Brand("Bodyflirt", "Low", "Retail"));
            brandRepository.save(new Brand("Bongo", "Low", "Retail"));
            brandRepository.save(new Brand("Buckhead Betties", "Low", "Retail"));
            brandRepository.save(new Brand("C.Dime", "Low", "Retail"));
            brandRepository.save(new Brand("C&A", "Low", "Retail"));
            brandRepository.save(new Brand("Cachecache", "Low", "Retail"));
            brandRepository.save(new Brand("Cc.Hughes", "Low", "Retail"));
            brandRepository.save(new Brand("Charlotte Russe", "Low", "Retail"));
            brandRepository.save(new Brand("Ckh", "Low", "Retail"));
            brandRepository.save(new Brand("Cole Daniel", "Low", "Retail"));
            brandRepository.save(new Brand("Collective Concept", "Low", "Retail"));
            brandRepository.save(new Brand("Crossroads", "Low", "Retail"));
            brandRepository.save(new Brand("Daba Girl", "Low", "Retail"));
            brandRepository.save(new Brand("Damai", "Low", "Retail"));
            brandRepository.save(new Brand("David Emanuel", "Low", "Retail"));
            brandRepository.save(new Brand("Diwo", "Low", "Retail"));
            brandRepository.save(new Brand("Dressbarn", "Low", "Retail"));
            brandRepository.save(new Brand("Dynamite", "Low", "Retail"));
            brandRepository.save(new Brand("Eight Sixty", "Low", "Retail"));
            brandRepository.save(new Brand("Eliza J", "Low", "Retail"));
            brandRepository.save(new Brand("Emma And Michele", "Low", "Retail"));
            brandRepository.save(new Brand("Esmara", "Medium", "Retail"));
            brandRepository.save(new Brand("Espirit", "High", "Premium"));
            brandRepository.save(new Brand("Espresso", "Medium", "Retail"));
            brandRepository.save(new Brand("Esprit", "High", "Premium"));
            brandRepository.save(new Brand("Forever 21", "Medium", "Retail"));
            brandRepository.save(new Brand("Freedom Trail", "Medium", "Retail"));
            brandRepository.save(new Brand("French Connection", "High", "Premium"));
            brandRepository.save(new Brand("Fun And Flirt", "None", "Retail"));
            brandRepository.save(new Brand("Gap", "High", "Premium"));
            brandRepository.save(new Brand("George", "High", "Premium"));
            brandRepository.save(new Brand("H&M", "High", "Premium"));
            brandRepository.save(new Brand("Han Han", "Low", "Retail"));
            brandRepository.save(new Brand("Hellen Blake", "Low", "Retail"));
            brandRepository.save(new Brand("Hitorat", "Low", "Retail"));
            brandRepository.save(new Brand("Holly And Whyte", "Low", "Retail"));
            brandRepository.save(new Brand("Hwalea", "Low", "Retail"));
            brandRepository.save(new Brand("I Love Next", "Low", "Retail"));
            brandRepository.save(new Brand("Iief", "Low", "Retail"));
            brandRepository.save(new Brand("Impulsive", "Low", "Retail"));
            brandRepository.save(new Brand("Isabel Maternity", "Low", "Retail"));
            brandRepository.save(new Brand("J.Crew", "High", "Premium"));
            brandRepository.save(new Brand("Jessica", "Low", "Retail"));
            brandRepository.save(new Brand("Jessica Haward", "Low", "Retail"));
            brandRepository.save(new Brand("Jkjs", "Low", "Retail"));
            brandRepository.save(new Brand("Joe Fresh", "Low", "Retail"));
            brandRepository.save(new Brand("John Rocha", "Low", "Retail"));
            brandRepository.save(new Brand("Kiabi", "Low", "Retail"));
            brandRepository.save(new Brand("Kuberas", "Medium", "Retail"));
            brandRepository.save(new Brand("Lauren Conrad", "Medium", "Premium"));
            brandRepository.save(new Brand("Le Chateau", "Medium", "Premium"));
            brandRepository.save(new Brand("Limited Edition", "High", "Retail"));
            brandRepository.save(new Brand("Lindex", "Low", "Retail"));
            brandRepository.save(new Brand("Linen Village", "Low", "Retail"));
            brandRepository.save(new Brand("Loft", "High", "Premium"));
            brandRepository.save(new Brand("Love Delirious", "Low", "Retail"));
            brandRepository.save(new Brand("Love Linen", "Low", "Retail"));
            brandRepository.save(new Brand("Lularoe", "Low", "Retail"));
            brandRepository.save(new Brand("Lusa", "Low", "Retail"));
            brandRepository.save(new Brand("M Y Queen", "Low", "Retail"));
            brandRepository.save(new Brand("Macy & Larry", "Medium", "Retail"));
            brandRepository.save(new Brand("Mango", "High", "Designer"));
            brandRepository.save(new Brand("Marc New York", "Low", "Retail"));
            brandRepository.save(new Brand("Maurice", "Low", "Retail"));
            brandRepository.save(new Brand("Maurices", "Low", "Retail"));
            brandRepository.save(new Brand("Mela Loves London", "Low", "Retail"));
            brandRepository.save(new Brand("Melissa Harper", "Low", "Retail"));
            brandRepository.save(new Brand("Merona", "Low", "Retail"));
            brandRepository.save(new Brand("Ming", "Low", "Retail"));
            brandRepository.save(new Brand("Miss Xue", "Low", "Retail"));
            brandRepository.save(new Brand("Mlle Gabrielle", "Low", "Retail"));
            brandRepository.save(new Brand("Motherhood", "Low", "Retail"));
            brandRepository.save(new Brand("Na Wain", "Low", "Retail"));
            brandRepository.save(new Brand("Naif", "Low", "Retail"));
            brandRepository.save(new Brand("New Lool", "Low", "Retail"));
            brandRepository.save(new Brand("Next", "High", "Premium"));
            brandRepository.save(new Brand("No Boundaries", "Low", "Retail"));
            brandRepository.save(new Brand("Nutmeg", "Low", "Retail"));
            brandRepository.save(new Brand("Ny Collection", "Low", "Retail"));
            brandRepository.save(new Brand("Oasis", "Low", "Retail"));
            brandRepository.save(new Brand("Ohconcept", "Low", "Retail"));
            brandRepository.save(new Brand("Old Navy", "High", "Premium"));
            brandRepository.save(new Brand("One Clothing", "None", "Retail"));
            brandRepository.save(new Brand("One Love Clothing", "Low", "Retail"));
            brandRepository.save(new Brand("Outfit Classic", "None", "Retail"));
            brandRepository.save(new Brand("Peter Pilotto", "Medium", "Designer"));
            brandRepository.save(new Brand("Prenatal", "None", "Retail"));
            brandRepository.save(new Brand("Primark", "High", "Retail"));
            brandRepository.save(new Brand("Promod", "Medium", "Retail"));
            brandRepository.save(new Brand("Reb&J", "Medium", "Retail"));
            brandRepository.save(new Brand("Reitmanz", "Medium", "Premium"));
            brandRepository.save(new Brand("Reserved", "Low", "Retail"));
            brandRepository.save(new Brand("Ritascimento", "Medium", "Premium"));
            brandRepository.save(new Brand("River Island", "Medium", "Premium"));
            brandRepository.save(new Brand("Robbie Bee", "Medium", "Retail"));
            brandRepository.save(new Brand("Rozali", "None", "Retail"));
            brandRepository.save(new Brand("Sadie Robertson", "Low", "Retail"));
            brandRepository.save(new Brand("Sassy Doll", "None", "Retail"));
            brandRepository.save(new Brand("Savida", "Medium", "Retail"));
            brandRepository.save(new Brand("Silkeer", "None", "Retail"));
            brandRepository.save(new Brand("Sister", "Low", "Retail"));
            brandRepository.save(new Brand("Soft Grey", "None", "Retail"));
            brandRepository.save(new Brand("Soul", "None", "Retail"));
            brandRepository.save(new Brand("South", "None", "Retail"));
            brandRepository.save(new Brand("Talbots", "Medium", "Retail"));
            brandRepository.save(new Brand("Temt", "Low", "Retail"));
            brandRepository.save(new Brand("Time And Tru", "Medium", "Retail"));
            brandRepository.save(new Brand("Today'S Emcee", "None", "Retail"));
            brandRepository.save(new Brand("Tova", "None", "Retail"));
            brandRepository.save(new Brand("Trendyol", "Low", "Retail"));
            brandRepository.save(new Brand("Umgee", "Low", "Retail"));
            brandRepository.save(new Brand("Urbanature", "Low", "Premium"));
            brandRepository.save(new Brand("Vibe", "Medium", "Retail"));
            brandRepository.save(new Brand("Xhilaration", "Medium", "Retail"));
            brandRepository.save(new Brand("Yang Yang", "None", "Retail"));
            brandRepository.save(new Brand("Zimmur", "None", "Retail"));
            brandRepository.save(new Brand("Papaya", "Low", "Retail"));
            brandRepository.save(new Brand("Smash", "None", "Retail"));
            brandRepository.save(new Brand("Mossimo", "Medium", "Retail"));
            brandRepository.save(new Brand("Kate Spade", "Medium", "Designer"));
            brandRepository.save(new Brand("Anna Sui", "Medium", "Luxury"));
            brandRepository.save(new Brand("Mikarose", "Medium", "Premium"));
            brandRepository.save(new Brand("Justfab", "Medium", "Retail"));
            brandRepository.save(new Brand("Ally", "None", "Retail"));
            brandRepository.save(new Brand("L.L.Bean", "Medium", "Retail"));
            brandRepository.save(new Brand("Knox Rose", "Medium", "Retail"));
            brandRepository.save(new Brand("Tommy Bahama", "Medium", "Designer"));
            brandRepository.save(new Brand("Laredoute", "Medium", "Retail"));
            brandRepository.save(new Brand("Express", "Low", "Retail"));
            brandRepository.save(new Brand("Giani Bini", "Medium", "Premium"));
            brandRepository.save(new Brand("Dunnes", "Medium", "Retail"));
            brandRepository.save(new Brand("Shein", "Medium", "Retail"));
            brandRepository.save(new Brand("Jessica Howard", "Low", "Retail"));
            brandRepository.save(new Brand("Glassons", "Medium", "Retail"));
            brandRepository.save(new Brand("Amly", "Low", "Retail"));
            brandRepository.save(new Brand("Studio", "None", "Retail"));
            brandRepository.save(new Brand("Womanwithin", "Medium", "Retail"));
            brandRepository.save(new Brand("Falls Creek", "Medium", "Retail"));
            brandRepository.save(new Brand("Peppermint Bay", "Low", "Retail"));
            brandRepository.save(new Brand("Faded Glory", "Medium", "Retail"));
            brandRepository.save(new Brand("Patrizia Dini", "Medium", "Premium"));
            brandRepository.save(new Brand("Thread+Supply", "Low", "Retail"));
            brandRepository.save(new Brand("Sunday", "None", "Retail"));
            brandRepository.save(new Brand("Jms Chaopin", "None", "Retail"));
            brandRepository.save(new Brand("Universal Thread", "None", "Retail"));
            brandRepository.save(new Brand("Zihui", "None", "Retail"));
            brandRepository.save(new Brand("Gold Coast", "None", "Retail"));
        }
        System.out.println("Tag properties = " + tagPropertyRepository.count());
        System.out.println("Items = " + itemRepository.count());
        System.out.println("Item properties = " + itemPropertyRepository.count());
        System.out.println("Brand = " + brandRepository.count());

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
        tagProperty = tagPropertyRepository.findByValue("formal");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("style"), "formal"));
        }
        tagProperty = tagPropertyRepository.findByValue("polyester");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("material"), "polyester"));
        }
        tagProperty = tagPropertyRepository.findByValue("work dress");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("subcategory"), "work dress"));
        }
        tagProperty = tagPropertyRepository.findByValue("burgundy");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("color"), "burgundy"));
        }
        tagProperty = tagPropertyRepository.findByValue("dark blue");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("color"), "dark blue"));
        }
        tagProperty = tagPropertyRepository.findByValue("blue");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("color"), "blue"));
        }
        tagProperty = tagPropertyRepository.findByValue("navy");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("color"), "navy"));
        }
        tagProperty = tagPropertyRepository.findByValue("maroon");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("color"), "maroon"));
        }
        tagProperty = tagPropertyRepository.findByValue("color block");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("pattern"), "color block"));
        }
        tagProperty = tagPropertyRepository.findByValue("cowl");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("neckline"), "cowl"));
        }
        tagProperty = tagPropertyRepository.findByValue("sweetheart");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("neckline"), "sweetheart"));
        }
        tagProperty = tagPropertyRepository.findByValue("chiffon");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("material"), "chiffon"));
        }
        tagProperty = tagPropertyRepository.findByValue("drop back");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("cut"), "drop back"));
        }
        tagProperty = tagPropertyRepository.findByValue("maroon");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("color"), "maroon"));
        }
        tagProperty = tagPropertyRepository.findByValue("cream");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("color"), "cream"));
        }
        tagProperty = tagPropertyRepository.findByValue("jungle green");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("color"), "jungle green"));
        }
        tagProperty = tagPropertyRepository.findByValue("luminous green");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("color"), "luminous green"));
        }
        tagProperty = tagPropertyRepository.findByValue("red-orange");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("color"), "red-orange"));
        }
        tagProperty = tagPropertyRepository.findByValue("collar frill");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("cut"), "collar frill"));
        }
        tagProperty = tagPropertyRepository.findByValue("fuschia");
        if (tagProperty == null) {
            tagPropertyRepository.save(new TagProperty(tagRepository.findByValue("color"), "fuschia"));
        }
    }
}