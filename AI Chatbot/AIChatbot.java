import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class AIChatbot extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private Map<String, String> faqDatabase;

    public AIChatbot() {
        initializeDatabase();

        setTitle("AI Chatbot Assistant");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        sendButton = new JButton("Send");
        sendButton.setBackground(new Color(70, 130, 180));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        chatArea.append("Bot: Hello! I am your AI Assistant. How can I help you today?\n");

        ActionListener sendAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processUserMessage();
            }
        };

        sendButton.addActionListener(sendAction);
        inputField.addActionListener(sendAction);
    }

    private void initializeDatabase() {
        faqDatabase = new HashMap<>();
        // Predefined QA database (Rule-based NLP)
        faqDatabase.put("hi", "Hello there! How can I assist you today?");
        faqDatabase.put("hello", "Hi! Hope you are doing well. What can I do for you?");
        faqDatabase.put("java", "Java is a popular, class-based, object-oriented programming language.");
        faqDatabase.put("nlp", "NLP stands for Natural Language Processing. It helps computers understand human language.");
        faqDatabase.put("help", "You can ask me about 'Java', 'NLP', or just say 'Bye'!");
        faqDatabase.put("bye", "Goodbye! Have a wonderful day ahead.");
    }

    private void processUserMessage() {
        String userText = inputField.getText().trim();
        if (userText.isEmpty()) return;

        chatArea.append("You: " + userText + "\n");
        inputField.setText("");
       String cleanText = userText.toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", "");
        String[] tokens = cleanText.split("\\s+");

        String botResponse = "Bot: I am sorry, I don't understand that question yet. Try asking 'help'!";
        for (String token : tokens) {
            if (faqDatabase.containsKey(token)) {
                botResponse = "Bot: " + faqDatabase.get(token);
                break;
            }
        }

        chatArea.append(botResponse + "\n\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AIChatbot().setVisible(true);
            }
        });
    }
}