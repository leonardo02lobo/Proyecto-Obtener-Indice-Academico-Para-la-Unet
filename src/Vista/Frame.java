package Vista;

import Modelo.*;
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Frame extends JFrame implements Runnable {

    Panel_Informacion_Materia info = new Panel_Informacion_Materia();
    PanelIndice panel;
    JPanel panelDinamico = new JPanel();
    Toolkit too = Toolkit.getDefaultToolkit();
    Dimension di = too.getScreenSize();
    JPanel mostrarResultados = new JPanel();
    JLabel indice = new JLabel();
    public static Conexion con = new Conexion();
    int sumaNotas = 0;
    int sumaUC = 0;

    public Frame(ResultSet r) {

        panel = new PanelIndice(info, r);
        setTitle("Indice Academico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(di);
        setResizable(false);
        setLocationRelativeTo(null);
        Thread hilo = new Thread(this);
        hilo.start();

        panelDinamico.setLayout(new BorderLayout());
        add(panelDinamico);
        panelDinamico.add(panel, BorderLayout.NORTH);
        panelDinamico.add(new JScrollPane(info), BorderLayout.CENTER);

        mostrarResultados.setLayout(new FlowLayout());
        add(mostrarResultados, BorderLayout.SOUTH);

        JButton mostrar = new JButton();
        mostrar.setText("Calcular Indice");
        mostrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panel.listaUsuarios.getSelectedIndex() != 0) {
                    try {
                        ResultSet r = con.traerConsulta("SELECT sum(notas*UC),sum(UC) FROM bkxv1cfk2iueraku6byw.notas_" + panel.getNombre() + "_" + panel.getCedula() + " where notas != 0;");

                        while (r.next()) {
                            sumaNotas = Integer.parseInt(r.getString("sum(notas*UC)"));
                            sumaUC = Integer.parseInt(r.getString("sum(UC)"));
                        }
                        float total = (float) sumaNotas / sumaUC;
                        String totalFormateado = String.format("%.2f", total);
                        indice.setText("Tu indice academico actual es de:" + totalFormateado);
                    } catch (SQLException ex) {

                    } catch (NumberFormatException | NullPointerException ex) {
                        indice.setText("Tu indice academico actual es de: 0");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Tienes que tener un usuario disponible");
                }
            }
        });
        mostrarResultados.add(mostrar);

        indice.setText("Tu indice academico actual es de:");
        mostrarResultados.add(indice);
    }

    @Override
    public void run() {
    }
}

class PanelIndice extends JPanel {

    public PanelIndice() {

    }

    public PanelIndice(Panel_Informacion_Materia panel, ResultSet r) {
        //----------COLOCAR LAYOUT Y PANELES---------------------------
        setLayout(new BorderLayout());

        panelDatosUsuarios.setLayout(new FlowLayout(20, 40, 2));
        add(panelDatosUsuarios, BorderLayout.WEST);

        panelIngresoDatos.setLayout(new FlowLayout(20, 30, 2));
        add(panelIngresoDatos, BorderLayout.EAST);
        //-----------CREANDO LOS LABELS---------------------------------
        try {
        CrearLabel(nombre, "Nombre: "+r.getString("nombre"));
        CrearLabel(apellido, "Apellido: "+r.getString("apellido"));
        CrearLabel(cedula, "Cedula: "+r.getString("cedula"));
        CrearLabel(carrera, "Carrera: "+r.getString("carrera"));

        //-------------CREANDO BOTONES----------------------------------
        JButton actualizarDatos = new JButton("Actualizar Datos");
        actualizarDatos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    panel.ActualizarDatos(nombre.getText(), cedula.getText(), carrera.getText());
                } catch (NullPointerException ex) {
                    JOptionPane.showMessageDialog(null, "No hubieron modificaciones en las notas");
                }
            }
        });
        panelIngresoDatos.add(actualizarDatos);

        
            String carrera = "";
            switch (r.getString("carrera")) {
                case "Ing Informatica":
                    carrera = "informatica";
                    break;
                case "Ing Electronica":
                    carrera = "electronica";
                    break;
                case "Ing Civil":
                    carrera = "civil";
                    break;
            }
            panel.mostrarSemestre(carrera, r.getString("nombre"), r.getString("cedula"));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void CrearLabel(JLabel label, String text) {
        label.setText(text);
        label.setFont(new Font("calibri", Font.BOLD, 20));
        panelDatosUsuarios.add(label);
    }

    public String getNombre() {
        String nom[] = nombre.getText().split(":");
        return nom[1].trim();
    }

    public String getApellido() {
        String ap[] = apellido.getText().split(":");
        return ap[1].trim();
    }

    public String getCedula() {
        String ce[] = cedula.getText().split(":");
        return ce[1].trim();
    }

    public String getCarrera() {
        String ca[] = carrera.getText().split(":");
        return ca[1].trim();
    }

    private JLabel nombre = new JLabel();
    private JLabel apellido = new JLabel();
    private JLabel cedula = new JLabel();
    private JLabel carrera = new JLabel();
    public JComboBox listaUsuarios = new JComboBox();
    private JPanel panelDatosUsuarios = new JPanel();
    private JPanel panelIngresoDatos = new JPanel();
}
