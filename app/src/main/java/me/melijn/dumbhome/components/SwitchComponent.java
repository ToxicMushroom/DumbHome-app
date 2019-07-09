package me.melijn.dumbhome.components;

//TODO convert to kotlin
public class SwitchComponent extends Component {

    private int id;
    private boolean on;


    public SwitchComponent(String name, Location location, int id, boolean state) {
        super(name, location, ComponentType.SWITCH);
        this.id = id;
        this.on = state;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public int getId() {
        return id;
    }
}
