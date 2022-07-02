<script setup lang="ts">
import type { IAngebotListeItem } from '@/services/IAngebotListeItem';
import GeoLink from '@/components/GeoLink.vue';

import { useAngebot } from '@/services/useAngebot';
import { useGebot } from '@/services/useGebot';
import { onMounted, ref } from 'vue';
import { computed } from '@vue/reactivity';
import { useLogin } from '@/services/useLogin';

const props = defineProps<{
    angebotidstr: string
}>()
console.log("angebotidstr: "+props.angebotidstr)

const { angebote } = useAngebot();
const { gebote, updateGebote, sendeGebot } = useGebot(parseInt(props.angebotidstr));
const { logindata } = useLogin();

onMounted(async () => {
    updateGebote()
    useLogin()
});

let angebot = angebote.angebotliste.find((ele) => { return ele.angebotid == parseInt(props.angebotidstr) })
const eingabefeld = ref("")
const listitems = computed(() => {
    let gebote_mit_angebotid = gebote.gebotliste.filter(e => (e.angebotid == parseInt(props.angebotidstr)))
    return gebote_mit_angebotid.sort((a, b) => {
        return new Date(a.gebotzeitpunkt).getTime() - new Date(b.gebotzeitpunkt).getTime()
    })
})

let topgebot = computed(() => {
    return gebote.gebotliste.find(ele => {
        return (ele.angebotid == parseInt(props.angebotidstr)) && (ele.betrag == gebote.topgebot)
    })
})

const restzeit = ref<number>()
function updateRestzeit() {
    if (angebot != undefined) {
        restzeit.value = angebot.ablaufzeitpunkt.getTime() - Date.now()
        if (restzeit.value <= 0) {
            clearInterval(timerid)
        }
    }
}
let timerid = setInterval(() => { updateRestzeit() }, restzeit.value)

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
        <h3>Versteigerung {{ angebot.beschreibung }} ab {{ angebot.topgebot }} EUR</h3>
        <p>von {{ angebot.anbietername }}, abholbar in </p>
        <GeoLink :lat="angebot.lat" :lon="angebot.lon"></GeoLink>
        <p>bis {{ angebot.ablaufzeitpunkt }}</p>
        <br>
    </div>

    <label>Bisheriges Topgebot von EUR {{ gebote.topgebot }} ist von {{ gebote.topbieter }}, Restzeit {{ restzeit }}
        Sekunden</label>
    <!-- Gebot abgeben -->
    <div>
        <input type="number" :model="eingabefeld">
        <button @click="sendeGebot(parseInt(eingabefeld))"
            style="width:auto; background-color: #487d75;">bieten</button>
    </div>
    <table v-if="listitems.length > 0">
        <tr v-for="index in 10" :key="index" style="border-bottom: 1px solid #fff">
            <td>{{ topgebot?.gebotzeitpunkt }}</td>
            <td>{{ gebote.topbieter }}</td>
            <td>bietet {{ gebote.topgebot }} EUR</td>
        </tr>
        <tr v-for="index in 10" :key="index" style="border-bottom: 1px solid #ddd">
            <td>{{ listitems[index].gebotzeitpunkt }}</td>
            <td>{{ listitems[index].gebietername }}</td>
            <td>bietet {{ listitems[index].betrag }} EUR</td>
        </tr>
    </table>

</template>
