<script setup lang="ts">
    import AngebotListe from '@/components/AngebotListe.vue';
    import { useAngebot } from '@/services/useAngebot';
    import { onMounted, reactive, ref, toRef } from 'vue';

    const { angebote, updateAngebote, receiveAngebotMessages } = useAngebot();
    
    onMounted(async() => {
        updateAngebote();
        receiveAngebotMessages();
    });
    
</script>

<template>
<h1>Wir haben aktuell {{angebote.angebotliste.length}} Angebote für Sie</h1>
<div v-if="angebote.errormessage != ''">
    <span style="color: #fa4a48; font-weight:bold;">{{angebote.errormessage}}</span>
</div>
<br>
<button @click="updateAngebote" style="width:auto;">update</button>
<br><br>
<AngebotListe></AngebotListe>
</template>