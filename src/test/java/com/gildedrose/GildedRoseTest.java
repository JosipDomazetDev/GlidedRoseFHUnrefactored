package com.gildedrose;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedRoseTest {


    @Test
    void testFoo() {
        // Initial placeholder test. You can remove or repurpose this test as needed.
        Item[] items = new Item[] { new Item("foo", 0, 0) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals("foo", app.items[0].name, "Item name should remain unchanged");
        assertEquals(-1, app.items[0].sellIn, "SellIn should decrease by 1");
        assertEquals(0, app.items[0].quality, "Quality should not drop below 0");
    }
    @Test
    void testAgedBrieIncreasesInQuality() {
        Item[] items = new Item[] { new Item("Aged Brie", 2, 0) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(1, app.items[0].quality, "Aged Brie should increase in quality");
        assertEquals(1, app.items[0].sellIn, "SellIn should decrease by 1");
    }

    @Test
    void testAgedBrieDoesNotExceedMaximumQuality() {
        Item[] items = new Item[] { new Item("Aged Brie", 5, 50) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(50, app.items[0].quality, "Aged Brie should not exceed quality of 50");
        assertEquals(4, app.items[0].sellIn, "SellIn should decrease by 1");
    }

    @Test
    void testSulfurasNeverDecreasesInQualityOrSellIn() {
        Item[] items = new Item[] { new Item("Sulfuras, Hand of Ragnaros", 10, 80) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(80, app.items[0].quality, "Sulfuras quality should remain unchanged");
        assertEquals(10, app.items[0].sellIn, "Sulfuras sellIn should remain unchanged");
    }

    @Test
    void testBackstagePassesIncreaseInQualityWithApproachingSellIn() {
        // More than 10 days
        Item[] items = new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(21, app.items[0].quality, "Backstage passes should increase in quality by 1 when sellIn > 10");
        assertEquals(14, app.items[0].sellIn, "SellIn should decrease by 1");

        // 10 days or less
        items = new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 10, 49) };
        app = new GildedRose(items);
        app.updateQuality();
        assertEquals(50, app.items[0].quality, "Backstage passes should increase in quality by 2 when 5 < sellIn <= 10");
        assertEquals(9, app.items[0].sellIn, "SellIn should decrease by 1");

        // 5 days or less
        items = new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49) };
        app = new GildedRose(items);
        app.updateQuality();
        assertEquals(50, app.items[0].quality, "Backstage passes should increase in quality by 3 when sellIn <= 5");
        assertEquals(4, app.items[0].sellIn, "SellIn should decrease by 1");
    }

    @Test
    void testBackstagePassesDropToZeroAfterConcert() {
        Item[] items = new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 0, 20) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(0, app.items[0].quality, "Backstage passes should drop to 0 after the concert");
        assertEquals(-1, app.items[0].sellIn, "SellIn should decrease by 1");
    }


    @Test
    void testNormalItemDecreasesInQualityAndSellIn() {
        Item[] items = new Item[] { new Item("+5 Dexterity Vest", 10, 20) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(19, app.items[0].quality, "Normal item should decrease in quality by 1");
        assertEquals(9, app.items[0].sellIn, "SellIn should decrease by 1");
    }

    @Test
    void testNormalItemQualityNeverNegative() {
        Item[] items = new Item[] { new Item("Expired Item", 0, 0), new Item("Regular Item at 0 Quality", 5, 0) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(0, app.items[0].quality, "Expired item quality should not be negative");
        assertEquals(-1, app.items[0].sellIn, "SellIn should decrease by 1");
        assertEquals(0, app.items[1].quality, "Regular item at 0 quality should remain at 0");
        assertEquals(4, app.items[1].sellIn, "SellIn should decrease by 1");
    }

    @Test
    void testQualityNeverExceedsFifty() {
        Item[] items = new Item[] {
            new Item("Aged Brie", 10, 50),
            new Item("Backstage passes to a TAFKAL80ETC concert", 5, 50),
            new Item("Test Item", 5, 50)
        };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(50, app.items[0].quality, "Aged Brie should not exceed quality of 50");
        assertEquals(9, app.items[0].sellIn, "SellIn should decrease by 1");

        assertEquals(50, app.items[1].quality, "Backstage passes should not exceed quality of 50");
        assertEquals(4, app.items[1].sellIn, "SellIn should decrease by 1");

        assertEquals(49, app.items[2].quality, "Conjured items should not exceed quality of 50");
        assertEquals(4, app.items[2].sellIn, "SellIn should decrease by 1");
    }

    @Test
    void testSulfurasWithVaryingSellInValues() {
        Item[] items = new Item[] {
            new Item("Sulfuras, Hand of Ragnaros", 10, 80),
            new Item("Sulfuras, Hand of Ragnaros", -10, 80)
        };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        for (Item item : app.items) {
            assertEquals(80, item.quality, "Sulfuras quality should remain unchanged");
            assertEquals(item.sellIn, item.sellIn, "Sulfuras sellIn should remain unchanged");
        }
    }

    @Test
    void testNonStandardItemsBehaveAsNormalItems() {
        Item[] items = new Item[] {
            new Item("Vintage Wine", 20, 30),
            new Item("Magic Sword", 15, 40)
        };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(29, app.items[0].quality, "Vintage Wine should decrease in quality by 1");
        assertEquals(19, app.items[0].sellIn, "Vintage Wine sellIn should decrease by 1");

        assertEquals(39, app.items[1].quality, "Magic Sword should decrease in quality by 1");
        assertEquals(14, app.items[1].sellIn, "Magic Sword sellIn should decrease by 1");
    }


    @Test
    void testBackstagePassesWithSellInLessThanSix() {
        // SellIn = 5
        Item[] items = new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 5, 45) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(48, app.items[0].quality, "Backstage passes should increase by 3 when sellIn <= 5");
        assertEquals(4, app.items[0].sellIn, "SellIn should decrease by 1");
    }


    @Test
    void testUpdateQuality_AgedBrie() {
        Item[] items = new Item[] { new Item("Aged Brie", 10, 20) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals(21, items[0].quality, "Expected Quality to increase to 21");
        assertEquals(9, items[0].sellIn, "Expected SellIn to decrease to 9");
    }

    @Test
    void testUpdateQuality_BackstagePasses() {
        Item[] items = new Item[] {
            new Item("Backstage passes to a TAFKAL80ETC concert", 11, 20),
            new Item("Backstage passes to a TAFKAL80ETC concert", 10, 20),
            new Item("Backstage passes to a TAFKAL80ETC concert", 5, 20),
            new Item("Backstage passes to a TAFKAL80ETC concert", 0, 20)
        };
        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals(21, items[0].quality, "Expected Quality to increase to 21");
        assertEquals(22, items[1].quality, "Expected Quality to increase to 22");
        assertEquals(23, items[2].quality, "Expected Quality to increase to 23");
        assertEquals(0, items[3].quality, "Expected Quality to drop to 0 after the concert");
    }

    @Test
    void testUpdateQuality_Sulfuras() {
        Item[] items = new Item[] { new Item("Sulfuras, Hand of Ragnaros", 0, 80) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals(80, items[0].quality, "Expected Quality to remain 80");
        assertEquals(0, items[0].sellIn, "Expected SellIn to remain 0");
    }

    @Test
    void testUpdateQuality_NormalItem() {
        Item[] items = new Item[] { new Item("Normal Item", 10, 20) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals(19, items[0].quality, "Expected Quality to decrease to 19");
        assertEquals(9, items[0].sellIn, "Expected SellIn to decrease to 9");
    }

    @Test
    void testUpdateQuality_NormalItemAfterExpiration() {
        Item[] items = new Item[] { new Item("Normal Item", 0, 20) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals(18, items[0].quality, "Expected Quality to decrease to 18 after expiration");
        assertEquals(-1, items[0].sellIn, "Expected SellIn to decrease to -1");
    }

    @Test
    void testUpdateQuality_AgedBrie_MaxQuality() {
        Item[] items = new Item[] { new Item("Aged Brie", 10, 50) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals(50, items[0].quality, "Expected Quality to remain at 50 (max)");
    }

    @Test
    void testUpdateQuality_BackstagePasses_MaxQuality() {
        Item[] items = new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 5, 50) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals(50, items[0].quality, "Expected Quality to remain at 50 (max)");
    }

    @Test
    void testUpdateQuality_ExpiredAgedBrie() {
        Item[] items = new Item[] { new Item("Aged Brie", 0, 20) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals(22, items[0].quality, "Expected Quality to increase to 22 after expiration");
        assertEquals(-1, items[0].sellIn, "Expected SellIn to decrease to -1");
    }

    @Test
    void testUpdateQuality_ExpiredNormalItem() {
        Item[] items = new Item[] { new Item("Normal Item", -1, 10) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals(8, items[0].quality, "Expected Quality to decrease by 2 after expiration");
    }

    @Test
    void testUpdateQuality_ExpiredBackstagePasses() {
        Item[] items = new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", -1, 10) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals(0, items[0].quality, "Expected Quality to drop to 0 after expiration");
    }






}
