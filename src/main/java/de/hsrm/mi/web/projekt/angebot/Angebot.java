package de.hsrm.mi.web.projekt.angebot;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;

@Entity
public class Angebot {
    @Id
    @GeneratedValue
    private long id;

    @Version
    private long version;

    private String beschreibung;

    private long mindestpreis;

    @DateTimeFormat(iso=ISO.DATE_TIME)
    private LocalDateTime ablaufzeitpunkt;
    
    private String abholort;

    private double lat;
    private double lon;

    @ManyToOne
    private BenutzerProfil anbieter;

    public Angebot(){
        this.beschreibung = "";
        this.mindestpreis = 0;
        this.ablaufzeitpunkt = LocalDateTime.now();
        this.abholort = "";
    }

    public long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public long getMindestpreis() {
        return mindestpreis;
    }

    public void setMindestpreis(long mindestpreis) {
        this.mindestpreis = mindestpreis;
    }

    public LocalDateTime getAblaufzeitpunkt() {
        return ablaufzeitpunkt;
    }

    public void setAblaufzeitpunkt(LocalDateTime ablaufzeitpunkt) {
        this.ablaufzeitpunkt = ablaufzeitpunkt;
    }

    public String getAbholort() {
        return abholort;
    }

    public void setAbholort(String abholort) {
        this.abholort = abholort;
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

    public BenutzerProfil getAnbieter() {
        return anbieter;
    }

    public void setAnbieter(BenutzerProfil anbieter) {
        this.anbieter = anbieter;
    }

}