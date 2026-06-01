import HomeView from '@/views/HomeView.vue'

import { Route } from '@/models/route.ts';

export const routeHome = new Route("/", HomeView, "home");
export const routeAbout = new Route("/about", () => import('../views/AboutView.vue'), "about");
export const routeLogin = new Route("/login", () => import('../views/LoginPage.vue'), "login");
export const routeRegister = new Route("/register", () => import('../views/RegisterPage.vue'), "register");

export const routes = [
    routeHome,
    routeAbout,
    routeLogin,
    routeRegister,
]

export const PATHS_NOT_ALLOWED_WHEN_REGISTERED = [
    routeLogin.path,
    routeRegister.path,
]