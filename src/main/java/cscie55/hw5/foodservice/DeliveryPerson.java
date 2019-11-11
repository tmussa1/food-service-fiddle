package cscie55.hw5.foodservice;

import cscie55.hw5.api.Passenger;
import cscie55.hw5.api.Person;
import cscie55.hw5.api.Shop;
import cscie55.hw5.impl.Address;
import cscie55.hw5.impl.Apartment;
import cscie55.hw5.impl.Building;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliveryPerson extends Person implements Passenger, Runnable{
    private Map<Integer, List<FoodOrder>> ordersReady = new HashMap<>();
    private Shop employer;
    private int destination;
    private int currentFloor;

    public DeliveryPerson(String firstName, String lastName, Address address, Shop employer){
        super(firstName, lastName, address);
        this.currentFloor = 1;
        this.destination = Building.UNDEFINED_FLOOR;
        this.employer = employer;
    }

    @Override
    public int getDestination() {
        return destination;
    }

    @Override
    public void setDestination(int destinationFloor) {
        this.destination = destinationFloor;
    }

    @Override
    public int getCurrentFloor() {
        return currentFloor;
    }

    @Override
    public void setCurrrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    @Override
    public void arriveOnFloor(int arrivalFloor) {
        this.currentFloor = arrivalFloor;
        this.destination = 1;
    }

    public String ringApartment(Apartment apt){
        return "Ringing floor: "+apt.getAddress().getFloorId()+", apt: "+apt.getAddress().getApartmentId();
    }

    public int getDoorKey() {
        return getAddress().hashCode();
    }

    /**
     * The list of dishes assigned to a delivery person is consumed and cleared upon delivery person's arrival
     * to the floor in question
     */
    @Override
    public void run() {
        if(ordersReady != null){

            ordersReady.get(currentFloor).stream()
                    .forEach(order -> order.consume());

            ordersReady.get(currentFloor)
                       .clear();
        }

    }

    public Map<Integer, List<FoodOrder>> getOrdersReady() {
        return ordersReady;
    }

    public void setOrdersReady(Map<Integer, List<FoodOrder>> ordersReady) {
        this.ordersReady = ordersReady;
    }
}
