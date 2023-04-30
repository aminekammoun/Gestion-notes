import java.awt.Color;
import java.awt.Component;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Gestion_des_notes extends javax.swing.JFrame {
    private javax.swing.JButton jButton1;//Supprimer
    private javax.swing.JButton jButton2;//Ajouter
    private javax.swing.JButton jButton3;//Recherche
    private javax.swing.JButton jButton4;//Actualiser
    private javax.swing.JButton jButton5;//Modifier
    private javax.swing.JButton jButton6;//Moyenne
    private javax.swing.JLabel jLabel1;//Nom
    private javax.swing.JLabel jLabel2;//id
    private javax.swing.JLabel jLabel3;//Prenom
    private javax.swing.JLabel jLabel4;//Note
    private javax.swing.JLabel jLabel5;//Absence
    private javax.swing.JLabel jLabel6;//gestion des notes
    private javax.swing.JLabel jLabel7;//for Wallpaper
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tble;
    // private javax.swing.JComboBox txtbr;
    private javax.swing.JTextField txtid;
    private javax.swing.JTextField txtno;
    private javax.swing.JTextField txtnot;
    private javax.swing.JTextField txtpr;
    private javax.swing.JTextField txtre;
    private javax.swing.JTextField txtmoy;
    private javax.swing.JTextField txtabs;
    Connecter conn = new Connecter();
    Statement stm;
    ResultSet Rs;
    DefaultTableModel model = new DefaultTableModel();

    // pour afiicher tous les donner de BDD dans le tableau
    private void afficher() {
        try {
            model.setRowCount(0);
            stm = conn.obtenirconnexion().createStatement();
            ResultSet Rs = stm.executeQuery("Select * from student");
            
            // Créer un rendu de cellule personnalisé
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value,
                            isSelected, hasFocus, row, column);
                    
                    // Colorer en rouge uniquement les lignes qui contiennent un nombre d'absences supérieur à 3
                    int absence = Integer.parseInt(table.getValueAt(row, 4).toString());
                    if (absence > 2) {
                        c.setBackground(Color.RED);
                    } else {
                        c.setBackground(table.getBackground());
                    }
                    
                    return c;
                }
            };
            
            while (Rs.next()) {
                model.addRow(new Object[] { Rs.getString("id"), Rs.getString("nom"), Rs.getString("Prenom"),
                        Rs.getString("note"), Rs.getString("absence") });
            }
            // Appliquer le rendu personnalisé à chaque colonne de votre tableau
            tble.setModel(model);
            for (int i = 0; i < tble.getColumnCount(); i++) {
                tble.getColumnModel().getColumn(i).setCellRenderer(renderer);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        
    }

    

    //Action pour Supprimer
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
        try {
            if (JOptionPane.showConfirmDialog(null,
                    "attention vous avez supprimer un étudient,vous êtes sûr?", "supprimer etudiant",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION)

                if (txtid.getText().length() != 0) {
                    stm.executeUpdate("Delete From student where id = " + txtid.getText());
                    afficher();
                } else {
                    JOptionPane.showMessageDialog(null, "veuillez SVP remplire le champ id ou sélectionner une ligne !");
                }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "erreur de supprimer \n" + e.getMessage());
        }

    }

    //Action pour Ajouter
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
        //les textField pour remplir
        String id = txtid.getText();
        String nom = txtno.getText();
        String prenom = txtpr.getText();
        // String branche = txtbr.getSelectedItem().toString();
        String note = txtnot.getText();
        String absence = txtabs.getText();
        String requete = "insert into student(id,nom,prenom,note,absence)VALUES('" +
                id + "','" + nom + "','" + prenom + "','"  + note + "','"  + absence + "')";
        try {
            stm.executeUpdate(requete);
            JOptionPane.showMessageDialog(null, "l'etudiant est bien ajouter");
            txtno.setText("");
            txtpr.setText("");
            // txtbr.setSelectedItem(2);
            txtnot.setText("");
            txtabs.setText("");
            afficher();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    //Pour la moyenne
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            // model.setRowCount(0);
            stm = conn.obtenirconnexion().createStatement();
            ResultSet Rs = stm.executeQuery("Select * from student");
            double moy = 0;
            int nbr = 0;
            while (Rs.next()) {    
                // model.addRow(new Object[] { Rs.getString("id"), Rs.getString("nom"), Rs.getString("Prenom"),
                //         Rs.getString("note"), Rs.getString("absence") });
                nbr+=1;
                moy += Rs.getFloat("note");
            }
            moy = moy / nbr;
txtmoy.setText(String.format("%.2f", moy));
        } catch (Exception e) {
            System.err.println(e);
        }
    }


    //cette fonction deplace les données de la résultat dans les textField
    private void deplace(int i) {
        try {
            txtid.setText(model.getValueAt(i, 0).toString());
            txtno.setText(model.getValueAt(i, 1).toString());
            txtpr.setText(model.getValueAt(i, 2).toString());
            // txtbr.setSelectedItem(model.getValueAt(i, 3).toString());
            txtnot.setText(model.getValueAt(i, 3).toString());
            txtabs.setText(model.getValueAt(i, 4).toString());
        } catch (Exception e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "erreur de deplacement" + e.getLocalizedMessage());
        }
    }


    //Action pour Recherche par note
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
        try {
            model.setRowCount(0);// pour vider la list des etudient
            {
                Rs = stm.executeQuery("Select * From student WHERE id = '" + txtre.getText() + "'");//textre == textField de recherche
            }
            while (Rs.next()) {

                Object[] etudient = { Rs.getInt(1), Rs.getString(2), Rs.getString(3), Rs.getString(4), Rs.getInt(5) };
                model.addRow(etudient);
            }
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "il y a aucun etudient");

            } else {
                // tble.setModel(model);
                int i = 0;
                deplace(i);
            }

        } catch (Exception e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    //Action pour actualiser
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton4ActionPerformed
        afficher();
    }// GEN-LAST:event_jButton4ActionPerformed


    //Action pour modifier
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton5ActionPerformed
        try {
            if (JOptionPane.showConfirmDialog(null, "confirmer la modification", "modification",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {

                stm.executeUpdate("UPDATE student SET nom='" + txtno.getText() + "',prenom='" + txtpr.getText() +
                        "',note='" + txtnot.getText() + "',absence='" + txtabs.getText() +
                        "' WHERE id= " + txtid.getText());
                afficher();

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "erreur de modifier !!!!!!!" + e.getMessage());
            System.err.println(e);
        }
    }


    //Pour la moyenne
    // private void jButton6MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jButton6MouseClicked
    // }


    // Action lorsque je clique sur une ligne de talbleau, les données de cette ligne s'affichent dans les textField
    private void tbleMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tbleMouseClicked
        try {
            int i = tble.getSelectedRow();
            deplace(i);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "erreur de deplacement " + e.getLocalizedMessage());
        }
    }


    //Constructeur, initialisation des composants et du model puis l'afficher dans le tableau
    public Gestion_des_notes() {
        initComponents();
        model.addColumn("id");
        model.addColumn("nom");
        model.addColumn("prenom");
        model.addColumn("note");
        model.addColumn("absence");
        afficher();
    }

    private void initComponents() {
        jButton1 = new javax.swing.JButton();//Supprimer
        jButton2 = new javax.swing.JButton();//Ajouter
        jButton3 = new javax.swing.JButton();//Recherche
        jButton4 = new javax.swing.JButton();//Actualiser
        jButton5 = new javax.swing.JButton();//Modifier
        jButton6 = new javax.swing.JButton();//Rien
        txtre = new javax.swing.JTextField();
        txtmoy = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tble = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();//id
        jLabel1 = new javax.swing.JLabel();//Nom
        jLabel3 = new javax.swing.JLabel();//Prenom
        jLabel4 = new javax.swing.JLabel();//Branche
        jLabel5 = new javax.swing.JLabel();//Note
        txtnot = new javax.swing.JTextField();
        txtabs = new javax.swing.JTextField();
        // txtbr = new javax.swing.JComboBox();
        txtpr = new javax.swing.JTextField();
        txtno = new javax.swing.JTextField();
        txtid = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();//For wallpaper
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();//Ajouter
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(770, 530));
        getContentPane().setLayout(null);

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icone/supprimer.png")));
        jButton1.setText("Supprimer");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(180, 400, 143, 40);

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icone/ajouter.png"))); // NOI18N
        jButton2.setText("Ajouter");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(40, 350, 130, 40);

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icone/rechercher.png"))); // NOI18N
        jButton3.setText("recherche");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3);
        jButton3.setBounds(380, 380, 150, 40);

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icone/modifier.png"))); // NOI18N
        jButton4.setText("actualiser");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4);
        jButton4.setBounds(180, 350, 140, 40);

        jButton5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icone/actualiser.png"))); // NOI18N
        jButton5.setText("modifier");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton5);
        jButton5.setBounds(40, 400, 130, 40);

        jButton6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        // jButton6.setIcon(new
        // javax.swing.ImageIcon(getClass().getResource("/icone/nouveau.png"))); //
        // NOI18N
        jButton6.setText("Moyenne");
        // jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
        //     public void mouseClicked(java.awt.event.MouseEvent evt) {
        //         jButton6MouseClicked(evt);
        //     }
        // });
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton6);
        jButton6.setBounds(40, 450, 130, 40);

        txtmoy.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtmoy.addKeyListener(new java.awt.event.KeyAdapter() {
        });
        getContentPane().add(txtmoy);
        txtmoy.setBounds(180, 457, 130, 30);

        txtre.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtre.addKeyListener(new java.awt.event.KeyAdapter() {
        });
        getContentPane().add(txtre);
        txtre.setBounds(560, 380, 130, 30);

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        jLabel6.setText("gestion des notes");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(260, 30, 350, 70);

        tble.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null }
                },
                new String[] {
                        "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
                }));
        tble.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbleMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tble);
        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(340, 140, 374, 90);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel2.setText("id :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(40, 110, 42, 17);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel1.setText("Nom :");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(40, 150, 42, 17);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setText("Prenom:");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(40, 190, 53, 17);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setText("Note :");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(40, 240, 60, 17);

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setText("Absence  :");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(50, 290, 40, 17);

        txtnot.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        getContentPane().add(txtnot);
        txtnot.setBounds(170, 240, 100, 23);

        // txtbr.setModel(new javax.swing.DefaultComboBoxModel(
        //         new String[] { "Réseau", "SGBD", "Embarqué", "R.O", "Gestion", "GL2", "Java Avancé" }));
        // getContentPane().add(txtbr);
        txtabs.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        getContentPane().add(txtabs);
        txtabs.setBounds(170, 290, 100, 22);

        txtpr.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        getContentPane().add(txtpr);
        txtpr.setBounds(170, 190, 100, 23);

        txtno.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        getContentPane().add(txtno);
        txtno.setBounds(170, 150, 100, 23);

        txtid.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        getContentPane().add(txtid);
        txtid.setBounds(170, 110, 100, 23);

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icone/wallpaper.jpg"))); // NOI18N
        getContentPane().add(jLabel7);
        jLabel7.setBounds(0, 0, 760, 500);

        jMenu1.setText("File");

        jMenuItem1.setAccelerator(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("ajouter");

        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem2.setText("modifier");

        jMenu1.add(jMenuItem2);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        jMenuItem3.setText("supprimer");

        jMenu1.add(jMenuItem3);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        jMenuItem4.setText("actualiser");

        jMenu1.add(jMenuItem4);

        jMenuItem5.setAccelerator(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("rechercher");

        jMenu1.add(jMenuItem5);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jMenuItem6.setAccelerator(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setText("realiser par");
        jMenu2.add(jMenuItem6);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Gestion_des_notes().setVisible(true);
            }
        });
    }
}
