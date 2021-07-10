package serinoTeam.adventureserino;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import serinoTeam.adventureserino.parser.ParserOutput;
import serinoTeam.adventureserino.type.AdvObject;
import serinoTeam.adventureserino.type.Command;
import serinoTeam.adventureserino.type.Inventory;
import serinoTeam.adventureserino.type.Room;
import serinoTeam.adventureserino.type.Timer;

public abstract class GameDescription {
    private final List<Room> rooms = new ArrayList();
    private final List<Command> commands = new ArrayList();
    private final Inventory inventory = new Inventory();
    private final Timer cronometro = new Timer();
    private Room currentRoom;
    private int timer;
    private boolean timerOn = false;

    public GameDescription() {
    }

    public boolean isTimerOn() {
        return this.timerOn;
    }

    public void setTimerOn(boolean timerOn) {
        this.timerOn = timerOn;
    }

    public Timer getCronometro() {
        return this.cronometro;
    }

    public int getTimer() {
        return this.timer;
    }

    public void setTimer(int timer) {
        this.timerOn = true;
        this.timer = timer;
    }

    public void startCronometro() {
        this.cronometro.avanza();
    }

    public List<Room> getRooms() {
        return this.rooms;
    }

    public List<Command> getCommands() {
        return this.commands;
    }

    public Room getCurrentRoom() {
        return this.currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public List<AdvObject> getInventory() {
        return this.inventory.getList();
    }

    public abstract void init() throws Exception;

    public abstract void nextMove(ParserOutput var1, PrintStream var2);
}