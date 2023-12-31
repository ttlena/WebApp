import type { IAngebotListeItem } from "@/services/IAngebotListeItem";
import type { IBackendInfoMessage } from "./IBackendInfoMessage";
import { reactive, readonly } from "vue";

import { Client } from '@stomp/stompjs';
import { stringifyQuery } from "vue-router";
const wsurl = `ws://${window.location.host}/stompbroker`

interface ILoginState {
    username: string,
    name: string,
    benutzerprofilid: number,
    loggedin: boolean,
    jwtToken: string
    errormessage: string
}

const loginState: ILoginState = /* reaktives Objekt zu Interface ILoginState */ reactive({ username: '', name: '', benutzerprofilid: 0, loggedin: false, jwtToken: '', errormessage: '' })

// Analog Java-Records auf Serverseite:
// public record JwtLoginResponseDTO(String username, String name, Long benutzerprofilid, String jwtToken) {};
interface IJwtLoginResponseDTO {
    username: string,
    name: string,
    benutzerprofilid: number,
    jwtToken: string
}

// public record JwtLoginRequestDTO(String username, String password) {};
interface IJwtLoginRequestDTO {
    username: string,
    password: string
}


async function login(username: string, password: string) {
    /*
     * sendet per fetch() POST auf Endpunkt /api/login ein IAddGebotRequestDTO als JSON
     * erwartet IJwtLoginResponseDTD-Struktur zurück (JSON)
     * 
     * Falls ok, wird 'errormessage' im State auf leer gesetzt,
     * die loginState-Eigenschaften aus der Antwort befüllt
     * und 'loggedin' auf true gesetzt
     * 
     * Falls Fehler, wird ein logout() ausgeführt und auf die Fehlermeldung in 'errormessage' geschrieben
     */
    console.log("im login() drin")
    console.log("user name ", username)
    console.log("password", password)

    const url = `/api/login`
    let neuerUser: IJwtLoginRequestDTO = { username: username, password: password }
    console.log("neuerUser", neuerUser)

    await fetch(url, {
        method: 'POST',
        body: JSON.stringify(neuerUser),
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then( response => {
        if (!response.ok) {
            loginState.errormessage = response.statusText
        } else {
            loginState.errormessage = ''
            return response.json()
        }
    })
    .then( jsondata => {
        console.log(jsondata)
        //let response:IJwtLoginResponseDTO = JSON.parse(jsondata)
        loginState.username = jsondata.username
        loginState.name = jsondata.name
        loginState.benutzerprofilid = jsondata.benutzerprofilid
        loginState.jwtToken = jsondata.jwtToken
        loginState.loggedin = true
        loginState.errormessage = ''
        console.log("eingeloggt")
    })
    .catch( fehler => {
        logout()
        console.log(`Fehler: ${fehler}`)
        loginState.errormessage = `Fehler: ${fehler}`
    })

}

function logout() {
    console.log(`logout(${loginState.name} [${loginState.username}])`)
    loginState.loggedin = false
    loginState.jwtToken = ""
    loginState.benutzerprofilid = 0
    loginState.name = ""
    loginState.username = ""
    console.log("ausgeloggt)")
}


export function useLogin() {
    return {
        logindata: readonly(loginState),
        login,
        logout,
    }
}

