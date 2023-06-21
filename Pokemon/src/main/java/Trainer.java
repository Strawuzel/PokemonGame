import java.util.ArrayList;
import java.util.Scanner;

public class Trainer {


    private String name;
    private ArrayList<Pokemon> pokeTeam = new ArrayList<>();

    public void setIsWinner(boolean isWinner) {
        this.isWinner = isWinner;
    }
    public boolean getIsWinner(){
        return isWinner;
    }

    private boolean isWinner = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Pokemon> getPokeTeam() {
        return pokeTeam;
    }

    public void setPokeTeam(ArrayList<Pokemon> pokeTeam) {
        this.pokeTeam = pokeTeam;
    }
    public void pokeAddPokemon(Pokemon pokemon){
        pokeTeam.add(pokemon);
    }
    public void pokeAddPokemon(int index, Pokemon pokemon){
        pokeTeam.add(index,pokemon);
    }
    public void setUpTrainer(Trainer user){
        Scanner sc = new Scanner(System.in);
        System.out.println("\nWillkommen zum Pokemon-Arena-Kampf!");
        System.out.println("Wie lautet dein Name?");
        String userName = sc.nextLine();
        user.setName(userName);
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "name='" + name + '\'' +
                ", pokeTeam=" + pokeTeam +
                '}';
    }

}
