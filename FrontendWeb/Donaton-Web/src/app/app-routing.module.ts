import { NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  {
    path: 'home',
    loadChildren: () => import('./pages/home/home.module').then(m => m.HomePageModule)
  },
  {
    path: 'login',
    loadChildren: () => import('./pages/login/login.module').then(m => m.LoginPageModule)
  },
  {
    path: 'registro',
    loadChildren: () => import('./pages/registro/registro.module').then(m => m.RegistroPageModule)
  },
  {
    path: 'registro/donante',
    loadChildren: () => import('./pages/registro-donante/registro-donante.module').then(m => m.RegistroDonatePageModule)
  },
  {
    path: 'registro/empresa',
    loadChildren: () => import('./pages/registro-empresa/registro-empresa.module').then(m => m.RegistroEmpresaPageModule)
  },
  {
    path: 'voluntarios',
    loadChildren: () => import('./pages/voluntarios/voluntarios.module').then(m => m.VoluntariosPageModule)
  },
  {
    path: 'emergencias',
    loadChildren: () => import('./pages/emergencias/emergencias.module').then(m => m.EmergenciasPageModule)
  },
  {
    path: 'nosotros',
    loadChildren: () => import('./pages/nosotros/nosotros.module').then(m => m.NosotrosPageModule)
  },
  {
    path: 'contacto',
    loadChildren: () => import('./pages/contacto/contacto.module').then(m => m.ContactoPageModule)
  },
  { path: '**', redirectTo: 'home' }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {}
