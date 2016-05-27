/**
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hedi.com.example.elarmor.unyielding;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Constants used in this sample.
 */
public final class Constants {

    private Constants() {
    }

    public static final String PACKAGE_NAME = "hedi.com.example.elarmor.unyielding";

    public static final String SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES_NAME";

    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";

    /**Radius are in metric system
     * Where in this case 50 meters
     * 50 Meters is enough to cover certain area where it will not overlapping.
     * 50 Meters was chosen because certain area in university will get bad signal
     * this will cause some problem
     */
    public static final float GeofenceRadius = 50;

    /**
     * HashMap is like HashTable where it will store a key and a value
     * Key in this situation will be String and a name of the location
     * Value will be the latitude and Longitude
     */
    public static final HashMap<String, LatLng> Sunway_University = new HashMap<String, LatLng>();
    static {
        //Side Gate A is nearby a shop call mynews
        Sunway_University.put("Side Gate A", new LatLng(3.068407, 101.603434));

        //Side Gate B is nearby the new building
        Sunway_University.put("Side Gate B", new LatLng(3.067719, 101.603420));

        //Canopy will cover part of cafeteria, Graduation Centre and a little bit of South building
        Sunway_University.put("Canopy", new LatLng(3.068340, 101.604567));

        //Cafeteria Old building
        Sunway_University.put("Cafeteria old building", new LatLng(3.068574, 101.603994));

        //Cafeteria New Building
        Sunway_University.put("Cafeteria New building", new LatLng(3.067352, 101.603866));

        //indah villa for testing
        Sunway_University.put("Indah Villa", new LatLng(3.070501, 101.604641));
    }
}
