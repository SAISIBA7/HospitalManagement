import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;

public class HospitalManagementSystem extends JFrame {
    private final List<Patient> patients = new ArrayList<>();
    private final List<Doctor> doctors = new ArrayList<>();
    private final List<Appointment> appointments = new ArrayList<>();

    private int nextPatientId = 1;
    private int nextDoctorId = 1;
    private int nextAppointmentId = 1;

    private final PatientTableModel patientTableModel = new PatientTableModel();
    private final DoctorTableModel doctorTableModel = new DoctorTableModel();
    private final AppointmentTableModel appointmentTableModel = new AppointmentTableModel();

    private JTable patientTable;
    private JTable doctorTable;
    private JTable appointmentTable;

    private JTextField patientNameField;
    private JTextField patientAgeField;
    private JTextField patientGenderField;
    private JTextField patientPhoneField;

    private JTextField doctorNameField;
    private JTextField doctorSpecializationField;
    private JTextField doctorScheduleField;

    private JComboBox<Patient> appointmentPatientCombo;
    private JComboBox<Doctor> appointmentDoctorCombo;
    private JTextField appointmentDateField;
    private JTextField appointmentTimeField;
    private JTextField appointmentReasonField;

    public HospitalManagementSystem() {
        setTitle("Hospital Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        seedData();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        tabbedPane.addTab("Patients", createPatientPanel());
        tabbedPane.addTab("Doctors", createDoctorPanel());
        tabbedPane.addTab("Appointments", createAppointmentPanel());

        add(tabbedPane, BorderLayout.CENTER);
        refreshDropdowns();
    }

    private void styleTable(JTable table) {
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(41, 128, 185, 40));
        table.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(245, 245, 245));
        header.setOpaque(true);
        header.setPreferredSize(new Dimension(100, 40));
    }

    private void styleButtons(JButton add, JButton update, JButton delete, JButton clear) {
        add.setForeground(new Color(34, 139, 34));   
        update.setForeground(new Color(0, 102, 204)); 
        delete.setForeground(new Color(220, 20, 60)); 
        clear.setForeground(Color.DARK_GRAY);        

        for (JButton btn : new JButton[]{add, update, delete, clear}) {
            btn.setFocusPainted(false);
            btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        }
    }

    private JPanel createPatientPanel() {
        patientTable = new JTable(patientTableModel);
        styleTable(patientTable);

        JPanel form = new JPanel(new GridLayout(0, 2, 12, 12));
        form.setBackground(new Color(245, 250, 255)); // Very light blue
        TitledBorder border = BorderFactory.createTitledBorder("Patient Details");
        border.setTitleColor(new Color(30, 100, 170)); // Medical blue
        form.setBorder(BorderFactory.createCompoundBorder(
                border,
                new EmptyBorder(10, 10, 10, 10)
        ));

        patientNameField = new JTextField();
        patientAgeField = new JTextField();
        patientGenderField = new JTextField();
        patientPhoneField = new JTextField();

        form.add(new JLabel("Name"));
        form.add(patientNameField);
        form.add(new JLabel("Age"));
        form.add(patientAgeField);
        form.add(new JLabel("Gender"));
        form.add(patientGenderField);
        form.add(new JLabel("Phone"));
        form.add(patientPhoneField);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");

        styleButtons(addButton, updateButton, deleteButton, clearButton);

        addButton.addActionListener(e -> addPatient());
        updateButton.addActionListener(e -> updatePatient());
        deleteButton.addActionListener(e -> deletePatient());
        clearButton.addActionListener(e -> clearPatientFields());

        JPanel buttonBar = new JPanel();
        buttonBar.add(addButton);
        buttonBar.add(updateButton);
        buttonBar.add(deleteButton);
        buttonBar.add(clearButton);

        patientTable.getSelectionModel().addListSelectionListener(e -> {
            int row = patientTable.getSelectedRow();
            if (row >= 0 && row < patients.size()) {
                Patient p = patients.get(row);
                patientNameField.setText(p.name);
                patientAgeField.setText(String.valueOf(p.age));
                patientGenderField.setText(p.gender);
                patientPhoneField.setText(p.phone);
            }
        });

        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.add(form);
        right.add(Box.createRigidArea(new Dimension(0, 10)));
        right.add(buttonBar);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.add(new JScrollPane(patientTable), BorderLayout.CENTER);
        panel.add(right, BorderLayout.EAST);

        return panel;
    }

    private JPanel createDoctorPanel() {
        doctorTable = new JTable(doctorTableModel);
        styleTable(doctorTable);

        JPanel form = new JPanel(new GridLayout(0, 2, 12, 12));
        form.setBackground(new Color(245, 250, 255)); // Very light blue
        TitledBorder border = BorderFactory.createTitledBorder("Doctor Details");
        border.setTitleColor(new Color(30, 100, 170)); // Medical blue
        form.setBorder(BorderFactory.createCompoundBorder(
                border,
                new EmptyBorder(10, 10, 10, 10)
        ));

        doctorNameField = new JTextField();
        doctorSpecializationField = new JTextField();
        doctorScheduleField = new JTextField();

        form.add(new JLabel("Name"));
        form.add(doctorNameField);
        form.add(new JLabel("Specialization"));
        form.add(doctorSpecializationField);
        form.add(new JLabel("Schedule"));
        form.add(doctorScheduleField);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");

        styleButtons(addButton, updateButton, deleteButton, clearButton);

        addButton.addActionListener(e -> addDoctor());
        updateButton.addActionListener(e -> updateDoctor());
        deleteButton.addActionListener(e -> deleteDoctor());
        clearButton.addActionListener(e -> clearDoctorFields());

        JPanel buttonBar = new JPanel();
        buttonBar.add(addButton);
        buttonBar.add(updateButton);
        buttonBar.add(deleteButton);
        buttonBar.add(clearButton);

        doctorTable.getSelectionModel().addListSelectionListener(e -> {
            int row = doctorTable.getSelectedRow();
            if (row >= 0 && row < doctors.size()) {
                Doctor d = doctors.get(row);
                doctorNameField.setText(d.name);
                doctorSpecializationField.setText(d.specialization);
                doctorScheduleField.setText(d.schedule);
            }
        });

        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.add(form);
        right.add(Box.createRigidArea(new Dimension(0, 10)));
        right.add(buttonBar);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.add(new JScrollPane(doctorTable), BorderLayout.CENTER);
        panel.add(right, BorderLayout.EAST);

        return panel;
    }

    private JPanel createAppointmentPanel() {
        appointmentTable = new JTable(appointmentTableModel);
        styleTable(appointmentTable);

        JPanel form = new JPanel(new GridLayout(0, 2, 12, 12));
        form.setBackground(new Color(245, 250, 255)); // Very light blue
        TitledBorder border = BorderFactory.createTitledBorder("Appointment Details");
        border.setTitleColor(new Color(30, 100, 170)); // Medical blue
        form.setBorder(BorderFactory.createCompoundBorder(
                border,
                new EmptyBorder(10, 10, 10, 10)
        ));

        appointmentPatientCombo = new JComboBox<>();
        appointmentDoctorCombo = new JComboBox<>();
        appointmentDateField = new JTextField();
        appointmentTimeField = new JTextField();
        appointmentReasonField = new JTextField();

        form.add(new JLabel("Patient"));
        form.add(appointmentPatientCombo);
        form.add(new JLabel("Doctor"));
        form.add(appointmentDoctorCombo);
        form.add(new JLabel("Date (YYYY-MM-DD)"));
        form.add(appointmentDateField);
        form.add(new JLabel("Time (HH:MM)"));
        form.add(appointmentTimeField);
        form.add(new JLabel("Reason"));
        form.add(appointmentReasonField);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");

        styleButtons(addButton, updateButton, deleteButton, clearButton);

        addButton.addActionListener(e -> addAppointment());
        updateButton.addActionListener(e -> updateAppointment());
        deleteButton.addActionListener(e -> deleteAppointment());
        clearButton.addActionListener(e -> clearAppointmentFields());

        JPanel buttonBar = new JPanel();
        buttonBar.add(addButton);
        buttonBar.add(updateButton);
        buttonBar.add(deleteButton);
        buttonBar.add(clearButton);

        appointmentTable.getSelectionModel().addListSelectionListener(e -> {
            int row = appointmentTable.getSelectedRow();
            if (row >= 0 && row < appointments.size()) {
                Appointment a = appointments.get(row);
                setComboSelectionByPatientId(a.patientId);
                setComboSelectionByDoctorId(a.doctorId);
                appointmentDateField.setText(a.date);
                appointmentTimeField.setText(a.time);
                appointmentReasonField.setText(a.reason);
            }
        });

        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.add(form);
        right.add(Box.createRigidArea(new Dimension(0, 10)));
        right.add(buttonBar);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.add(new JScrollPane(appointmentTable), BorderLayout.CENTER);
        panel.add(right, BorderLayout.EAST);

        return panel;
    }

    private void addPatient() {
        String name = patientNameField.getText().trim();
        String ageText = patientAgeField.getText().trim();
        String gender = patientGenderField.getText().trim();
        String phone = patientPhoneField.getText().trim();

        if (name.isEmpty() || ageText.isEmpty() || gender.isEmpty() || phone.isEmpty()) {
            showError("Please fill all patient fields.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
            if (age <= 0) {
                showError("Age must be a positive number.");
                return;
            }
        } catch (NumberFormatException ex) {
            showError("Age must be numeric.");
            return;
        }

        patients.add(new Patient(nextPatientId++, name, age, gender, phone));
        patientTableModel.fireTableDataChanged();
        refreshDropdowns();
        clearPatientFields();
    }

    private void updatePatient() {
        int row = patientTable.getSelectedRow();
        if (row < 0 || row >= patients.size()) {
            showError("Select a patient row to update.");
            return;
        }

        String name = patientNameField.getText().trim();
        String ageText = patientAgeField.getText().trim();
        String gender = patientGenderField.getText().trim();
        String phone = patientPhoneField.getText().trim();

        if (name.isEmpty() || ageText.isEmpty() || gender.isEmpty() || phone.isEmpty()) {
            showError("Please fill all patient fields.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
            if (age <= 0) {
                showError("Age must be a positive number.");
                return;
            }
        } catch (NumberFormatException ex) {
            showError("Age must be numeric.");
            return;
        }

        Patient p = patients.get(row);
        p.name = name;
        p.age = age;
        p.gender = gender;
        p.phone = phone;

        patientTableModel.fireTableDataChanged();
        refreshDropdowns();
    }

    private void deletePatient() {
        int row = patientTable.getSelectedRow();
        if (row < 0 || row >= patients.size()) {
            showError("Select a patient row to delete.");
            return;
        }

        int patientId = patients.get(row).id;
        patients.remove(row);
        appointments.removeIf(a -> a.patientId == patientId);

        patientTableModel.fireTableDataChanged();
        appointmentTableModel.fireTableDataChanged();
        refreshDropdowns();
        clearPatientFields();
    }

    private void addDoctor() {
        String name = doctorNameField.getText().trim();
        String specialization = doctorSpecializationField.getText().trim();
        String schedule = doctorScheduleField.getText().trim();

        if (name.isEmpty() || specialization.isEmpty() || schedule.isEmpty()) {
            showError("Please fill all doctor fields.");
            return;
        }

        doctors.add(new Doctor(nextDoctorId++, name, specialization, schedule));
        doctorTableModel.fireTableDataChanged();
        refreshDropdowns();
        clearDoctorFields();
    }

    private void updateDoctor() {
        int row = doctorTable.getSelectedRow();
        if (row < 0 || row >= doctors.size()) {
            showError("Select a doctor row to update.");
            return;
        }

        String name = doctorNameField.getText().trim();
        String specialization = doctorSpecializationField.getText().trim();
        String schedule = doctorScheduleField.getText().trim();

        if (name.isEmpty() || specialization.isEmpty() || schedule.isEmpty()) {
            showError("Please fill all doctor fields.");
            return;
        }

        Doctor d = doctors.get(row);
        d.name = name;
        d.specialization = specialization;
        d.schedule = schedule;

        doctorTableModel.fireTableDataChanged();
        refreshDropdowns();
    }

    private void deleteDoctor() {
        int row = doctorTable.getSelectedRow();
        if (row < 0 || row >= doctors.size()) {
            showError("Select a doctor row to delete.");
            return;
        }

        int doctorId = doctors.get(row).id;
        doctors.remove(row);
        appointments.removeIf(a -> a.doctorId == doctorId);

        doctorTableModel.fireTableDataChanged();
        appointmentTableModel.fireTableDataChanged();
        refreshDropdowns();
        clearDoctorFields();
    }

    private void addAppointment() {
        if (patients.isEmpty() || doctors.isEmpty()) {
            showError("Add at least one patient and one doctor first.");
            return;
        }

        Patient patient = (Patient) appointmentPatientCombo.getSelectedItem();
        Doctor doctor = (Doctor) appointmentDoctorCombo.getSelectedItem();
        String date = appointmentDateField.getText().trim();
        String time = appointmentTimeField.getText().trim();
        String reason = appointmentReasonField.getText().trim();

        if (patient == null || doctor == null || date.isEmpty() || time.isEmpty() || reason.isEmpty()) {
            showError("Please fill all appointment fields.");
            return;
        }

        appointments.add(new Appointment(nextAppointmentId++, patient.id, doctor.id, date, time, reason));
        appointmentTableModel.fireTableDataChanged();
        clearAppointmentFields();
    }

    private void updateAppointment() {
        int row = appointmentTable.getSelectedRow();
        if (row < 0 || row >= appointments.size()) {
            showError("Select an appointment row to update.");
            return;
        }

        Patient patient = (Patient) appointmentPatientCombo.getSelectedItem();
        Doctor doctor = (Doctor) appointmentDoctorCombo.getSelectedItem();
        String date = appointmentDateField.getText().trim();
        String time = appointmentTimeField.getText().trim();
        String reason = appointmentReasonField.getText().trim();

        if (patient == null || doctor == null || date.isEmpty() || time.isEmpty() || reason.isEmpty()) {
            showError("Please fill all appointment fields.");
            return;
        }

        Appointment a = appointments.get(row);
        a.patientId = patient.id;
        a.doctorId = doctor.id;
        a.date = date;
        a.time = time;
        a.reason = reason;

        appointmentTableModel.fireTableDataChanged();
    }

    private void deleteAppointment() {
        int row = appointmentTable.getSelectedRow();
        if (row < 0 || row >= appointments.size()) {
            showError("Select an appointment row to delete.");
            return;
        }

        appointments.remove(row);
        appointmentTableModel.fireTableDataChanged();
        clearAppointmentFields();
    }

    private void clearPatientFields() {
        patientNameField.setText("");
        patientAgeField.setText("");
        patientGenderField.setText("");
        patientPhoneField.setText("");
        patientTable.clearSelection();
    }

    private void clearDoctorFields() {
        doctorNameField.setText("");
        doctorSpecializationField.setText("");
        doctorScheduleField.setText("");
        doctorTable.clearSelection();
    }

    private void clearAppointmentFields() {
        appointmentDateField.setText("");
        appointmentTimeField.setText("");
        appointmentReasonField.setText("");
        appointmentTable.clearSelection();
    }

    private void refreshDropdowns() {
        if (appointmentPatientCombo != null) {
            appointmentPatientCombo.removeAllItems();
            for (Patient patient : patients) {
                appointmentPatientCombo.addItem(patient);
            }
        }

        if (appointmentDoctorCombo != null) {
            appointmentDoctorCombo.removeAllItems();
            for (Doctor doctor : doctors) {
                appointmentDoctorCombo.addItem(doctor);
            }
        }
    }

    private void setComboSelectionByPatientId(int patientId) {
        for (int i = 0; i < appointmentPatientCombo.getItemCount(); i++) {
            Patient patient = appointmentPatientCombo.getItemAt(i);
            if (patient.id == patientId) {
                appointmentPatientCombo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void setComboSelectionByDoctorId(int doctorId) {
        for (int i = 0; i < appointmentDoctorCombo.getItemCount(); i++) {
            Doctor doctor = appointmentDoctorCombo.getItemAt(i);
            if (doctor.id == doctorId) {
                appointmentDoctorCombo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation", JOptionPane.WARNING_MESSAGE);
    }

    private void seedData() {
        Patient p1 = new Patient(nextPatientId++, "Asha Singh", 31, "Female", "9876543210");
        Patient p2 = new Patient(nextPatientId++, "Rahul Verma", 44, "Male", "9123456780");

        Doctor d1 = new Doctor(nextDoctorId++, "Dr. Meera Nair", "Cardiology", "Mon-Fri 10:00-14:00");
        Doctor d2 = new Doctor(nextDoctorId++, "Dr. Arjun Rao", "Orthopedics", "Mon-Sat 16:00-20:00");

        patients.add(p1);
        patients.add(p2);
        doctors.add(d1);
        doctors.add(d2);

        appointments.add(new Appointment(nextAppointmentId++, p1.id, d1.id, "2026-04-11", "11:00", "Routine check-up"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                setupTheme();
            } catch (Exception ignored) {
            }
            new HospitalManagementSystem().setVisible(true);
        });
    }

    private static void setupTheme() {
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("TabbedPane.background", Color.WHITE);
        
        Font mainFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font boldFont = new Font("Segoe UI", Font.BOLD, 14);
        
        UIManager.put("Label.font", mainFont);
        UIManager.put("TextField.font", mainFont);
        UIManager.put("ComboBox.font", mainFont);
        UIManager.put("Table.font", mainFont);
        UIManager.put("TableHeader.font", boldFont);
        UIManager.put("TitledBorder.font", boldFont);
        UIManager.put("TabbedPane.font", boldFont);
    }

    private class PatientTableModel extends AbstractTableModel {
        private final String[] columns = {"ID", "Name", "Age", "Gender", "Phone"};

        @Override
        public int getRowCount() {
            return patients.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Patient p = patients.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return p.id;
                case 1:
                    return p.name;
                case 2:
                    return p.age;
                case 3:
                    return p.gender;
                case 4:
                    return p.phone;
                default:
                    return "";
            }
        }
    }

    private class DoctorTableModel extends AbstractTableModel {
        private final String[] columns = {"ID", "Name", "Specialization", "Schedule"};

        @Override
        public int getRowCount() {
            return doctors.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Doctor d = doctors.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return d.id;
                case 1:
                    return d.name;
                case 2:
                    return d.specialization;
                case 3:
                    return d.schedule;
                default:
                    return "";
            }
        }
    }

    private class AppointmentTableModel extends AbstractTableModel {
        private final String[] columns = {"ID", "Patient", "Doctor", "Date", "Time", "Reason"};

        @Override
        public int getRowCount() {
            return appointments.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Appointment a = appointments.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return a.id;
                case 1:
                    return findPatientName(a.patientId);
                case 2:
                    return findDoctorName(a.doctorId);
                case 3:
                    return a.date;
                case 4:
                    return a.time;
                case 5:
                    return a.reason;
                default:
                    return "";
            }
        }

        private String findPatientName(int patientId) {
            for (Patient patient : patients) {
                if (patient.id == patientId) {
                    return patient.name;
                }
            }
            return "Unknown";
        }

        private String findDoctorName(int doctorId) {
            for (Doctor doctor : doctors) {
                if (doctor.id == doctorId) {
                    return doctor.name;
                }
            }
            return "Unknown";
        }
    }

    private static class Patient {
        final int id;
        String name;
        int age;
        String gender;
        String phone;

        Patient(int id, String name, int age, String gender, String phone) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.phone = phone;
        }

        @Override
        public String toString() {
            return id + " - " + name;
        }
    }

    private static class Doctor {
        final int id;
        String name;
        String specialization;
        String schedule;

        Doctor(int id, String name, String specialization, String schedule) {
            this.id = id;
            this.name = name;
            this.specialization = specialization;
            this.schedule = schedule;
        }

        @Override
        public String toString() {
            return id + " - " + name + " (" + specialization + ")";
        }
    }

    private static class Appointment {
        final int id;
        int patientId;
        int doctorId;
        String date;
        String time;
        String reason;

        Appointment(int id, int patientId, int doctorId, String date, String time, String reason) {
            this.id = id;
            this.patientId = patientId;
            this.doctorId = doctorId;
            this.date = date;
            this.time = time;
            this.reason = reason;
        }
    }
}
