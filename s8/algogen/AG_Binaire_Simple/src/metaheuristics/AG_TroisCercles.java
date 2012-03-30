/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metaheuristics;

import base.variables.Individual;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import problems.Problem;
import problems.TroisCercles;

/**
 *
 * @author kawa
 */
public class AG_TroisCercles extends JApplet {
    
    private JLabel 
            lab_taillePop,
            lab_nbGen,
            lab_tauxCr,
            lab_tauxMut,
            lab_display;
    
    private JTextField 
            text_taillePop,
            text_nbGen,
            text_tauxCr,
            text_tauxMut;
   
    private JButton but_valider;
    
    @Override
    public void init()
    {
        
        lab_taillePop = new JLabel("Taille de la population :");
        lab_nbGen = new JLabel("Nombre de générations :");
        lab_tauxCr = new JLabel("Taux de croisement :");
        lab_tauxMut = new JLabel("Taux de mutation :");
        lab_display = new JLabel();
                
        text_taillePop = new JTextField();
        text_nbGen = new JTextField();
        text_tauxCr = new JTextField();
        text_tauxMut = new JTextField();
        
        but_valider = new JButton("Valider");
        
        but_valider.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                launch_AG();
            }
        });
        
        
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
       
        GroupLayout.Alignment align;
                
        align = GroupLayout.Alignment.LEADING;
        layout.setHorizontalGroup(
            layout.createParallelGroup(align)
            .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(align)
                .addComponent(lab_taillePop)
                .addComponent(lab_nbGen)
                .addComponent(lab_tauxCr)
                .addComponent(lab_tauxMut))
            .addGroup(layout.createParallelGroup(align)
                .addComponent(text_taillePop)
                .addComponent(text_nbGen)
                .addComponent(text_tauxCr)
                .addComponent(text_tauxMut)))
            .addComponent(but_valider)
            .addComponent(lab_display)
            );        
        
        
        align = GroupLayout.Alignment.BASELINE;
        layout.setVerticalGroup(
            layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(align)
                .addComponent(lab_taillePop)
                .addComponent(text_taillePop))
            .addGroup(layout.createParallelGroup(align)
                .addComponent(lab_nbGen)
                .addComponent(text_nbGen))
            .addGroup(layout.createParallelGroup(align)
                .addComponent(lab_tauxCr)
                .addComponent(text_tauxCr))
            .addGroup(layout.createParallelGroup(align)
                .addComponent(lab_tauxMut)
                .addComponent(text_tauxMut))
            .addComponent(but_valider)
            .addComponent(lab_display)
            );
        
    }
            
         
    public void launch_AG()
    {
        Individual bestSolution = null;
        Problem problem = new TroisCercles();
        
        AG_Simple algo = new AG_Simple(problem);
       
        try
        {
            algo.popSize = Integer.parseInt(text_taillePop.getText());
            algo.nbGenerations = Integer.parseInt(text_nbGen.getText());
            algo.xProba = Float.parseFloat(text_tauxCr.getText());
            algo.mProba = Float.parseFloat(text_tauxMut.getText());
        
            bestSolution = algo.execute();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        lab_display.setText(
                "<html><font size=\"2\" color=\"gray\" face=\"Arial\">La meilleure valeur de fitness trouvée est : <br/> " 
                + bestSolution.getFitness() + "</font></html>"
                );
    }
            
    
    
}
