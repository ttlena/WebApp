<script setup lang="ts">
import AngebotListItem from './AngebotListItem.vue';
import { useAngebot } from '@/services/useAngebot';
import { ref, computed } from 'vue';

const { angebote } = useAngebot();
const suchfeld = ref("")
const listitems = computed(() => {
    const n: number = suchfeld.value.length;
    if (n < 1) {
        return angebote.angebotliste;
    } else {
        return angebote.angebotliste.filter(e => (e.abholort.toLowerCase().includes(suchfeld.value.toLowerCase()) ||
            e.beschreibung.toLowerCase().includes(suchfeld.value.toLowerCase()) || e.anbietername.toLowerCase().includes(suchfeld.value.toLowerCase()))
        );
    }
});

function clear() {
    suchfeld.value = "";
}

</script>

<template>
    <div>
        <input type="text" v-model="suchfeld" placeholder="Suchbegriff" />
        <button style="width:auto;" v-on:click="clear">clear</button>
    </div>
    <AngebotListItem :angebot="a" v-for="a in listitems" />
</template>
