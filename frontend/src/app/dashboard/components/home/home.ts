import { Component } from '@angular/core';
import { Navbar } from '../navbar/navbar';

@Component({
  selector: 'app-home',
  standalone:true,
  imports: [Navbar],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class DashHomeComponent {

}
