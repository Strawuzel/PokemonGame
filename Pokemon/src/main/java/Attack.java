public class Attack {

    private int index;
    private String name;
    private String effect;
    private String type;
    private String kind;
    private int power;
    private double accuracy;
    private int powerPoints;

    public Attack(int index, String name, String effect, String type, String kind, int power, double accuracy, int powerPoints) {
        this.index = index;
        this.name = name;
        this.effect = effect;
        this.type = type;
        this.kind = kind;
        this.power = power;
        this.accuracy = accuracy;
        this.powerPoints = powerPoints;
    }
    public Attack(){

    }
    public String getType(){
        return type;
    }
    public void setPowerPoints(int powerPoints) {
        this.powerPoints = powerPoints;
    }
    public String getName() {
        return name;
    }

    public String getKind() {
        return kind;
    }

    public int getPower() {
        return power;
    }

    public int getPowerPoints() {
        return powerPoints;
    }
    public int getIndex() {
        return index;
    }
    public String getEffect() {
        return effect;
    }
    public double getAccuracy() {
        return accuracy;
    }

    public String toString() {
        return "Attack{" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", effect='" + effect + '\'' +
                ", type='" + type + '\'' +
                ", kind='" + kind + '\'' +
                ", power=" + power +
                ", accuracy=" + accuracy +
                ", powerPoints=" + powerPoints +
                '}';
    }
}
