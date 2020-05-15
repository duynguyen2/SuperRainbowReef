package SuperRainbowReef;

import java.util.Observable;

public class GameWorldObservable extends Observable {

    @Override
    protected synchronized void setChanged() {
        super.setChanged();
    }

}
