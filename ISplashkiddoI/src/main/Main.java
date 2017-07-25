package main;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.NPC;

import java.awt.*;
import java.util.Timer;

import static org.dreambot.api.methods.skills.Skill.*;

@ScriptManifest(author = "IkiddoI", category = Category.MAGIC, description = "Splashes all the way to 99", name = "ISplashKiddoI", version = 0.5)
public class Main extends AbstractScript {

    private Timer t = new Timer();
    private Area splashArea = new Area(new Tile(3208, 9615), new Tile(3218, 9616), new Tile(3218, 9623), new Tile(3214, 9625), new Tile(3208, 9622));


    @Override
    public void onStart() {
        for (Skill s : values()) { //Start all skill trackers.
            getSkillTracker().start(s, !getClient().getInstance().getScriptManager().isRunning());
            log("Welcome to your simple splasher by IkiddoI");
            log("if you experience any issues while running this script please report them to me on the forums, maybe im gonna fix them");
            log("Enjoy the script, ISplashKiddoI afk nuubs");
        }
    }

    private boolean attack(NPC npc) {
        if (npc != null) {
            if (npc.interact("Attack")) {
                if (sleepUntil(() -> getLocalPlayer().isInCombat() || npc.isInCombat(), 6000)) { //If true, we're now attacking an NPC
                    if (sleepUntil(() -> !getLocalPlayer().isInCombat(), Calculations.random(400000, 1000000)))
                        return true;
                }
            }
        }
        return false;
    }


    @Override
    public int onLoop() {
        if (!getClient().isLoggedIn()) {
            return 600;
        }


        if (attack(getNpcs().closest(npc -> (npc.getName().equals("Spider") && npc.hasAction("Attack"))))) {
        }
        return Calculations.random(300, 600);

    }




    public void onExit() {
        super.onExit();
    }



public void onPaint(Graphics g) {
    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", 1, 11));
}
}



