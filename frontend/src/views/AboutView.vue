<script setup lang="ts">
    import GeoLink from '@/components/GeoLink.vue';
    import AngebotListItem from '@/components/AngebotListItem.vue';
    import AngebotListe from '@/components/AngebotListe.vue';
    import { useFakeAngebot } from '@/services/useFakeAngebot';

    const { angebote, bietenSimulieren } = useFakeAngebot();
</script>

<template>
    <main>
        <GeoLink :lat="52.9849" :lon="9.5794">Visselhövede</GeoLink>
        <br>
        <GeoLink :lat="52.9849" :lon="9.5794" :zoom="15" >Visselhövede</GeoLink>

        <br><br><br>

        <h3>About...</h3>
        <div v-for="a in angebote">
          <AngebotListItem :angebot="a" />
        </div>
        <button v-on:click="bietenSimulieren">bieten simulieren</button>

        <br><br>

        <h3>Aktuelle Angebote</h3>
        <AngebotListe></AngebotListe>
    </main>
</template>

<style>
@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>
