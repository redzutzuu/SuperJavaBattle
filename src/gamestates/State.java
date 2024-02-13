package gamestates;

import UI.MenuButton;
import audio.AudioPlayer;
import main.Game;

import java.awt.event.MouseEvent;

public class State {

    protected Game game;
    public State(Game game){
        this.game = game;
    }

    // Verifică dacă evenimentul de mouse se află în interiorul unui buton de meniu
    public boolean isIn(MouseEvent e, MenuButton mb){
        return mb.getBounds().contains(e.getX(), e.getY());
    }

    // Returnează obiectul de joc asociat stării curente
    public Game getGame(){
        return game;
    }

    // Setează starea jocului
    public void setGameState(Gamestate state){
        switch (state){
            case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
            case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLvlIndex());
        }
        Gamestate.state = state;
    }
}
