package main;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.utilities.impl.Condition;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

@ScriptManifest(author = "IkiddoI", category = Category.MONEYMAKING, description = "Basic clay miner", name = "IclayminerI", version = 1.6)
public class Main extends AbstractScript {

    private final Area MINE_AREA = new Area(3176, 3379, 3185, 3364, 0);
    private final Area BANK_AREA = new Area(3179, 3448, 3191, 3431, 0);
    private final Tile lootTile = new Tile(3181, 3373, 0);


    Timer t = new Timer();
    private State state;
    private int itemsMade;
    private String pickaxe;
    private GameObject currRock = null;


    public void mine(GameObject myORE)
    {
        if(myORE.exists() && myORE.isOnScreen())
        {
            myORE.interact("Mine");
        }
    }


    public void onStart() {
        log("Welcome to my IClayminerI.");
        log("if you experience any issues while running this script please report them to me on the forums, maybe im gonna fix them");
        log("Argumentum ad captandum!");
    }

    private State getState() {
        if (getInventory().isFull()) {
            if (BANK_AREA.contains(getLocalPlayer())) {
                return State.BANK;
            } else {
                return State.WALK_TO_BANK;
            }
        } else {
            if (MINE_AREA.contains(getLocalPlayer())) {
                return State.MINE;
            } else {
                return State.WALK_TO_MINE;
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
        if (!getWalking().isRunEnabled() && getWalking().getRunEnergy() > 30) {
            getWalking().toggleRun();
        }
        state = getState();
        switch (state) {

            case BANK: {
                getWalking().walk(BankLocation.VARROCK_WEST.getCenter());
                getBank().openClosest();
                if (sleepUntil(() -> getBank().isOpen(), 9000)) {
                    if (getBank().depositAllExcept(item -> item != null && item.getName().contains("Pickaxe"))) {
                        if (sleepUntil(() -> !getInventory().isFull(), 8000)) {
                            if (getBank().close()) {
                                sleepUntil(() -> !getBank().isOpen(), 8000);
                            }
                        } else {
                            getBank().open();
                            sleepUntil(new Condition() {
                                public boolean verify() {
                                    return getBank().isOpen();
                                }
                            }, Calculations.random(900, 1200));
                        }
                    }
                }
            }


            case MINE: // nog fixen
                if (!MINE_AREA.contains(getLocalPlayer())) {
                    getWalking().walk(MINE_AREA.getCenter());
                    GameObject myORE = getGameObjects().closest(ore -> ore.getID() == 6705);
                    {
                        mine(myORE);
                    }return Calculations.random(300, 600);
                }
                break;

            case WALK_TO_BANK: {
                getBank().openClosest();
                Calculations.random(2400, 2800);
                break;
            }

            case WALK_TO_MINE: {
                if (!MINE_AREA.contains(getLocalPlayer())) {
                    getWalking().walk(lootTile);
                    Calculations.random(200, 400);
                }
                break;
            }
        }
        return Calculations.random(200, 400);
    }

    @Override
    public void onExit() {
        super.onExit();
    }

    private enum State {
        BANK, WALK_TO_BANK, WALK_TO_MINE, MINE;
    }
}

