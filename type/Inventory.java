package serinoTeam.adventureserino.type;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Antonio Serino
 */
public class Inventory {

    private List<AdvObject> list = new ArrayList<>();

    public List<AdvObject> getList() {
        return list;
    }

    public void setList(List<AdvObject> list) {
        this.list = list;
    }

    public void add(AdvObject o) {
        list.add(o);
    }

    public void remove(AdvObject o) {
        list.remove(o);
    }
}
