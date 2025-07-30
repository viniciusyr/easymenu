import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
})
export class LoginComponent {
  email = '';
  password = '';

  loginWithGoogle() {
    console.log('Login com Google');
  }

  loginWithFacebook() {
    console.log('Login com Facebook');
  }

  loginWithEmail() {
    console.log(`Login com: ${this.email} / ${this.password}`);
  }
}
