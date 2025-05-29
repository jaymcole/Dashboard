package dashboard.dataObjects;

public class Tuple <T, E> {
    public T left;
    public E right;

    public Tuple (T left, E right ) {
        this.left = left;
        this.right = right;
    }

}
