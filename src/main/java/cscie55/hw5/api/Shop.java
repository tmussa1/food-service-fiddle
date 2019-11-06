package cscie55.hw5.api;

import cscie55.hw5.impl.Address;

import java.nio.file.Path;
import java.util.List;

public interface Shop <T>{
    Order placeOrder(Address address, T[] items);
    List<T> getMenu();
    void setNewMenu(List<T> newMenu);
}
