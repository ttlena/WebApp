<script setup lang="ts">
import AngebotListItem from './AngebotListItem.vue';
import { useFakeAngebot } from '@/services/useFakeAngebot';
import { ref, computed } from 'vue';

const { angebote } = useFakeAngebot();
const suchfeld = ref("")
const listitems = computed(() => {
    const n: number = suchfeld.value.length;
    if (n < 1) {
        return angebote.value;
    } else {
        return angebote.value.filter(e => (e.abholort.toLowerCase().includes(suchfeld.value.toLowerCase()) ||
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
