package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.Random;

public abstract class Ability {
    private static ArrayList<Province> provinces;
    private static ArrayList<Unit> invadingUnits;
    private static ArrayList<Unit> defendingUnits; 

    public static void setProvinces(ArrayList<Province> provinces) {
        Ability.provinces = provinces;
    }
    
    public static void initiateInvade(ArrayList<Unit> units) {
        Ability.invadingUnits = units;
        for (Unit u : units) {
            processAbility(u);
        }
    }

    public static void initiateDefend(ArrayList<Unit> units) {
        Ability.defendingUnits = units;
        for (Unit u : units) {
            processAbility(u);
        }
    }

	public static void restore(ArrayList<Unit> units) {
        for (Unit u : units) {
            restoreAbility(u);
        }
	}

    public static void processAbility(Unit u) {
        switch (u.getAbility()) {
            case "Legionary Eagle": processLegionaryEagle(u); break;
            case "Berserker Rage": processBerserkerRage(u); break;
            case "Phalanx": processPhalanx(u); break;
            case "Cantabrian Circle":
            case "Druidic Fervour":
        }
    }

    public static void restoreAbility(Unit u) {
        switch (u.getAbility()) {
            case "Legionary Eagle": restoreLegionaryEagle(u); break;
            case "Berserker Rage": restoreBerserkerRage(u); break;
            case "Phalanx": restorePhalanx(u); break;
            case "Cantabrian Circle":
            case "Druidic Fervour":
        }

        if (u.getRange().equals("melee") && u.getType().equals("cavalry")) {
            restoreHeroicCharge(u);
        }
    }

    private static void processLegionaryEagle(Unit u) {
        for (Unit u2 : getUnits(u)) {
            u2.addMorale(1);
        }
    }

    private static void restoreLegionaryEagle(Unit u) {
        for (Unit u2 : getUnits(u)) {
            u2.minusMorale(1);
        }
    }

    /**
     * Processes legionary eagle penalty after each battle
     * @param unit
     * @param initialTroops
     * @param humanProvince
     */
    public static void processLegionaryEagleDeath(Unit u, int casualty, Province p) {
        if (u.getAbility() == "Legionary Eagle") {
            double penalty = casualty * 0.2;
            sufferMoralePenalty(u.getPlayer(), penalty);
            u.getPlayer().addToLEPenaltyMap(p.getName(), penalty);
        }
    }

    public static void sufferMoralePenalty(Player player, double penalty) {
        for (Province p : provinces) {
            if (p.getPlayer().equals(player)) {
                ArrayList<Unit> provinceUnits = p.getUnits();
                for (Unit u2 : provinceUnits) {
                    u2.minusMorale(penalty);
                }
                player.increaseMoralePenalty(penalty);
            }
        }
    }

	public static void checkLERecapture(Province p) {
        Player player = p.getPlayer();
        if (player.mapContainsProvince(p)) {
            player.mapRemoveProvince(p);
        }
	}

    private static void processBerserkerRage(Unit u) {
        u.addMorale(9999);
        u.setMeleeAttack(u.getMeleeAttack() * 2);
        u.setArmour(0);
        u.setShieldDefense(0);
    }

    private static void restoreBerserkerRage(Unit u) {
        u.minusMorale(9999);
        u.setMeleeAttack(u.getMeleeAttack() / 2);
        restoreArmour(u);
        restoreShieldDefense(u);
    }

    private static void restoreArmour(Unit u) {
        UnitFactory uf = provinces.get(0).getFactories().get(0);
        u.setArmour(uf.restoreArmour(u.getUnitType()));
    }

    private static void restoreShieldDefense(Unit u) {
        UnitFactory uf = provinces.get(0).getFactories().get(0);
        u.setArmour(uf.restoreArmour(u.getUnitType()));
    }

    public static void processHeroicCharge() {
        if (invadingUnits.size() * 2 < defendingUnits.size()) {
            for (Unit u : invadingUnits) {
                if (u.getRange().equals("melee") && u.getType().equals("cavalry")) {
                    u.setMeleeAttack(u.getMeleeAttack() * 2);  // double melee attack
                    u.addMorale(u.getMorale() / 2);            // 50% higher morale
                }
            }
        }
	}

    public static void restoreHeroicCharge(Unit u) {
        u.setMeleeAttack(u.getMeleeAttack() / 2);
        u.setMorale(u.getMorale() * (2/3));
    }

    private static void processPhalanx(Unit u) {
        u.setDefenseSkill(u.getDefenseSkill() * 2);   // double defense
        u.setSpeed(u.getSpeed() / 2);                 // half speed
    }

    private static void restorePhalanx(Unit u) {
        u.setDefenseSkill(u.getDefenseSkill() / 2);
        u.setSpeed(u.getSpeed() / 2);
    }

    public static void processSkirmisherAntiArmour(Unit javelinSkirmisher, Unit other) {
        if (javelinSkirmisher.getAbility().equals("Skirmisher Anti-Armour")) {
            other.setArmour(other.getArmour() / 2);
        }
    }

    public static void restoreSkirmisherAntiArmour(Unit javelinSkirmisher, Unit other) {
        if (javelinSkirmisher.getAbility().equals("Skirmisher Anti-Armour")) {
           other.setArmour(other.getArmour() * 2);
        }
    }

    public static Unit processElephantAmok(Unit elephant, Unit other) {
        if (!elephant.getAbility().equals("Elephant Amok")) {
            return other;
        }

        ArrayList<Unit> units = getUnits(elephant);        
        if (units.size() != 1) {
            Random r = new Random();
            if (/*r.nextDouble() <= 0.1*/true) {
                int randNum = r.nextInt(units.size());
                if (units.get(randNum).equals(elephant)) {
                    randNum = findNextIndex(units, randNum);
                }
                Unit u = units.get(randNum);
                return u;
            }
        }
        return other;
    }

    private static ArrayList<Unit> getUnits(Unit u) {
        if (invadingUnits.contains(u)) {
            return invadingUnits;
        }
        return defendingUnits;
    }

    private static int findNextIndex(ArrayList<Unit> units, int index) {
        if (index == units.size() - 1) {
            return 0;
        } 
        return index++;
    }

}
