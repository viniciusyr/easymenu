import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Header } from '../header/header';
import { Hero } from '../hero/hero'

@Component({
  standalone: true,
  imports: [FormsModule, Header, Hero],
  selector: 'app-home',
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home {


}
