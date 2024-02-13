package audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class AudioPlayer {
    public static int MENU_1 = 0;
    public static int LEVEL_1 = 1;
    public static int LEVEL_2 = 2;
    public static int DIE = 0;
    public static int JUMP = 1;
    public static int GAMEOVER = 2;
    public static int LVL_COMPLETED = 3;

    private Clip[] songs, effects;
    private int currentSongId;
    private float volume = 1f;
    private boolean songMute, effectMute;
    private Random rand = new Random();
    public AudioPlayer(){
        loadSongs();
        loadEffects();
        playSong(MENU_1);
    }

    // Această metodă încarcă fișierele audio pentru coloana sonoră a jocului. Numele fișierelor sunt specificate într-un
    // vector de șiruri de caractere, iar metoda utilizează getClip() pentru a obține un obiect Clip pentru fiecare fișier audio.
    private void loadSongs(){
        String[] names = {"menu", "level1", "level2"};
        songs = new Clip[names.length];
        for(int i = 0; i < songs.length; i++)
            songs[i] = getClip(names[i]);
    }

    // Această metodă încarcă fișierele audio pentru efectele sonore ale jocului. Numele fișierelor sunt specificate într-un vector
    // de șiruri de caractere, iar metoda utilizează getClip() pentru a obține un obiect Clip pentru fiecare fișier audio.
    private void loadEffects(){
        String[] effectNames = {"die", "jump", "gameover", "lvlcompleted", "attack1", "attack2", "attack3"};
        effects = new Clip[effectNames.length];
        for(int i = 0; i < effects.length; i++)
            effects[i] = getClip(effectNames[i]);
        updateEffectsVolume();
    }

    // Această metodă primește numele unui fișier audio și încearcă să obțină un obiect Clip corespunzător.
    // Metoda utilizează getClass().getResource() pentru a obține URL-ul fișierului audio și AudioSystem.getAudioInputStream() pentru a obține
    // un flux de intrare audio. Apoi, este creat și returnat un obiect Clip folosind AudioSystem.getClip() și Clip.open().
    private Clip getClip(String name){
        URL url = getClass().getResource("/res/audio/" + name + ".wav");
        AudioInputStream audio;

        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip();
            c.open(audio);
            return c;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Această metodă primește un indice pentru un efect sonor și îl redă de la început.
    // Efectul sonor corespunzător este obținut din vectorul effects și redat utilizând metodele setMicrosecondPosition(0) și start() ale obiectului Clip.
    public void playEffect(int effect){
        effects[effect].setMicrosecondPosition(0);
        effects[effect].start();
    }

    // Această metodă primește un indice pentru o piesă de muzică și o redă în buclă continuă. Piesa de muzică corespunzătoare este obținută din
    // vectorul songs și redată utilizând metodele setMicrosecondPosition(0) și loop(Clip.LOOP_CONTINUOUSLY) ale obiectului Clip.
    // Înainte de a reda noua piesă, se oprește redarea piesei curente, dacă este activă.
    public void playSong(int song){
        if(songs[currentSongId].isActive())
            songs[currentSongId].stop();
        currentSongId = song;
        updateSongVolume();
        songs[currentSongId].setMicrosecondPosition(0);
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
    }

    // Această metodă redă un sunet de atac. Este selectat un efect sonor de atac aleatoriu utilizând
    // obiectul Random și apoi efectul sonor este redat utilizând metoda playEffect().
    public void playAttackSound(){
        int start = 4;
        start = start + rand.nextInt(3);
        playEffect(start);
    }

    // Această metodă primește un nivel de volum și actualizează volumul pieselor de muzică și efectelor sonore. Este utilizat
    // un obiect FloatControl pentru a obține controlul volumului pentru fiecare Clip și a seta valoarea volumului în funcție de nivelul specificat.
    public void setVolume(float volume){
        this.volume = volume;
        updateSongVolume();
        updateEffectsVolume();
    }

    // Această metodă oprește redarea piesei de muzică curente, dacă este activă.
    public void stopSong(){
        if(songs[currentSongId].isActive())
            songs[currentSongId].stop();
    }

    // Această metodă primește un indice de nivel și selectează piesa de muzică corespunzătoare pentru nivelul respectiv.
    // Dacă indicele nivelului este par, se redă piesa LEVEL_1, altfel se redă piesa LEVEL_2.
    public void setLevelSong(int lvlIndex){
        if(lvlIndex % 2 == 0)
            playSong(LEVEL_1);
        else
            playSong(LEVEL_2);
    }

    // Această metodă oprește redarea piesei de muzică curente și redă efectul sonor LVL_COMPLETED pentru a marca finalizarea nivelului.
    public void lvlCompleted(){
        stopSong();
        playEffect(LVL_COMPLETED);
    }

    // Această metodă actualizează volumul piesei de muzică curente utilizând controlul MASTER_GAIN al obiectului Clip. Se calculează valoarea volumului în
    // funcție de nivelul global de volum și se setează valoarea volumului utilizând setValue().
    private void updateSongVolume(){
        FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }

    // Această metodă actualizează volumul efectelor sonore utilizând controlul MASTER_GAIN pentru fiecare efect sonor din vectorul effects.
    // Se calculează valoarea volumului în funcție de nivelul global de volum și se setează valoarea volumului utilizând setValue().
    private void updateEffectsVolume(){
        for(Clip c : effects) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }

    // Această metodă inversează starea de mutare a pieselor de muzică și actualizează controlul MUTE pentru fiecare piesă de muzică în consecință.
    // Este utilizat un obiect BooleanControl pentru a obține controlul de mutare și a seta valoarea de mutare.
    public void toggleSongMute(){
        this.songMute = !songMute;
        for(Clip c : songs){
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
    }

    // Această metodă inversează starea de mutare a efectelor sonore și actualizează controlul MUTE pentru fiecare efect sonor
    // în consecință. Este utilizat un obiect BooleanControl pentru a obține controlul de mutare și a seta valoarea de mutare.
    // Dacă mutarea efectelor sonore este dezactivată, se redă efectul sonor JUMP.
    public void toggleEffectMute(){
        this.effectMute = !effectMute;
        for(Clip c : effects){
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(effectMute);
        }
        if(!effectMute)
            playEffect(JUMP);
    }

}
