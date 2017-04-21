// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.network;

import android.app.Activity;

class Tuple {
    public Activity activity;
    public NetworkInterface networkInterface;

    public Tuple(Activity activity, NetworkInterface networkInterface) {
        this.activity = activity;
        this.networkInterface = networkInterface;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;

        if (obj instanceof Tuple) {
            Tuple other = (Tuple) obj;
            return activity.equals(other.activity) && networkInterface.equals(other.networkInterface);
        }
        return false;
    }
}