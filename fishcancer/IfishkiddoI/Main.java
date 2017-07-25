package IfishkiddoI;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.impl.Condition;
import org.dreambot.api.wrappers.interactive.NPC;

import javax.swing.plaf.nimbus.State;

import java.awt.*;

import static org.dreambot.api.methods.skills.Skill.values;

@ScriptManifest(category = Category.FISHING, name = "IFishKiddoI", author = "IkiddoI", version = 1.0, description = "free ez bank")
public class Main extends AbstractScript{

    private State state;
    private final Area BANK_AREA = new Area(new Tile(3098, 3500), new Tile(3090, 3500), new Tile(3090, 3487), new Tile(3098, 3487), new Tile(3099, 3487));
    private final Area LOOT_AREA = new Area(3100, 3422, 3109, 3434, 0);
    private final Tile lootTile = new Tile(3109,3432,0);


    private enum State {
        BANK, WALK_TO_BANK, WALK_TO_FISH, FISH
    }

    @Override
    public void onStart() {
        for (Skill s : values()) { //Start all skill trackers.
            getSkillTracker().start(s, !getClient().getInstance().getScriptManager().isRunning());
            log("Welcome to your simple fisher by IkiddoI");
            log("if you experience any issues while running this script please report them to me on the forums, maybe im gonna fix them");
            log("Enjoy the script, IFishKiddoI");
            log("cogito ergo sum!");
        }
    }
    private State getState() {
        //if your inventory is full, you should either be walking to the bank or banking
        if (getInventory().isFull()) {
            if (BANK_AREA.contains(getLocalPlayer())) {
                return State.BANK;
            } else {
                return State.WALK_TO_BANK;
            }
        } else {
            //if it isn't full, you'll want to either be picking up hides or walking to the cows
            if (LOOT_AREA.contains(getLocalPlayer())) {
                return State.FISH;
            } else {
                return State.WALK_TO_FISH;
            }
        }
    }

    private void walkingSleep(){
        sleepUntil(new Condition(){
            public boolean verify(){
                return getLocalPlayer().isMoving();
            }
        }, Calculations.random(1200,1600));
        sleepUntil(new Condition(){
            public boolean verify(){
                return !getLocalPlayer().isMoving();
            }
        },Calculations.random(2400,3600));
    }



    @Override
    public int onLoop() {

        if (getLocalPlayer().isMoving()) {
            Tile dest = getClient().getDestination();
            if (dest != null && getLocalPlayer().getTile().distance(dest) > 1) {
                return Calculations.random(100, 250);
            }
        }
        if (!getWalking().isRunEnabled() && getWalking().getRunEnergy() > 70) {
            getWalking().toggleRun();
        }

        switch (state) {
            case BANK:

            {

                if (getBank().isOpen()) {
                    sleepUntil(new Condition() {
                        public boolean verify() {
                            return true;
                        }
                    }, Calculations.random(900, 1200));
                    //this sleepUntil will sleep until your inventory is empty.
                } else {
                    //if it isn't open, open it!
                    getBank().open();

                    //now we get into our first sleepUntil, this takes a Condition and a timeout as arguments
                    //a Condition is an object that has a verify method, when it returns true the sleepUntil cuts out
                    //or it'll wait until you reach the timeout
                    sleepUntil(new Condition() {
                        public boolean verify() {

                            if(getBank().depositAllExcept(item -> item != null && item.getName().contains("Rod"));
                            return getBank().isOpen();
                        }
                    }, Calculations.random(900, 1200));
                    //so in that condition we set it to go until the bank is open, or 900-1200 milliseconds has passed
                }
                //the reak statement is required after each case otherwise it'll spill over into the next
                //this says to break out of your switch/case and to not continue on to the others.
                break;
            }
            case FISH: {
                NPC spot = getNpcs().closest("Fishing spot");
                if (spot != null) {
                    spot.interact("Lure");
                }
            }
            break;

            case WALK_TO_BANK:
            {
                if (!LOOT_AREA.contains(getLocalPlayer())) {
                }
                break;
            }
            case WALK_TO_FISH:
                if (!LOOT_AREA.contains(getLocalPlayer())) {
                    getWalking().walk(lootTile);
                }
                break;
    } return Calculations.random(200, 400);
}

    @Override
    public void onExit() {
        log("Thanks for using my IBarbkiddofisherI feel free to use it again!") ;
    }

    public void onPaint(Graphics g) {

    }


}

