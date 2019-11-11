package cscie55.hw5.api;

import cscie55.hw5.impl.Address;

public abstract class Person {

	private String firstName;

	private String lastName;

	private Address address;

	public Person(String firstName, String lastName, Address address){
		this.firstName = firstName;
		this.lastName = lastName;
        this.address = address;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
