package com.example.mbsnewproject.Tracking;

import static org.junit.Assert.*;

import android.location.Location;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TrackerServiceTest {


    @Test
    public void testStartCalled(){
        MockTrackerHandler mockTrackerHandler = new MockTrackerHandler();
        ITrackerService trackerService = new TrackerService(null, mockTrackerHandler);
        trackerService.startHandler();
        assertTrue(mockTrackerHandler.startCalled);
    }
    @Test
    public void testStopCalled() {
        MockTrackerHandler mockTrackerHandler = new MockTrackerHandler();
        ITrackerService trackerService = new TrackerService(null, mockTrackerHandler);
        trackerService.stopHandler("myTitle");
        assertTrue(mockTrackerHandler.stopCalled);
        assertEquals("myTitle", mockTrackerHandler.title);
    }

    @Test
    public void testAddLocationCalled() {
        double value = 50.0;
        MockTrackerHandler mockTrackerHandler = new MockTrackerHandler();
        TrackerService trackerService = new TrackerService(null, mockTrackerHandler);
        Location mockLocation = Mockito.mock(Location.class);
        Mockito.when(mockLocation.getLongitude()).thenReturn(value);
        Mockito.when(mockLocation.getLatitude()).thenReturn(value);
        trackerService.onLocationChanged(mockLocation);
        assertTrue(mockTrackerHandler.addLocationCalled);
        assertEquals(mockLocation, mockTrackerHandler.location);
        assertEquals(value, mockTrackerHandler.location.getLongitude(),0.001);
        assertEquals(value, mockTrackerHandler.location.getLongitude(),0.001);

    }


    private class MockTrackerHandler implements ITrackerHandler{
        private boolean startCalled;
        private boolean stopCalled;
        private boolean addLocationCalled;

        private String title;
        private Location location;

        public MockTrackerHandler(){
            startCalled = false;
            stopCalled = false;
        }

        @Override
        public void addLocation(Location location) {
            addLocationCalled = true;
            this.location = location;
        }

        @Override
        public void stop(String title) {
            stopCalled = true;
            this.title = title;
        }

        @Override
        public void start() {
            startCalled = true;
        }
    }
}

