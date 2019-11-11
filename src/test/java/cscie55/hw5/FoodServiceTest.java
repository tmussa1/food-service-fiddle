package cscie55.hw5;

import cscie55.hw5.exception.NoSuchApartmentException;
import cscie55.hw5.foodservice.DeliveryPerson;
import cscie55.hw5.foodservice.Dish;
import cscie55.hw5.foodservice.FoodOrder;
import cscie55.hw5.foodservice.TakeOutShop;
import cscie55.hw5.impl.Address;
import cscie55.hw5.impl.Building;
import cscie55.hw5.impl.WebBrowser;
import cscie55.hw5.reader.MenuFileReader;
import cscie55.hw5.utils.NumUtil;
import cscie55.hw5.writer.MenuWriter;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FoodServiceTest {

    private TakeOutShop takeOutShop;
    private List<Dish> menu;
    private WebBrowser browser;


    @Before
    public void setup(){
        takeOutShop = new TakeOutShop();
        menu = takeOutShop.getMenu();
    }

    @Test
    public void verifyShopAndMenu(){
        assertNotNull(takeOutShop);
        assertEquals(9, menu.size());
    }

    @Test
    public void testDeliveryPeople(){
        ArrayList<DeliveryPerson> deliveryPeople = (ArrayList<DeliveryPerson>) takeOutShop.getDeliveryPeople();
        assertEquals(7,deliveryPeople.size());
        DeliveryPerson dp = deliveryPeople.get(0);
        assertEquals("Sims7",dp.getLastName());
    }

    @Test
    public void placeOrderTest(){
        Address a = new Address(1,1,1);
        Dish[] dishes = {menu.get(0), menu.get(3), menu.get(5)};
        FoodOrder fo = takeOutShop.placeOrder(a,dishes);
        assertNotNull(fo);
        assertEquals(1,fo.getOrderId());
        assertEquals(1,fo.getAddress().getFloorId());
        assertEquals(1,fo.getAddress().getApartmentId());
        assertTrue(takeOutShop.getOrdersIn().containsKey(1));
        ArrayDeque<FoodOrder> fol = takeOutShop.getOrdersIn().get(1);
        assertTrue(fol.contains(fo));
    }

    @Test
    public void placeMultipleOrdersPerFloorTest(){

        Address a = new Address(1,1,1);
        Dish[] dishes = {menu.get(0), menu.get(3), menu.get(5)};
        FoodOrder fo = takeOutShop.placeOrder(a,dishes);
        assertNotNull(fo);
        ArrayDeque<FoodOrder> foIn1 = takeOutShop.getOrdersIn().get(1);
        // test that we can insert more than one food order per floor
        Dish[] dishes2 = {menu.get(1), menu.get(4), menu.get(6)};
        FoodOrder fo2 = takeOutShop.placeOrder(a,dishes2);
        // Both orders should be in same list
        assertTrue(foIn1.contains(fo));
        assertTrue(foIn1.contains(fo2));
        assertEquals(2,foIn1.size());
        assertEquals(1,takeOutShop.getOrdersIn().size());
    }
    @Test
    public void webBrowserOrderTest(){
        Building b = new Building();
        try {
            browser = b.getFloor(2).getApartment(1).getBrowser();
        }
        catch(NoSuchApartmentException nse){

        }
        List<Dish> itemsToOrder= new ArrayList<>();
        int maxItem = menu.size()-1;
        itemsToOrder.add(menu.get(NumUtil.getRandomBetween(0,maxItem)));
        itemsToOrder.add(menu.get(NumUtil.getRandomBetween(0,maxItem)));
        itemsToOrder.add(menu.get(NumUtil.getRandomBetween(0,maxItem)));
        assertTrue(browser.placeOrder(itemsToOrder));
    }

    @Test
    public void testPrepareOrder(){
        Address a = new Address(1,1,1);
        Dish[] dishes = {menu.get(0), menu.get(3), menu.get(5)};
        FoodOrder fo = takeOutShop.placeOrder(a,dishes);
        assertNotNull(fo);
        takeOutShop.processOrder(fo);
        assertEquals(1,takeOutShop.getOrdersReadyOut().size());
        ArrayDeque<FoodOrder> list = takeOutShop.getOrdersReadyOut().get(1);
        assertTrue(list.contains(fo));
    }

    @Test
    public void testRushHour(){
       /* int seed = 15;
        List<FoodOrder> mealTime = new ArrayList<>();
        while(seed > 0){
            Dish[] d = createRandomDishes();
            FoodOrder fo = new FoodOrder(createRandomAddress(),d);
            takeOutShop.placeOrder(fo.getAddress(), d);
            seed--;
        }
        // now we make the hash
        takeOutShop.getOrdersIn().values().stream().forEach( orderList ->
                orderList.stream().forEach( order ->
                        takeOutShop.processOrder(order)
                )
        );
        Integer readyForPickup = (int) (long)takeOutShop.getOrdersReadyOut().values().stream().collect(Collectors.counting());
        assertEquals((Integer)15, readyForPickup); */
    }


    @Test
    public void testAddSingleMenuItem(){
        Dish d = new Dish("Pulled pork hero", false, 1800, Dish.Type.MEAT);
        takeOutShop.addMenuItem(d);
        List<Dish> newMenu = takeOutShop.getMenu();
        assertTrue(newMenu.contains(d));
    }

    @Test
    public void testAddMenuItemList(){
        //TODO: create an ArrayList<Dish> of 10 Dishes here in the test class
        // and add them to the TakeOutShop's menu using the addMenuItemList that you implemented there.
        // use helper method getRandomOrder(int numItems)
        List<Dish> randomDishes = createDishes();
        takeOutShop.addMenuItemList(randomDishes);
        assertTrue(menu.size() == 19);
    }

    @Test
    public void testSetNewMenu(){
        // original size of menu
        assertEquals(9, takeOutShop.getMenu().size());
        List<Dish> random = getRandomOrder(15);
        takeOutShop.setNewMenu(random);
        assertEquals(15, takeOutShop.getMenu().size());
    }
/* NOTE: This test generated a file named 'menu.json' that contains a json representation of the original hard-coded menu in the TakeOutShop class.
That menu contains 9 elements
 */

    @Test
    public void testPublishDefaultMenu() {
        // original size hard-coded menu has 9 items
        assertEquals(9, takeOutShop.getMenu().size());
        MenuWriter.publish("menu.json", takeOutShop.getMenu());
    }

    @Test
    public void testFileReader(){
        MenuFileReader mr = new MenuFileReader();
        takeOutShop.setNewMenu(mr.read("menu.json"));
        assertEquals(9,takeOutShop.getMenu().size());
    }

    @Test
    public void testPublishMenu(){
        // original size hard-coded menu has 9 items
        assertEquals(9,takeOutShop.getMenu().size());
        //TODO: Manually create an ArrayList<Dish> of 10 Dishes here in the test class
        // and add them to the TakeOutShop's menu using the addMenuItemList that you implemented there.
        // dish format:
        //new Dish(String name, boolean isVegetarian, int calories, Enum Dish.Type.[MEAT, FISH, OTHER]);

        List<Dish> randomDishes = createDishes();
        takeOutShop.addMenuItemList(randomDishes);
        takeOutShop.setNewMenu(randomDishes);
        //TODO: add the above dishes to the takeOutShop's current menu using takeOutShop.setNewMenu as above

        assertEquals(10,takeOutShop.getMenu().size());
        //TODO: publish new, improved menu to output using static method:MenuWriter.publish()
        //TODO: provide a personal file name
        MenuWriter.publish("tofik_menu.json", takeOutShop.getMenu() );
    }

    private List<Dish> createDishes() {
        List<Dish> randomDishes = new ArrayList<>();
        String [] dishNames = {"Calas", "Charleston red rice", "Chicken bog", "Dirty rice", "Glorified rice",
                "Gumbo", "Hawaiian haystack", "Jambalaya", "Rice and gravy", "Shrimp Creole"};

        for(int i = 0; i < dishNames.length; i++){
            boolean isVegetarian = i % 2 == 0 ? true : false;
            int calories = i * 37 * (new Random().nextInt(23)) / (new Random().nextInt(13) + 1);
            Dish.Type type1 = isVegetarian ? Dish.Type.MEAT : Dish.Type.OTHER;
            Dish.Type type2 = i % 3 == 0 ? Dish.Type.FISH : Dish.Type.OTHER;
            Dish dish;
            if(i % 2 == 0){
                dish = new Dish(dishNames[i], isVegetarian, calories, type1);
            } else {
                dish = new Dish(dishNames[i], isVegetarian, calories, type2);
            }
            randomDishes.add(dish);
        }
        return randomDishes;
    }


    @Test
    public void testGenerateReceipt(){
        Address addr = new Address(1,2,3);
        MenuWriter.publish(addr.toString(), getRandomOrder(5));
    }

    @Test
    public void testLoadExternalMenuFile(){
        assertEquals(9,takeOutShop.getMenu().size());
        MenuFileReader mfr = new MenuFileReader();
        //TODO: replace fileName with the one you created in testPublishMenu
        List<Dish> importedMenu = mfr.read("tofik_menu.json");
        takeOutShop.setNewMenu(importedMenu);
        assertEquals(importedMenu.size(),takeOutShop.getMenu().size());
        assertEquals(10, takeOutShop.getMenu().size());
    }

    /**======================= helper methods  ================================ */

    private Address createRandomAddress(){
        return new Address(1, NumUtil.getRandomBetween(0,6), NumUtil.getRandomBetween(0,3));

    }
    private Dish[] createRandomDishes(){
        Dish[] dishes = {menu.get(NumUtil.getRandomBetween(0,8)), menu.get(NumUtil.getRandomBetween(0,8)), menu.get(NumUtil.getRandomBetween(0,8))};
        return dishes;
    }

    private List<Dish> getRandomOrder(int numItems){
        List<Dish> itemsToOrder= new ArrayList<>();
        int maxItem = menu.size()-1;
        int items = numItems;
        while(items > 0) {
            itemsToOrder.add(menu.get(NumUtil.getRandomBetween(0, maxItem)));
            items--;
        }
        return itemsToOrder;
    }
}
