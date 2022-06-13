<script setup lang="ts">
    import GeoLink from '@/components/GeoLink.vue';
</script>

<template>
    <main>
        <GeoLink :lat="52.9849" :lon="9.5794">Visselhövede</GeoLink>
        <br>
        <GeoLink :lat="52.9849" :lon="9.5794" :zoom="15" >Visselhövede</GeoLink>
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
