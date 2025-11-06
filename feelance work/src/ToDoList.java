import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ToDoList extends JFrame {

    private final JPanel taskPanel;
    private final JTextField taskInput;
    private final JComboBox<Integer> dayBox, yearBox;
    private final JComboBox<String> monthBox;
    private final JButton addButton, deleteButton;
    private final ArrayList<JCheckBox> tasks = new ArrayList<>();

    public ToDoList() {
        super("To-Do List");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(480, 540);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        taskInput = new JTextField(35);
        taskInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        Integer[] days = new Integer[31];
        for (int i = 0; i < 31; i++) days[i] = i + 1;
        dayBox = new JComboBox<>(days);

        String[] months = {
                "Янв", "Фев", "Мар", "Апр", "Май", "Июн",
                "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"
        };
        monthBox = new JComboBox<>(months);

        int currentYear = LocalDate.now().getYear();
        Integer[] years = {currentYear, currentYear + 1, currentYear + 2, currentYear + 3};
        yearBox = new JComboBox<>(years);

        LocalDate today = LocalDate.now();
        dayBox.setSelectedItem(today.getDayOfMonth());
        monthBox.setSelectedIndex(today.getMonthValue() - 1);
        yearBox.setSelectedItem(today.getYear());

        addButton = new JButton("Добавить задачу");

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Задача:"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(taskInput, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Дата выполнения:"), gbc);

        gbc.gridx = 1;
        inputPanel.add(dayBox, gbc);

        gbc.gridx = 2;
        inputPanel.add(monthBox, gbc);

        gbc.gridx = 3;
        inputPanel.add(yearBox, gbc);

        gbc.gridx = 4; gbc.gridy = 0; gbc.gridheight = 2;
        inputPanel.add(addButton, gbc);
        gbc.gridheight = 1;

        taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(taskPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        deleteButton = new JButton("Удалить выполненные");

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(deleteButton, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addTask());
        deleteButton.addActionListener(e -> deleteCompletedTasks());
        taskInput.addActionListener(e -> addTask());

        addButton.setBackground(new Color(90, 180, 90));
        addButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(200, 80, 80));
        deleteButton.setForeground(Color.WHITE);

        setVisible(true);
    }

    private void addTask() {
        String text = taskInput.getText().trim();

        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите задачу!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int day = (Integer) dayBox.getSelectedItem();
        int month = monthBox.getSelectedIndex() + 1;
        int year = (Integer) yearBox.getSelectedItem();

        LocalDate date;
        try {
            date = LocalDate.of(year, month, day);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Некорректная дата!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String taskText = text + " (до " + date + ")";
        JCheckBox checkBox = new JCheckBox(taskText);
        checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        tasks.add(checkBox);
        taskPanel.add(checkBox);

        taskPanel.revalidate();
        taskPanel.repaint();
        taskInput.setText("");
    }

    private void deleteCompletedTasks() {
        tasks.removeIf(task -> {
            if (task.isSelected()) {
                taskPanel.remove(task);
                return true;
            }
            return false;
        });

        taskPanel.revalidate();
        taskPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoList::new);
    }
}
