package com.example.mediaplayer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;

public class MediaController {

    @FXML
    private Button btnPlay;

    @FXML
    private Button btnStop;

    @FXML
    private Button selectMedia;

    @FXML
    private Label toDuration;

    @FXML
    private MediaView mediaView;

    @FXML
    private Slider slider;

    private Media media;
    private MediaPlayer mediaPlayer;

    @FXML
    void btnPlay(ActionEvent event) {
        if (mediaPlayer != null) {
            MediaPlayer.Status status = mediaPlayer.getStatus();
            if (status == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                btnPlay.setText("Play ▶");
            } else {
                mediaPlayer.play();
                btnPlay.setText("Pause ❚❚");
            }
        }
    }

    @FXML
    void btnStop(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.seek(Duration.ZERO);
            btnPlay.setText("Play ▶");
        }
    }

    @FXML
    void selectMedia(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Media");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Media Files", "*.mp4", "*.mp3", "*.wav"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String url = selectedFile.toURI().toString();

            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }

            media = new Media(url);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.setAutoPlay(true);
            btnPlay.setText("Pause ❚❚");
            mediaView.fitWidthProperty().bind(mediaView.getScene().widthProperty());
            mediaView.fitHeightProperty().bind(mediaView.getScene().heightProperty().subtract(100)); // Adjusting for controls


            mediaPlayer.setOnReady(() -> {
                toDuration.setText(formatTime(mediaPlayer.getTotalDuration().toSeconds()));
                slider.setMax(mediaPlayer.getTotalDuration().toSeconds());
            });

            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                slider.setValue(newValue.toSeconds());
                toDuration.setText(formatTime(newValue.toSeconds()) + " / " + formatTime(mediaPlayer.getTotalDuration().toSeconds()));
            });

            slider.setOnMouseReleased(event1 -> {
                mediaPlayer.seek(Duration.seconds(slider.getValue()));
            });
        }
    }

    private String formatTime(double seconds) {
        int mins = (int) (seconds / 60);
        int secs = (int) (seconds % 60);
        return String.format("%02d:%02d", mins, secs);
    }
}
