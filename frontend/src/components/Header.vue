<script setup lang="ts">
import { RouterLink } from 'vue-router'
import { useUserStore } from '@/stores/user.stores';
import type { ApiError } from '@/models/api/apiError';
import router from '@/router';
import { storeToRefs } from 'pinia';

const userStore = useUserStore();
const {user} = storeToRefs(userStore);

async function signOut(){
    const apiError: ApiError|null = await userStore.logout();
    if (apiError === null) router.push({"name": "home"});
}
</script>

<template>
    <div style="background-color: cadetblue;">
        
        
        <button><RouterLink to="/">Home</RouterLink></button>
        <div style="text-align: right">
            <div v-if="userStore.isAuthenticated">
                <span style="
                    background: white;
                    border-style: solid;
                    padding: 2pt;
                    margin: 2pt;
                    "
                >{{ userStore.user?.username }}
                </span>
                <button @click="signOut" class="btn-primary">Logout</button>
            </div>
            <div v-else>
                
                <button><RouterLink to="/login">Login</RouterLink></button>
                <button><RouterLink to="/register">Register</RouterLink></button>
            </div>
        </div>
    </div>
</template>