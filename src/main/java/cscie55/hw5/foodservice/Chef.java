package cscie55.hw5.foodservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Map;

public class Chef implements Runnable{

    private String name = "";
    private FoodOrder order;
    private boolean foodOrderReady = false;
    private Map<Integer, ArrayDeque<FoodOrder>> ordersIn;
    private Map<Integer, ArrayDeque<FoodOrder>> ordersReady;
    private static int orderCompletedId = 0;
    private int chefId = 0;
    private static int hiringNumber = 0;
    Object  lock1 = new Object();
    Object  lock2 = new Object();

    private static final Logger logger = LogManager.getLogger(Chef.class.getName());

    public Chef(String name, Map<Integer, ArrayDeque<FoodOrder>> ordersIn , Map<Integer, ArrayDeque<FoodOrder>> ordersReady){
        this.name = name;
        this.ordersIn = ordersIn;
        this.ordersReady = ordersReady;
        this.chefId = ++hiringNumber;
    }

    protected void prepareOrder(FoodOrder order){
        if(null != order) {
            this.order = order;
            run();
        }
    }

     /*
     * Orders will be added to Maps keyed by FloorId.
     * this makes it easier for the deliveryPeople to organize their deliveries
     *
     */
    @Override
    public void run() {
    // use number of Dishes * 1000 as a sleep value
        Thread t = Thread.currentThread();
        t.setName(this.name);
        try {
            t.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int destinationFloor = 1;
        /*
        NumUtil.getRandomBetween(0,6);
         mark the order completed with an Id
         Orders will be added to a Map sorted by FloorId.
        int destinationFloor = order.getAddress().getFloorId();
        ArrayDeque<FoodOrder> inList = ordersIn.get(destinationFloor);
        Synchronize the minimum collection for removal of the order from the ordersIn and placment into ordersReady
         */
        Object outerLock = chefId > ordersIn.size() ?  lock1 : lock2;
        Object innerLock = chefId > ordersIn.size() ? lock2 : lock1;
        synchronized(outerLock) {
            synchronized (innerLock) {
                ArrayDeque<FoodOrder> inList = ordersIn.get(destinationFloor);
                order = inList.removeFirst();
                ordersReady.computeIfAbsent(destinationFloor, ArrayDeque::new);
                ArrayDeque<FoodOrder> orderReadyList = ordersReady.get(destinationFloor);
                order.setPickupId(++orderCompletedId);
                order.setChefSignature(this.name);
                orderReadyList.addLast(order);
            }
        }

        logger.info("Food is prepared and moved to ready list");
    }

    public String getName(){
        return name;
    }

}
