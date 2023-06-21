import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Database {
    private static final String[][] attackDatabase = new String[217][8];
    private static final HashMap<String, ArrayList<Attack>> attacks = new HashMap<>();
    private static final String[][] pokemonDatabase = new String[152][11];
    private static final HashMap<String, ArrayList<Pokemon>> pokemons = new HashMap<>();
    private static final String[] pokeTypes = new String[15];
    private static final String[][] effectivenessDatabase = new String[19][19];
    private static final HashMap<String, HashMap<String, Double>> effectivenessDB = new HashMap<>();
    private static final ArrayList<File> cryList = new ArrayList<>();
    public Database() {
    }

    private Attack createAttacks(int i) {
        return new Attack(Integer.parseInt(attackDatabase[i][0]), attackDatabase[i][1], attackDatabase[i][2], attackDatabase[i][3], attackDatabase[i][4], Integer.parseInt(attackDatabase[i][5]), Double.parseDouble(attackDatabase[i][6].replace('%', ' ')), Integer.parseInt(attackDatabase[i][7]));

    }

    private Pokemon createPokemons(int i) {
        return new Pokemon(0, Integer.parseInt(pokemonDatabase[i][0]), pokemonDatabase[i][1], pokemonDatabase[i][2], pokemonDatabase[i][3], new Attack(), new Attack(), new Attack(), new Attack(), getAttackDatabase().get("Typeless").get(0), Integer.parseInt(pokemonDatabase[i][5]), Integer.parseInt(pokemonDatabase[i][6]), Integer.parseInt(pokemonDatabase[i][7]), Integer.parseInt(pokemonDatabase[i][8]), Integer.parseInt(pokemonDatabase[i][9]), Integer.parseInt(pokemonDatabase[i][10]));
    }

    public void createAttacksHM() {
        for (int i = 1; i < attackDatabase.length; i++) {
            Attack attack = createAttacks(i - 1);
            if (!attacks.containsKey(attack.getType())) {
                attacks.put(attack.getType(), new ArrayList<>());
                attacks.get(attack.getType()).add(attack);
            } else {
                attacks.get(attack.getType()).add(attack);
            }
        }
    }

    public void createPokemonHM() {
        int counter = 0;
        for (int i = 0; i < pokemonDatabase.length - 1; i++) {
            Pokemon pokemon = createPokemons(i);
            if (!pokemons.containsKey(pokemon.getTypeOne())) {
                pokemons.put(pokemon.getTypeOne(), new ArrayList<>());
                pokeTypes[counter] = pokemon.getTypeOne();
                pokemons.get(pokemon.getTypeOne()).add(pokemon);
                counter++;
            } else {
                pokemons.get(pokemon.getTypeOne()).add(pokemon);
            }
        }
    }

    public void readAttacksCSV() throws IOException {
        File attackCSV = new File("src\\main\\resources\\CSV\\Attacks.csv");
        Scanner fileScanner = new Scanner(attackCSV);
        ArrayList<String[]> databaseUnsplit = new ArrayList<>();
        for (int i = 0; i < 218; i++) {
            databaseUnsplit.add(i, fileScanner.nextLine().split(";"));
        }
        for (int i = 1; i < databaseUnsplit.size(); i++) {
            for (int j = 0; j < databaseUnsplit.get(i).length; j++) {
                attackDatabase[i - 1][j] = databaseUnsplit.get(i)[j];
            }
        }
    }

    public void createEffectivenessHM() {
        for (int i = 0; i < effectivenessDatabase.length - 1; i++) {
            for (int j = 0; j < effectivenessDatabase[i].length - 1; j++) {
                if (!effectivenessDB.containsKey(effectivenessDatabase[i + 1][0])) {
                    effectivenessDB.put(effectivenessDatabase[i + 1][0], new HashMap<>());
                }
                effectivenessDB.get(effectivenessDatabase[i + 1][0]).put(effectivenessDatabase[0][j + 1], Double.parseDouble(effectivenessDatabase[i+1][j+1]));
            }
        }
    }

    public void readEffectivenessCSV() throws IOException {
        File effectivenessCSV = new File("src\\main\\resources\\CSV\\Effectiveness.csv");
        Scanner fileScanner = new Scanner(effectivenessCSV);
        ArrayList<String[]> databaseUnsplit = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            databaseUnsplit.add(i, fileScanner.nextLine().split(";"));
        }
        for (int i = 0; i < databaseUnsplit.size(); i++) {
            for (int j = 0; j < databaseUnsplit.get(i).length; j++) {
                effectivenessDatabase[i][j] = databaseUnsplit.get(i)[j];
            }
        }
    }
    public void readCries() {
        for (int i = 1; i < 152; i++) {
            String var = (i) + ".wav";
            File file = new File("src\\main\\resources\\Pokemon_Cries\\"+ var);
            cryList.add(i-1,file);
        }
    }

    public HashMap<String, HashMap<String, Double>> getEffectivenessDB() {
        return effectivenessDB;
    }

    public HashMap<String, ArrayList<Attack>> getAttackDatabase() {
        return attacks;
    }

    public HashMap<String, ArrayList<Pokemon>> getPokemonDatabase() {
        return pokemons;
    }

    public String[] getPokeTypes() {
        return pokeTypes;
    }

    public void readPokemonCSV() throws FileNotFoundException {
        File pkmnCSV = new File("src\\main\\resources\\CSV\\Pokemon.csv");
        Scanner fileScanner = new Scanner(pkmnCSV);
        ArrayList<String[]> databaseUnsplit = new ArrayList<>();
        for (int i = 0; i < 152; i++) {
            databaseUnsplit.add(i, fileScanner.nextLine().split(";"));
        }
        for (int i = 1; i < databaseUnsplit.size(); i++) {
            for (int j = 0; j < databaseUnsplit.get(i).length; j++) {
                pokemonDatabase[i - 1][j] = databaseUnsplit.get(i)[j];
            }
        }
    }

    public String[][] getPokemonDB() {
        return pokemonDatabase;
    }
    public ArrayList<File> getCries(){
        return cryList;
    }

}

