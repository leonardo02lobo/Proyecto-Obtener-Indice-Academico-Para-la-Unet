package Vista;

import Modelo.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class Inicio extends JFrame{
    
    public Inicio(){
        setTitle("Iniciar sesion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);
        setResizable(false);
        setLocationRelativeTo(null);
        add(new PanelInicio(this));
    }
}

class PanelInicio extends JPanel{
    
    public PanelInicio(Inicio f){
        setLayout(new BorderLayout());
        panelFrontal.setLayout(new FlowLayout(FlowLayout.CENTER,0,50));
        add(panelFrontal,BorderLayout.NORTH);
        
        panelInferior.setLayout(null);
        add(panelInferior,BorderLayout.CENTER);
        
        panelBoton.setLayout(new FlowLayout());
        add(panelBoton,BorderLayout.SOUTH);
        
        CrearLabel(inicio, 0, 0, 0, 0, "Iniciar Sesion", panelFrontal, 40);
        CrearLabel(usuario, 20, 10, 200, 30, "Ingrese su usuario", panelInferior, 20);
        crearCajas(cajaUsuario, 20, 40, 400, 30);
        CrearLabel(contrasenia, 20, 100, 200, 30, "Ingrese su contraseña", panelInferior, 20);
        crearCajas(cajaContrasenia, 20, 140, 400, 30);
        
        boton.setText("Ingresar");
        boton.setFont(new Font("calibri", Font.BOLD, 20));
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ResultSet r = cone.traerConsulta("select * from bkxv1cfk2iueraku6byw.user;");
                    while(r.next()){
                        if(cajaUsuario.getText().equals(r.getString("usuario")) && cajaContrasenia.getText().equals(r.getString("contrasenia"))){
                            Frame v = new Frame(r);
                            v.setVisible(true);
                            f.dispose();
                        }
                    }
                } catch (SQLException | NullPointerException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        panelBoton.add(boton);
        
        nuevo.setText("Nuevo Usuario");
        nuevo.setFont(new Font("calibri", Font.BOLD, 20));
        nuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame_AgregarUsuario v = new Frame_AgregarUsuario(f);
                v.setVisible(true);
                f.dispose();
            }
        });
        panelBoton.add(nuevo);
    }
    
    private void CrearLabel(JLabel label, int x,int y, int ancho,int alto,String texto,JPanel panel,int tam){
        label.setText(texto);
        label.setBounds(x,y,ancho,alto);
        label.setFont(new Font("calibri", Font.BOLD, tam));
        panel.add(label);
    }
    
    private void crearCajas(JTextField caja,int x,int y,int ancho,int alto){
        caja.setBounds(x, y, ancho, alto);
        panelInferior.add(caja);
    }
    
    private JLabel inicio = new JLabel();
    private JLabel usuario = new JLabel();
    private JLabel contrasenia = new JLabel();
    private JTextField cajaUsuario = new JTextField();
    private JTextField cajaContrasenia = new JTextField();
    private JButton boton = new JButton();
    private JButton nuevo = new JButton();
    private JPanel panelFrontal = new JPanel();
    private JPanel panelInferior = new JPanel();
    private JPanel panelBoton = new JPanel();
    private Conexion cone = new Conexion();
}
