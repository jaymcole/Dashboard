package dashboard.apps.weatherApp.weatherDataObjects;

public enum MapDirection {
    N,
    NE,
    E,
    SE,
    S,
    SW,
    W;

    public static MapDirection parseMapDirection(String direction) {
        if (direction.toLowerCase().compareTo("n") == 0 || direction.toLowerCase().compareTo("north") == 0) {
            return N;
        }
        if (direction.toLowerCase().compareTo("ne") == 0 || direction.toLowerCase().compareTo("northeast") == 0) {
            return NE;
        }
        if (direction.toLowerCase().compareTo("e") == 0 || direction.toLowerCase().compareTo("east") == 0) {
            return E;
        }
        if (direction.toLowerCase().compareTo("se") == 0 || direction.toLowerCase().compareTo("southeast") == 0) {
            return SE;
        }
        if (direction.toLowerCase().compareTo("s") == 0 || direction.toLowerCase().compareTo("south") == 0) {
            return S;
        }
        if (direction.toLowerCase().compareTo("sw") == 0 || direction.toLowerCase().compareTo("southwest") == 0) {
            return SW;
        }
        return W;
    }

}
