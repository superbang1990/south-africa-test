package org.marsclub.test;

public class DMSVehicleInfo {

    private final Entity vehicleEntity, trackingEntity;

    public DMSVehicleInfo(Entity vehicleEntity, Entity trackingEntity) {
        this.vehicleEntity = vehicleEntity;
        this.trackingEntity = trackingEntity;
    }

    public String getVin() {
        return vehicleEntity.getString5();
    }

    public String getTrackingStatus() {
        return trackingEntity == null ? null : trackingEntity.getString6();
    }
}
