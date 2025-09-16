import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-navbar',
  standalone:true,
  imports:[CommonModule],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.css']
})
export class Navbar {
  sidebarCollapsed = signal(false);
  status: "Online" | "Offline" = 'Online';

  openSection: string | null = null;

  toggleSidebar() {
    this.sidebarCollapsed.update(v => !v);
  }

  toggleSection(section: string) {
    this.openSection = this.openSection === section ? null : section;
  }

}
