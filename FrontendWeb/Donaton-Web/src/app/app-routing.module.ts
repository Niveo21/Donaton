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
    path: 'donar',
    loadChildren: () => import('./pages/donar/donar.module').then(m => m.DonarPageModule)
  },
  {
    path: 'perfil-voluntario',
    loadChildren: () => import('./pages/perfil-voluntario/perfil-voluntario.module').then(m => m.PerfilVoluntarioPageModule)
  },
  {
    path: 'chat-voluntarios',
    loadChildren: () => import('./pages/chat-voluntarios/chat-voluntarios.module').then(m => m.ChatVoluntariosPageModule)
  },
  {
    path: 'admin',
    loadChildren: () => import('./pages/admin/admin.module').then(m => m.AdminPageModule)
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
