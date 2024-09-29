package Controlador;

import Modelo.Conexion;
import Vista.Inicio;

public class App {

    public static void main(String[] args) {
        try {
            Inicio v = new Inicio();
            v.setVisible(true);
            new Conexion();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
