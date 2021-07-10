package serinoTeam.adventureserino.parser;

import java.util.LinkedList;
import serinoTeam.adventureserino.type.AdvObject;
import serinoTeam.adventureserino.type.Command;
import java.util.List;
import java.util.Queue;
import serinoTeam.adventureserino.type.Room;

/**
 *
 * @author Antonio Serino
 */
public class Parser {

    public Queue<ParserOutput> parse(String command, List<Command> commands,  List<AdvObject> inventory, Room room) {
        Queue<ParserOutput> movements = new LinkedList();
        List<AdvObject> objects = room.getObjects();
        String cmd = command.toLowerCase().trim();
        String[] tokens = cmd.split("\\s+");
        int length = tokens.length;
        int lengthVer = tokens.length-1;
        boolean verify = false;
        int objectType;
        int inventoryIndex;
        for (int i = 0; i<length; i++){
            objectType=-1;
            inventoryIndex=-1;
            int commandType = searchCommand(tokens[i], commands);
            if (commandType > -1) {
                if (lengthVer>i) {
                    if (commandType==0 && room.getNorth() != null) {//Se il comandando è di spostamento devo prima effettuare delle operazioni
                        objects = room.getNorth().getObjects();
                        room = room.getNorth();
                    }
                    if (commandType==2 && room.getSouth() != null) {
                        objects = room.getSouth().getObjects();
                        room = room.getSouth();
                    }
                    if (commandType==3 && room.getEast() != null) {
                        objects = room.getEast().getObjects();
                        room = room.getEast();
                    }
                    if (commandType==4 && room.getWest() != null) {
                        objects = room.getWest().getObjects();
                        room = room.getWest();
                    }

                    int posNextCommand = positionNextCommand (tokens, i+1, commands);
                    if (posNextCommand>-1){ //se presente un altro comando
                        for (int k=i+1; k<posNextCommand; k++){ //scorro i token fino al comando
                            if(objectType<0){
                                objectType = searchObject(tokens[k], objects);
                            }
                            if (inventoryIndex<0){
                                inventoryIndex = searchObject(tokens[k], inventory);
                            }
                        }
                        i=posNextCommand-1;
                    } else {//scorro i token fino all'ultimo
                        for (int k=i+1; k<length; k++){
                            if(objectType<0){
                                objectType = searchObject(tokens[k], objects);
                            }
                            if (inventoryIndex<0){
                                inventoryIndex = searchObject(tokens[k], inventory);
                            }
                        }
                        i=length;
                    }
                    verify=true;
                    if (objectType >-1 && inventoryIndex>-1)//se sta utilizzando sia l'oggetto in stanza che l'inventario
                    {
                        verify=true;
                        movements.add(new ParserOutput(commands.get(commandType), objects.get(objectType), inventory.get(inventoryIndex)));
                    } else if (objectType >-1 || inventoryIndex>-1) {
                        if (objectType >-1) {  //se sta utilizzando in oggetto in stanza
                            verify=true;
                            movements.add(new ParserOutput(commands.get(commandType), objects.get(objectType ), null));
                        }
                        if (inventoryIndex>-1) { //se ha utilizzato un oggetto nell'inventario
                            verify=true;
                            movements.add(new ParserOutput(commands.get(commandType), null, inventory.get(inventoryIndex)));
                        }
                    } else if (objectType==-1 && inventoryIndex==-1){
                        movements.add(new ParserOutput(commands.get(commandType), null, null));
                    }
                } else {
                    verify=true;
                    movements.add(new ParserOutput(commands.get(commandType), null, null));
                }
            }
        }
        if (verify==false) {
            movements.add (new ParserOutput(null, null));
        }
        return movements; //Lista dei comandi ricercati in una frase
    }

    private int positionNextCommand (String [] tokensList, int actualPos, List<Command> commands){
        int position = -1;
        int length = tokensList.length;
        for (int k = actualPos; k < length; k++){
            position = searchCommand(tokensList[k], commands);
            if (position>-1){
                position=k;
                break;
            }
        }
        return position;
    }

    private int searchCommand(String token, List<Command> commands) {
        for (int i = 0; i < commands.size(); i++) {
            if (commands.get(i).getName().equals(token) || commands.get(i).getAlias().contains(token)) {
                return i;
            }
        }
        return -1;
    }

    private int searchObject(String token, List<AdvObject> obejcts) {
        for (int i = 0; i < obejcts.size(); i++) {
            if (obejcts.get(i).getName().equals(token) || obejcts.get(i).getAlias().contains(token)) {
                return i;
            }
        }
        return -1;
    }
}