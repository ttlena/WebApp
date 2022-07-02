import { reactive, readonly } from "vue";
import type { IAngebotListeItem } from "./IAngebotListeItem";
import { Client, type Message } from '@stomp/stompjs';
import type { IBackendInfoMessage } from './IBackendInfoMessage';
import { useLogin } from "./useLogin";

interface IAngebotState {
    angebotliste: IAngebotListeItem[],
    errormessage: string
}

const angebotState: IAngebotState = reactive({ angebotliste: [], errormessage: '' });

export function useAngebot() {

    const wsurl = `ws://${window.location.host}/stompbroker`;
    const DEST = "/topic/angebot";

    async function updateAngebote(): Promise<void> {
        try {
            const url = `api/angebot`
            const response = await fetch(url, {
                headers: {
                    'Authorization': 'Bearer ' + useLogin().logindata.jwtToken
                }
            })
            console.log("useAngebot(): response aus fetch(/api/angebot - " + response.json.toString)
            if (!response.ok) {
                angebotState.errormessage = response.statusText
                console.log("useAngebot(): response ok")
            } else {
                angebotState.errormessage = "";
                const jsondata: IAngebotListeItem[] = await response.json()
                angebotState.angebotliste = jsondata
                console.log("useAngebot(): response nicht ok")
            }
        } catch (reason) {
            angebotState.errormessage = `FEHLER: ${reason}`
        }
    }

    function receiveAngebotMessages(): void {
        const stompclient = new Client({ brokerURL: wsurl });
        console.log("STOMP Client erstellt");
        stompclient.onWebSocketError = (event) => { /* WS-Error */ console.log("error on WebSocket") }
        stompclient.onStompError = (frame) => { /* STOMP-Error */ console.log("error on StompError") }

        stompclient.onConnect = (frame) => {
            //Callback: erfolgreicher Verbindungsaufbau zu Broker
            console.log("erfolgreich");

            stompclient.subscribe(DEST, (message) => {
                // Callback: Nachricht auf DEST empfangen
                // empfangene Nutzdaten in message.body abrufbar,
                // ggf. mit JSON.parse(message.body) zu JS konvertieren
                console.log("im subscribe drin, message: " + message);
                updateAngebote();
                let jsonobj = JSON.parse(message.body);
                console.log("message.body: " + message.body);
                let backendInfoMessage = reactive({
                    ...jsonobj
                } as IBackendInfoMessage)
            })
        };
        stompclient.onDisconnect = () => { /* Verbindung abgebaut */ console.log("Verbindung abgebaut") };

        //Verbindung zum Broker aufbauen
        stompclient.activate();

        // Nachrichtenversand vom Client zum Server
        try {
            stompclient.publish({
                destination: DEST, headers: {},
                body: "BODY STRING"
                // ... oder body: JSON.stringify(datenobjekt)
            });
        } catch (fehler) {
            // P
        }

    }
        return {
            angebote: readonly(angebotState),
            updateAngebote,
            receiveAngebotMessages
        }
    }
