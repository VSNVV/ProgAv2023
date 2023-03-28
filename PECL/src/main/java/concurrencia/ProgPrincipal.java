/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrencia;

import javax.swing.*;
import java.util.Formatter;

/**
 *
 * @author vsnv
 */
public class ProgPrincipal extends javax.swing.JFrame {
    //Atributos de la clase ProgPrincipal
    private int numTotalHormigas = 0, numHormigasObreras = 0, numHormigasSoldado = 0, numHormigasCria = 0;
    private Log log = new Log(true);
    private Colonia colonia = new Colonia(getLog(), getjTextFieldHormigasBuscandoComida(), getjTextFieldHormigasContraInvasor(),
            getjTextFieldHormigasAlmacenComida(), getjTextFieldHormiasLlevandoComida(), getjTextFieldHormigasHaciendoInstruccion(),
            getjTextFieldUnidadesComidaAlmacen(), getjTextFieldUnidadesComidaZonaComer(), getjTextFieldHormigasDescansando(),
            getjTextFieldHormigasZonaComer(), getjTextFieldHormigasRefugio());

    /**
     * Creates new form ProgPrincipal
     */
    public ProgPrincipal() {
        initComponents();
        //Creamos los hilos
        Formatter fmt = new Formatter();
        while(getNumTotalHormigas() < 10000){
            for (int i = 0; i < 3; i++){
                fmt.format("%05d", getNumHormigasObreras());
                String identificadorObrera = "HO" + fmt;
                Hormiga hormigaObrera = new Hormiga(getColonia(), getLog(), identificadorObrera, getNumHormigasObreras());
                hormigaObrera.setName(identificadorObrera);
                hormigaObrera.start();
                setNumHormigasObreras(getNumHormigasObreras() + 1);
            }
            //Por cada 3 obreras, se hace una soldada y una cria
            //Creamos una hormiga soldado
            fmt.format("%05d", getNumHormigasSoldado());
            String identificadorSoldado = "HS" + fmt;
            Hormiga hormigaSoldado = new Hormiga(getColonia(), getLog(), identificadorSoldado, getNumHormigasSoldado());
            hormigaSoldado.setName(identificadorSoldado);
            hormigaSoldado.start();
            setNumHormigasSoldado(getNumHormigasSoldado() + 1);
            getColonia().setNumHormigasSoldado(getNumHormigasSoldado());

            //Creamos una hormiga cria
            fmt.format("%05d", getNumHormigasCria());
            String identificadorCria = "HC" + fmt;
            Hormiga hormigaCria = new Hormiga(getColonia(), getLog(), identificadorCria, getNumHormigasCria());
            hormigaCria.setName(identificadorCria);
            hormigaCria.start();
            setNumHormigasCria(getNumHormigasCria() + 1);
            //Esperamos entre 0.8 y 3.5 segundos para hacer la siguiente ronda
            try{
                Thread.sleep((int) (Math.random() * 800 + 3500));
            }catch(InterruptedException ie){}
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelPrincipal = new javax.swing.JPanel();
        JPanelHormigasBuscandoComida = new javax.swing.JPanel();
        jLabelHormigasBuscandoComida = new javax.swing.JLabel();
        jTextFieldHormigasBuscandoComida = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabelHormigasContraInvasor = new javax.swing.JLabel();
        jTextFieldHormigasContraInvasor = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabelInteriorColonia = new javax.swing.JLabel();
        jLabelHormigasAlmacenComida = new javax.swing.JLabel();
        jTextFieldHormigasAlmacenComida = new javax.swing.JTextField();
        jLabelHormigasLlevandoComida = new javax.swing.JLabel();
        jTextFieldHormiasLlevandoComida = new javax.swing.JTextField();
        jLabelHormigasHaciendoInstruccion = new javax.swing.JLabel();
        jTextFieldHormigasHaciendoInstruccion = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldHormigasDescansando = new javax.swing.JTextField();
        jLabelUnidadesComidaAlmacen = new javax.swing.JLabel();
        jTextFieldUnidadesComidaAlmacen = new javax.swing.JTextField();
        jLabelUnidadesComidaZonaComer = new javax.swing.JLabel();
        jTextFieldUnidadesComidaZonaComer = new javax.swing.JTextField();
        jLabelZonaComer = new javax.swing.JLabel();
        jTextFieldHormigasZonaComer = new javax.swing.JTextField();
        jLabelRefugio = new javax.swing.JLabel();
        jTextFieldHormigasRefugio = new javax.swing.JTextField();
        jButtonPausarReanudar = new javax.swing.JButton();
        jButtonGenerarAmenazaInsectoInvasor = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelPrincipal.setBackground(new java.awt.Color(255, 255, 255));
        jPanelPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JPanelHormigasBuscandoComida.setBackground(new java.awt.Color(153, 153, 153));
        JPanelHormigasBuscandoComida.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelHormigasBuscandoComida.setBackground(new java.awt.Color(255, 255, 255));
        jLabelHormigasBuscandoComida.setText("Hormigas buscando comida");
        JPanelHormigasBuscandoComida.add(jLabelHormigasBuscandoComida, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));
        JPanelHormigasBuscandoComida.add(jTextFieldHormigasBuscandoComida, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 1430, 60));

        jPanelPrincipal.add(JPanelHormigasBuscandoComida, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1630, 90));

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelHormigasContraInvasor.setBackground(new java.awt.Color(255, 255, 255));
        jLabelHormigasContraInvasor.setText("Hormigas repeliendo un insecto invasor ");
        jPanel1.add(jLabelHormigasContraInvasor, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 10, -1, -1));
        jPanel1.add(jTextFieldHormigasContraInvasor, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 1610, 120));

        jPanelPrincipal.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 1630, 170));

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelInteriorColonia.setBackground(new java.awt.Color(0, 0, 0));
        jLabelInteriorColonia.setForeground(new java.awt.Color(255, 0, 51));
        jLabelInteriorColonia.setText("Interior de la colonia");
        jPanel2.add(jLabelInteriorColonia, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 10, -1, -1));

        jLabelHormigasAlmacenComida.setBackground(new java.awt.Color(255, 255, 255));
        jLabelHormigasAlmacenComida.setText("Hormigas en el Almacen de Comida");
        jPanel2.add(jLabelHormigasAlmacenComida, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));
        jPanel2.add(jTextFieldHormigasAlmacenComida, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 30, 1380, 30));

        jLabelHormigasLlevandoComida.setBackground(new java.awt.Color(0, 0, 0));
        jLabelHormigasLlevandoComida.setText("Hormigas llevando comida a la Zona para Comer");
        jPanel2.add(jLabelHormigasLlevandoComida, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));
        jPanel2.add(jTextFieldHormiasLlevandoComida, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 70, 1320, 30));

        jLabelHormigasHaciendoInstruccion.setBackground(new java.awt.Color(255, 255, 255));
        jLabelHormigasHaciendoInstruccion.setText("Hormigas haciendo Instruccion");
        jPanel2.add(jLabelHormigasHaciendoInstruccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));
        jPanel2.add(jTextFieldHormigasHaciendoInstruccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 110, 1420, 30));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Hormigas descansando");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, -1, -1));
        jPanel2.add(jTextFieldHormigasDescansando, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 150, 720, 30));

        jLabelUnidadesComidaAlmacen.setBackground(new java.awt.Color(255, 255, 255));
        jLabelUnidadesComidaAlmacen.setText("Unidades de Comida (Almacen)");
        jPanel2.add(jLabelUnidadesComidaAlmacen, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 160, -1, -1));
        jPanel2.add(jTextFieldUnidadesComidaAlmacen, new org.netbeans.lib.awtextra.AbsoluteConstraints(1380, 150, 60, 30));

        jLabelUnidadesComidaZonaComer.setBackground(new java.awt.Color(255, 255, 255));
        jLabelUnidadesComidaZonaComer.setText("Unidades de Comida (Zona para Comer)");
        jPanel2.add(jLabelUnidadesComidaZonaComer, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 200, -1, -1));
        jPanel2.add(jTextFieldUnidadesComidaZonaComer, new org.netbeans.lib.awtextra.AbsoluteConstraints(1380, 190, 60, 30));

        jLabelZonaComer.setBackground(new java.awt.Color(255, 255, 255));
        jLabelZonaComer.setText("Zona para Comer");
        jPanel2.add(jLabelZonaComer, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, -1));
        jPanel2.add(jTextFieldHormigasZonaComer, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 1010, 90));

        jLabelRefugio.setBackground(new java.awt.Color(255, 255, 255));
        jLabelRefugio.setText("Refugio");
        jPanel2.add(jLabelRefugio, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, -1, -1));
        jPanel2.add(jTextFieldHormigasRefugio, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 300, 1010, 110));

        jButtonPausarReanudar.setText("Pausar");
        jPanel2.add(jButtonPausarReanudar, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 440, 150, 40));

        jButtonGenerarAmenazaInsectoInvasor.setText("Generar amenaza de insecto invasor");
        jPanel2.add(jButtonGenerarAmenazaInsectoInvasor, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 440, 340, 40));

        jPanelPrincipal.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 1630, 500));

        getContentPane().add(jPanelPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1650, 800));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ProgPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProgPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProgPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProgPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ProgPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPanelHormigasBuscandoComida;
    private javax.swing.JButton jButtonGenerarAmenazaInsectoInvasor;
    private javax.swing.JButton jButtonPausarReanudar;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelHormigasAlmacenComida;
    private javax.swing.JLabel jLabelHormigasBuscandoComida;
    private javax.swing.JLabel jLabelHormigasContraInvasor;
    private javax.swing.JLabel jLabelHormigasHaciendoInstruccion;
    private javax.swing.JLabel jLabelHormigasLlevandoComida;
    private javax.swing.JLabel jLabelInteriorColonia;
    private javax.swing.JLabel jLabelRefugio;
    private javax.swing.JLabel jLabelUnidadesComidaAlmacen;
    private javax.swing.JLabel jLabelUnidadesComidaZonaComer;
    private javax.swing.JLabel jLabelZonaComer;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelPrincipal;
    private javax.swing.JTextField jTextFieldHormiasLlevandoComida;
    private javax.swing.JTextField jTextFieldHormigasAlmacenComida;
    private javax.swing.JTextField jTextFieldHormigasBuscandoComida;
    private javax.swing.JTextField jTextFieldHormigasContraInvasor;
    private javax.swing.JTextField jTextFieldHormigasDescansando;
    private javax.swing.JTextField jTextFieldHormigasHaciendoInstruccion;
    private javax.swing.JTextField jTextFieldHormigasRefugio;
    private javax.swing.JTextField jTextFieldHormigasZonaComer;
    private javax.swing.JTextField jTextFieldUnidadesComidaAlmacen;
    private javax.swing.JTextField jTextFieldUnidadesComidaZonaComer;
    // End of variables declaration//GEN-END:variables

    //Métodos get y set

    public JTextField getjTextFieldHormiasLlevandoComida() {
        return jTextFieldHormiasLlevandoComida;
    }

    public JTextField getjTextFieldHormigasAlmacenComida() {
        return jTextFieldHormigasAlmacenComida;
    }

    public JTextField getjTextFieldHormigasBuscandoComida() {
        return jTextFieldHormigasBuscandoComida;
    }

    public JTextField getjTextFieldHormigasContraInvasor() {
        return jTextFieldHormigasContraInvasor;
    }

    public JTextField getjTextFieldHormigasDescansando() {
        return jTextFieldHormigasDescansando;
    }

    public JTextField getjTextFieldHormigasHaciendoInstruccion() {
        return jTextFieldHormigasHaciendoInstruccion;
    }

    public JTextField getjTextFieldHormigasRefugio() {
        return jTextFieldHormigasRefugio;
    }

    public JTextField getjTextFieldUnidadesComidaAlmacen() {
        return jTextFieldUnidadesComidaAlmacen;
    }

    public JTextField getjTextFieldUnidadesComidaZonaComer() {
        return jTextFieldUnidadesComidaZonaComer;
    }

    public JTextField getjTextFieldHormigasZonaComer() {
        return jTextFieldHormigasZonaComer;
    }

    public Log getLog(){
        return this.log;
    }

    public Colonia getColonia(){
        return this.colonia;
    }

    public int getNumTotalHormigas(){
        return this.numTotalHormigas;
    }

    public void setNumTotalHormigas(int numTotalHormigas){
        this.numTotalHormigas = numTotalHormigas;
    }

    public int getNumHormigasObreras(){
        return this.numHormigasObreras;
    }

    public void setNumHormigasObreras(int numHormigasObreras){
        this.numHormigasObreras = numHormigasObreras;
    }

    public int getNumHormigasSoldado(){
        return this.numHormigasSoldado;
    }

    public void setNumHormigasSoldado(int numHormigasSoldado){
        this.numHormigasSoldado = numHormigasSoldado;
    }

    public int getNumHormigasCria(){
        return this.numHormigasCria;
    }

    public void setNumHormigasCria(int numHormigasCria){
        this.numHormigasCria = numHormigasCria;
    }
}
