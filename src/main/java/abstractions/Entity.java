package abstractions;

public abstract class Entity {
    private int _id = 0;
    private static int _objectcount = 0;

    public Entity() {
        _id = _objectcount++;
    }

    public Entity(int id) {
        _id = id;
    }
}
