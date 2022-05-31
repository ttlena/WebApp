package de.hsrm.mi.web.projekt.api.benutzer;

import java.time.LocalDateTime;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.gebot.Gebot;

public record GetAngebotResponseDTO (
    long angebotid,                     // ID des Angebots
    String beschreibung,                // Beschreibung
    long anbieterid,                    // ID des Anbieters
    String anbietername,                // Name des Anbieters
    long mindestpreis,                  // geforderter Mindestpreis
    LocalDateTime ablaufzeitpunkt,      // Ende der Versteigerung
    String abholort,                    // Abholort der Sache
    double lat,                         // mit Breite
    double lon,                         // und Laenge
    long topgebot,                      // bisher max. Gebot fuer dieses Angebot 
    long gebote                         // Anzahl bisheriger Gebote
) {
    public static GetAngebotResponseDTO from(Angebot a) {
        long topgebot = 0;
        for (Gebot g: a.getGebote()){
            if (topgebot < g.getBetrag()){
                topgebot = g.getBetrag();
            }
        }
        return new GetAngebotResponseDTO(
                    a.getId(), 
                    a.getBeschreibung(), 
                    a.getAnbieter().getId(), 
                    a.getAnbieter().getName(), 
                    a.getMindestpreis(), 
                    a.getAblaufzeitpunkt(), 
                    a.getAbholort(), 
                    a.getLat(), 
                    a.getLon(), 
                    topgebot, 
                    a.getGebote().size());
    }
}
