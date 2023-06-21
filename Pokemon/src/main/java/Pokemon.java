import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Pokemon {
    private int index;
    private String name;
    private int level;
    private String typeOne;
    private String typeTwo;
    private Attack attackTypeNormal;
    private Attack attackOnePkmnType;
    private Attack attackTwoPkmnType;
    private Attack attackSecondType;
    private Attack struggle = new Attack();
    private int healthPoints;
    private int curHealthPoints;
    private int attackStat;
    private int defenseStat;
    private int specialAttackStat;
    private int specialDefenseStat;
    private int speedStat;
    private boolean isAlive = false;

    public Pokemon(int level, int index, String name, String typeOne, String typeTwo, Attack attackTypeNormal, Attack attackOnePkmnType, Attack attackTwoPkmnType, Attack attackSecondType, Attack struggle, int healthPoints, int attackStat, int defenseStat, int specialAttackStat, int specialDefenseStat, int speedStat) {
        this.level = level;
        this.index = index;
        this.name = name;
        this.typeOne = typeOne;
        this.typeTwo = typeTwo;
        this.healthPoints = healthPoints;
        this.attackTypeNormal = attackTypeNormal;
        this.attackOnePkmnType = attackOnePkmnType;
        this.attackTwoPkmnType = attackTwoPkmnType;
        this.attackSecondType = attackSecondType;
        this.struggle = struggle;
        this.attackStat = attackStat;
        this.defenseStat = defenseStat;
        this.specialAttackStat = specialAttackStat;
        this.specialDefenseStat = specialDefenseStat;
        this.speedStat = speedStat;
    }

    public Pokemon(int level, int index, String name, String typeOne, int healthPoints, Attack attackTypeNormal, Attack attackOnePkmnType, Attack attackTwoPkmnType, Attack attackSecondType, Attack struggle, int attackStat, int defenseStat, int specialAttackStat, int specialDefenseStat, int speedStat) {
        this(level, index, name, typeOne, null, attackTypeNormal, attackOnePkmnType, attackTwoPkmnType, attackSecondType, struggle, healthPoints, attackStat, defenseStat, specialAttackStat, specialDefenseStat, speedStat);
    }

    public Pokemon(int level, int index, String name, String typeOne, String typeTwo, Attack struggle, int healthPoints, int attackStat, int defenseStat, int specialAttackStat, int specialDefenseStat, int speedStat) {
        this.level = level;
        this.index = index;
        this.name = name;
        this.typeOne = typeOne;
        this.typeTwo = typeTwo;
        this.struggle = struggle;
        this.healthPoints = healthPoints;
        this.attackStat = attackStat;
        this.defenseStat = defenseStat;
        this.specialAttackStat = specialAttackStat;
        this.specialDefenseStat = specialDefenseStat;
        this.speedStat = speedStat;
    }
    public Attack getStruggle() {
        return struggle;
    }

    public int getCurHealthPoints() {
        return curHealthPoints;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void pokeSummon(Trainer trainer, Database database) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        System.out.println("\n" + trainer.getName() + " schickt " + getName() + " in den Kampf!");
        File file= database.getCries().get(getIndex()-1);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void setCurHealthPoints(int curHealthPoints) {
        this.curHealthPoints = curHealthPoints;
    }

    public String getTypeTwo() {
        return typeTwo;
    }

    public Attack getAttackTypeNormal() {
        return attackTypeNormal;
    }

    public void setAttackTypeNormal(Attack attackTypeNormal) {
        this.attackTypeNormal = attackTypeNormal;
    }

    public Attack getAttackOnePkmnType() {
        return attackOnePkmnType;
    }

    public void setAttackOnePkmnType(Attack attackOnePkmnType) {
        this.attackOnePkmnType = attackOnePkmnType;
    }

    public Attack getAttackTwoPkmnType() {
        return attackTwoPkmnType;
    }

    public void setAttackTwoPkmnType(Attack attackTwoPkmnType) {
        this.attackTwoPkmnType = attackTwoPkmnType;
    }

    public Attack getAttackSecondType() {
        return attackSecondType;
    }

    public void setAttackSecondType(Attack attackSecondType) {
        this.attackSecondType = attackSecondType;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public int getAttackStat() {
        return attackStat;
    }

    public void setAttackStat(int attackStat) {
        this.attackStat = attackStat;
    }

    public int getDefenseStat() {
        return defenseStat;
    }

    public void setDefenseStat(int defenseStat) {
        this.defenseStat = defenseStat;
    }

    public int getSpecialAttackStat() {
        return specialAttackStat;
    }

    public void setSpecialAttackStat(int specialAttackStat) {
        this.specialAttackStat = specialAttackStat;
    }

    public int getSpecialDefenseStat() {
        return specialDefenseStat;
    }

    public void setSpecialDefenseStat(int specialDefenseStat) {
        this.specialDefenseStat = specialDefenseStat;
    }

    public int getSpeedStat() {
        return speedStat;
    }

    public void setSpeedStat(int speedStat) {
        this.speedStat = speedStat;
    }

    public String getTypeOne() {
        return typeOne;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public int getLevel() {
        return level;
    }

    public Pokemon() {

    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", typeOne='" + typeOne + '\'' +
                ", typeTwo='" + typeTwo + '\'' +
                ", attackTypeNormal=" + attackTypeNormal +
                ", attackOnePkmnType=" + attackOnePkmnType +
                ", attackTwoPkmnType=" + attackTwoPkmnType +
                ", attackSecondType=" + attackSecondType +
                ", struggle=" + struggle +
                ", healthPoints=" + healthPoints +
                ", attackStat=" + attackStat +
                ", defenseStat=" + defenseStat +
                ", specialAttackStat=" + specialAttackStat +
                ", specialDefenseStat=" + specialDefenseStat +
                ", speedStat=" + speedStat +
                '}';
    }
}
