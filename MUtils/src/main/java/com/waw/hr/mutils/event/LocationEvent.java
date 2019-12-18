package com.waw.hr.mutils.event;

public class LocationEvent {

    public static class GET_LOCATION_EVENT {
        private String city;

        public GET_LOCATION_EVENT(String city) {
            this.city = city;
        }

        public String getCity() {
            return city;
        }
    }

}
