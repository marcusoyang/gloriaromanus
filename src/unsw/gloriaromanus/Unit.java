package unsw.gloriaromanus;

import java.io.IOException;

import org.json.JSONObject;

/**
 * Represents a basic unit of soldiers
 * 
 * incomplete - should have heavy infantry, skirmishers, spearmen, lancers,
 * heavy cavalry, elephants, chariots, archers, slingers, horse-archers,
 * onagers, ballista, etc... higher classes include ranged infantry, cavalry,
 * infantry, artillery
 * 
 * current version represents a heavy infantry unit (almost no range, decent
 * armour and morale)
 */
public class Unit {
    private int numTroops; // the number of troops in this unit (should reduce based on depletion)
    private int meleeAttack; // can be either missile or melee attack to simplify. Could improve
    private int rangedAttack; // implementation by differentiating!
    private int defenseSkill; // skill to defend in battle. Does not protect from arrows!
    private int armour; // armour defense
    private int shieldDefense; // a shield
    private int morale; // resistance to fleeing
    private int speed; // ability to disengage from disadvantageous battle
    private String range; // range of the unit
    private String type;
    private String ability;
    private int turnsToProduce;

    public Unit(String unitType, int numTroops2, String config) throws IOException {

        JSONObject unitStats = new JSONObject(config).getJSONObject(unitType);

        // Unit stat values obtained from unit config file
        this.numTroops = numTroops2;
        this.meleeAttack = unitStats.getInt("meleeAttack");
        this.rangedAttack = unitStats.optInt("rangedAttack");
        this.defenseSkill = unitStats.getInt("defense");
        this.armour = unitStats.getInt("armour");
        this.shieldDefense = unitStats.getInt("shield");
        this.morale = unitStats.getInt("morale");
        this.speed = unitStats.getInt("speed");
        this.range = unitStats.getString("range");
        this.type = unitStats.getString("type");
        this.ability = unitStats.getString("ability");
        this.turnsToProduce = unitStats.getInt("turnsToProduce");

        // TODO = shield charge ability
        // TODO = heroic charge ability

    }

    public int getNumTroops() {
        return this.numTroops;
    }

    public String getType() {
        return this.type;
    }

    public String getAbility() {
        return this.ability;
    }

    /*
     * public static void main(String[] args) throws JsonGenerationException,
     * JsonMappingException, IOException { String content =
     * Files.readString(Paths.get("src/unsw/gloriaromanus/unit_config.json"));
     * JSONObject config = new JSONObject(content); JSONObject unitStats =
     * config.getJSONObject("berserker");
     * 
     * // ObjectMapper objectMapper = new ObjectMapper(); //
     * objectMapper.writeValue(new File("src/unsw/gloriaromanus/testFile.json"),
     * unitStats.toString());
     * 
     * FileWriter file = new FileWriter("src/unsw/gloriaromanus/testFile.json");
     * file.write(unitStats.toString()); file.close(); }
     */
}
