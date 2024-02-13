package UI;

import gamestates.Gamestate;
import main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;

import static utilz.Constants.UI.PauseButtons.SOUND_SIZE;
import static utilz.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static utilz.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

public class AudioOptions {
    private VolumeButton volumeB;
    private SoundButton musicButton, sfxButton;
    private Game game;

    // Constructorul clasei care primește o instanță a clasei Game și inițializează obiectele volumeB,
    // musicButton și sfxButton pentru gestionarea setărilor audio.
    public AudioOptions(Game game){
        this.game = game;
        createSoundButtons();
        createVolumeButton();

    }

    // Metodă privată care creează butonul de volum și setează coordonatele sale în funcție de scala jocului.
    private void createVolumeButton() {
        int vX = (int)(309 * Game.SCALE);
        int vY = (int)(278 * Game.SCALE);
        volumeB = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    // Metodă privată care creează butoanele de sunet pentru muzică și efecte sonore și setează coordonatele lor în funcție de scala jocului.
    private void createSoundButtons() {
        int soundX = (int)(450 * Game.SCALE);
        int musicY = (int)(140 * Game.SCALE);
        int sfxY = (int)(186 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    // Metodă care actualizează starea butoanelor de sunet și a butonului de volum.
    public void update(){
        musicButton.update();
        sfxButton.update();
        volumeB.update();
    }

    // Metodă care desenează butoanele de sunet și butonul de volum pe ecran, folosind obiectul Graphics furnizat.
    public void draw(Graphics g){
        musicButton.draw(g);
        sfxButton.draw(g);
        volumeB.draw(g);
    }

    // Metodă apelată atunci când se trage de mouse. Verifică dacă butonul de volum este apăsat și actualizează
    // volumul jocului în funcție de poziția cursorului.
    public void mouseDragged(MouseEvent e){
        if(volumeB.isMousePressed()){
            float valueBefore = volumeB.getFloatValue();
            volumeB.changeX(e.getX());
            float valueAfter = volumeB.getFloatValue();
            if(valueBefore != valueAfter)
                game.getAudioPlayer().setVolume(valueAfter);
        }
    }

    // Metodă apelată atunci când se apasă butonul de mouse. Verifică dacă butoanele de sunet sau butonul de volum au fost apăsate
    // și setează starea corespunzătoare pentru fiecare buton.
    public void mousePressed(MouseEvent e) {
        if (isIn(e, musicButton))
            musicButton.setMousePressed(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMousePressed(true);
        else if (isIn(e, volumeB))
            volumeB.setMousePressed(true);
    }

    // Metodă apelată atunci când se eliberează butonul de mouse. Verifică dacă butoanele de sunet sau butonul de volum au fost eliberate
    // și realizează acțiunile corespunzătoare, cum ar fi mutarea sau activarea/dezactivarea sunetului.
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, musicButton)) {
            if (musicButton.isMousePressed()) {
                musicButton.setMuted(!musicButton.isMuted());
                game.getAudioPlayer().toggleSongMute();
            }
        } else if (isIn(e, sfxButton)) {
            if (sfxButton.isMousePressed()) {
                sfxButton.setMuted(!sfxButton.isMuted());
                game.getAudioPlayer().toggleEffectMute();
            }
        }
        musicButton.resetBools();
        sfxButton.resetBools();
        volumeB.resetBools();
    }

    // Metodă apelată atunci când se mișcă cursorul mouse-ului. Verifică dacă cursorul se află deasupra butoanelor de sunet sau butonului
    // de volum și setează starea corespunzătoare pentru fiecare buton.
    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        volumeB.setMouseOver(false);

        if (isIn(e, musicButton))
            musicButton.setMouseOver(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMouseOver(true);
        else if (isIn(e, volumeB))
            volumeB.setMouseOver(true);
    }

    // Metodă privată care verifică dacă evenimentul MouseEvent se află în interiorul zonei de acțiune a unui buton specificat.
    private boolean isIn(MouseEvent e, PauseButton b){
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
