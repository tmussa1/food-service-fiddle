package cscie55.hw5.foodservice;

import cscie55.hw5.api.Shop;
import cscie55.hw5.impl.Address;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import cscie55.hw5.utils.NumUtil;
import cscie55.hw5.writer.MenuWriter;

public class TakeOutShop implements Shop<Dish> {


    private List<Dish> menu = new ArrayList<>();
    private static int ordersReceivedId = 0;
    private Map<Integer, ArrayDeque<FoodOrder>> ordersReadyOut = new HashMap<>();
    private Map<Integer, ArrayDeque<FoodOrder>> ordersIn = new HashMap<>();
    private List<Chef> chefs = new ArrayList<>();
    private List<DeliveryPerson> deliveryPeople = new ArrayList<>();
    private static final int NUMBER_CHEFS_ON_STAFF = 10;
    private static final int DELIVERY_PERSONS_ON_STAFF = 7;
    private String[] names = {"James Beard", "Melissa Clark", "Gordon Ramsey", "Ayesha Curry", "Anthony Bourdain", "Julia Child", "Gaida De Laurentis", "Jamie Oliver", "Jiro Ono", "Alice Waters"};


    /**
     * Constructor. Here we initialize the menu so we have something to sell
     */
    public TakeOutShop() {
        Dish[] menuArray = {
                new Dish("pork", false, 800, Dish.Type.MEAT),
                new Dish("beef", false, 700, Dish.Type.MEAT),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french fries", true, 530, Dish.Type.OTHER),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("season fruit", true, 120, Dish.Type.OTHER),
                new Dish("pizza", true, 550, Dish.Type.OTHER),
                new Dish("prawns", false, 400, Dish.Type.FISH),
                new Dish("salmon", false, 450, Dish.Type.FISH)
        };
        Arrays.stream(menuArray).forEach(menu::add);

        // create some chefs to run the shop
        int numChefs = NUMBER_CHEFS_ON_STAFF;
        while (numChefs > 0) {
            chefs.add(new Chef(names[numChefs - 1], ordersIn, ordersReadyOut));
            numChefs--;
        }

        int deliverors = DELIVERY_PERSONS_ON_STAFF;
        while (deliverors > 0) {
            deliveryPeople.add(new DeliveryPerson("Thomas", "Sims" + deliverors, new Address(2, 1, deliverors), this));
            deliverors--;
        }
    }

    @Override
    public List<Dish> getMenu() {
        return menu;
    }

    @Override
    public void setNewMenu(List<Dish> newMenu) {
        //TODO: implement. This method replaces the current menu with the newMenu items
        this.menu = newMenu;
    }

    /**
     * This method should use the MenuWriter's publish method to write a receipt for a foodOrder
     * @param foodOrder
     */
    public void generateReceipt(FoodOrder foodOrder){
        //TODO: implement
        MenuWriter.publish(foodOrder.getAddress().toString(), foodOrder.getItems());
    }
    /**
     * This method allows the menu to grow.
     *
     * @param dish
     */
    public void addMenuItem(Dish dish) {
        menu.add(dish);
    }

    public void addMenuItemList(List<Dish> dishes) {
        //TODO: add the dishes passed in to the current menu
        menu.addAll(dishes);
    }

    /**
     * This method receives the Dish[] from the world and uses that and the addresses to create a FoodOrder object.
     * It stores the order in a List in a Map, using the address' floorId as the Map's key
     * It calls processOrder to get the Chef working
     * and it returns a reference to the FoodOrder to the source.
     *
     * @param address
     * @param dishes
     * @return
     */
    @Override
    public FoodOrder placeOrder(Address address, Dish[] dishes) {
        FoodOrder order = new FoodOrder(address, dishes);
        order.setOrderAckId(++ordersReceivedId);
        int destinationFloor = address.getFloorId();
        ordersIn.computeIfAbsent(destinationFloor, ArrayDeque::new);
        ArrayDeque<FoodOrder> list = ordersIn.get(destinationFloor);
        list.addLast(order);
        return order;
    }

    /**
     * This method selects a Chef at random to whip up the meal
     *
     * @param order
     */
    public void processOrder(FoodOrder order) {
        Chef randomChef = chefs.get(NumUtil.getRandomBetween(0, 9));
        randomChef.run();//.prepareOrder(order);
    }

    public void pickupOrdersToDeliver() {
        //for later, when delivery service starts...
        ordersReadyOut.forEach((key, value) -> {

        });
    }

    public Map<Integer, ArrayDeque<FoodOrder>> getOrdersIn() {
        return ordersIn;
    }

    public Map<Integer, ArrayDeque<FoodOrder>> getOrdersReadyOut() {
        return ordersReadyOut;
    }


    public Collection<DeliveryPerson> getDeliveryPeople() {
        return deliveryPeople;
    }

    public List<Chef> getChefs() {
        return chefs;
    }
}

