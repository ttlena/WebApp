<script setup lang="ts">
import { useLogin } from '@/services/useLogin';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const { logindata, login, logout } = useLogin()

onMounted(() => {
    logout()
})

let username = ref('')
let password = ref('')
const router = useRouter()

async function doLogin(): Promise<void> {
    await login(username.value, password.value)

    
    if (logindata.errormessage == '') {
        router.push("http://localhost:3000/").then(()=>{
        console.log("router pushed")
    }).catch((err)=> {
        console.log("fehler ",err)
    })
    }
    console.log('errormessage: '+logindata.errormessage)

}
</script>

<template>
    <h1>Treffliche Angebote</h1>
    <h4>Sie sind nicht eingeloggt</h4>
    <br><br>
    <h3>Bitte inloggen Sie sich:</h3>
    <form>
        <input v-model="username" type="text" style="width: 100%;">
        <input v-model="password" type="password" style="width: 100%;">
    </form>
    <button @click="doLogin">login</button>
</template>