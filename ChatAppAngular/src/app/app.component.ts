import { Component, OnInit } from '@angular/core';
import { ScriptLoaderServiceService } from './utils/script-loader-service.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{
  title = 'GapShap';

  constructor(private scriptLoader: ScriptLoaderServiceService) {}
  ngOnInit(): void {
    this.loadScript();
    
  }

  loadScript() {
    const scriptSrc = '/assets/js/pages/index.init.js';
    this.scriptLoader.loadScript(scriptSrc).subscribe(
      () => {
        console.log('Script loaded successfully');
        // You can now use functions from the loaded script
      },
      (error) => {
        console.error('Error loading script:', error);
      }
    );
  }
}
