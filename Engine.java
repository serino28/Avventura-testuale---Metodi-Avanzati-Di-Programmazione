package serinoTeam.adventureserino;

import java.util.Iterator;
import java.util.Queue;
import serinoTeam.adventureserino.parser.ParserOutput;
import serinoTeam.adventureserino.parser.Parser;
import serinoTeam.adventureserino.games.SerinoAdventure;
import java.util.Scanner;
import serinoTeam.adventureserino.type.CommandType;

/**
 *
 * @author Antonio Serino
 */
public class Engine {

    private final GameDescription game;

    private final Parser parser;

    public Engine(GameDescription game) {
        this.game = game;
        try {
            this.game.init();
        } catch (Exception ex) {
            System.err.println(ex);
        }
        parser = new Parser();
    }

    public GameDescription getGame() {
        return game;
    }

    public Parser getParser() {
        return parser;
    }

    public void run() {
        if (game.isTimerOn()){
            game.getCronometro().avanza();
        }
        System.out.println(game.getCurrentRoom().getName());
        System.out.println("> "+game.getCurrentRoom().getDescription());
        System.out.println("-------------------------------------------------------------");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            if (game.isTimerOn() && game.getCronometro().leggi()>game.getTimer()) {
                System.out.println("Tempo esaurito!");
                break;
            } else if (game.isTimerOn()){
                System.out.println("[Ti rimangono "+((game.getTimer()-game.getCronometro().leggi())/1000) +" secondi]");
            }
            String command = scanner.nextLine();
            listParser : { Queue<ParserOutput> movements = parser.parse(command, game.getCommands(), game.getInventory(), game.getCurrentRoom());
                for (Iterator<ParserOutput> iter = movements.iterator(); iter.hasNext();) {
                    ParserOutput p = iter.next();
                    try {
                        if (p.getCommand() != null && p.getCommand().getType() == CommandType.END) {
                            System.out.println("Hai abbandonato il gioco!");
                            break;
                        } else {
                            game.nextMove(p, System.out);
                            iter.remove();
                            System.out.println("-------------------------------------------------------------");
                        }
                    }
                    catch (Exception e) {
                        System.out.println("Restart da: "+game.getCurrentRoom().getName());
                        System.out.println("> "+game.getCurrentRoom().getDescription());
                        System.out.println("-------------------------------------------------------------");
                        break listParser;
                    }
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Engine engine = new Engine(new SerinoAdventure());
        engine.run();
    }

}