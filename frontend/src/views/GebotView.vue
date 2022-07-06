<script setup lang="ts">
import type { IAngebotListeItem } from '@/services/IAngebotListeItem';
import GeoLink from '@/components/GeoLink.vue';

import { useAngebot } from '@/services/useAngebot';
import { useGebot } from '@/services/useGebot';
import { onMounted, ref } from 'vue';
import { computed, reactive, type ComputedRef } from '@vue/reactivity';
import { useLogin } from '@/services/useLogin';

const props = defineProps<{
    angebotidstr: string
}>()
console.log("angebotidstr: " + props.angebotidstr)

const { angebote } = useAngebot();
const { gebote, updateGebote, sendeGebot } = useGebot(parseInt(props.angebotidstr));
const { logindata } = useLogin();

onMounted(async () => {
    await updateGebote()
    useLogin()
});

let angebot = angebote.angebotliste.find((ele) => { return ele.angebotid == parseInt(props.angebotidstr) })
const eingabefeld = ref(0)
let listitems = computed(() => {
    let gebote_mit_angebotid = gebote.gebotliste.filter(e => (e.angebotid == parseInt(props.angebotidstr)))
    return gebote_mit_angebotid.sort((a, b) => {
        return new Date(a.gebotzeitpunkt).getTime() - new Date(b.gebotzeitpunkt).getTime()
    })
})

const restzeit = ref<number>()
function updateRestzeit() {
    if (angebot != undefined) {
        restzeit.value = new Date(angebot.ablaufzeitpunkt).getTime() - Date.now()
        restzeit.value = Math.ceil(restzeit.value / 1000)
        if (restzeit.value <= 0) {
            clearInterval(timerid)
        }
    }
}
let timerid = setInterval(() => { updateRestzeit() }, restzeit.value)

async function gebotSenden(): Promise<void> {
    console.log("Eingabe für Gebot: " + eingabefeld.value)
    await sendeGebot(eingabefeld.value)
        .then(() => {
            listitems = computed(() => {
                let gebote_mit_angebotid = gebote.gebotliste.filter(e => (e.angebotid == parseInt(props.angebotidstr)))
                console.log("gebote mit der angebotid: " + gebote_mit_angebotid.length)
                return gebote_mit_angebotid.sort((a, b) => {
                    return new Date(a.gebotzeitpunkt).getTime() - new Date(b.gebotzeitpunkt).getTime()
                })
            })
            console.log("gebotliste length: " + listitems.length)
        })
    await updateGebote()

}
let sortedGebotListe = computed(() => {
    let gebotListe = gebote.gebotliste.slice()
    gebotListe.sort((a, b) => {
        return new Date(b.gebotzeitpunkt).getTime() - new Date(a.gebotzeitpunkt).getTime()
    })
    let topbetrag = Math.max(...gebotListe.map(o => o.betrag))
    let topgebot = gebotListe.find((o) => {
        return o.betrag == topbetrag
    })
    return gebotListe
})

let topgebot = computed(() => {
    return gebote.gebotliste.find(ele => {
        return (ele.angebotid == parseInt(props.angebotidstr)) && (ele.betrag == gebote.topgebot)
    })
})

</script>

<template>
    <a :href="`/logout`">Logout</a>
    <h1>Treffliche Angebote</h1>
    <!-- <h4>Sie sind {{ logindata.username }}</h4> -->

    <!-- Error-Message anzigen, falls vorhanden -->
    <div v-if="gebote.errormessage != ''">
        <span style="color: #fa4a48; font-weight:bold;">{{ angebote.errormessage }}</span>
    </div>

    <br>
    <!-- Angebot-Angaben anzeigen, falls gefunden -->
    <div v-if="angebot != undefined">
        <h3>Versteigerung {{ angebot.beschreibung }} ab {{ angebot.mindestpreis }} EUR</h3>
        <p>von {{ angebot.anbietername }}, abholbar in </p>
        <GeoLink :lat="angebot.lat" :lon="angebot.lon">{{ angebot.abholort }}</GeoLink>
        <p>bis {{ angebot.ablaufzeitpunkt }}</p>
        <br>
    </div>

    <label>Bisheriges Topgebot von EUR {{ gebote.topgebot }} ist von {{ gebote.topbieter }}, Restzeit {{ restzeit }}
        Sekunden</label>
    <!-- Gebot abgeben -->
    <div>
        <input type="number" v-model="eingabefeld">
        <button @click="gebotSenden()" style="width:auto; background-color: #487d75;">bieten</button>
    </div>
    <!-- Gebot-Liste anzeigen lassen -->
    <table v-if="gebote.gebotliste.length > 0" style="border-collapse: collapse;">
        <tbody>
            <label>Topgebot:</label>
            <div>
                <tr style="border-bottom: 1pt solid #fff;">
                    <td>{{ topgebot?.gebotzeitpunkt }}</td>
                    <td>{{ gebote.topbieter }}</td>
                    <td>bietet {{ gebote.topgebot }} EUR</td>
                </tr>
            </div>
            <label>weitere Gebote:</label>
            <tr v-for="gebot in sortedGebotListe" style="border-bottom: 1px solid #ddd;">
                <span v-if="gebot.gebietername != gebote.topbieter">
                    <td>{{ gebot.gebotzeitpunkt }}</td>
                    <td>{{ gebot.gebietername }}</td>
                    <td>bietet {{ gebot.betrag }} EUR</td>
                </span>
            </tr>
        </tbody>
    </table>

</template>
