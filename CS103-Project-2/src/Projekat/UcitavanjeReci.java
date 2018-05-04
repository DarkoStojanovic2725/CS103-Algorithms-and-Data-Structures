/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Projekat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author darko
 */
public class UcitavanjeReci {
    private ArrayList<String> reci = new ArrayList<String>();
    private static final String nazivFajla = "/resources/fajl.txt";
    
    public UcitavanjeReci(){
        try{
           InputStream input = getClass().getResourceAsStream(nazivFajla);
           BufferedReader br = new BufferedReader(new InputStreamReader(input));
           
           String tekst = "";
           while((tekst = br.readLine()) != null){
               reci.add(tekst);
           }
        }
        catch(Exception e){
            System.out.println("Fajl ne postoji: " + nazivFajla);
            System.out.println("Greska: " + e.getMessage());
        }
    }
    public String randomRec(){
        if(reci.isEmpty()){
            return "Nema reci";
        }
        else
            return reci.get((int)(Math.random()*reci.size()));
    }
    
            
}
