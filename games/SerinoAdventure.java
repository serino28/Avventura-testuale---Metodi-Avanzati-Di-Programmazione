package serinoTeam.adventureserino.games;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import serinoTeam.adventureserino.GameDescription;
import serinoTeam.adventureserino.parser.ParserOutput;
import serinoTeam.adventureserino.type.AdvObject;
import serinoTeam.adventureserino.type.Command;
import serinoTeam.adventureserino.type.CommandType;
import serinoTeam.adventureserino.type.Room;

/**
 *
 * @author Antonio Serino
 */
public class SerinoAdventure extends GameDescription {

    @Override
    public void init() throws Exception {
        startDescription();
        setTimer(300000);
        tryConnection();
        var url = "jdbc:h2:~/test";
        Connection conn = DriverManager.getConnection (url, "sa", ""); //Creazione connessione al DB
        Room ufficio;
        try (Statement stm = conn.createStatement()) {
            //Commands
            try (ResultSet rs1 = stm.executeQuery("SELECT * FROM comandi;")) { //salva i risultati di una query

                rs1.next();
                Command nord = new Command(CommandType.NORD, rs1.getString("nome"));
                nord.setAlias(tokenAlias(rs1.getString("alias")));
                getCommands().add(nord);
                rs1.next();
                Command sud = new Command(CommandType.SOUTH, rs1.getString("nome"));
                sud.setAlias(tokenAlias(rs1.getString("alias")));
                getCommands().add(sud);
                rs1.next();
                Command est = new Command(CommandType.EAST, rs1.getString("nome"));
                est.setAlias(tokenAlias(rs1.getString("alias")));
                getCommands().add(est);
                rs1.next();
                Command ovest = new Command(CommandType.WEST, rs1.getString("nome"));
                ovest.setAlias(tokenAlias(rs1.getString("alias")));
                getCommands().add(ovest);
                rs1.next();
                Command inventory = new Command(CommandType.INVENTORY, rs1.getString("nome"));
                inventory.setAlias(tokenAlias(rs1.getString("alias")));
                getCommands().add(inventory);
                rs1.next();
                Command look = new Command(CommandType.LOOK_AT, rs1.getString("nome"));
                look.setAlias(tokenAlias(rs1.getString("alias")));
                getCommands().add(look);
                rs1.next();
                Command pickup = new Command(CommandType.PICK_UP, rs1.getString("nome"));
                pickup.setAlias(tokenAlias(rs1.getString("alias")));
                getCommands().add(pickup);
                rs1.next();
                Command use = new Command(CommandType.USE, rs1.getString("nome"));
                use.setAlias(tokenAlias(rs1.getString("alias")));
                getCommands().add(use);
                rs1.next();
                Command turn_on = new Command(CommandType.TURN_ON, rs1.getString("nome"));
                turn_on.setAlias(tokenAlias(rs1.getString("alias")));
                getCommands().add(turn_on);
                rs1.next();
                Command push = new Command(CommandType.PUSH, rs1.getString("nome"));
                push.setAlias(tokenAlias(rs1.getString("alias")));
                getCommands().add(push);
                rs1.next();
                Command end = new Command(CommandType.END, rs1.getString("nome"));
                end.setAlias(tokenAlias(rs1.getString("alias")));
                getCommands().add(end);
                rs1.close();
            }
            //Mappa
            Room anticameraReattore;
            Room ingressoReattore;
            Room alternatore;
            Room turbine;
            Room salaControllo;
            Room  gasRoom;
            Room  reattore;
            try ( //Rooms
                ResultSet rs2 = stm.executeQuery("SELECT * FROM stanze;")) {
                rs2.next();
                ufficio = new Room(rs2.getInt("ID"), rs2.getString("nome"), rs2.getString("descrizione"));
                ufficio.setLook(rs2.getString("setlook"));
                rs2.next();
                anticameraReattore = new Room(rs2.getInt("ID"), rs2.getString("nome"), rs2.getString("descrizione"));
                anticameraReattore.setLook(rs2.getString("setlook"));
                rs2.next();
                ingressoReattore = new Room(rs2.getInt("ID"), rs2.getString("nome"), rs2.getString("descrizione"));
                ingressoReattore.setHideDescription(rs2.getString("hidedescription"));
                ingressoReattore.setLook(rs2.getString("setlook"));
                ingressoReattore.setVisible(false);
                rs2.next();
                alternatore = new Room(rs2.getInt("ID"), rs2.getString("nome"), rs2.getString("descrizione"));
                alternatore.setLook(rs2.getString("setlook"));
                rs2.next();
                turbine = new Room(rs2.getInt("ID"), rs2.getString("nome"), rs2.getString("descrizione"));
                turbine.setLook(rs2.getString("setlook"));
                rs2.next();
                salaControllo = new Room(rs2.getInt("ID"), rs2.getString("nome"), rs2.getString("descrizione"));
                salaControllo.setLook(rs2.getString("setlook"));
                rs2.next();
                gasRoom = new Room(rs2.getInt("ID"), rs2.getString("nome"), rs2.getString("descrizione"));
                gasRoom.setLook(rs2.getString("setlook"));
                gasRoom.setDeathRoom (true);
                rs2.next();
                reattore = new Room(rs2.getInt("ID"), rs2.getString("nome"), rs2.getString("descrizione"));
                reattore.setLook(rs2.getString("setlook"));
                rs2.close();
            }
            
            ufficio.setNorth(anticameraReattore);
            anticameraReattore.setSouth(ufficio);
            anticameraReattore.setEast(ingressoReattore);
            ingressoReattore.setNorth(gasRoom);
            ingressoReattore.setEast(alternatore);
            ingressoReattore.setWest(anticameraReattore);
            gasRoom.setSouth(ingressoReattore);
            alternatore.setWest(ingressoReattore);
            alternatore.setNorth(turbine);
            alternatore.setEast(reattore);
            alternatore.setSouth(salaControllo);
            turbine.setSouth(alternatore);
            reattore.setWest(alternatore);
            salaControllo.setNorth(alternatore);

            try ( //Oggetti
                ResultSet rs3 = stm.executeQuery("SELECT * FROM OGGETTI;")) {
                //Tuta (code 1)
                rs3.next();
                AdvObject tuta = new AdvObject(rs3.getInt("ID"), rs3.getString("nome"), rs3.getString("descrizione"));
                tuta.setAlias(tokenAlias(rs3.getString("alias")));
                anticameraReattore.getObjects().add(tuta);
                tuta.setPickupable(true);
                //Chiavetta (code 2)
                rs3.next();
                AdvObject chiavetta = new AdvObject(rs3.getInt("ID"), rs3.getString("nome"), rs3.getString("descrizione"));
                chiavetta.setAlias(tokenAlias(rs3.getString("alias")));
                gasRoom.getObjects().add(chiavetta);
                chiavetta.setPickupable(true);
                //Pulsante (code 3)
                rs3.next();
                AdvObject pulsante = new AdvObject(rs3.getInt("ID"), rs3.getString("nome"), rs3.getString("descrizione"));
                pulsante.setAlias(tokenAlias(rs3.getString("alias")));
                turbine.getObjects().add(pulsante);
                pulsante.setPushable(true);
                //Computer (code 4)
                rs3.next();
                AdvObject computer = new AdvObject(rs3.getInt("ID"), rs3.getString("nome"), rs3.getString("descrizione"));
                computer.setAlias(tokenAlias(rs3.getString("alias")));
                salaControllo.getObjects().add(computer);
                computer.setIgnitable(true);
                //Terminale (code 5)
                rs3.next();
                AdvObject terminale = new AdvObject(rs3.getInt("ID"), rs3.getString("nome"), rs3.getString("descrizione"));
                terminale.setAlias(tokenAlias(rs3.getString("alias")));
                reattore.getObjects().add(terminale);
                terminale.setUsable(true);
                rs3.close();
            }
            stm.close();
        }
        //Stanza iniziale
        setCurrentRoom(ufficio);
    }

    @Override
    public void nextMove(ParserOutput p, PrintStream out) {

        if (p.getCommand() == null) {
            out.println("Non ho capito cosa devo fare! Prova con un altro comando.");
        } else {
            //move
            boolean noroom = false;
            boolean move = false;
            if (null != p.getCommand().getType()) switch (p.getCommand().getType()) {
                case NORD:
                    //Se il comando è NORD
                    if (getCurrentRoom().getNorth() != null) {
                        if (getCurrentRoom().getNorth().isVisible()){
                            try {
                                 execute (getCurrentRoom().getNorth().getId(), 3);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(SerinoAdventure.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            setCurrentRoom(getCurrentRoom().getNorth());
                            move = true;
                        } else {
                            out.println("> "+getCurrentRoom().getNorth().getHideDescription());
                        }
                    } else {
                        noroom = true;
                    }   break;
                case SOUTH:
                    //Se il comando è SUD
                    if (getCurrentRoom().getSouth() != null) {
                        if (getCurrentRoom().getSouth().isVisible()){
                            try {
                                execute (getCurrentRoom().getSouth().getId(), 3);
                                setCurrentRoom(getCurrentRoom().getSouth());
                                move = true;
                            } catch (InterruptedException ex) {
                                Logger.getLogger(SerinoAdventure.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            out.println(getCurrentRoom().getSouth().getHideDescription()); 
                        }
                    } else {
                        noroom = true;
                    }   break;
                case EAST:
                    //Se il comando è EST
                    if (getCurrentRoom().getEast() != null) {
                        if (getCurrentRoom().getEast().isVisible()){
                            try {
                                execute (getCurrentRoom().getEast().getId(), 3);
                                setCurrentRoom(getCurrentRoom().getEast());
                                move = true;
                            } catch (InterruptedException ex) {
                                Logger.getLogger(SerinoAdventure.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            out.println(getCurrentRoom().getEast().getHideDescription()); 
                        }
                    } else {
                        noroom = true;
                    }   break;
                case WEST:
                    //Se il comando è OVEST
                    if (getCurrentRoom().getWest() != null) {
                        if (getCurrentRoom().getWest().isVisible()){
                            try {
                                execute (getCurrentRoom().getWest().getId(), 3);
                                setCurrentRoom(getCurrentRoom().getWest());
                                move = true;
                            } catch (InterruptedException ex) {
                                Logger.getLogger(SerinoAdventure.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            out.println(getCurrentRoom().getWest().getHideDescription()); 
                        }
                    } else {
                        noroom = true;
                    }   break;
                case INVENTORY:
                    out.println("Nel tuo inventario ci sono:");
                    getInventory().forEach((o) -> {
                        out.println(o.getName() + ": " + o.getDescription());
                });
                 break;

                case LOOK_AT:
                    if (getCurrentRoom().getLook() != null) {
                        out.println(getCurrentRoom().getLook());
                    } else {
                        out.println("Non c'è niente di interessante qui.");
                    }   break;
                case PICK_UP:
                    if (p.getObject() != null) {
                        if (p.getObject().isPickupable()) {
                            p.getObject().setUsable(true);
                            getInventory().add(p.getObject());
                            getCurrentRoom().getObjects().remove(p.getObject());
                            out.println("Hai raccolto: " + p.getObject().getDescription());  
                        } else {
                            out.println("Non puoi raccogliere questo oggetto.");
                        }
                    } else {
                        out.println("Non c'è niente da raccogliere qui.");
                    }   break;
                case TURN_ON:
                    if (p.getObject() != null && p.getObject().isIgnitable() && p.getObject().isUsable()==false) {
                        p.getObject().setUsable(true);
                        out.println("Hai acceso: "+ p.getObject().getName());
                    } else {
                        if (p.getObject().isIgnitable()==true) {
                            out.println("Questo oggetto è già acceso!");
                        } else {
                            out.println("Non è possibile accendere questo oggetto!");
                        }
                    }
                       if(p.getObject().isIgnitable()==true && getCurrentRoom().getId()==5){
                           out.println("bene il PC ora è acceso, prova a inserire la chiavetta");
                       }
                    break;
                case PUSH:
                 boolean executePush=false;
                 //ricerca oggetti pushabili
                 if (p.getObject() != null && p.getObject().isPushable()) {
                        out.println("Hai premuto: " + p.getObject().getName());
                try {
                    //PUSH OGGETTI IN STANZA
                    executePush=execute (p.getObject().getId(), 2);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SerinoAdventure.class.getName()).log(Level.SEVERE, null, ex);
                }
                    } else if (p.getInvObject() != null && p.getInvObject().isPushable()) {
                        out.println("Hai premuto: " + p.getInvObject().getName());
                try {
                    //PUSH OGGETTI IN INVENTARIO
                    executePush=execute (p.getInvObject().getId(), 2);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SerinoAdventure.class.getName()).log(Level.SEVERE, null, ex);
                }
                    } else {
                        out.println("Non ci sono oggetti che puoi premere qui.");
                    }   break;
                case USE:
                    boolean executeUse=false;
                    if (p.getInvObject() != null && p.getObject() != null && p.getInvObject().isUsable() && p.getObject().isUsable() ){//OGGETTI SIA NELL'INVENTARIO CHE NELLA STANZA
                        try {
                         //ESECUZIONE OGGETTI
                          executeUse=execute(p.getInvObject().getId(),p.getObject().getId(), 3);
                         } catch (InterruptedException ex) {
                             Logger.getLogger(SerinoAdventure.class.getName()).log(Level.SEVERE, null, ex);
                         } 
                        if (executeUse==true){
                               out.println("Hai utilizzato: "+ p.getInvObject().getName()+" con "+p.getObject().getName());
                               getInventory().remove(p.getInvObject()); //Rimozione oggetti dall'inventario
                         }
                    } else if (p.getInvObject() != null && p.getInvObject().isUsable()){//OGGETTI NELL'INVENTARIO
                         try {
                         //ESECUZIONE OGGETTI
                          executeUse=execute(p.getInvObject().getId(), 1);
                         } catch (InterruptedException ex) {
                             Logger.getLogger(SerinoAdventure.class.getName()).log(Level.SEVERE, null, ex);
                         }
                         if (executeUse==true){
                               out.println("Hai utilizzato: "+ p.getInvObject().getName());
                               getInventory().remove(p.getInvObject()); //Rimozione oggetti dall'inventario
                         }
                    } else if (p.getObject() != null && p.getObject().isUsable()) {//OGGETTI NELLA STANZA
                         try {
                              executeUse=execute(p.getObject().getId(), 1);
                         } catch (InterruptedException ex) {
                               Logger.getLogger(SerinoAdventure.class.getName()).log(Level.SEVERE, null, ex);
                         }
                          if (executeUse==true){
                             out.println("Hai utilizzato: "+ p.getObject().getName());
                         } 
                    }
                    if (executeUse==false){
                        out.println("Oggetto inutilizzabile!");
                    }
                    break;
                default:
                    break;
            }
            if (noroom) {
                out.println("Attento, non è possibile andare in quella direzione!");
            } else if (move) {
                out.println(getCurrentRoom().getName());
                out.println("> "+getCurrentRoom().getDescription());
            }
        }
    }
       
        private void newDB (){
        var url = "jdbc:h2:~/test";
        Connection conn;
        try {
            conn = DriverManager.getConnection (url, "sa", "");
            Statement stm = conn.createStatement();
            String CREATE_TABLE = "CREATE TABLE stanze (id int, nome varchar(255), descrizione varchar(255), setLook varchar(500), hideDescription varchar(500));";
            String FILL_TABLE = "INSERT INTO stanze (id, nome, descrizione, setlook) VALUES ('0', 'Ufficio', 'Ambiente cupo e grigio', 'A NORD anticamera del reattore'); INSERT INTO stanze (id, nome, descrizione, setlook) VALUES ('1', 'Anticamera Reattore', 'Una stanza da cui è possibile accedere al centro di controllo del reattore nucleare', 'A destra un cartello con su scritto: Indossare dispositivi di protezione, PERICOLO DI MORTE! - Delle strane tute bianche sono appese al muro- A EST ingresso del reattore nucleare'); INSERT INTO stanze (id, nome, descrizione, setlook, hidedescription) VALUES ('2', 'Ingresso Reattore', 'Sei nell ingresso del reattore nucleare', 'A NORD sembra esserci una fuga di gas nocivo - A EST la stanza alternatore del reattore', 'Non puoi accederci senza una protezione!'); INSERT INTO stanze (id, nome, descrizione, setlook) VALUES ('3', 'Alternatore rettore', 'Sei molto vicino al nucleo del reattore', 'A NORD Stanza turbine - A EST Reattore H - A SUD Sala di controllo - A OVEST ingresso reattore'); INSERT INTO stanze (id, nome, descrizione, setlook) VALUES ('4', 'Stanza turbine', 'Questa stanza controlla un impianto di areazione dell intero edificio', 'Un pulsante lampeggiante - A SUD la stanza alternatore del reattore'); INSERT INTO stanze (id, nome, descrizione, setlook) VALUES ('5', 'Sala controllo', 'Questa sala gestisce le informazioni sensibili della centrale', 'Un gigantesco PC - A NORD la stanza alternatore del reattore'); INSERT INTO stanze (id, nome, descrizione, setlook) VALUES ('6', 'Stanza esperimenti', 'Il capo della sala di controllo è morto provando ad entrare in questa stanza, chissa perchè?!?', 'Una chiavetta USB, forse il capo la stava cercando per qualche mortivo... - A SUD ingresso della centrale'); INSERT INTO stanze (id, nome, descrizione, setlook) VALUES ('7', 'Reattore H', 'Le radiazioni sono cosi forti che iniziano a lacerare la tuta di protezione!!!', 'Un terminale che richiede un codice - A OVEST alternatore del reattore');";
            stm.executeUpdate(CREATE_TABLE);
            stm.executeUpdate(FILL_TABLE);
            CREATE_TABLE = "CREATE TABLE oggetti (id int, nome varchar(255), descrizione varchar(255), alias varchar(500));";
            FILL_TABLE = "INSERT INTO oggetti (id, nome, descrizione, alias) VALUES  ('1', 'Tuta protettiva', 'Una tuta che protegge dalle radiazioni', 'tuta-protezione'); INSERT INTO oggetti (id, nome, descrizione, alias) VALUES ('2', 'Chiavetta USB', 'La chiavetta USB che il capo della sala di controllo ha pagato con la vita', 'chiavetta-penna-USB'); INSERT INTO oggetti (id, nome, descrizione, alias) VALUES ('3', 'Pulsante', 'Pulsante di riavvio areazione edificio', 'bottone-pulsante'); INSERT INTO oggetti (id, nome, descrizione, alias) VALUES ('4', 'Computer', 'Il computer del capo della sala controllo', 'pc'); INSERT INTO oggetti (id, nome, descrizione, alias) VALUES ('5', 'Terminale', 'Terminale reattore', 'interfaccia-terminale');";
            stm.executeUpdate(CREATE_TABLE);
            stm.executeUpdate(FILL_TABLE);
            CREATE_TABLE = "CREATE TABLE comandi (id int, nome varchar(255), alias varchar(500));";
            FILL_TABLE = "INSERT INTO comandi (id, nome, alias) VALUES  ('1', 'nord', 'Nord-NORD'); INSERT INTO comandi (id, nome, alias) VALUES  ('2', 'sud', 'Sud-SUD'); INSERT INTO comandi (id, nome, alias) VALUES  ('3', 'est', 'Est-EST'); INSERT INTO comandi (id, nome, alias) VALUES  ('4', 'ovest', 'Ovest-OVEST'); INSERT INTO comandi (id, nome, alias) VALUES  ('5', 'inventario', 'inv-inventario'); INSERT INTO comandi (id, nome, alias) VALUES  ('6', 'osserva', 'guarda-vedi-trova-cerca-descrivi'); INSERT INTO comandi (id, nome, alias) VALUES  ('7', 'raccogli', 'prendi'); INSERT INTO comandi (id, nome, alias) VALUES  ('8', 'usa', 'utilizza-applica-indossa-inserisci'); INSERT INTO comandi (id, nome, alias) VALUES  ('9', 'accendi', 'attiva'); INSERT INTO comandi (id, nome, alias) VALUES  ('10', 'premi', 'spingin'); INSERT INTO comandi (id, nome, alias) VALUES  ('11', 'fine', 'end-esci-muori-ammazzati-ucciditi-suicidati-exit');";
            stm.executeUpdate(CREATE_TABLE);
            stm.executeUpdate(FILL_TABLE);
            stm.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } 
    }
    
    private void tryConnection () {
        var url = "jdbc:h2:~/test";
        Connection conn;
        try{
        conn = DriverManager.getConnection (url, "sa", "");
        Statement stm = conn.createStatement();
        stm.executeQuery("SELECT * FROM stanze;");
        stm.close();
        } catch (SQLException ex) {
            if (ex.getErrorCode()==42102){
                newDB ();
            } else {
                System.out.println(ex.getMessage());
            }
        }   
    }
    
    private String[] tokenAlias (String alias){ //permette di separare ogni alias presente nella stringa del DB
        String [] aliasVector = alias.split("-");
        return aliasVector;
    }

    private boolean execute (int id, int type) throws InterruptedException {
        boolean returnValue = false;
        if (type == 1){ //Tipo 1 USE
            if (id == 1) {
            getCurrentRoom().getEast().setVisible(true);
            returnValue = true;
            }
            if(id==2 && getCurrentRoom().getId()==5) {
                List<AdvObject> list = getCurrentRoom().getObjects();
                for (int i = 0; i<list.size(); i++) {
                    if(list.get(i).getId()==4){
                        if(list.get(i).isUsable()) {
                            System.out.println("Appare un codice: 'XZ14MA'");
                            returnValue = true;
                            i=list.size();
                        }
                    }
                }
            }
            if(id==2 && getCurrentRoom().getId()==5) {
                List<AdvObject> list = getCurrentRoom().getObjects();
                for (int i = 0; i<list.size(); i++) {
                    if(list.get(i).getId()==4){
                        if(list.get(i).isUsable()==false) {
                            System.out.println("Devi prima accendere il PC!");
                            returnValue = false;
                            i=list.size();
                        }
                    }
                }
            }

            if (id == 5) {
            System.out.println("Inserire codice per disattivare reattore: ");
            Scanner scanner = new Scanner(System.in);
            String codeIn = scanner.nextLine();
            System.out.println("Codice inserito!");
            for (int i = 5; i>0; i--) {
                System.out.println(i);
                Thread.sleep(1000);
            }
            if ("XZ14MA".equals(codeIn)){
                end();
            } else
            {
                death();
            }
            returnValue = true;
            }
        }

        if (type == 2){ //Tipo 2 PUSH
            if (id == 3) {
            getCurrentRoom().getSouth().getWest().getNorth().setDeathRoom (false);
            System.out.println("L'impianto di ventilazione ha ripreso a funzionare...");
            getCurrentRoom().getSouth().getWest().setLook("A NORD la stanza degli esperimenti - A EST la stanza alternatore del reattore");
            returnValue = true;
            }
        }

        if (type == 3){ //Tipo 3 Comandi stanze
            if (id == 6 && getCurrentRoom().getNorth().isDeathRoom()) {
            death();
            returnValue = true;
            }
        }
        if (type == 4){ //Tipo 4 USE di due oggetti insieme
            if (id == 2 && getCurrentRoom().getId()==5) {
            System.out.println("Appare un codice: 'XZ14MA'");
            returnValue = true;
            }
        }
        return returnValue;
    }

    private boolean execute (int id, int id_2, int type) throws InterruptedException {
        boolean returnValue = false;
        if (type == 3){ //Tipo 3 USE di due oggetti insieme
            if (id == 2 && id_2==4 && getCurrentRoom().getId()==5) {
            System.out.println("Appare un codice: 'XZ14MA'");
            returnValue = true;
            }
        }
        return returnValue;
    }
    private void startDescription() throws InterruptedException{
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Sei Jhonas Khanvald un giovane adolescente proveniente dall'anno 2021 e tornato indietro nel tempo al 27 giugno 2019."+
                             "\n");
        System.out.println("Provieni da un futuro distopico nel quale il mondo verrà distrutto da un esplosione nucleare causata dalla centrale, " +
                           "sei l'unico a conoscenza di ciò che sta per accadere."+
                            "\n");
        System.out.println("Sei uno dei pochi sopravvissuti al disastro e l'unico che ha la possibilità di evitarlo potendo cosi cambiare il corso degli eventi."+
                "\n");
        System.out.println("Sei in un ufficio all'interno della centrale."+
                "\n");
        System.out.println("Devi bloccare l'esplosione del nucleo della centrale, prima che sia troppo tardi..."+
                "\n");
        System.out.println("BUONA FORTUNA RAGAZZO!");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");
        Thread.sleep(4000);
    }
    private void death () {
        System.out.println("Sei MORTO!");
        System.exit(0);
    }
    private void end() {
        System.out.println("Sei riuscito a disattivare il reattore, il tuo futuro è ufficialmente salvo!");
        System.exit(0);
    }
}