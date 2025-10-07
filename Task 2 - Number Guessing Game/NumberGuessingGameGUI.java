import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class NumberGuessingGameGUI extends JFrame implements ActionListener {
    private int randomNumber;
    private int attemptsLeft;
    private int score;
    private final int MAX_ATTEMPTS = 5;
    private final int MAX_NUMBER = 100;

    private JTextField guessField;
    private JButton guessButton, restartButton;
    private JLabel messageLabel, attemptsLabel, scoreLabel;

    public NumberGuessingGameGUI() {
        setTitle("🎯 Number Guessing Game");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1, 10, 10));
        setLocationRelativeTo(null);

        messageLabel = new JLabel("Guess a number between 1 and " + MAX_NUMBER, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));

        guessField = new JTextField();
        guessField.setHorizontalAlignment(JTextField.CENTER);

        guessButton = new JButton("Submit Guess");
        guessButton.addActionListener(this);

        restartButton = new JButton("Restart Game");
        restartButton.addActionListener(e -> restartGame());

        attemptsLabel = new JLabel("Attempts left: " + MAX_ATTEMPTS, SwingConstants.CENTER);
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);

        add(messageLabel);
        add(guessField);
        add(guessButton);
        add(attemptsLabel);
        add(scoreLabel);
        add(restartButton);

        startNewGame();
    }

    private void startNewGame() {
        Random rand = new Random();
        randomNumber = rand.nextInt(MAX_NUMBER) + 1;
        attemptsLeft = MAX_ATTEMPTS;
        messageLabel.setText("Guess a number between 1 and " + MAX_NUMBER);
        attemptsLabel.setText("Attempts left: " + attemptsLeft);
        guessField.setText("");
        guessButton.setEnabled(true);
    }

    private void restartGame() {
        score = 0;
        scoreLabel.setText("Score: 0");
        startNewGame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int userGuess = Integer.parseInt(guessField.getText());
            attemptsLeft--;

            if (userGuess == randomNumber) {
                int points = attemptsLeft * 10;
                score += points;
                messageLabel.setText("🎉 Correct! You earned " + points + " points!");
                scoreLabel.setText("Score: " + score);
                guessButton.setEnabled(false);
            } else if (userGuess < randomNumber) {
                messageLabel.setText("Too low! Try again.");
            } else {
                messageLabel.setText("Too high! Try again.");
            }

            if (attemptsLeft <= 0 && userGuess != randomNumber) {
                messageLabel.setText("❌ Out of attempts! Number was " + randomNumber);
                guessButton.setEnabled(false);
            }

            attemptsLabel.setText("Attempts left: " + attemptsLeft);
            guessField.setText("");
        } catch (NumberFormatException ex) {
            messageLabel.setText("⚠️ Please enter a valid number!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NumberGuessingGameGUI game = new NumberGuessingGameGUI();
            game.setVisible(true);
        });
    }
}
