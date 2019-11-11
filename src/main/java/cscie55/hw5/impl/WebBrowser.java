package cscie55.hw5.impl;

import cscie55.hw5.api.Shop;
import cscie55.hw5.foodservice.Dish;
import cscie55.hw5.foodservice.FoodOrder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class WebBrowser {

    private Shop<Dish> foodService;
    private Apartment apartment;
    Logger LOGGER = LogManager.getLogger(WebBrowser.class.getName());

    /**
     * Constructor.
     * @param apartment - the containing Apartment. IOW, the parent object since the WebBrowser is in the Apt
     * @param shop -the food 'shop' [here TakeOutShop'] that will receive and process the order
     */
    public WebBrowser(Apartment apartment, Shop shop){
        foodService = shop;
        this.apartment = apartment;
    }

    /**
     *
     * @param itemsToOrder
     * @return -boolean indicating that the order has been placed w/ the FoodService
     */
    public boolean placeOrder(List<Dish> itemsToOrder){
        List<Dish> menu = foodService.getMenu();
        FoodOrder fo = (FoodOrder) foodService
                .placeOrder(this.apartment.getAddress(), itemsToOrder.toArray(new Dish[itemsToOrder.size()]));
        apartment.addOrderWaiting(fo);
        LOGGER.info("Order has been submitted!");
        return (fo != null);
    }
}
