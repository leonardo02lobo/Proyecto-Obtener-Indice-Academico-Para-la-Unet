package Vista;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Frame_AgregarUsuario extends JFrame {

    public Frame_AgregarUsuario(Inicio i) {
        Panel_AgregarUsuario panel = new Panel_AgregarUsuario(this,i);
        setSize(300, 380);
        setLocationRelativeTo(null);
        setResizable(false);
        add(panel);
        setTitle("Agregar Usuario");
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                panel.EliminarDatos();
                i.setVisible(true);
            }

        });
    }
}

class Panel_AgregarUsuario extends JPanel {

    public Panel_AgregarUsuario(JFrame c,Inicio i) {
        setLayout(null);
        //--------------CREAR LABELS----------------------------
        CrearLabel(new JLabel(), 20, 50, "Nombre: ");
        CrearLabel(new JLabel(), 20, 90, "Apellido: ");
        CrearLabel(new JLabel(), 20, 130, "Cedula: ");
        CrearLabel(new JLabel(), 20, 170, "Usuario: ");
        CrearLabel(new JLabel(), 20, 210, "Contraseña: ");
        CrearLabel(new JLabel(), 20, 250, "Carrera: ");
        //-------------CREAR CAJAS------------------------------
        CrearCajas(cajaNombre, 130, 50);
        CrearCajas(cajaApellido, 130, 90);
        CrearCajas(cajaCedula, 130, 130);
        CrearCajas(cajaUsuario, 130, 170);
        CrearCajas(cajaContrasenia, 130, 210);
        //-------------BOTONES----------------------------------
        JButton enviarDatos = new JButton();

        enviarDatos.setText("Guardar Datos");
        enviarDatos.setBounds(80, 300, 120, 30);
        enviarDatos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame.con.insertarDatos(cajaNombre.getText(), cajaApellido.getText(), cajaCedula.getText(),cajaUsuario.getText(),cajaContrasenia.getText(), (String) cajaCarrera.getSelectedItem());
                Frame.con.CrearTabla(cajaNombre.getText(), cajaCedula.getText());
                c.dispose();
                i.setVisible(true);
            }
        });
        add(enviarDatos);

        cajaCarrera.setBounds(130, 250, 150, 25);
        add(cajaCarrera);
    }

    private void CrearLabel(JLabel label, int x, int y, String text) {
        label.setText(text);
        label.setFont(new Font("calibri", Font.BOLD, 20));
        label.setBounds(x, y, 150, 30);
        add(label);
    }

    private void CrearCajas(JTextField caja, int x, int y) {
        caja.setBounds(x, y, 150, 25);
        caja.setFont(new Font("calibri", Font.BOLD, 20));
        add(caja);
    }

    public void EliminarDatos() {
        cajaNombre.setText("");
        cajaApellido.setText("");
        cajaUsuario.setText("");
        cajaContrasenia.setText("");
        cajaCarrera.setSelectedIndex(0);
    }

    private JTextField cajaNombre = new JTextField();
    private JTextField cajaApellido = new JTextField();
    private JTextField cajaCedula = new JTextField();
    private JTextField cajaUsuario = new JTextField();
    private JTextField cajaContrasenia = new JTextField();
    private JComboBox cajaCarrera = new JComboBox(new Object[]{"Seleccionar", "Ing Informatica", "Ing Electronica", "Ing Civil"});
}
