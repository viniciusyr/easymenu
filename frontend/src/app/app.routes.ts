import { Routes } from '@angular/router';
import { Home } from './public-site/components/home/home';
import { LoginComponent } from './public-site/components/login/login';
import { DashHomeComponent } from './dashboard/components/home/home';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'dashboard.home',
        pathMatch: 'full'
    },
    {
        path: 'home',
        component: Home
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'dashboard.home',
        component: DashHomeComponent
    }
];


