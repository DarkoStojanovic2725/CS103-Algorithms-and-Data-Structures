/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs103.projekat1;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author darko
 */
public class Projekat {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Unesite broj loptica: ");
        int brojLoptica = sc.nextInt();
        System.out.println("Unesite broj slotova: ");
        int brojSlotova = sc.nextInt();
        beanMachine(brojLoptica, brojSlotova);
        
        
    }
   
    
         public static void beanMachine(int brojLoptica, int brojSlotova){
             char[] nizPozicija = new char[brojSlotova - 1];  //br slotova - 1 jer svaki put ima n-1 nailova
             int[] pozicijaLopte = new int[brojSlotova];
             
             for(int i = 0; i < brojLoptica; i++){
             pozicijaPada(nizPozicija);
             cuvajPoziciju(nizPozicija, pozicijaLopte);
             prikazNiza(nizPozicija);
             }
             prikazPozicijeLopte(pozicijaLopte);
         }
         
         
         //graficki prikaz loptia koriscenjem int niza PozicijaLopte
         public static void prikazPozicijeLopte(int[] pozicijaLopte){
             int maxHL = mxl(pozicijaLopte, pozicijaLopte.length-1);
             for(int i = maxHL; i > 0; i--){ 
                 for(int j = 0; j < pozicijaLopte.length; j++)
                     if(i == pozicijaLopte[j]){
                         System.out.printf(" %-2d", 0);
                         pozicijaLopte[j]--;
                     }
             else
                         System.out.printf(" %-2s", "");
             System.out.println();
      
             }        
             
         }
         //nalazenje max broja loptica u 1 slotu
         public static int mxl(int[] pozicijaLopte, int i){
             if(i > 0)
                 return Math.max(pozicijaLopte[i], mxl(pozicijaLopte, i-1));
             else
                 return pozicijaLopte[0];

         }
         
         //cuvanje pozicije loptice iz niza char u niz tipa int
         public static void cuvajPoziciju(char[] nizPozicija, int[] pozicijaLopte){
             int middlePosition = (nizPozicija.length + 1) / 2;  //srednja pozicija niza
             for(int i = 0; i < nizPozicija.length; i++)
                 if(nizPozicija[i] == 'L' && middlePosition > 0)  //u zavisnosti da li lopta ide levo ili desno oduzimaj ili dodaj na midPos
                     middlePosition--;
                 else if(middlePosition < nizPozicija.length)
                     middlePosition++;
             
             //cuvaj u nizu pozicijaLopte
            pozicijaLopte[middlePosition]++; 
         }
         
         //prikaz L I R u nizu koriscenjem toString
         public static void prikazNiza(char[] nizPozicija){
             System.out.println(Arrays.toString(nizPozicija));
         }
         
         
         //random punjenje niza tipa char slovima l i r
         public static void pozicijaPada(char[] nizPozicija){
             Random rand = new Random();
              for(int i = 0; i < nizPozicija.length; i++)
                 if(rand.nextBoolean() == true)
                  nizPozicija[i] = 'L';
              else
                  nizPozicija[i] = 'R';  
         }

}
