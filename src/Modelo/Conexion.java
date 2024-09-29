package Modelo;

import java.sql.*;

public class Conexion {

    String url = "jdbc:mysql://bkxv1cfk2iueraku6byw-mysql.services.clever-cloud.com:3306/bkxv1cfk2iueraku6byw";
    String usuario = "u5e4nmaddbkt8jwj";
    String contraseña = "qux15guPHiJQs4AUwQOc";
    String driver = "com.mysql.cj.jdbc.Driver";

    public Conexion() {
        try {
            Class.forName(driver);
            //miConexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/Obtener_Indice_Academico", "root", "root1234");
            miConexion = DriverManager.getConnection(url, usuario, contraseña);
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void insertarDatos(String nombre, String apellido, String cedula,String usuario,String contrasenia, String carrera) {
        switch (carrera) {
            case "Ing Informatica":
                CarreraDefinitiva = "Ingenieria Informatica";
                nombreCarrera = "informatica";
                break;
            case "Ing Electronica":
                CarreraDefinitiva = "Ingenieria Electronica";
                nombreCarrera = "electronica";
                break;
            case "Ing Civil":
                CarreraDefinitiva = "Ingenieria Civil";
                nombreCarrera = "civil";
                break;
        }
        try {
            PreparedStatement c = miConexion.prepareStatement(consultaInsertarDatos);
            c.setString(1, nombre);
            c.setString(2, apellido);
            c.setString(3, cedula);
            c.setString(4, carrera);
            c.setString(5, contrasenia);
            c.setString(6, usuario);
            c.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void CrearTabla(String nombre, String cedula) {

        String tableName = "notas_" + nombre.replaceAll("\\s+", "_") + "_" + cedula.replaceAll("\\s+", "_");

        try {
            PreparedStatement consulta2 = miConexion.prepareStatement("create table `" + tableName + "` (\n"
                    + "cedula int,\n"
                    + "codigo varchar(50),\n"
                    + "notas int,\n"
                    + "semestre int,\n"
                    + "UC int,\n"
                    + "carrera varchar(100)\n"
                    + ");");
            consulta2.executeUpdate();

            //rellenar tabla con los datos vacios
            consulta = miConexion.createStatement();

            ResultSet r = consulta.executeQuery("SELECT * FROM bkxv1cfk2iueraku6byw.materias_" + nombreCarrera + ";");
            while (r.next()) {
                AgregarNotas(tableName, cedula, r.getString("codigo"), "0", r.getString("semestre"), r.getString("UC"), CarreraDefinitiva);
            }
            consulta.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet traerConsulta(String cons) {
        try {
            consulta = miConexion.createStatement();

            return consulta.executeQuery(cons);

        } catch (SQLException | NullPointerException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public void AgregarNotas(String nombreTabla, String cedula, String codigo, String nota, String semestre, String UC, String carrera) {
        try {
            String consultaInsertarNotas = "insert into bkxv1cfk2iueraku6byw." + nombreTabla + " (cedula,codigo,notas,semestre,UC,carrera) values (?,?,?,?,?,?);";
            PreparedStatement consulta = miConexion.prepareStatement(consultaInsertarNotas);
            consulta.setString(1, cedula);
            consulta.setString(2, codigo);
            consulta.setString(3, nota);
            consulta.setString(4, semestre);
            consulta.setString(5, UC);
            consulta.setString(6, carrera);

            consulta.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void ActualizarDato(String nombreTabla, String nota, String codigo) {
        try {
            consulta = miConexion.createStatement();
            consulta.executeUpdate("update bkxv1cfk2iueraku6byw." + nombreTabla + "\n"
                    + "set bkxv1cfk2iueraku6byw." + nombreTabla + ".notas = " + nota + "\n"
                    + "where bkxv1cfk2iueraku6byw." + nombreTabla + ".codigo = '" + codigo + "';");
            
            consulta.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private Connection miConexion = null;
    private Statement consulta = null;
    private String CarreraDefinitiva = "";
    private String nombreCarrera = "";
    private final String consultaInsertarDatos = "insert into bkxv1cfk2iueraku6byw.user(nombre,apellido,cedula,carrera,contrasenia,usuario) values (?,?,?,?,?,?);";
}
