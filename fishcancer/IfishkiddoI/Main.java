
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.utilities.impl.Condition;
import org.dreambot.api.wrappers.interactive.NPC;


import java.awt.*;
import java.util.Random;

import static org.dreambot.api.methods.skills.Skill.values;

@ScriptManifest(category = Category.FISHING, name = "IFishKiddoI", author = "IkiddoI", version = 1.7, description = "fishes fish at barb village gets you ez 99 fishing")
public class Main extends AbstractScript{

    Timer t = new Timer();
    private State state;
    Area BANK_AREA = new Area(3090, 3499, 3099, 3488);
    private final Area LOOT_AREA = new Area(3100, 3422, 3109, 3434, 0);
    private final Tile lootTile = new Tile(3109,3432,0);
    Random rand = new Random();
    int  n = rand.nextInt(Integer.MAX_VALUE / 20)/5 + 50;



    private enum State {
        BANK, WALK_TO_BANK, WALK_TO_FISH, FISH
    }

    @Override
    public void onStart() {
            log("Welcome to your simple fisher by IkiddoI");
            log("if you experience any issues while running this script please report them to me on the forums, im gonna fix them");
            log("Enjoy the script, IFishKiddoI");
            log("cogito ergo sum!");
        }

    private State getState() {
        if (getInventory().isFull()) {
            if (BANK_AREA.contains(getLocalPlayer())) {
                return State.BANK;
            } else {
                return State.WALK_TO_BANK;
            }
        } else {
            if (LOOT_AREA.contains(getLocalPlayer())) {
                return State.FISH;
            } else {
                return State.WALK_TO_FISH;
            }
        }
    }

    @Override
    public int onLoop() {
        if (getLocalPlayer().isMoving()) {
            Tile dest = getClient().getDestination();
            if (dest != null && getLocalPlayer().getTile().distance(dest) > 5) {
                return Calculations.random(100, 250);
            }
        }
        if (!getWalking().isRunEnabled() && getWalking().getRunEnergy() >= Calculations.random(50, 100)) {
            getWalking().toggleRun();
        }

        state = getState();
        switch (state) {
            case BANK: {
                if (getBank().isOpen()) {
                    getBank().depositAllItems();
                    if(!getInventory().contains("Feather", "Fly fishing rod"));
                    getBank().withdraw("Fly fishing rod", 1);
                    sleep(800);
                    getBank().withdrawAll("Feather");
                    sleep(800);
                    sleepUntil(new Condition() {
                        public boolean verify() {
                         return getInventory().contains("Feather", "Fly fishing rod");
                        }
                    }, Calculations.random(900, 1200));
                } else {
                    getBank().open();
                    sleepUntil(new Condition() {
                        public boolean verify() {
                            return getBank().isOpen();
                        }
                    }, Calculations.random(900, 1200));
                }
            }
            break;

            case FISH: {//nog fixen
                NPC fishingSpot = getNpcs().closest("Fishing spot");
                if (fishingSpot != null && fishingSpot.isOnScreen())
                    fishingSpot.interact("Lure");
                sleep(16000, 25000);
                if (!getLocalPlayer().isInteracting(fishingSpot)) {
                    fishingSpot.interact("Lure");
                    if (getLocalPlayer().distance(fishingSpot) > 3) {
                        getWalking().walk(fishingSpot);
                    } else getCamera().rotateToEntity(getGameObjects().closest("spot"));

                }
            }
            break;

            case WALK_TO_BANK:
            {
                getWalking().walk(BankLocation.EDGEVILLE.getCenter());
                Calculations.random(2400, 2800);
                getBank().openClosest();
                break;
            }

            case WALK_TO_FISH:
                if (!LOOT_AREA.contains((getLocalPlayer()))){
                    getWalking().walk(lootTile);
                    Calculations.random(200, 400);
                }
                break;
        }
        return Calculations.random(200, 400);

    }


    @Override
    public void onExit() {
        log("Thanks for using my IBarbkiddofisherI feel free to use it again!") ;
    }

    public void onPaint(Graphics g) {
        g.drawString("Time ran: " + t.formatTime(), 8, 335);
    }


}
