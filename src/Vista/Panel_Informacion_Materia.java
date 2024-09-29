package Vista;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.util.*;
import javax.swing.table.*;

public class Panel_Informacion_Materia extends JPanel {

    public Panel_Informacion_Materia() {
        setLayout(new FlowLayout());
    }

    public void mostrarSemestre(String carrera, String nombre, String cedula) {
        for (int i = 0; i < semestre.length; i++) {
            if (semestre[i] != null) {
                remove(semestre[i]);
            }
        }
        for (int i = 0; i < semestre.length; i++) {
            semestre[i] = new Semestre(i + 1, carrera, nombre, cedula);
            add(semestre[i]);
        }
    }

    public void ActualizarDatos(String nombre, String cedula, String carrera) {
        String[] nom = nombre.split(":");
        String[] ced = cedula.split(":");
        String[] ca = carrera.split(":");
        for (int i = 0; i < semestre.length; i++) {
            for (int j = 0; j < semestre[i].tabla.getRowCount(); j++) {
                int nota = 0;
                if (semestre[i].tabla.getValueAt(j, 3).equals("-")) {
                    nota = 0;
                } else {
                    nota = Integer.parseInt((String) semestre[i].tabla.getValueAt(j, 3));
                }
                notas.add(nota);
            }
        }
        ComprobarDatos(nom[1], ced[1], ca[1]);
    }

    private void ComprobarDatos(String nombre, String cedula, String ca) {
        String carrera = null;
        switch (ca) {
            case " Ing Informatica":
                carrera = "informatica";
                break;
            case " Ing Electronica":
                carrera = "electronica";
                break;
            case " Ing Civil":
                carrera = "civil";
                break;
        }
        int i = 0;
        String nombreTabla = "notas" + nombre.replaceAll("\\s+", "_") + "" + cedula.replaceAll("\\s+", "_");
        ResultSet r = Frame.con.traerConsulta("select codigo,notas,"
                + "bkxv1cfk2iueraku6byw." + nombreTabla + ".semestre,\n"
                + "bkxv1cfk2iueraku6byw.materias_" + carrera + ".materia"
                + " from bkxv1cfk2iueraku6byw." + nombreTabla + "\n"
                + "inner join bkxv1cfk2iueraku6byw.materias_" + carrera + " using(codigo);");
        try {
            while (r.next()) {
                if (Integer.parseInt(r.getString("notas").trim()) != notas.get(i)) {
                    Frame.con.ActualizarDato(nombreTabla, Integer.toString(notas.get(i)), r.getString("codigo"));
                }
                i++;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    private Semestre[] semestre = new Semestre[10];
    private ArrayList<Integer> notas = new ArrayList<>();
}

class Semestre extends JPanel {

    public Semestre(int semestre, String carrera, String nombre, String cedula) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(new FlowLayout());
        try {
            RellenarLista(semestre, carrera, nombre, cedula);
        } catch (SQLException ex) {
        }
    }

    private void RellenarLista(int semestre, String carrera, String nombre, String cedula) throws SQLException {
        ResultSet r = Frame.con.traerConsulta(""
                + "SELECT distinct bkxv1cfk2iueraku6byw.materias_" + carrera + ".codigo,"
                + "bkxv1cfk2iueraku6byw.materias_" + carrera + ".materia,"
                + "bkxv1cfk2iueraku6byw.materias_" + carrera + ".UC,"
                + "bkxv1cfk2iueraku6byw.notas_" + nombre + "_" + cedula + ".notas FROM bkxv1cfk2iueraku6byw.materias_" + carrera + ""
                + " INNER JOIN bkxv1cfk2iueraku6byw.notas_" + nombre + "_" + cedula + " USING(codigo) "
                + "WHERE bkxv1cfk2iueraku6byw.materias_" + carrera + ".semestre = " + semestre + ";");

        modelo.addColumn("Codigo");
        modelo.addColumn("Materia");
        modelo.addColumn("UC");
        modelo.addColumn("nota");
        while (r.next()) {
            String nota = "-";
            if (!r.getString("notas").equals("0")) {
                nota = r.getString("notas");
            }
            modelo.addRow(new Object[]{r.getString("codigo"), r.getString("materia"), r.getString("UC"), nota});
            i++;
        }
        ajustarColumna(tabla);
        tabla.setEnabled(false);
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (modelo.getColumnName(e.getClickCount()).equals("nota")) {
                    tabla.setEnabled(true);
                } else {
                    tabla.setEnabled(false);
                }
            }
        });
        add(new JScrollPane(tabla));
    }

    private void ajustarColumna(JTable table) {
        TableColumnModel columnModel = table.getColumnModel();
        int width = 200;
        for (int row = 0; row < table.getRowCount(); row++) {
            TableCellRenderer renderer = table.getCellRenderer(row, 1);
            Component comp = table.prepareRenderer(renderer, row, 1);
            width = Math.max(comp.getPreferredSize().width + 1, width);
            columnModel.getColumn(1).setPreferredWidth(width);
        }
    }

    private DefaultTableModel modelo = new DefaultTableModel();
    public JTable tabla = new JTable(modelo);
    public static int i = 0;
}
