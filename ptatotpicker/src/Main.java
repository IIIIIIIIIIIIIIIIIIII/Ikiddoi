import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;

import java.util.Random;

@ScriptManifest(author = "IkiddoI", category = Category.MISC, name = "shit picker", version = 1.4, description = "picking shit up")
public class Main extends AbstractScript {

    Area ricefield = new Area(new Tile(3156, 3289), new Tile(3139, 3289), new Tile(3138, 3270), new Tile(3155, 3270));
    Tile depositboxtile = new Tile(3094, 3241, 0);
    GameObject Potato;
    GameObject depositbox;
    private final Tile IN_FRONT_OF_GATE = new Tile(3145, 3292, 0);
    private final Tile INSIDE_GATE = new Tile(3216, 3290, 0);


    @Override
    public int onLoop() {
        {
            if (!getInventory().isFull()) {
                if (ricefield.contains(getLocalPlayer())) {
                    Potato = getGameObjects().closest(c -> c != null && c.getName().equals("Potato") && c.hasAction("Pick"));
                    Potato.interact("Pick");
                    sleepUntil(() -> getLocalPlayer().getAnimation() == 827, Calculations.random(1429, 2845));
                    sleep(Calculations.random(300, 600));
                } else {
                    getWalking().walk(ricefield.getRandomTile());
                    sleep(Calculations.random(1627, 2482));
                }

            } else {
                if (depositboxtile.distance(getLocalPlayer()) <= 4) {
                    if (getDepositBox().open()) {
                            getDepositBox().depositAllItems();
                            sleepUntil(() -> getInventory().isEmpty(), 2500);
                    }
                        } else {
                            depositbox = getGameObjects().closest(d -> d != null && d.getName().equals("Bank deposit box") && d.hasAction("Deposit"));
                            depositbox.interact("Deposit");
                            sleepUntil(() -> getDepositBox().isOpen(), Calculations.random(929, 1845));

                    }
                }
            }
            return Calculations.random(204, 712);
        }

    }