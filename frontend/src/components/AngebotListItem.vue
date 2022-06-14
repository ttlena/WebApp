<script setup lang="ts">
import type { IAngebotListeItem } from '@/services/IAngebotListeItem';
import GeoLink from './GeoLink.vue'
import { ref } from 'vue';

const props = defineProps<{
    angebot: IAngebotListeItem
}>()

let detailsOn = ref(false);

function details() {
    detailsOn.value = !detailsOn.value
}
</script>

<template>
    <table>
        <thead>
            <th style="width:25%; text-align: left;">{{props.angebot.beschreibung}}</th>
            <th style="width:45%; text-align: right;">{{props.angebot.gebote}} Gebote</th>
            <th style="width:30%; text-align: center;"> {{props.angebot.topgebot}} EUR </th>
            <button v-on:click="details" style="width:auto; align: right;">
                <i v-if="detailsOn==false" class="fas fa-star">zu</i>
                <i v-if="detailsOn==true" class="fa-solid fa-arrow-down">auf</i>
            </button>
        </thead>
    </table>

    <table v-if="detailsOn === true">
        <thead>
            <th style="width:40%;"></th>
            <th style="width:60%;"></th>
        </thead>
        <tr style="border-bottom: 1px solid #ddd;">
            <td>Letztes Gebot</td>
            <td>{{props.angebot.topgebot}} EUR (Mindestpreis war {{props.angebot.mindestpreis}} EUR)</td>
        </tr>
        <tr style="border-bottom: 1px solid #ddd;">
            <td>Abholort</td>
            <td><GeoLink :lat="props.angebot.lat" :lon="props.angebot.lon">{{props.angebot.abholort}}</GeoLink></td>
        </tr>
         <tr style="border-bottom: 1px solid #ddd;">
            <td>bei</td>
            <td>{{props.angebot.anbietername}}</td>
        </tr>
         <tr style="border-bottom: 1px solid #ddd;">
            <td>bis</td>
            <td>{{props.angebot.ablaufzeitpunkt}}</td>
        </tr>
    </table>
</template>
