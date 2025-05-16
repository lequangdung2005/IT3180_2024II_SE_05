package com.hust.ittnk68.cnpm.communication;

import com.hust.ittnk68.cnpm.model.Vehicle;

public class PostVehicle extends ClientMessageBase {

    private Vehicle vehicle;

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public PostVehicle (String username, Vehicle vehicle) {
        super(username);
        this.vehicle = vehicle;
    }
    
}

