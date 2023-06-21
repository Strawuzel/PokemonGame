import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;


public class PokeMain {
    public static void main(String[] args) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
    Arena arena = new Arena();
    arena.pokeStart();
    }
}
