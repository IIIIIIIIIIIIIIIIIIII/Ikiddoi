package main;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;

import java.awt.*;
import java.util.Timer;

@ScriptManifest(author = "IkiddoI", category = Category.COMBAT, name = "Edge Men Killer", version = 1.1, description = "Simple Edge Men killer")
public class Main extends AbstractScript {
    public String npcName = "Men";
    private Timer t = new Timer();
    private NPC currentNpc;
    private State state = null;

    private State getState() {
        if (getLocalPlayer().isInCombat()) {
            return State.SLEEP;
        } else {
            GroundItem gi = getGroundItems().closest("bones", "Chaos rune", "Clue scroll(easy)", "Grimy lantadyme", "Grimy dwarf weed", "Grimy cadantine", "Grimy harralander", "Grimy ranarr weed", "Grimy kwuarm", "Grimy irit leaf", "grimy avantoe", "Chaos rune");
            if (gi != null) {
                return State.LOOT;
            } else if (getInventory().contains("bones")) {
                return State.BURY;
            } else
                return State.KILL;
        }
    }

    @Override
    public void onStart() {
        getSkillTracker().start();
    }

    @Override
    public int onLoop() {
        if (!getClient().isLoggedIn()) {
            return 600;
        }
        state = getState();
        switch (state) {
            case BURY:
                getInventory().interact("bones", "Bury");
                sleep(600, 900);
                break;
            case KILL:
                currentNpc = getNpcs().closest(npc -> npc != null && npc.getName() != null && npc.getName().equals(npcName) && !npc.isInCombat() && npc.getInteractingCharacter() == null);
                if (currentNpc != null) { //does the npc exist?
                    if (!getLocalPlayer().isInCombat() && getLocalPlayer().getInteractingCharacter() == null) {
                        if (currentNpc.interact("Attack")) {
                            sleepUntil(() -> getLocalPlayer().isInCombat() || getLocalPlayer().getInteractingCharacter() != null, 2000);
                        }
                        return 100;
                    } else {
                        return 100;
                    }
                }
                break;

            case LOOT:
                GroundItem gi = getGroundItems().closest("bones", "Chaos rune", "Clue scroll(easy)", "Grimy lantadyme", "Grimy dwarf weed", "Grimy cadantine", "Grimy harralander", "Grimy ranarr weed", "Grimy kwuarm", "Grimy irit leaf", "grimy avantoe", "Chaos rune");
                if (gi != null) {
                    if (gi.isOnScreen()) {
                        gi.interact("Take");
                        sleep(900, 1200);
                    } else {
                        getWalking().walk(gi.getTile());
                    }
                }
                break;
            case SLEEP:
                sleep(300, 600);
                break;
        }
        return Calculations.random(300, 600);
    }

    public void onPaint(Graphics g) {
    }

    @Override
    public void onExit() {
        super.onExit();
    }

    private enum State {
        KILL, LOOT, BURY, SLEEP
    }
}
