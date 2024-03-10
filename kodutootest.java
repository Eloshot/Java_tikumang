import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;


public class kodutootest {
    static int remainingMatches;

    public static void main(String[] args) {

        JFrame frame = new JFrame("My Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent newgame) {
                panel.removeAll();
                frame.revalidate();
                frame.repaint();
                startGame(frame, panel);
            }
        });
        panel.add(startButton);

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        panel.add(quitButton);

        frame.getContentPane().add(panel);
        frame.setPreferredSize(new Dimension(1200, 800));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void startGame(JFrame frame, JPanel panel) {
        boolean onint = true;
        String input = JOptionPane.showInputDialog(frame, "tikkude arv:"); // kusime sisendit
        String tikkudearv = (JOptionPane.showInputDialog(frame, "maksimaalne tikkude votmis arv:"));
        while (onint) {
            try {// vaatame et sisendid oleksid integerid
                int input1 = Integer.parseInt(input);
                int input2 = Integer.parseInt(tikkudearv);
                if (input2 < input1) {
                    onint = false;
                    remainingMatches = input1;
                } else { //vaatame et tiku arv oleks suurem kui eemaldatav tikkude arv
                    input = JOptionPane.showInputDialog(frame, "sisestage tikkude arv uuesti:");
                    tikkudearv = (JOptionPane.showInputDialog(frame, "sisestage maksimaalne tikkude votmis arv uuesti:"));
                }
            } catch (NumberFormatException l) {
                input = JOptionPane.showInputDialog(frame, "sisestage tikkude arv uuesti:");
                tikkudearv = (JOptionPane.showInputDialog(frame, "sisestage maksimaalne tikkude votmis arv uuesti:"));
            }
        }
        if (input != null && !input.isEmpty()) {
            int number = Integer.parseInt(input);
            drawLines(panel, number);
            playGame(frame, panel, Integer.parseInt(tikkudearv)); //alustame mangu
        }
    }

    private static void drawLines(JPanel panel, int number) {
        panel.setLayout(new GridLayout(number, 1));
        int arv = (int) Math.ceil(number / 10); // vaatame mitu 10 mahub arvu sisse
        int proov = number;
        boolean vist = true;
        while (vist) {
            for (int i = 0; i < arv; i++) { // kuna iga rida votab maksimaalselt 1o tikku siis kui arv on suurem kui 10 votame 10 ara ja joonistame rea 10 tikuga
                panel.add(new LinePanel(10));
                proov = proov - 10;
            }

            panel.add(new LinePanel(proov));
            vist = false;

        }
        panel.revalidate();
        panel.repaint();
    }

    static class LinePanel extends JPanel {
        private int numLines;

        public LinePanel(int numLines) {
            this.numLines = numLines;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int width = getWidth();
            int gap = 1;
            int startX = width / 10; //  seal kus algab joonistus ja jargmine on kus loppeb maxilt
            int endX = width - startX;
            int y = getHeight() / 2;
            int lineHeight = getHeight() / 2;
            if( numLines != 0) {
                gap = (endX - startX) / numLines; // vahe nende
            }



            // joonistame jooni nii palju kui on vaja
            for (int i = 0; i < numLines; i++) {
                int x = startX + i * gap;
                g.drawLine(x, y - lineHeight / 2, x, y + lineHeight / 2);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(100, 20); // ma pole kindel mida se teeb aga ta eksisteerib
        }

    }

    private static void playGame(JFrame frame, JPanel panel, int maxMatches) {
        Random random = new Random(); //suvaloine arv arvutile
        while (true) {
            // Player's turn
            int playerChoice = getPlayerChoice(frame, maxMatches);
            remainingMatches -= playerChoice;
            panel.removeAll();
            frame.revalidate();
            frame.repaint();
            drawLines(panel, remainingMatches);
            if (remainingMatches == 0) {
                JOptionPane.showMessageDialog(frame, "Palju õnne sa võitsid!");
                break;
            }

            // Computer's turn
            int computerChoice = random.nextInt(Math.min(maxMatches, remainingMatches));
            if (computerChoice == 0){
                computerChoice = 1;
            }
            remainingMatches -= computerChoice;
            panel.removeAll();
            frame.revalidate();
            frame.repaint();
            drawLines(panel, remainingMatches);
            if (remainingMatches == 0) {
                JOptionPane.showMessageDialog(frame, "Sa kaotasid");
                break;
            }
        }
        int choice = JOptionPane.showConfirmDialog(frame, "Kas sa tahad uuesti mängida? ", "Play Again", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            startGame(frame, panel);
        } else {
            System.exit(0);
        }
    }

    private static int getPlayerChoice(JFrame frame, int maxMatches) {
        int choice = -1;
        while (choice <= 0 || choice > maxMatches || choice > remainingMatches) {
            String input = JOptionPane.showInputDialog(frame, "Sisestage eemaldatav tikkude arv (1-" + Math.min(maxMatches, remainingMatches) + " alles on "+ remainingMatches +"):");
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                // Input is not a number
            }
        }
        return choice;
    }


}
