package de.hsrm.mi.web.projekt.benutzerprofil;

import java.time.LocalDate;

public class TestKlasse {

    public static void main(String[] args){
        BenutzerProfil bp1 = new BenutzerProfil();
        BenutzerProfil bp2 = new BenutzerProfil();
        bp1.setName("Dumbo");
        bp2.setName("Dumbo");
        bp1.setGeburtsdatum(LocalDate.of(1,2,3));
        bp2.setGeburtsdatum(LocalDate.of(1,2,3));
        if(bp1.equals(bp2)){
            System.out.println("Gleich!");
        }else{
            System.out.println("Ungleich!");
        }
        bp1.setInteressen("schwimmen,lesen, schreiben");
        for(String interesse: bp1.getInteressenListe()){
            System.out.println(interesse);
        }

    }
    
}
