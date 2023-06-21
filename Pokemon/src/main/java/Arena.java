import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Arena {
    private Trainer user;
    private Trainer aI;
    private static final String[][] MENU = new String[][]{
            {"1", "Pokedex anzeigen"},
            {"2", "Team zusammenstellen und kämpfen"},
            {"0", "Abbrechen"}
    };
    private static final String[][] pokedex = new String[][]{
            {"0", "Abbrechen"}
    };
    private static final String[][] fightMenu = new String[][]{
            {"1", "Pokemon auswählen"},
            {"0", "Abbrechen"}
    };
    private static final String[][] battleMenu = new String[][]{
            {"1", "Attacken"},
            {"2", "Pokemon wechseln"},
            {"0", "Autobattle"}
    };

    public Arena() {
    }

    private Pokemon pokePrep(Database database, Trainer user, Trainer aI) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        int userInput;
        Pokemon userPokemon = new Pokemon();
        do {
            pokePrintMenu(fightMenu);
            userInput = pokeUserInputConfirm(fightMenu);
            if (userInput == 1) {
                userPokemon = pokeSearch(database);
                if (userPokemon.getTypeOne() != null) {
                    System.out.println(userPokemon.getName() + " ausgewählt");
                    userPokemon.setAlive(true);
                    pokeLeveling(userPokemon);
                    pokeAddAttacks(userPokemon, database);
                    user.pokeAddPokemon(userPokemon);
                    pokeFillUpTeam(database, 1, user);
                    pokeFillUpTeam(database, 0, aI);
                    return pokeUserChoose(user, database);
                }
            }
        } while (userInput != 0);
        return userPokemon;
    }

    public void pokeStart() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        Database database = new Database();
        database.readAttacksCSV();
        database.readPokemonCSV();
        database.readEffectivenessCSV();
        database.createAttacksHM();
        database.createPokemonHM();
        database.createEffectivenessHM();
        database.readCries();
        aI = new Trainer();
        user = new Trainer();
        int userInput;
        user.setUpTrainer(user);
        aI.setName("Echt voll so der arg böse Bösewicht, trust me bro");
        do {
            pokePrintMenu(MENU);
            System.out.println();
            userInput = pokeUserInputConfirm(MENU);
            if (userInput == 1) {
                pokePrintPokedex(database);
            }
            if (userInput == 2) {
                Pokemon chosenPkmn = pokePrep(database, user, aI);
                if (chosenPkmn.getTypeOne() != null) {
                    Pokemon aIPokemon = pokeAiChoose(aI, database,false);
                    boolean userWon = pokeFightMenu(user, aI, chosenPkmn, aIPokemon, false, database);
                    if (userWon) {
                        System.out.println("\nHerzlichen Glückwunsch! " + user.getName() + " hat gewonnen!\n");
                    } else {
                        System.out.println("\n" + aI.getName() + " hat gewonnen!\n");
                    }
                    user.setPokeTeam(new ArrayList<>());
                    aI.setPokeTeam(new ArrayList<>());
                }
            }
        } while (userInput != 0);
    }

    private Pokemon pokeUserChoose(Trainer user, Database database) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        int deathCount = 0;
        for (int i = 0; i < user.getPokeTeam().size(); i++) {
            if (!user.getPokeTeam().get(i).isAlive()) {
                deathCount++;
            }
        }
        if (deathCount == user.getPokeTeam().size()) {
            return new Pokemon();
        }
        System.out.println("\nDas ist dein Team \n");
        int length = user.getPokeTeam().size();
        System.out.printf("%-10s%-20s%-10s%-10s%-10s%-10s%-10s%-10s%-10s", "Index", "Name", "Level", "HP", "ATK", "DEF", "SpezATK", "SpezDef", "Init");
        System.out.println();
        for (int i = 0; i < user.getPokeTeam().size(); i++) {
            System.out.printf("%-10s%-20s%-10s%-10s%-10s%-10s%-10s%-10s%-10s", (i + 1), user.getPokeTeam().get(i).getName(), user.getPokeTeam().get(i).getLevel(), user.getPokeTeam().get(i).getCurHealthPoints() + "/" + user.getPokeTeam().get(i).getHealthPoints(), user.getPokeTeam().get(i).getAttackStat(), user.getPokeTeam().get(i).getDefenseStat(), user.getPokeTeam().get(i).getSpecialAttackStat(), user.getPokeTeam().get(i).getSpecialDefenseStat(), user.getPokeTeam().get(i).getSpeedStat());
            System.out.println();
        }
        Pokemon returnPokemon = user.getPokeTeam().get(pokeChoosePkmnConfirm(length) - 1);
        if (!returnPokemon.isAlive()) {
            System.out.println(returnPokemon.getName() + " wurde bereits besiegt!");
            return pokeUserChoose(user, database);
        } else {
            returnPokemon.pokeSummon(user, database);
            return returnPokemon;
        }
    }

    private Pokemon pokeAiChoose(Trainer trainer, Database database, boolean autoBattle) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        Random r = new Random();
        int deathCount = 0;
        for (int i = 0; i < trainer.getPokeTeam().size(); i++) {
            if (!trainer.getPokeTeam().get(i).isAlive()) {
                deathCount++;
            }
        }
        if (deathCount == trainer.getPokeTeam().size()) {
            return new Pokemon();
        }
        Pokemon returnPokemon = trainer.getPokeTeam().get(r.nextInt(trainer.getPokeTeam().size()));
        if (!returnPokemon.isAlive()) {
            return pokeAiChoose(trainer, database,autoBattle);
        } else {
            if (!autoBattle){
            returnPokemon.pokeSummon(trainer, database);}
            return returnPokemon;
        }
    }

    private void pokePrintPokedex(Database database) {
        int userInput;
        do {
            for (int i = 0; i < database.getPokemonDB().length - 1; i++) {
                for (int j = 0; j <= 3; j++) {
                    System.out.print(database.getPokemonDB()[i][j] + " / ");
                }
                System.out.println();
            }
            System.out.println();
            pokePrintMenu(pokedex);
            userInput = pokeUserInputConfirm(pokedex);
        } while (userInput != 0);
    }

    private void pokePrintAttacks(Pokemon chosenPkmn) {
        System.out.printf("%-10s%-20s%-10s%-10s%-10s", "Nummer", "Name", "Type", "Power", "Power Points");
        System.out.println();
        System.out.printf("%-10s%-20s%-10s%-10s%-10s", 1, chosenPkmn.getAttackOnePkmnType().getName(), chosenPkmn.getAttackOnePkmnType().getType(), chosenPkmn.getAttackOnePkmnType().getPower(), chosenPkmn.getAttackOnePkmnType().getPowerPoints());
        System.out.println();
        System.out.printf("%-10s%-20s%-10s%-10s%-10s", 2, chosenPkmn.getAttackTwoPkmnType().getName(), chosenPkmn.getAttackTwoPkmnType().getType(), chosenPkmn.getAttackTwoPkmnType().getPower(), chosenPkmn.getAttackTwoPkmnType().getPowerPoints());
        System.out.println();
        System.out.printf("%-10s%-20s%-10s%-10s%-10s", 3, chosenPkmn.getAttackSecondType().getName(), chosenPkmn.getAttackSecondType().getType(), chosenPkmn.getAttackSecondType().getPower(), chosenPkmn.getAttackSecondType().getPowerPoints());
        System.out.println();
        System.out.printf("%-10s%-20s%-10s%-10s%-10s", 4, chosenPkmn.getAttackTypeNormal().getName(), chosenPkmn.getAttackTypeNormal().getType(), chosenPkmn.getAttackTypeNormal().getPower(), chosenPkmn.getAttackTypeNormal().getPowerPoints());
        System.out.println();
    }

    private boolean pokeFightMenu(Trainer user, Trainer aI, Pokemon chosenPkmn, Pokemon aIPokemon, boolean autoBattle, Database database) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        if (pokeCheckLose(user)) {
            return false;
        } else if (pokeCheckLose(aI)) {
            return true;
        }
        do {
            int userChoice = 1;
            if (!autoBattle) {
                pokePrintMenu(battleMenu);
                userChoice = pokeUserInputConfirm(battleMenu);
            }
            if (userChoice == 1) {
                Attack chosenAttack;
                if (!autoBattle) {
                    pokePrintAttacks(chosenPkmn);
                    chosenAttack = pokeChooseAttackConfirm(user, chosenPkmn);
                } else {
                    chosenAttack = pokeAiAttack(chosenPkmn, user);
                }
                Attack aIAttack = pokeAiAttack(aIPokemon, aI);
                pokeBattleRound(user, aI, chosenPkmn, chosenAttack, aIPokemon, aIAttack, autoBattle, database);
            } else if (userChoice == 2) {
                chosenPkmn = pokeUserChoose(user, database);
                System.out.println();
            } else if (userChoice == 0) {
                autoBattle = true;
                pokeFightMenu(user, aI, chosenPkmn, aIPokemon, autoBattle, database);
            }
        } while (chosenPkmn.isAlive() && aIPokemon.isAlive());
        if (pokeCheckLose(user)) {
            return false;
        } else return pokeCheckLose(aI);
    }

    private boolean pokeAliveCheck(Pokemon pokemon) {
        return pokemon.getCurHealthPoints() > 0;
    }

    private void pokeBattleRound(Trainer user, Trainer aI, Pokemon chosenPkmn, Attack chosenAttack, Pokemon aIPokemon, Attack aIAttack, boolean autoBattle, Database database) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        boolean userFirst = true;

        if (aIPokemon.getSpeedStat() > chosenPkmn.getSpeedStat()) {
            userFirst = false;
        }
        if (userFirst) {

            pokePkmnAttack(chosenPkmn, chosenAttack, aIPokemon);
            aIPokemon.setAlive(pokeAliveCheck(aIPokemon));
            if (aIPokemon.isAlive()) {
                pokePkmnAttack(aIPokemon, aIAttack, chosenPkmn);
                chosenPkmn.setAlive(pokeAliveCheck(chosenPkmn));
                if (!chosenPkmn.isAlive()) {
                    System.out.println(chosenPkmn.getName() + " wurde besiegt!");
                    if (autoBattle) {
                        pokeFightMenu(user, aI, pokeAiChoose(user, database,autoBattle), aIPokemon, autoBattle, database);
                    } else {
                        pokeFightMenu(user, aI, pokeUserChoose(user, database), aIPokemon, autoBattle, database);
                    }
                }
            } else {
                System.out.println(aIPokemon.getName() + " wurde besiegt!");
                pokeFightMenu(user, aI, chosenPkmn, pokeAiChoose(aI, database,autoBattle), autoBattle, database);
            }
        } else {
            pokePkmnAttack(aIPokemon, aIAttack, chosenPkmn);
            chosenPkmn.setAlive(pokeAliveCheck(chosenPkmn));
            if (chosenPkmn.isAlive()) {
                pokePkmnAttack(chosenPkmn, chosenAttack, aIPokemon);
                aIPokemon.setAlive(pokeAliveCheck(aIPokemon));
                if (!aIPokemon.isAlive()) {
                    System.out.println(aIPokemon.getName() + " wurde besiegt!");
                    aIPokemon = pokeAiChoose(aI, database,autoBattle);
                    pokeFightMenu(user, aI, chosenPkmn, aIPokemon, autoBattle, database);
                }
            } else {
                System.out.println(chosenPkmn.getName() + " wurde besiegt!");
                if (autoBattle) {
                    pokeFightMenu(user, aI, pokeAiChoose(user, database,autoBattle), aIPokemon, autoBattle, database);
                } else {
                    pokeFightMenu(user, aI, pokeUserChoose(user, database), aIPokemon, autoBattle, database);
                }
            }
        }
    }

    private boolean pokeCheckLose(Trainer trainer) {
        int deathCount = 0;
        for (int i = 0; i < trainer.getPokeTeam().size(); i++) {
            if (!trainer.getPokeTeam().get(i).isAlive()) {
                deathCount++;
            }
        }
        return deathCount == trainer.getPokeTeam().size();
    }

    private void pokePkmnAttack(Pokemon attackerPkmn, Attack chosenAttack, Pokemon defenderPkmn) throws IOException {
        int dmg = (int) pokeAttackFormula(attackerPkmn, defenderPkmn, chosenAttack);
        defenderPkmn.setCurHealthPoints(defenderPkmn.getCurHealthPoints() - dmg);
        if (defenderPkmn.getCurHealthPoints() < 0) {
            defenderPkmn.setCurHealthPoints(0);
        }
        System.out.println(attackerPkmn.getName() + " setzt " + chosenAttack.getName() + " ein!\n" + dmg + " Schaden zugefügt!");
    }

    private double pokeAttackFormula(Pokemon attackPokemon, Pokemon defendPokemon, Attack attackerAttack) throws IOException {
        Random r = new Random();
        double random = r.nextDouble(0.5, 1.5);
        double stab = 1.0;
        if (attackerAttack.getType().equalsIgnoreCase(defendPokemon.getTypeOne()) || attackerAttack.getType().equalsIgnoreCase(defendPokemon.getTypeTwo())) {
            stab = 1.25;
        }
        int attackPower = attackerAttack.getPower();
        int attackStat = attackPokemon.getAttackStat();
        int defenseStat = defendPokemon.getDefenseStat();
        int level = attackPokemon.getLevel();
        int specialAttackStat = attackPokemon.getSpecialAttackStat();
        int specialDefenseStat = defendPokemon.getSpecialDefenseStat();
        Database database = new Database();
        database.readEffectivenessCSV();
        database.createEffectivenessHM();
        String attackType = attackerAttack.getType();
        String defendType1 = defendPokemon.getTypeOne();
        String defendType2 = defendPokemon.getTypeTwo();
        double eff1 = database.getEffectivenessDB().get(attackType).get(defendType1);
        double eff2 = 1;
        if (!defendType2.equals("")) {
            eff2 = database.getEffectivenessDB().get(attackType).get(defendType2);
        }
        if (attackerAttack.getKind().equalsIgnoreCase("physical")) {
            return ((attackPower) * ((double) attackStat / defenseStat) * (level / (double) 50) * random * stab * eff1 * eff2);
        } else
            return ((attackPower) * ((double) specialAttackStat / specialDefenseStat) * (level / (double) 50) * random * stab * eff1 * eff2);
    }

    private Pokemon pokeSearch(Database pokemonDB) {
        String userInput;
        Pokemon returnPokemon = new Pokemon();
        Pokemon tempPokemon = new Pokemon();
        Scanner sc = new Scanner(System.in);
        int searchInt = 0;
        Random r = new Random();
        int rndLevel = r.nextInt(10, 101);

        System.out.println("Wählen Sie den Namen ODER die Pokedex-Nummer des gewünschten Pokemons oder drücken Sie 0 zum Abbrechen");

        userInput = sc.nextLine();
        try {
            searchInt = Integer.parseInt(userInput);
            if (searchInt == 0) {
                return returnPokemon;
            }
        } catch (Exception e) {
            for (int i = 0; i < pokemonDB.getPokemonDB().length; i++) {
                if (userInput.equalsIgnoreCase(pokemonDB.getPokemonDB()[i][1])) {
                    String key = pokemonDB.getPokemonDB()[i][2];
                    for (int j = 0; j < pokemonDB.getPokemonDB()[i].length; j++) {
                        if (pokemonDB.getPokemonDatabase().get(key).get(j).getName().equalsIgnoreCase(userInput)) {
                            tempPokemon = pokemonDB.getPokemonDatabase().get(key).get(j);
                            return new Pokemon(rndLevel, tempPokemon.getIndex(), tempPokemon.getName(), tempPokemon.getTypeOne(), tempPokemon.getTypeTwo(), tempPokemon.getStruggle(), tempPokemon.getHealthPoints(), tempPokemon.getAttackStat(), tempPokemon.getDefenseStat(), tempPokemon.getSpecialAttackStat(), tempPokemon.getSpecialDefenseStat(), tempPokemon.getSpeedStat());
                        }
                    }
                }
            }
            if (returnPokemon.getTypeOne() == null) {
                System.out.println("Pokemon nicht auffindbar!");
                return pokeSearch(pokemonDB);
            }
        }
        try {
            for (int i = 0; i < pokemonDB.getPokemonDB().length; i++) {
                if (userInput.equalsIgnoreCase(pokemonDB.getPokemonDB()[i][0])) {
                    String key = pokemonDB.getPokemonDB()[i][2];
                    for (int j = 0; j < pokemonDB.getPokemonDB()[i].length; j++) {
                        if (pokemonDB.getPokemonDatabase().get(key).get(j).getIndex() == searchInt) {
                            tempPokemon = pokemonDB.getPokemonDatabase().get(key).get(j);
                            return new Pokemon(rndLevel, tempPokemon.getIndex(), tempPokemon.getName(), tempPokemon.getTypeOne(), tempPokemon.getTypeTwo(), tempPokemon.getStruggle(), tempPokemon.getHealthPoints(), tempPokemon.getAttackStat(), tempPokemon.getDefenseStat(), tempPokemon.getSpecialAttackStat(), tempPokemon.getSpecialDefenseStat(), tempPokemon.getSpeedStat());
                        }
                    }
                }
            }
        } catch (
                IndexOutOfBoundsException iE) {
            System.out.println("Bitte eine gültige Pokedex-Nummer angeben 1-151");
            return pokeSearch(pokemonDB);
        }
        return returnPokemon;
    }

    private int pokeUserInputConfirm(String[][] menu) {
        int returnInt;
        Scanner sc = new Scanner(System.in);
        System.out.println("Wähle bitte einen Menupunkt");
        String userInput = sc.nextLine();
        try {
            returnInt = Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            System.out.println("Bitte eine gültige Zahl eingeben");
            return pokeUserInputConfirm(menu);
        }
        if (returnInt >= 0 && returnInt <= menu.length - 1) {
            return returnInt;
        } else {
            return pokeUserInputConfirm(menu);
        }
    }

    private int pokeChoosePkmnConfirm(int length) {
        int returnInt;
        Scanner sc = new Scanner(System.in);
        System.out.println("Wähle ein Pokemon aus (NR)");
        String userInput = sc.nextLine();
        try {
            returnInt = Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            System.out.println("Bitte eine gültige Zahl eingeben");
            return pokeChoosePkmnConfirm(length);
        }
        if (returnInt >= 0 && returnInt <= length) {
            return returnInt;
        } else {
            return pokeChoosePkmnConfirm(length);
        }
    }

    private Attack pokeAiAttack(Pokemon pokemon, Trainer trainer) {
        int random;
        Random r = new Random();
        random = r.nextInt(1, 5);
        Attack returnAttack = new Attack();
        if (pokemon.getAttackTypeNormal().getPowerPoints() == 0 && pokemon.getAttackOnePkmnType().getPowerPoints() == 0 && pokemon.getAttackTwoPkmnType().getPowerPoints() == 0 && pokemon.getAttackSecondType().getPowerPoints() == 0) {
            return pokemon.getStruggle();
        }
        if (random == 1) {
            returnAttack = pokePPCheck(pokemon.getAttackOnePkmnType(), trainer, pokemon);
        } else if (random == 2) {
            returnAttack = pokePPCheck(pokemon.getAttackTwoPkmnType(), trainer, pokemon);
        } else if (random == 3) {
            returnAttack = pokePPCheck(pokemon.getAttackSecondType(), trainer, pokemon);
        } else if (random == 4) {
            returnAttack = pokePPCheck(pokemon.getAttackTypeNormal(), trainer, pokemon);
        }
        return returnAttack;
    }

    private Attack pokeChooseAttackConfirm(Trainer user, Pokemon userPokemon) {
        int userChoice;
        Attack returnAttack;
        Scanner sc = new Scanner(System.in);
        System.out.println("Wähle eine Attacke aus (NR)");
        String userInput = sc.nextLine();
        try {
            userChoice = Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            System.out.println("Bitte eine gültige Zahl eingeben");
            return pokeChooseAttackConfirm(user, userPokemon);
        }
        if (userPokemon.getAttackTypeNormal().getPowerPoints() == 0 && userPokemon.getAttackOnePkmnType().getPowerPoints() == 0 && userPokemon.getAttackSecondType().getPowerPoints() == 0 && userPokemon.getAttackTwoPkmnType().getPowerPoints() == 0) {
            return userPokemon.getStruggle();
        }
        if (userChoice == 1) {
            returnAttack = pokePPCheck(userPokemon.getAttackOnePkmnType(), user, userPokemon);
            return returnAttack;
        } else if (userChoice == 2) {
            returnAttack = pokePPCheck(userPokemon.getAttackTwoPkmnType(), user, userPokemon);
            return returnAttack;
        } else if (userChoice == 3) {
            returnAttack = pokePPCheck(userPokemon.getAttackSecondType(), user, userPokemon);
            return returnAttack;
        } else if (userChoice == 4) {
            returnAttack = pokePPCheck(userPokemon.getAttackTypeNormal(), user, userPokemon);
            return returnAttack;
        } else {
            return pokeChooseAttackConfirm(user, userPokemon);
        }
    }

    private Attack pokePPCheck(Attack attack, Trainer trainer, Pokemon trainerPokemon) {
        if (attack.getPowerPoints() == 0) {
            if (trainer.equals(user)) {
                System.out.println("Keine PP mehr übrig, bitte wähle eine andere Attacke");
                return pokeChooseAttackConfirm(trainer, trainerPokemon);
            } else return pokeAiAttack(trainerPokemon, trainer);
        }
        attack.setPowerPoints(attack.getPowerPoints() - 1);
        return attack;
    }

    private void pokePrintMenu(String[][] menu) {
        for (int i = 0; i < menu.length; i++) {
            for (int j = 0; j < menu[i].length; j++) {
                System.out.printf("%-5s", menu[i][j]);
            }
            System.out.println();
        }
    }

    private Attack pokeFairy(Pokemon pokemon, Database database) {
        Random r = new Random();
        Attack tempAttack = database.getAttackDatabase().get("Dark").get(r.nextInt(database.getAttackDatabase().get("Dark").size()));
        return new Attack(tempAttack.getIndex(), tempAttack.getName(), tempAttack.getEffect(), tempAttack.getType(), tempAttack.getKind(), tempAttack.getPower(), tempAttack.getAccuracy(), tempAttack.getPowerPoints());
    }

    private void pokeFillUpTeam(Database database, int listHasItem, Trainer trainer) {
        Random r = new Random();
        for (int i = listHasItem; i < 6; i++) {

            String pokeTypeKeyRnd = database.getPokeTypes()[r.nextInt(database.getPokeTypes().length)];
            int rndPokemon = r.nextInt(database.getPokemonDatabase().get(pokeTypeKeyRnd).size());
            int level = r.nextInt(10, 101);
            Pokemon tempPokemon = database.getPokemonDatabase().get(pokeTypeKeyRnd).get(rndPokemon);

            Pokemon pokemon = new Pokemon(level, tempPokemon.getIndex(), tempPokemon.getName(), tempPokemon.getTypeOne(), tempPokemon.getTypeTwo(), tempPokemon.getStruggle(), tempPokemon.getHealthPoints(), tempPokemon.getAttackStat(), tempPokemon.getDefenseStat(), tempPokemon.getSpecialAttackStat(), tempPokemon.getSpecialDefenseStat(), tempPokemon.getSpeedStat());
            pokemon.setAlive(true);
            pokeLeveling(pokemon);
            pokeAddAttacks(pokemon, database);
            trainer.pokeAddPokemon(i, pokemon);
        }
    }

    private int pokeStatMultiplier(int level, int statToMultiply, double multiplier) {
        double returnDouble;
        returnDouble = statToMultiply + Math.floor(statToMultiply * (level * (multiplier)));
        return (int) returnDouble;
    }

    private void pokeLeveling(Pokemon pokemon) {
        Random r = new Random();
        int hp = pokeStatMultiplier(pokemon.getLevel(), pokemon.getHealthPoints(), 0.04);
        pokemon.setHealthPoints(hp);
        pokemon.setCurHealthPoints(hp);
        int attackStat = pokeStatMultiplier(pokemon.getLevel(), pokemon.getAttackStat(), 0.02);
        pokemon.setAttackStat(attackStat);
        int defenseStat = pokeStatMultiplier(pokemon.getLevel(), pokemon.getDefenseStat(), 0.02);
        pokemon.setDefenseStat(defenseStat);
        int specialAttackStat = pokeStatMultiplier(pokemon.getLevel(), pokemon.getSpecialAttackStat(), 0.02);
        pokemon.setSpecialAttackStat(specialAttackStat);
        int specialDefenseStat = pokeStatMultiplier(pokemon.getLevel(), pokemon.getSpecialDefenseStat(), 0.02);
        pokemon.setSpecialDefenseStat(specialDefenseStat);
        int speedStat = pokeStatMultiplier(pokemon.getLevel(), pokemon.getSpeedStat(), 0.01);
        pokemon.setSpeedStat(speedStat);

    }

    private void pokeAddAttacks(Pokemon pokemon, Database database) {
        Random r = new Random();
        Attack tempAttack = database.getAttackDatabase().get("Normal").get(r.nextInt(database.getAttackDatabase().get("Normal").size()));
        Attack normalAttack = new Attack(tempAttack.getIndex(), tempAttack.getName(), tempAttack.getEffect(), tempAttack.getType(), tempAttack.getKind(), tempAttack.getPower(), tempAttack.getAccuracy(), tempAttack.getPowerPoints());
        pokemon.setAttackTypeNormal(normalAttack);
        if (pokemon.getTypeTwo().equalsIgnoreCase("Fairy")) {
            pokemon.setAttackSecondType(pokeFairy(pokemon, database));
        } else if (!pokemon.getTypeTwo().equalsIgnoreCase("")) {
            tempAttack = database.getAttackDatabase().get(pokemon.getTypeTwo()).get(r.nextInt(database.getAttackDatabase().get(pokemon.getTypeTwo()).size()));
            Attack attackTypeTwo = new Attack(tempAttack.getIndex(), tempAttack.getName(), tempAttack.getEffect(), tempAttack.getType(), tempAttack.getKind(), tempAttack.getPower(), tempAttack.getAccuracy(), tempAttack.getPowerPoints());
            pokemon.setAttackSecondType(attackTypeTwo);
        } else if (!pokemon.getTypeOne().equalsIgnoreCase("Fairy")) {
            tempAttack = database.getAttackDatabase().get(pokemon.getTypeOne()).get(r.nextInt(database.getAttackDatabase().get(pokemon.getTypeOne()).size()));
            Attack attackTypeOne = new Attack(tempAttack.getIndex(), tempAttack.getName(), tempAttack.getEffect(), tempAttack.getType(), tempAttack.getKind(), tempAttack.getPower(), tempAttack.getAccuracy(), tempAttack.getPowerPoints());
            pokemon.setAttackSecondType(attackTypeOne);
        } else {
            pokemon.setAttackSecondType(pokeFairy(pokemon, database));
        }
        if (!pokemon.getTypeOne().equalsIgnoreCase("Fairy")) {
            tempAttack = database.getAttackDatabase().get(pokemon.getTypeOne()).get(r.nextInt(database.getAttackDatabase().get(pokemon.getTypeOne()).size()));
            Attack attackOneTypePkmn = new Attack(tempAttack.getIndex(), tempAttack.getName(), tempAttack.getEffect(), tempAttack.getType(), tempAttack.getKind(), tempAttack.getPower(), tempAttack.getAccuracy(), tempAttack.getPowerPoints());
            pokemon.setAttackOnePkmnType(attackOneTypePkmn);
        } else {
            pokemon.setAttackOnePkmnType(pokeFairy(pokemon, database));
        }
        if (!pokemon.getTypeOne().equalsIgnoreCase("Fairy")) {
            tempAttack = database.getAttackDatabase().get(pokemon.getTypeOne()).get(r.nextInt(database.getAttackDatabase().get(pokemon.getTypeOne()).size()));
            Attack attackTwoTypePkmn = new Attack(tempAttack.getIndex(), tempAttack.getName(), tempAttack.getEffect(), tempAttack.getType(), tempAttack.getKind(), tempAttack.getPower(), tempAttack.getAccuracy(), tempAttack.getPowerPoints());
            pokemon.setAttackTwoPkmnType(attackTwoTypePkmn);
        } else {
            pokemon.setAttackTwoPkmnType(pokeFairy(pokemon, database));
        }
    }
}
