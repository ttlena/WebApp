import { reactive, readonly } from "vue";
import type { IAngebotListeItem } from "./IAngebotListeItem";

interface IAngebotState {
    angebotliste: IAngebotListeItem[],
    errormessage: string
}

const angebotState:IAngebotState = reactive({angebotliste: [], errormessage: ''});

export function useAngebot() {

    async function updateAngebote(): Promise<void> {
        try {
            console.log("drin")
            const url = `api/angebot`
            const response = await fetch(url)
            if (!response.ok) {
                angebotState.errormessage = response.statusText
            }else {
                angebotState.errormessage = "";
                const jsondata:IAngebotListeItem[] = await response.json()
                angebotState.angebotliste = jsondata
            }
        } catch (reason) {
            angebotState.errormessage = `FEHLER: ${reason}`
        }
    }

    return {
        angebote: readonly(angebotState),
        updateAngebote
    }
}
