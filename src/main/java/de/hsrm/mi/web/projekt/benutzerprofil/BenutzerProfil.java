package de.hsrm.mi.web.projekt.benutzerprofil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.gebot.Gebot;
import de.hsrm.mi.web.projekt.projektuser.ProjektUser;
import de.hsrm.mi.web.projekt.validierung.Bunt;

@Entity
public class BenutzerProfil {
    @Id
    @GeneratedValue
    private long id;

    @Version
    private long version;

    @Size(min=3,max=60, message="{name.fehler}")
    @NotNull(message = "Name darf nicht fehlen")
    private String name;

    @DateTimeFormat(iso = ISO.DATE)
    @PastOrPresent
    @NotNull
    private LocalDate geburtsdatum;
    
    @NotNull
    private String adresse;
    
    @Email(message = "{email.fehler}")
    private String email;
    
    @NotNull
    @Bunt(message = "{bunt.fehler}")
    private String lieblingsfarbe;

    @NotNull
    private String interessen;

    private double lat;
    private double lon;

    @OneToMany(mappedBy = "anbieter", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Angebot> angebote;

    @OneToMany(mappedBy = "gebieter")
    private List<Gebot> gebote;

    @OneToOne(mappedBy = "benutzerProfil", cascade = CascadeType.REMOVE)
    private ProjektUser projektUser;
    
    public BenutzerProfil(){
        this.name = "";
        this.geburtsdatum = LocalDate.of(1,1,1);
        this.adresse = "";
        this.email = null;
        this.lieblingsfarbe = "";
        this.interessen = "";
        this.angebote = new ArrayList<>();
        this.gebote = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(LocalDate geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLieblingsfarbe() {
        return lieblingsfarbe;
    }

    public void setLieblingsfarbe(String lieblingsfarbe) {
        this.lieblingsfarbe = lieblingsfarbe;
    }

    public String getInteressen() {
        return interessen;
    }

    public void setInteressen(String interessen) {
        this.interessen = interessen;
    }

    public List<Angebot> getAngebote() {
        return angebote;
    }

    public void setAngebote(List<Angebot> angebote) {
        this.angebote = angebote;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setGebot(Gebot gebot){
        this.gebote.add(gebot);
    }

    public List<Gebot> getGebote(){
        return this.gebote;
    } 

    public ProjektUser getProjektUser() {
        return projektUser;
    }

    public void setProjektUser(ProjektUser projektUser) {
        this.projektUser = projektUser;
    }

    public List<String> getInteressenListe(){
        List<String> interessenListe = new ArrayList<>();
        if (!this.interessen.equals("")){
            interessenListe = Arrays.asList(this.interessen.split("\\s*,\\s*"));
        }
        return interessenListe;
    }

    public String toString(){
        return 
            "Id: " + this.id +
            "\nVersion: " +  this.version +
            "\nName: " + this.name +
            "\nGeburtsdatum: " + this.geburtsdatum +
            "\nAdresse: " + this.adresse +
            "\nE-Mail: " + this.email +
            "\nLieblingsfarbe: " + this.lieblingsfarbe +
            "\nInteressen: " + this.interessen +
            "\ngeografische Breite: " + this.lat +
            "\ngeografische Länge: " + this.lon;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((geburtsdatum == null) ? 0 : geburtsdatum.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BenutzerProfil other = (BenutzerProfil) obj;
        if (geburtsdatum == null) {
            if (other.geburtsdatum != null)
                return false;
        } else if (!geburtsdatum.equals(other.geburtsdatum))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
    
}
