import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Transaction {
    private String type;
    private double amount;
    private Date date;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
        this.date = new Date();
    }

    public String toString() {
        return String.format("%s - %s: ₹%.2f", date.toString(), type, amount);
    }
}

class Account {
    private double balance;
    private java.util.List<Transaction> history;

    public Account() {
        balance = 0;
        history = new ArrayList<>();
    }

    public void deposit(double amt) {
        balance += amt;
        history.add(new Transaction("Deposit", amt));
    }

    public boolean withdraw(double amt) {
        if (amt > balance) return false;
        balance -= amt;
        history.add(new Transaction("Withdraw", amt));
        return true;
    }

    public boolean transfer(Account receiver, double amt) {
        if (amt > balance) return false;
        balance -= amt;
        receiver.deposit(amt);
        history.add(new Transaction("Transfer Sent", amt));
        receiver.history.add(new Transaction("Transfer Received", amt));
        return true;
    }

    public double getBalance() {
        return balance;
    }

    public java.util.List<Transaction> getHistory() {
        return history;
    }
}

class User {
    private String id;
    private String pin;
    private Account account;

    public User(String id, String pin) {
        this.id = id;
        this.pin = pin;
        this.account = new Account();
    }

    public boolean validate(String id, String pin) {
        return this.id.equals(id) && this.pin.equals(pin);
    }

    public String getId() { return id; }
    public Account getAccount() { return account; }
}

public class ATMInterfaceGUI extends JFrame implements ActionListener {

    // global
    private CardLayout card;
    private JPanel mainPanel;
    private JTextField loginIdField, signupIdField;
    private JPasswordField loginPinField, signupPinField;
    private User currentUser;
    private HashMap<String, User> users;

    public ATMInterfaceGUI() {
        setTitle("💳 ATM Interface System");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        users = new HashMap<>();
        users.put("user123", new User("user123", "1234")); // default user

        card = new CardLayout();
        mainPanel = new JPanel(card);

        mainPanel.add(createLoginPanel(), "login");
        mainPanel.add(createSignupPanel(), "signup");
        mainPanel.add(createMenuPanel(), "menu");

        add(mainPanel);
        setVisible(true);
    }

    // ---------- LOGIN PANEL ----------
    private JPanel createLoginPanel() {
        JPanel panel = createStyledPanel();

        JLabel title = createTitle("🏦 ATM LOGIN");
        JLabel idLbl = new JLabel("User ID:");
        JLabel pinLbl = new JLabel("PIN:");

        loginIdField = new JTextField();
        loginPinField = new JPasswordField();

        JButton loginBtn = createButton("Login");
        JButton signupBtn = createButton("Signup");

        loginBtn.addActionListener(this);
        signupBtn.addActionListener(e -> card.show(mainPanel, "signup"));

        panel.add(title);
        panel.add(idLbl);
        panel.add(loginIdField);
        panel.add(pinLbl);
        panel.add(loginPinField);
        panel.add(loginBtn);
        panel.add(signupBtn);

        return panel;
    }

    // ---------- SIGNUP PANEL ----------
    private JPanel createSignupPanel() {
        JPanel panel = createStyledPanel();

        JLabel title = createTitle("📝 CREATE ACCOUNT");
        JLabel idLbl = new JLabel("Choose User ID:");
        JLabel pinLbl = new JLabel("Choose PIN:");

        signupIdField = new JTextField();
        signupPinField = new JPasswordField();

        JButton createBtn = createButton("Create Account");
        JButton backBtn = createButton("Back to Login");

        createBtn.addActionListener(this);
        backBtn.addActionListener(e -> card.show(mainPanel, "login"));

        panel.add(title);
        panel.add(idLbl);
        panel.add(signupIdField);
        panel.add(pinLbl);
        panel.add(signupPinField);
        panel.add(createBtn);
        panel.add(backBtn);

        return panel;
    }

    // ---------- MENU PANEL ----------
    private JPanel createMenuPanel() {
        JPanel panel = createStyledPanel();

        JLabel title = createTitle("💰 ATM MAIN MENU");

        JButton depositBtn = createButton("Deposit");
        JButton withdrawBtn = createButton("Withdraw");
        JButton transferBtn = createButton("Transfer");
        JButton historyBtn = createButton("Transaction History");
        JButton balanceBtn = createButton("Check Balance");
        JButton logoutBtn = createButton("Logout");

        depositBtn.addActionListener(this);
        withdrawBtn.addActionListener(this);
        transferBtn.addActionListener(this);
        historyBtn.addActionListener(this);
        balanceBtn.addActionListener(this);
        logoutBtn.addActionListener(e -> card.show(mainPanel, "login"));

        panel.add(title);
        panel.add(depositBtn);
        panel.add(withdrawBtn);
        panel.add(transferBtn);
        panel.add(historyBtn);
        panel.add(balanceBtn);
        panel.add(logoutBtn);

        return panel;
    }

    // ---------- UI HELPERS ----------
    private JPanel createStyledPanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(0, 1, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        p.setBackground(new Color(240, 248, 255)); // light blue
        return p;
    }

    private JButton createButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(new Color(70, 130, 180));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 15));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return b;
    }

    private JLabel createTitle(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        return label;
    }

    // ---------- EVENT HANDLER ----------
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch (cmd) {

            // LOGIN
            case "Login":
                String id = loginIdField.getText().trim();
                String pin = new String(loginPinField.getPassword());
                if (users.containsKey(id) && users.get(id).validate(id, pin)) {
                    currentUser = users.get(id);
                    JOptionPane.showMessageDialog(this, "✅ Login Successful! Welcome, " + id);
                    card.show(mainPanel, "menu");
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Invalid ID or PIN");
                }
                break;

            // SIGNUP
            case "Create Account":
                String newId = signupIdField.getText().trim();
                String newPin = new String(signupPinField.getPassword());
                if (newId.isEmpty() || newPin.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "⚠️ Please fill all fields");
                } else if (users.containsKey(newId)) {
                    JOptionPane.showMessageDialog(this, "⚠️ User ID already exists!");
                } else {
                    users.put(newId, new User(newId, newPin));
                    JOptionPane.showMessageDialog(this, "🎉 Account Created Successfully!");
                    card.show(mainPanel, "login");
                }
                break;

            // DEPOSIT
            case "Deposit":
                String dep = JOptionPane.showInputDialog(this, "Enter deposit amount:");
                if (dep != null && !dep.isEmpty()) {
                    double amount = Double.parseDouble(dep);
                    currentUser.getAccount().deposit(amount);
                    JOptionPane.showMessageDialog(this, "✅ ₹" + amount + " deposited!");
                }
                break;

            // WITHDRAW
            case "Withdraw":
                String wd = JOptionPane.showInputDialog(this, "Enter withdrawal amount:");
                if (wd != null && !wd.isEmpty()) {
                    double amt = Double.parseDouble(wd);
                    if (currentUser.getAccount().withdraw(amt))
                        JOptionPane.showMessageDialog(this, "✅ ₹" + amt + " withdrawn!");
                    else
                        JOptionPane.showMessageDialog(this, "❌ Insufficient balance!");
                }
                break;

            // TRANSFER
            case "Transfer":
                String recv = JOptionPane.showInputDialog(this, "Enter receiver User ID:");
                if (recv != null && users.containsKey(recv)) {
                    String amtStr = JOptionPane.showInputDialog(this, "Enter transfer amount:");
                    if (amtStr != null && !amtStr.isEmpty()) {
                        double amt = Double.parseDouble(amtStr);
                        if (currentUser.getAccount().transfer(users.get(recv).getAccount(), amt))
                            JOptionPane.showMessageDialog(this, "✅ Transferred ₹" + amt + " to " + recv);
                        else
                            JOptionPane.showMessageDialog(this, "❌ Insufficient balance!");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "⚠️ Receiver not found!");
                }
                break;

            // HISTORY
            case "Transaction History":
                java.util.List<Transaction> hist = currentUser.getAccount().getHistory();
                if (hist.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No transactions yet.");
                } else {
                    JTextArea area = new JTextArea(10, 30);
                    for (Transaction t : hist) area.append(t.toString() + "\n");
                    area.setEditable(false);
                    JOptionPane.showMessageDialog(this, new JScrollPane(area), "📜 Transaction History", JOptionPane.INFORMATION_MESSAGE);
                }
                break;

            // BALANCE
            case "Check Balance":
                JOptionPane.showMessageDialog(this, "💰 Current Balance: ₹" + currentUser.getAccount().getBalance());
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ATMInterfaceGUI());
    }
}
